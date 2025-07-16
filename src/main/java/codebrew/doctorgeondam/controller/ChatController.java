package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.controller.dto.ChatDto;
import codebrew.doctorgeondam.controller.dto.ChatDto.ChatResponse;
import codebrew.doctorgeondam.entity.ChatLogEntity;
import codebrew.doctorgeondam.jwt.CustomUserDetails;
import codebrew.doctorgeondam.repository.jpa.ChatJpaRepository;
import codebrew.doctorgeondam.service.chat.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

  private final GeminiService geminiService;
  private final ChatJpaRepository chatJpaRepository;


  @PostMapping("/chat")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(
      summary = "AI 건강기능식품 추천",
      description = "사용자의 질문을 바탕으로 AI가 건강기능식품을 추천합니다."
  )
  public ResponseEntity<ChatResponse> chat(
      @Parameter(description = "건강 관련 질문")
      @RequestBody ChatDto.ChatRequest request,

      @Parameter(hidden = true)
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    ChatResponse response = geminiService.processChat(request, userDetails.getId());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/chat/logs")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "내 대화 기록 조회", description = "현재 로그인한 사용자의 대화 기록을 조회합니다.")
  public ResponseEntity<List<ChatLogEntity>> getMyChatLogs(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getId();
    List<ChatLogEntity> logs = chatJpaRepository.findByUser_IdOrderByTimestampDesc(userId);
    return ResponseEntity.ok(logs);
  }
}

