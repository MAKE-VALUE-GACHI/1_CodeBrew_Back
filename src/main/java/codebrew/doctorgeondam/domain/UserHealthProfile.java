package codebrew.doctorgeondam.domain;

import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.Set;

@Builder
@Getter
public class UserHealthProfile {
    
    private UserHealthProfileId id;
    private User.UserId userId;
    private Integer birthYear;
    private Integer sleepHours;
    private Integer exerciseFrequencyPerWeek;
    private UserHealthProfileEntity.SmokingHabit smokingHabit;
    private UserHealthProfileEntity.DrinkingHabit drinkingHabit;
    private Set<HealthConcernType> healthConcerns;
    private Boolean hasAllergies;
    private String allergyDetails;
    private String currentMedications;
    private Set<SupplementForm> preferredSupplementForms;

    @AllArgsConstructor
    @Getter
    public static class UserHealthProfileId {
        private final Long value;
    }

    public static UserHealthProfile from(UserHealthProfileEntity entity) {
        return UserHealthProfile.builder()
                .id(entity.getId() != null ? new UserHealthProfileId(entity.getId()) : null)
                .userId(entity.getUser() != null ? new User.UserId(entity.getUser().getId()) : null)
                .birthYear(entity.getBirthYear())
                .sleepHours(entity.getSleepHours())
                .exerciseFrequencyPerWeek(entity.getExerciseFrequencyPerWeek())
                .smokingHabit(entity.getSmokingHabit())
                .drinkingHabit(entity.getDrinkingHabit())
                .healthConcerns(entity.getHealthConcerns())
                .hasAllergies(entity.getHasAllergies())
                .allergyDetails(entity.getAllergyDetails())
                .currentMedications(entity.getCurrentMedications())
                .preferredSupplementForms(entity.getPreferredSupplementForms())
                .build();
    }

    public UserHealthProfileEntity toEntity() {
        return UserHealthProfileEntity.builder()
                .birthYear(this.birthYear)
                .sleepHours(this.sleepHours)
                .exerciseFrequencyPerWeek(this.exerciseFrequencyPerWeek)
                .smokingHabit(this.smokingHabit)
                .drinkingHabit(this.drinkingHabit)
                .healthConcerns(this.healthConcerns)
                .hasAllergies(this.hasAllergies)
                .allergyDetails(this.allergyDetails)
                .currentMedications(this.currentMedications)
                .preferredSupplementForms(this.preferredSupplementForms)
                .build();
    }
}
