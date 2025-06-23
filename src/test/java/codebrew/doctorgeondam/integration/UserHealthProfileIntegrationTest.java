package codebrew.doctorgeondam.integration;

import codebrew.doctorgeondam.controller.dto.AuthDto;
import codebrew.doctorgeondam.controller.dto.UserHealthProfileDto;
import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@ActiveProfiles("test")
class UserHealthProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("회원가입부터 건강 프로필 생성까지 전체 플로우 테스트")
    void fullUserHealthProfileFlow() throws Exception {
        // 1. 회원가입
        AuthDto.SignupRequest signupRequest = new AuthDto.SignupRequest();
        signupRequest.setName("통합테스트 사용자");
        signupRequest.setPhoneNumber("010-9999-9999");
        signupRequest.setEmail("integration@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setTermsAgreed(true);
        signupRequest.setPrivacyPolicyAgreed(true);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").exists());

        // 2. 로그인
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();
        loginRequest.setPhoneNumber("010-9999-9999");
        loginRequest.setPassword("password123");

        String loginResponse = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Access Token 추출
        String accessToken = extractAccessToken(loginResponse);

        // 3. 건강 프로필 생성
        UserHealthProfileDto.CreateHealthProfileRequest profileRequest = UserHealthProfileDto.CreateHealthProfileRequest.builder()
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH, HealthConcernType.EYE_HEALTH))
                .hasAllergies(false)
                .currentMedications("비타민 D")
                .preferredSupplementForms(Set.of(SupplementForm.CAPSULE))
                .build();

        mockMvc.perform(post("/api/user/health-profile")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.birthYear").value(1990))
                .andExpect(jsonPath("$.data.sleepHours").value(7))
                .andExpect(jsonPath("$.data.exerciseFrequencyPerWeek").value(3))
                .andExpect(jsonPath("$.data.smokingHabit").value("NON_SMOKER"))
                .andExpect(jsonPath("$.data.drinkingHabit").value("SOCIAL_DRINKER"))
                .andExpect(jsonPath("$.data.hasAllergies").value(false))
                .andExpect(jsonPath("$.data.currentMedications").value("비타민 D"));

        // 4. 건강 프로필 조회
        mockMvc.perform(get("/api/user/health-profile")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.birthYear").value(1990))
                .andExpect(jsonPath("$.data.sleepHours").value(7));

        // 5. 건강 프로필 업데이트
        UserHealthProfileDto.UpdateHealthProfileRequest updateRequest = UserHealthProfileDto.UpdateHealthProfileRequest.builder()
                .sleepHours(8)
                .exerciseFrequencyPerWeek(5)
                .build();

        mockMvc.perform(put("/api/user/health-profile")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sleepHours").value(8))
                .andExpect(jsonPath("$.data.exerciseFrequencyPerWeek").value(5));

        // 6. 사용자 정보 조회 (건강 프로필 포함)
        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("통합테스트 사용자"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-9999-9999"))
                .andExpect(jsonPath("$.data.email").value("integration@test.com"))
                .andExpect(jsonPath("$.data.termsAgreed").value(true))
                .andExpect(jsonPath("$.data.privacyPolicyAgreed").value(true))
                .andExpect(jsonPath("$.data.healthProfile").exists())
                .andExpect(jsonPath("$.data.healthProfile.sleepHours").value(8))
                .andExpect(jsonPath("$.data.healthProfile.exerciseFrequencyPerWeek").value(5));
    }

    @Test
    @DisplayName("메타데이터 API 전체 테스트")
    void metadataApiTest() throws Exception {
        // 건강 고민 목록 조회
        mockMvc.perform(get("/api/metadata/health-concerns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6));

        // 제형 목록 조회
        mockMvc.perform(get("/api/metadata/supplement-forms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6));

        // 흡연 습관 목록 조회
        mockMvc.perform(get("/api/metadata/smoking-habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6));

        // 음주 습관 목록 조회
        mockMvc.perform(get("/api/metadata/drinking-habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6));
    }

    private String extractAccessToken(String loginResponse) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        return jsonNode.get("data").get("accessToken").asText();
    }
}
