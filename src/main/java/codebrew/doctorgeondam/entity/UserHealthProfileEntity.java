package codebrew.doctorgeondam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "user_health_profiles")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserHealthProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "sleep_hours")
    private Integer sleepHours;

    @Column(name = "exercise_frequency_per_week")
    private Integer exerciseFrequencyPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "smoking_habit")
    private SmokingHabit smokingHabit;

    @Enumerated(EnumType.STRING)
    @Column(name = "drinking_habit")
    private DrinkingHabit drinkingHabit;

    @ElementCollection(targetClass = HealthConcernType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_health_concerns",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "concern_type")
    private Set<HealthConcernType> healthConcerns;

    @Column(name = "has_allergies")
    private Boolean hasAllergies;

    @Column(name = "allergy_details", columnDefinition = "TEXT")
    private String allergyDetails;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    @ElementCollection(targetClass = SupplementForm.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_preferred_forms",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "form_type")
    private Set<SupplementForm> preferredSupplementForms;

    public enum SmokingHabit {
        NON_SMOKER("비흡연"),
        OCCASIONAL_SMOKER("가끔 흡연"),
        LIGHT_SMOKER("하루 1-10개비"),
        MODERATE_SMOKER("하루 11-20개비"),
        HEAVY_SMOKER("하루 21개비 이상"),
        EX_SMOKER("금연");

        private final String description;

        SmokingHabit(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum DrinkingHabit {
        NON_DRINKER("비음주"),
        OCCASIONAL_DRINKER("가끔 음주"),
        SOCIAL_DRINKER("사회적 음주"),
        MODERATE_DRINKER("주 2-3회"),
        FREQUENT_DRINKER("주 4-5회"),
        DAILY_DRINKER("매일 음주");

        private final String description;

        DrinkingHabit(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
