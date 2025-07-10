package codebrew.doctorgeondam.repository;

import codebrew.doctorgeondam.entity.*;
import codebrew.doctorgeondam.repository.jpa.UserHealthProfileJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserHealthProfileJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserHealthProfileJpaRepository userHealthProfileJpaRepository;

    @Test
    @DisplayName("사용자 ID로 건강 프로필을 조회할 수 있다")
    void findByUserId() {
        // given
        UserEntity user = UserEntity.builder()
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .password("password123")
                .termsAgreed(true)
                .privacyPolicyAgreed(true)
                .build();
        
        UserEntity savedUser = entityManager.persistAndFlush(user);

        UserHealthProfileEntity profile = UserHealthProfileEntity.builder()
                .user(savedUser)
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH))
                .hasAllergies(false)
                .preferredSupplementForms(Set.of(SupplementForm.CAPSULE))
                .build();
        
        entityManager.persistAndFlush(profile);

        // when
        Optional<UserHealthProfileEntity> found = userHealthProfileJpaRepository.findByUserId(savedUser.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getBirthYear()).isEqualTo(1990);
        assertThat(found.get().getSleepHours()).isEqualTo(7);
        assertThat(found.get().getExerciseFrequencyPerWeek()).isEqualTo(3);
        assertThat(found.get().getSmokingHabit()).isEqualTo(UserHealthProfileEntity.SmokingHabit.NON_SMOKER);
        assertThat(found.get().getDrinkingHabit()).isEqualTo(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER);
        assertThat(found.get().getHealthConcerns()).containsExactly(HealthConcernType.JOINT_BONE_HEALTH);
        assertThat(found.get().getHasAllergies()).isFalse();
        assertThat(found.get().getPreferredSupplementForms()).containsExactly(SupplementForm.CAPSULE);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회하면 빈 Optional을 반환한다")
    void findByUserId_NotFound() {
        // when
        Optional<UserHealthProfileEntity> found = userHealthProfileJpaRepository.findByUserId(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("사용자 ID로 건강 프로필 존재 여부를 확인할 수 있다")
    void existsByUserId() {
        // given
        UserEntity user = UserEntity.builder()
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .email("test@example.com")
                .password("password123")
                .termsAgreed(true)
                .privacyPolicyAgreed(true)
                .build();
        
        UserEntity savedUser = entityManager.persistAndFlush(user);

        UserHealthProfileEntity profile = UserHealthProfileEntity.builder()
                .user(savedUser)
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .hasAllergies(false)
                .build();
        
        entityManager.persistAndFlush(profile);

        // when & then
        assertThat(userHealthProfileJpaRepository.existsByUserId(savedUser.getId())).isTrue();
        assertThat(userHealthProfileJpaRepository.existsByUserId(999L)).isFalse();
    }
}
