package codebrew.doctorgeondam.controller.dto;

import codebrew.doctorgeondam.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SignupRequest {

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotNull(message = "이용약관 동의는 필수입니다.")
        private Boolean termsAgreed;

        @NotNull(message = "개인정보처리방침 동의는 필수입니다.")
        private Boolean privacyPolicyAgreed;

        private String gender;
        private String address;
        private String addressDetail;
        private LocalDate birthDate;
        private String job;
        private Double height;
        private Double weight;
        private Double bmi;

        public User toUser() {
            return User.builder().name(this.name)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .password(this.password)
                .termsAgreed(this.termsAgreed)
                .privacyPolicyAgreed(this.privacyPolicyAgreed)
                .gender(this.gender)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .birthDate(this.birthDate)
                .job(this.job)
                .height(this.height)
                .weight(this.weight)
                .bmi(this.bmi)
                .build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class SignupResponse {
        private Long userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        private String phoneNumber;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;
    }

    @AllArgsConstructor
    @Getter
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private String name;
        private String role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RefreshTokenRequest {
        @NotBlank(message = "리프레시 토큰은 필수 입력값입니다.")
        private String refreshToken;
    }

    @AllArgsConstructor
    @Getter
    public static class RefreshTokenResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SendAuthCodeRequest {
        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class VerifyAuthCodeRequest {
        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        private String phoneNumber;

        @NotBlank(message = "인증 코드는 필수 입력값입니다.")
        @Pattern(regexp = "^\\d{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
        private String authCode;
    }

    @AllArgsConstructor
    @Getter
    public static class VerifyAuthCodeResponse {
        private boolean verified;
    }
}
