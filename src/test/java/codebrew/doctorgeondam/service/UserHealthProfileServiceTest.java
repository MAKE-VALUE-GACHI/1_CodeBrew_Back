package codebrew.doctorgeondam.service;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.domain.UserHealthProfile;
import codebrew.doctorgeondam.entity.*;
import codebrew.doctorgeondam.exception.UserException;
import codebrew.doctorgeondam.repository.jpa.UserHealthProfileJpaRepository;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import codebrew.doctorgeondam.service.user.UserHealthProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserHealthProfileServiceTest {

    @Mock
    private UserHealthProfileJpaRepository userHealthProfileJpaRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserHealthProfileService userHealthProfileService;

    @Test
    @DisplayName("건강 프로필을 생성할 수 있다")
    void createHealthProfile() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("테스트 사용자")
                .phoneNumber("010-1234-5678")
                .build();

        UserHealthProfile profile = UserHealthProfile.builder()
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH))
                .hasAllergies(false)
                .build();

        UserHealthProfileEntity savedEntity = UserHealthProfileEntity.builder()
                .id(1L)
                .user(userEntity)
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH))
                .hasAllergies(false)
                .build();

        given(userJpaRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(userHealthProfileJpaRepository.existsByUserId(1L)).willReturn(false);
        given(userHealthProfileJpaRepository.save(any(UserHealthProfileEntity.class))).willReturn(savedEntity);

        // when
        UserHealthProfile.UserHealthProfileId result = userHealthProfileService.createHealthProfile(userId, profile);

        // then
        assertThat(result.getValue()).isEqualTo(1L);
        verify(userHealthProfileJpaRepository).save(any(UserHealthProfileEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 건강 프로필 생성 시 예외가 발생한다")
    void createHealthProfile_UserNotFound() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserHealthProfile profile = UserHealthProfile.builder().build();

        given(userJpaRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userHealthProfileService.createHealthProfile(userId, profile))
                .isEqualTo(UserException.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("이미 건강 프로필이 존재하는 사용자의 건강 프로필 생성 시 예외가 발생한다")
    void createHealthProfile_AlreadyExists() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        UserHealthProfile profile = UserHealthProfile.builder().build();

        given(userJpaRepository.findById(1L)).willReturn(Optional.of(userEntity));
        given(userHealthProfileJpaRepository.existsByUserId(1L)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userHealthProfileService.createHealthProfile(userId, profile))
                .isEqualTo(UserException.HEALTH_PROFILE_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("건강 프로필을 업데이트할 수 있다")
    void updateHealthProfile() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserEntity userEntity = UserEntity.builder().id(1L).build();

        UserHealthProfileEntity existingProfile = UserHealthProfileEntity.builder()
                .id(1L)
                .user(userEntity)
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .hasAllergies(false)
                .build();

        UserHealthProfile updateData = UserHealthProfile.builder()
                .sleepHours(8)
                .exerciseFrequencyPerWeek(5)
                .build();

        UserHealthProfileEntity updatedEntity = UserHealthProfileEntity.builder()
                .id(1L)
                .user(userEntity)
                .birthYear(1990)
                .sleepHours(8)
                .exerciseFrequencyPerWeek(5)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .hasAllergies(false)
                .build();

        given(userHealthProfileJpaRepository.findByUserId(1L)).willReturn(Optional.of(existingProfile));
        given(userHealthProfileJpaRepository.save(any(UserHealthProfileEntity.class))).willReturn(updatedEntity);

        // when
        UserHealthProfile result = userHealthProfileService.updateHealthProfile(userId, updateData);

        // then
        assertThat(result.getSleepHours()).isEqualTo(8);
        assertThat(result.getExerciseFrequencyPerWeek()).isEqualTo(5);
        verify(userHealthProfileJpaRepository).save(any(UserHealthProfileEntity.class));
    }

    @Test
    @DisplayName("존재하지 않는 건강 프로필 업데이트 시 예외가 발생한다")
    void updateHealthProfile_NotFound() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserHealthProfile updateData = UserHealthProfile.builder().build();

        given(userHealthProfileJpaRepository.findByUserId(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userHealthProfileService.updateHealthProfile(userId, updateData))
                .isEqualTo(UserException.HEALTH_PROFILE_NOT_FOUND);
    }

    @Test
    @DisplayName("사용자 ID로 건강 프로필을 조회할 수 있다")
    void getHealthProfileByUserId() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserEntity userEntity = UserEntity.builder().id(1L).build();

        UserHealthProfileEntity entity = UserHealthProfileEntity.builder()
                .id(1L)
                .user(userEntity)
                .birthYear(1990)
                .sleepHours(7)
                .build();

        given(userHealthProfileJpaRepository.findByUserId(1L)).willReturn(Optional.of(entity));

        // when
        Optional<UserHealthProfile> result = userHealthProfileService.getHealthProfileByUserId(userId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getBirthYear()).isEqualTo(1990);
        assertThat(result.get().getSleepHours()).isEqualTo(7);
    }

    @Test
    @DisplayName("건강 프로필을 삭제할 수 있다")
    void deleteHealthProfile() {
        // given
        User.UserId userId = new User.UserId(1L);
        UserEntity userEntity = UserEntity.builder().id(1L).build();

        UserHealthProfileEntity profile = UserHealthProfileEntity.builder()
                .id(1L)
                .user(userEntity)
                .build();

        given(userHealthProfileJpaRepository.findByUserId(1L)).willReturn(Optional.of(profile));

        // when
        userHealthProfileService.deleteHealthProfile(userId);

        // then
        verify(userHealthProfileJpaRepository).delete(profile);
    }
}
