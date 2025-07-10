package codebrew.doctorgeondam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserHealthProfileTest {

    @Test
    @DisplayName("UserHealthProfile 도메인 객체를 UserHealthProfileEntity로 변환할 수 있다")
    void toEntity() {
        // given
        UserHealthProfile profile = UserHealthProfile.builder()
            .birthYear(1990)
            .sleepHours(7)
            .exerciseFrequencyPerWeek(3)
            .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
            .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
            .healthConcerns(
                Set.of(HealthConcernType.JOINT_BONE_HEALTH, HealthConcernType.EYE_HEALTH))
            .hasAllergies(false)
            .currentMedications("비타민 D")
            .preferredSupplementForms(Set.of(SupplementForm.CAPSULE, SupplementForm.TABLET))
            .build();

        // when
        UserHealthProfileEntity entity = profile.toEntity();

        // then
        assertThat(entity.getBirthYear()).isEqualTo(1990);
        assertThat(entity.getSleepHours()).isEqualTo(7);
        assertThat(entity.getExerciseFrequencyPerWeek()).isEqualTo(3);
        assertThat(entity.getSmokingHabit()).isEqualTo(
            UserHealthProfileEntity.SmokingHabit.NON_SMOKER);
        assertThat(entity.getDrinkingHabit()).isEqualTo(
            UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER);
        assertThat(entity.getHealthConcerns()).containsExactlyInAnyOrder(
            HealthConcernType.JOINT_BONE_HEALTH, HealthConcernType.EYE_HEALTH);
        assertThat(entity.getHasAllergies()).isFalse();
        assertThat(entity.getCurrentMedications()).isEqualTo("비타민 D");
        assertThat(entity.getPreferredSupplementForms()).containsExactlyInAnyOrder(
            SupplementForm.CAPSULE, SupplementForm.TABLET);
    }

    @Test
    @DisplayName("UserHealthProfileEntity를 UserHealthProfile 도메인 객체로 변환할 수 있다")
    void fromEntity() {
        // given
        UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("테스트 사용자")
            .phoneNumber("010-1234-5678")
            .build();

        UserHealthProfileEntity entity = UserHealthProfileEntity.builder()
            .id(1L)
            .user(userEntity)
            .birthYear(1985)
            .sleepHours(8)
            .exerciseFrequencyPerWeek(5)
            .smokingHabit(UserHealthProfileEntity.SmokingHabit.EX_SMOKER)
            .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.MODERATE_DRINKER)
            .healthConcerns(Set.of(HealthConcernType.CHRONIC_FATIGUE))
            .hasAllergies(true)
            .allergyDetails("견과류 알레르기")
            .currentMedications("혈압약")
            .preferredSupplementForms(Set.of(SupplementForm.JELLY))
            .build();

        // when
        UserHealthProfile profile = UserHealthProfile.from(entity);

        // then
        assertThat(profile.getId().getValue()).isEqualTo(1L);
        assertThat(profile.getUserId().getValue()).isEqualTo(1L);
        assertThat(profile.getBirthYear()).isEqualTo(1985);
        assertThat(profile.getSleepHours()).isEqualTo(8);
        assertThat(profile.getExerciseFrequencyPerWeek()).isEqualTo(5);
        assertThat(profile.getSmokingHabit()).isEqualTo(
            UserHealthProfileEntity.SmokingHabit.EX_SMOKER);
        assertThat(profile.getDrinkingHabit()).isEqualTo(
            UserHealthProfileEntity.DrinkingHabit.MODERATE_DRINKER);
        assertThat(profile.getHealthConcerns()).containsExactly(HealthConcernType.CHRONIC_FATIGUE);
        assertThat(profile.getHasAllergies()).isTrue();
        assertThat(profile.getAllergyDetails()).isEqualTo("견과류 알레르기");
        assertThat(profile.getCurrentMedications()).isEqualTo("혈압약");
        assertThat(profile.getPreferredSupplementForms()).containsExactly(SupplementForm.JELLY);
    }
}
