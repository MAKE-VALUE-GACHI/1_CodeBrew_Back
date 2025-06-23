package codebrew.doctorgeondam.repository;

import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("전화번호로 사용자를 조회할 수 있다")
    void findByPhoneNumber() {
        // given
        UserEntity user = UserEntity.builder()
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .password("password123")
                .gender("M")
                .birthDate(LocalDate.of(1990, 1, 1))
                .termsAgreed(true)
                .privacyPolicyAgreed(true)
                .build();
        
        entityManager.persistAndFlush(user);

        // when
        Optional<UserEntity> found = userJpaRepository.findByPhoneNumber("010-1234-5678");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("테스트 사용자");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 전화번호로 조회하면 빈 Optional을 반환한다")
    void findByPhoneNumber_NotFound() {
        // when
        Optional<UserEntity> found = userJpaRepository.findByPhoneNumber("010-9999-9999");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("전화번호 존재 여부를 확인할 수 있다")
    void existsByPhoneNumber() {
        // given
        UserEntity user = UserEntity.builder()
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .password("password123")
                .termsAgreed(true)
                .privacyPolicyAgreed(true)
                .build();
        
        entityManager.persistAndFlush(user);

        // when & then
        assertThat(userJpaRepository.existsByPhoneNumber("010-1234-5678")).isTrue();
        assertThat(userJpaRepository.existsByPhoneNumber("010-9999-9999")).isFalse();
    }

    @Test
    @DisplayName("이메일 존재 여부를 확인할 수 있다")
    void existsByEmail() {
        // given
        UserEntity user = UserEntity.builder()
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .password("password123")
                .termsAgreed(true)
                .privacyPolicyAgreed(true)
                .build();
        
        entityManager.persistAndFlush(user);

        // when & then
        assertThat(userJpaRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userJpaRepository.existsByEmail("notfound@example.com")).isFalse();
    }
}
