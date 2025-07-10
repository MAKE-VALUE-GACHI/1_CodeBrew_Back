package codebrew.doctorgeondam.domain;

import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
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
    private UserEntity.Role role;
    private Boolean termsAgreed;
    private Boolean privacyPolicyAgreed;
    private UserHealthProfile healthProfile;

    @AllArgsConstructor
    @Getter
    public static class UserId {

        private final Long value;
    }

    public static User from(UserEntity userEntity) {
        return User.builder()
            .id(new UserId(userEntity.getId()))
            .name(userEntity.getName())
            .phoneNumber(userEntity.getPhoneNumber())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .gender(userEntity.getGender())
            .address(userEntity.getAddress())
            .addressDetail(userEntity.getAddressDetail())
            .birthDate(userEntity.getBirthDate())
            .job(userEntity.getJob())
            .height(userEntity.getHeight())
            .weight(userEntity.getWeight())
            .bmi(userEntity.getBmi())
            .role(userEntity.getRole())
            .termsAgreed(userEntity.getTermsAgreed())
            .privacyPolicyAgreed(userEntity.getPrivacyPolicyAgreed())
            .healthProfile(userEntity.getHealthProfile() != null ?
                UserHealthProfile.from(userEntity.getHealthProfile()) : null)
            .build();
    }

    public UserEntity toUserEntity() {
        UserEntity userEntity = UserEntity.builder()
            .name(this.getName())
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
            .role(this.getRole() != null ? this.getRole() : UserEntity.Role.USER)
            .termsAgreed(this.getTermsAgreed() != null ? this.getTermsAgreed() : false)
            .privacyPolicyAgreed(
                this.getPrivacyPolicyAgreed() != null ? this.getPrivacyPolicyAgreed() : false)
            .build();

        if (this.getHealthProfile() != null) {
            UserHealthProfileEntity healthProfileEntity = this.getHealthProfile().toEntity();
            healthProfileEntity.setUser(userEntity);
            userEntity.setHealthProfile(healthProfileEntity);
        }

        return userEntity;
    }
}
