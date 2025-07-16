package codebrew.doctorgeondam.service.chat;

import codebrew.doctorgeondam.controller.dto.ChatDto.ChatRequest;
import codebrew.doctorgeondam.controller.dto.ChatDto.ChatResponse;
import codebrew.doctorgeondam.entity.ChatLogEntity;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.repository.jpa.ChatJpaRepository;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

  private final ChatJpaRepository chatLogRepository;
  private final UserJpaRepository userRepository;

  @Value("${gemini.api-key}")
  private String apiKey;

  public ChatResponse processChat(ChatRequest request, Long userId) {
    String userMessage = request.getMessage();

    // 유저 조회
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 1. 검색 키워드 추출
    String tagPrompt = "다음 문장에서 건강기능식품 검색에 사용할 키워드 3개를 뽑아줘. 쉼표로 구분: " + userMessage;
    String tagString = callGeminiApi(tagPrompt);
    List<String> tags = Arrays.stream(tagString.split(",")).map(String::trim).toList();

    // 2. Gemini가 직접 추천 (크롤링 X)
    String recommendPrompt = """
        사용자의 건강 관련 질문에 대해 도움이 될 수 있는 건강기능식품을 추천해주세요.
        사용자의 질문: %s

        아래는 AI가 뽑은 주요 키워드입니다:
        %s

        위 키워드를 참고해서 관련 건강기능식품을 2~3개 추천하고, 각각 간단한 이유를 덧붙여 주세요.
        정중한 존댓말로, 믿을 수 있는 전문가처럼 답변해 주세요.
        """.formatted(userMessage, String.join(", ", tags));

    String finalReply = callGeminiApi(recommendPrompt);

    // 3. DB 저장
    ChatLogEntity log = ChatLogEntity.builder()
        .user(user)
        .userMessage(userMessage)
        .aiReply(finalReply)
        .timestamp(LocalDateTime.now())
        .build();
    chatLogRepository.save(log);

    return new ChatResponse(finalReply);
  }

  private String callGeminiApi(String prompt) {
    String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> requestBody = Map.of(
        "contents", List.of(
            Map.of("parts", List.of(Map.of("text", prompt)))
        )
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
      Map contentMap = ((List<Map>) response.getBody().get("candidates")).get(0);
      Map content = (Map) contentMap.get("content");
      List<Map> parts = (List<Map>) content.get("parts");
      return parts.get(0).get("text").toString();
    } catch (Exception e) {
      return "죄송해요. AI 응답에 실패했어요.";
    }
  }
}
