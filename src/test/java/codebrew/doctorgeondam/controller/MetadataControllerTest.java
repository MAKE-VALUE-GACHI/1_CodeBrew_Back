package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetadataController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("건강 고민 목록을 조회할 수 있다")
    void getHealthConcerns() throws Exception {
        mockMvc.perform(get("/api/metadata/health-concerns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").exists())
                .andExpect(jsonPath("$.data[0].description").exists())
                .andExpect(jsonPath("$.data[?(@.code == 'JOINT_BONE_HEALTH')].description").value("관절/뼈 건강"))
                .andExpect(jsonPath("$.data[?(@.code == 'BLOOD_CIRCULATION')].description").value("혈액순환"))
                .andExpect(jsonPath("$.data[?(@.code == 'DIGESTIVE_HEALTH')].description").value("장 건강"))
                .andExpect(jsonPath("$.data[?(@.code == 'EYE_HEALTH')].description").value("눈 건강"))
                .andExpect(jsonPath("$.data[?(@.code == 'CHRONIC_FATIGUE')].description").value("만성 피로"))
                .andExpect(jsonPath("$.data[?(@.code == 'IMMUNE_ENHANCEMENT')].description").value("면역력 증진"));
    }

    @Test
    @DisplayName("건강기능식품 제형 목록을 조회할 수 있다")
    void getSupplementForms() throws Exception {
        mockMvc.perform(get("/api/metadata/supplement-forms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").exists())
                .andExpect(jsonPath("$.data[0].description").exists())
                .andExpect(jsonPath("$.data[?(@.code == 'CAPSULE')].description").value("캡슐"))
                .andExpect(jsonPath("$.data[?(@.code == 'TABLET')].description").value("정제"))
                .andExpect(jsonPath("$.data[?(@.code == 'POWDER')].description").value("분말"))
                .andExpect(jsonPath("$.data[?(@.code == 'JELLY')].description").value("젤리"))
                .andExpect(jsonPath("$.data[?(@.code == 'LIQUID')].description").value("액상"))
                .andExpect(jsonPath("$.data[?(@.code == 'SOFT_CAPSULE')].description").value("소프트캡슐"));
    }

    @Test
    @DisplayName("흡연 습관 목록을 조회할 수 있다")
    void getSmokingHabits() throws Exception {
        mockMvc.perform(get("/api/metadata/smoking-habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").exists())
                .andExpect(jsonPath("$.data[0].description").exists())
                .andExpect(jsonPath("$.data[?(@.code == 'NON_SMOKER')].description").value("비흡연"))
                .andExpect(jsonPath("$.data[?(@.code == 'OCCASIONAL_SMOKER')].description").value("가끔 흡연"))
                .andExpect(jsonPath("$.data[?(@.code == 'LIGHT_SMOKER')].description").value("하루 1-10개비"))
                .andExpect(jsonPath("$.data[?(@.code == 'MODERATE_SMOKER')].description").value("하루 11-20개비"))
                .andExpect(jsonPath("$.data[?(@.code == 'HEAVY_SMOKER')].description").value("하루 21개비 이상"))
                .andExpect(jsonPath("$.data[?(@.code == 'EX_SMOKER')].description").value("금연"));
    }

    @Test
    @DisplayName("음주 습관 목록을 조회할 수 있다")
    void getDrinkingHabits() throws Exception {
        mockMvc.perform(get("/api/metadata/drinking-habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").exists())
                .andExpect(jsonPath("$.data[0].description").exists())
                .andExpect(jsonPath("$.data[?(@.code == 'NON_DRINKER')].description").value("비음주"))
                .andExpect(jsonPath("$.data[?(@.code == 'OCCASIONAL_DRINKER')].description").value("가끔 음주"))
                .andExpect(jsonPath("$.data[?(@.code == 'SOCIAL_DRINKER')].description").value("사회적 음주"))
                .andExpect(jsonPath("$.data[?(@.code == 'MODERATE_DRINKER')].description").value("주 2-3회"))
                .andExpect(jsonPath("$.data[?(@.code == 'FREQUENT_DRINKER')].description").value("주 4-5회"))
                .andExpect(jsonPath("$.data[?(@.code == 'DAILY_DRINKER')].description").value("매일 음주"));
    }
}
