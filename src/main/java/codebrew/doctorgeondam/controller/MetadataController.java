package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metadata")
@Tag(name = "메타데이터", description = "선택 옵션 메타데이터 조회 API")
public class MetadataController {

    @GetMapping("/health-concerns")
    @Operation(summary = "건강 고민 목록 조회", description = "선택 가능한 건강 고민 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "건강 고민 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<EnumMetadata>>> getHealthConcerns() {
        List<EnumMetadata> healthConcerns = Arrays.stream(HealthConcernType.values())
                .map(type -> EnumMetadata.builder()
                        .code(type.name())
                        .description(type.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return ApiResponse.ok(healthConcerns);
    }

    @GetMapping("/supplement-forms")
    @Operation(summary = "건강기능식품 제형 목록 조회", description = "선택 가능한 건강기능식품 제형 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제형 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<EnumMetadata>>> getSupplementForms() {
        List<EnumMetadata> supplementForms = Arrays.stream(SupplementForm.values())
                .map(form -> EnumMetadata.builder()
                        .code(form.name())
                        .description(form.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return ApiResponse.ok(supplementForms);
    }

    @GetMapping("/smoking-habits")
    @Operation(summary = "흡연 습관 목록 조회", description = "선택 가능한 흡연 습관 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "흡연 습관 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<EnumMetadata>>> getSmokingHabits() {
        List<EnumMetadata> smokingHabits = Arrays.stream(UserHealthProfileEntity.SmokingHabit.values())
                .map(habit -> EnumMetadata.builder()
                        .code(habit.name())
                        .description(habit.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return ApiResponse.ok(smokingHabits);
    }

    @GetMapping("/drinking-habits")
    @Operation(summary = "음주 습관 목록 조회", description = "선택 가능한 음주 습관 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "음주 습관 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<EnumMetadata>>> getDrinkingHabits() {
        List<EnumMetadata> drinkingHabits = Arrays.stream(UserHealthProfileEntity.DrinkingHabit.values())
                .map(habit -> EnumMetadata.builder()
                        .code(habit.name())
                        .description(habit.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return ApiResponse.ok(drinkingHabits);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EnumMetadata {
        private String code;
        private String description;
    }
}
