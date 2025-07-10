package codebrew.doctorgeondam.controller.dto;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.jwt.CustomUserDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserResponse {

        private Long id;
        private String name;
        private String phoneNumber;
        private String email;
        private String gender;
        private String address;
        private String addressDetail;
        private LocalDate birthDate;
        private String job;
        private Double height;
        private Double weight;
        private Double bmi;
        private String role;
        private Boolean termsAgreed;
        private Boolean privacyPolicyAgreed;
        private UserHealthProfileDto.HealthProfileResponse healthProfile;

        public static UserResponse from(CustomUserDetails userDetails) {
            return UserResponse.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .phoneNumber(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(userDetails.getRole().name())
                .build();
        }

        public static UserResponse from(User user) {
            return UserResponse.builder()
                .id(user.getId().getValue())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .gender(user.getGender())
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .birthDate(user.getBirthDate())
                .job(user.getJob())
                .height(user.getHeight())
                .weight(user.getWeight())
                .bmi(user.getBmi())
                .role(user.getRole().name())
                .termsAgreed(user.getTermsAgreed())
                .privacyPolicyAgreed(user.getPrivacyPolicyAgreed())
                .healthProfile(user.getHealthProfile() != null ?
                    UserHealthProfileDto.HealthProfileResponse.from(user.getHealthProfile()) : null)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserRequest {

        private String name;

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        private String gender;
        private String address;
        private String addressDetail;
        private LocalDate birthDate;
        private String job;
        private Double height;
        private Double weight;

        public User toUser() {
            return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .address(address)
                .addressDetail(addressDetail)
                .birthDate(birthDate)
                .job(job)
                .height(height)
                .weight(weight)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {

        @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String newPassword;
    }
}
