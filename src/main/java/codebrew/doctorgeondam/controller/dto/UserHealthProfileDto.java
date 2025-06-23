package codebrew.doctorgeondam.controller.dto;

import codebrew.doctorgeondam.domain.UserHealthProfile;
import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserHealthProfileDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateHealthProfileRequest {

        @NotNull(message = "태어난 연도는 필수 입력값입니다.")
        @Min(value = 1900, message = "태어난 연도는 1900년 이후여야 합니다.")
        @Max(value = 2024, message = "태어난 연도는 2024년 이전이어야 합니다.")
        private Integer birthYear;

        @NotNull(message = "평소 수면시간은 필수 입력값입니다.")
        @Min(value = 1, message = "수면시간은 최소 1시간 이상이어야 합니다.")
        @Max(value = 24, message = "수면시간은 최대 24시간 이하여야 합니다.")
        private Integer sleepHours;

        @NotNull(message = "일주일 운동 횟수는 필수 입력값입니다.")
        @Min(value = 0, message = "운동 횟수는 0회 이상이어야 합니다.")
        @Max(value = 7, message = "운동 횟수는 7회 이하여야 합니다.")
        private Integer exerciseFrequencyPerWeek;

        @NotNull(message = "흡연 습관은 필수 선택값입니다.")
        private UserHealthProfileEntity.SmokingHabit smokingHabit;

        @NotNull(message = "음주 습관은 필수 선택값입니다.")
        private UserHealthProfileEntity.DrinkingHabit drinkingHabit;

        private Set<HealthConcernType> healthConcerns;

        @NotNull(message = "알레르기 여부는 필수 선택값입니다.")
        private Boolean hasAllergies;

        private String allergyDetails;

        private String currentMedications;

        private Set<SupplementForm> preferredSupplementForms;

        public UserHealthProfile toUserHealthProfile() {
            return UserHealthProfile.builder()
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

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HealthProfileResponse {

        private Long id;
        private Long userId;
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

        public static HealthProfileResponse from(UserHealthProfile userHealthProfile) {
            return HealthProfileResponse.builder()
                .id(userHealthProfile.getId() != null ? userHealthProfile.getId().getValue() : null)
                .userId(
                    userHealthProfile.getUserId() != null ? userHealthProfile.getUserId().getValue()
                        : null)
                .birthYear(userHealthProfile.getBirthYear())
                .sleepHours(userHealthProfile.getSleepHours())
                .exerciseFrequencyPerWeek(userHealthProfile.getExerciseFrequencyPerWeek())
                .smokingHabit(userHealthProfile.getSmokingHabit())
                .drinkingHabit(userHealthProfile.getDrinkingHabit())
                .healthConcerns(userHealthProfile.getHealthConcerns())
                .hasAllergies(userHealthProfile.getHasAllergies())
                .allergyDetails(userHealthProfile.getAllergyDetails())
                .currentMedications(userHealthProfile.getCurrentMedications())
                .preferredSupplementForms(userHealthProfile.getPreferredSupplementForms())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateHealthProfileRequest {

        @Min(value = 1900, message = "태어난 연도는 1900년 이후여야 합니다.")
        @Max(value = 2024, message = "태어난 연도는 2024년 이전이어야 합니다.")
        private Integer birthYear;

        @Min(value = 1, message = "수면시간은 최소 1시간 이상이어야 합니다.")
        @Max(value = 24, message = "수면시간은 최대 24시간 이하여야 합니다.")
        private Integer sleepHours;

        @Min(value = 0, message = "운동 횟수는 0회 이상이어야 합니다.")
        @Max(value = 7, message = "운동 횟수는 7회 이하여야 합니다.")
        private Integer exerciseFrequencyPerWeek;

        private UserHealthProfileEntity.SmokingHabit smokingHabit;
        private UserHealthProfileEntity.DrinkingHabit drinkingHabit;
        private Set<HealthConcernType> healthConcerns;
        private Boolean hasAllergies;
        private String allergyDetails;
        private String currentMedications;
        private Set<SupplementForm> preferredSupplementForms;

        public UserHealthProfile toUserHealthProfile() {
            return UserHealthProfile.builder()
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
}
