package codebrew.doctorgeondam.domain;

import codebrew.doctorgeondam.entity.UserEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {

    private UserId id;
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

    @AllArgsConstructor
    public static class UserId {

        public Long id;
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder().name(this.getName())
            .phoneNumber(this.getPhoneNumber())
            .email(this.getEmail())
            .password(this.getPassword())
            .gender(this.getGender())
            .address(this.getAddress())
            .addressDetail(this.getAddressDetail())
            .birthDate(this.getBirthDate())
            .job(this.getJob())
            .height(this.getHeight())
            .weight(this.getWeight())
            .bmi(this.getBmi())
            .build();
    }
}
