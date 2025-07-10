package codebrew.doctorgeondam.controller.dto;

import codebrew.doctorgeondam.domain.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;

public class AuthDto {

    public static class SignupRequest {

        private String name;
        private String phoneNumber;
        private String email;
        private String password;
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
    public static class SignupResponse {

        private Long userId;
    }

}
