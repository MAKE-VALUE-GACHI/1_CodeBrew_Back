package codebrew.doctorgeondam.service.user;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.domain.UserHealthProfile;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import codebrew.doctorgeondam.exception.UserException;
import codebrew.doctorgeondam.repository.jpa.UserHealthProfileJpaRepository;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserHealthProfileService {

    private final UserHealthProfileJpaRepository userHealthProfileJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public UserHealthProfile.UserHealthProfileId createHealthProfile(User.UserId userId,
        UserHealthProfile userHealthProfile) {
        UserEntity userEntity = userJpaRepository.findById(userId.getValue())
            .orElseThrow(() -> UserException.USER_NOT_FOUND);

        if (userHealthProfileJpaRepository.existsByUserId(userId.getValue())) {
            throw UserException.HEALTH_PROFILE_ALREADY_EXISTS;
        }

        UserHealthProfileEntity healthProfileEntity = userHealthProfile.toEntity();
        healthProfileEntity.setUser(userEntity);

        UserHealthProfileEntity savedEntity = userHealthProfileJpaRepository.save(
            healthProfileEntity);
        return new UserHealthProfile.UserHealthProfileId(savedEntity.getId());
    }

    @Transactional
    public UserHealthProfile updateHealthProfile(User.UserId userId, UserHealthProfile updateData) {
        UserHealthProfileEntity existingProfile = userHealthProfileJpaRepository.findByUserId(
                userId.getValue())
            .orElseThrow(() -> UserException.HEALTH_PROFILE_NOT_FOUND);

        updateProfileFields(existingProfile, updateData);
        UserHealthProfileEntity updatedEntity = userHealthProfileJpaRepository.save(
            existingProfile);
        return UserHealthProfile.from(updatedEntity);
    }

    public Optional<UserHealthProfile> getHealthProfileByUserId(User.UserId userId) {
        return userHealthProfileJpaRepository.findByUserId(userId.getValue())
            .map(UserHealthProfile::from);
    }

    public UserHealthProfile getHealthProfileByUserIdOrThrow(User.UserId userId) {
        return getHealthProfileByUserId(userId)
            .orElseThrow(() -> UserException.HEALTH_PROFILE_NOT_FOUND);
    }

    @Transactional
    public void deleteHealthProfile(User.UserId userId) {
        UserHealthProfileEntity profile = userHealthProfileJpaRepository.findByUserId(
                userId.getValue())
            .orElseThrow(() -> UserException.HEALTH_PROFILE_NOT_FOUND);

        userHealthProfileJpaRepository.delete(profile);
    }

    private void updateProfileFields(UserHealthProfileEntity entity, UserHealthProfile updateData) {
        if (updateData.getBirthYear() != null) {
            entity.setBirthYear(updateData.getBirthYear());
        }
        if (updateData.getSleepHours() != null) {
            entity.setSleepHours(updateData.getSleepHours());
        }
        if (updateData.getExerciseFrequencyPerWeek() != null) {
            entity.setExerciseFrequencyPerWeek(updateData.getExerciseFrequencyPerWeek());
        }
        if (updateData.getSmokingHabit() != null) {
            entity.setSmokingHabit(updateData.getSmokingHabit());
        }
        if (updateData.getDrinkingHabit() != null) {
            entity.setDrinkingHabit(updateData.getDrinkingHabit());
        }
        if (updateData.getHealthConcerns() != null) {
            entity.setHealthConcerns(updateData.getHealthConcerns());
        }
        if (updateData.getHasAllergies() != null) {
            entity.setHasAllergies(updateData.getHasAllergies());
        }
        if (updateData.getAllergyDetails() != null) {
            entity.setAllergyDetails(updateData.getAllergyDetails());
        }
        if (updateData.getCurrentMedications() != null) {
            entity.setCurrentMedications(updateData.getCurrentMedications());
        }
        if (updateData.getPreferredSupplementForms() != null) {
            entity.setPreferredSupplementForms(updateData.getPreferredSupplementForms());
        }
    }
}
