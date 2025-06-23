package codebrew.doctorgeondam.entity;

import lombok.Getter;

@Getter
public enum HealthConcernType {
    JOINT_BONE_HEALTH("관절/뼈 건강"),
    BLOOD_CIRCULATION("혈액순환"),
    DIGESTIVE_HEALTH("장 건강"),
    EYE_HEALTH("눈 건강"),
    CHRONIC_FATIGUE("만성 피로"),
    IMMUNE_ENHANCEMENT("면역력 증진");

    private final String description;

    HealthConcernType(String description) {
        this.description = description;
    }

}
