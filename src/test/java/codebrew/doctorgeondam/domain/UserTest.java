package codebrew.doctorgeondam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import codebrew.doctorgeondam.entity.UserEntity;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("User 도메인 객체를 UserEntity로 변환할 수 있다")
    void toUserEntity() {
        // given
        User user = User.builder()
            .name("테스트 사용자")
            .phoneNumber("010-1234-5678")
            .email("test@example.com")
            .password("password123")
            .gender("M")
            .birthDate(LocalDate.of(1990, 1, 1))
            .termsAgreed(true)
            .privacyPolicyAgreed(true)
            .build();

        // when
        UserEntity userEntity = user.toUserEntity();

        // then
        assertThat(userEntity.getName()).isEqualTo("테스트 사용자");
        assertThat(userEntity.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(userEntity.getEmail()).isEqualTo("test@example.com");
        assertThat(userEntity.getPassword()).isEqualTo("password123");
        assertThat(userEntity.getGender()).isEqualTo("M");
        assertThat(userEntity.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(userEntity.getTermsAgreed()).isTrue();
        assertThat(userEntity.getPrivacyPolicyAgreed()).isTrue();
        assertThat(userEntity.getRole()).isEqualTo(UserEntity.Role.USER);
    }

    @Test
    @DisplayName("UserEntity를 User 도메인 객체로 변환할 수 있다")
    void fromUserEntity() {
        // given
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("테스트 사용자")
            .phoneNumber("010-1234-5678")
            .email("test@example.com")
            .password("password123")
            .gender("F")
            .birthDate(LocalDate.of(1995, 5, 15))
            .termsAgreed(true)
            .privacyPolicyAgreed(false)
            .role(UserEntity.Role.USER)
            .build();

        // when
        User user = User.from(userEntity);

        // then
        assertThat(user.getId().getValue()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("테스트 사용자");
        assertThat(user.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getGender()).isEqualTo("F");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 15));
        assertThat(user.getTermsAgreed()).isTrue();
        assertThat(user.getPrivacyPolicyAgreed()).isFalse();
        assertThat(user.getRole()).isEqualTo(UserEntity.Role.USER);
    }

    @Test
    @DisplayName("약관 동의 값이 null일 때 기본값 false로 설정된다")
    void defaultTermsAgreedValues() {
        // given
        User user = User.builder()
            .name("테스트 사용자")
            .phoneNumber("010-1234-5678")
            .email("test@example.com")
            .password("password123")
            .termsAgreed(null)
            .privacyPolicyAgreed(null)
            .build();

        // when
        UserEntity userEntity = user.toUserEntity();

        // then
        assertThat(userEntity.getTermsAgreed()).isFalse();
        assertThat(userEntity.getPrivacyPolicyAgreed()).isFalse();
    }
}
