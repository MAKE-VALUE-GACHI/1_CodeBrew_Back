package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import codebrew.doctorgeondam.controller.dto.UserHealthProfileDto;
import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.domain.UserHealthProfile;
import codebrew.doctorgeondam.jwt.CustomUserDetails;
import codebrew.doctorgeondam.service.user.UserHealthProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/health-profile")
@RequiredArgsConstructor
@Tag(name = "건강 프로필", description = "사용자 건강 프로필 관리 API")
public class UserHealthProfileController {

    private final UserHealthProfileService userHealthProfileService;

    @PostMapping
    @Operation(summary = "건강 프로필 생성", description = "사용자의 건강 프로필을 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "건강 프로필 생성 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 건강 프로필이 존재함")
    })
    public ResponseEntity<ApiResponse<UserHealthProfileDto.HealthProfileResponse>> createHealthProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody UserHealthProfileDto.CreateHealthProfileRequest request
    ) {
        User.UserId userId = new User.UserId(userDetails.getId());
        UserHealthProfile.UserHealthProfileId profileId = userHealthProfileService.createHealthProfile(
            userId,
            request.toUserHealthProfile()
        );

        UserHealthProfile createdProfile = userHealthProfileService.getHealthProfileByUserIdOrThrow(
            userId);
        UserHealthProfileDto.HealthProfileResponse response = UserHealthProfileDto.HealthProfileResponse.from(
            createdProfile);

        return ApiResponse.ok(response);
    }

    @GetMapping
    @Operation(summary = "건강 프로필 조회", description = "현재 사용자의 건강 프로필을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "건강 프로필 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "건강 프로필을 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<UserHealthProfileDto.HealthProfileResponse>> getHealthProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User.UserId userId = new User.UserId(userDetails.getId());
        UserHealthProfile healthProfile = userHealthProfileService.getHealthProfileByUserIdOrThrow(
            userId);
        UserHealthProfileDto.HealthProfileResponse response = UserHealthProfileDto.HealthProfileResponse.from(
            healthProfile);

        return ApiResponse.ok(response);
    }

    @PutMapping
    @Operation(summary = "건강 프로필 수정", description = "사용자의 건강 프로필을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "건강 프로필 수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "건강 프로필을 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<UserHealthProfileDto.HealthProfileResponse>> updateHealthProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody UserHealthProfileDto.UpdateHealthProfileRequest request
    ) {
        User.UserId userId = new User.UserId(userDetails.getId());
        UserHealthProfile updatedProfile = userHealthProfileService.updateHealthProfile(
            userId,
            request.toUserHealthProfile()
        );

        UserHealthProfileDto.HealthProfileResponse response = UserHealthProfileDto.HealthProfileResponse.from(
            updatedProfile);
        return ApiResponse.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "건강 프로필 삭제", description = "사용자의 건강 프로필을 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "건강 프로필 삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "건강 프로필을 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<Void>> deleteHealthProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User.UserId userId = new User.UserId(userDetails.getId());
        userHealthProfileService.deleteHealthProfile(userId);

        return ApiResponse.ok();
    }
}
