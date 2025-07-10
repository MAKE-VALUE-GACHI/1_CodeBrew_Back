package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.config.TestConfig;
import codebrew.doctorgeondam.controller.dto.UserHealthProfileDto;
import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.domain.UserHealthProfile;
import codebrew.doctorgeondam.entity.HealthConcernType;
import codebrew.doctorgeondam.entity.SupplementForm;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import codebrew.doctorgeondam.jwt.CustomUserDetails;
import codebrew.doctorgeondam.service.user.UserHealthProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserHealthProfileController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
class UserHealthProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserHealthProfileService userHealthProfileService;

    @Test
    @DisplayName("건강 프로필을 생성할 수 있다")
    void createHealthProfile() throws Exception {
        // given
        UserHealthProfileDto.CreateHealthProfileRequest request = UserHealthProfileDto.CreateHealthProfileRequest.builder()
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH))
                .hasAllergies(false)
                .preferredSupplementForms(Set.of(SupplementForm.CAPSULE))
                .build();

        UserHealthProfile.UserHealthProfileId profileId = new UserHealthProfile.UserHealthProfileId(1L);
        UserHealthProfile createdProfile = UserHealthProfile.builder()
                .id(profileId)
                .userId(new User.UserId(1L))
                .birthYear(1990)
                .sleepHours(7)
                .exerciseFrequencyPerWeek(3)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.JOINT_BONE_HEALTH))
                .hasAllergies(false)
                .preferredSupplementForms(Set.of(SupplementForm.CAPSULE))
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .phoneNumber("010-1234-5678")
                .password("password")
                .name("테스트 사용자")
                .email("test@example.com")
                .role(UserEntity.Role.USER)
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        given(userHealthProfileService.createHealthProfile(any(User.UserId.class), any(UserHealthProfile.class)))
                .willReturn(profileId);
        given(userHealthProfileService.getHealthProfileByUserIdOrThrow(any(User.UserId.class)))
                .willReturn(createdProfile);

        // when & then
        mockMvc.perform(post("/api/user/health-profile")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.birthYear").value(1990))
                .andExpect(jsonPath("$.data.sleepHours").value(7))
                .andExpect(jsonPath("$.data.exerciseFrequencyPerWeek").value(3))
                .andExpect(jsonPath("$.data.smokingHabit").value("NON_SMOKER"))
                .andExpect(jsonPath("$.data.drinkingHabit").value("SOCIAL_DRINKER"))
                .andExpect(jsonPath("$.data.hasAllergies").value(false));
    }

    @Test
    @DisplayName("건강 프로필을 조회할 수 있다")
    void getHealthProfile() throws Exception {
        // given
        UserHealthProfile profile = UserHealthProfile.builder()
                .id(new UserHealthProfile.UserHealthProfileId(1L))
                .userId(new User.UserId(1L))
                .birthYear(1985)
                .sleepHours(8)
                .exerciseFrequencyPerWeek(5)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.EX_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.MODERATE_DRINKER)
                .healthConcerns(Set.of(HealthConcernType.CHRONIC_FATIGUE))
                .hasAllergies(true)
                .allergyDetails("견과류 알레르기")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .phoneNumber("010-1234-5678")
                .password("password")
                .name("테스트 사용자")
                .email("test@example.com")
                .role(UserEntity.Role.USER)
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        given(userHealthProfileService.getHealthProfileByUserIdOrThrow(any(User.UserId.class)))
                .willReturn(profile);

        // when & then
        mockMvc.perform(get("/api/user/health-profile")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.birthYear").value(1985))
                .andExpect(jsonPath("$.data.sleepHours").value(8))
                .andExpect(jsonPath("$.data.exerciseFrequencyPerWeek").value(5))
                .andExpect(jsonPath("$.data.smokingHabit").value("EX_SMOKER"))
                .andExpect(jsonPath("$.data.drinkingHabit").value("MODERATE_DRINKER"))
                .andExpect(jsonPath("$.data.hasAllergies").value(true))
                .andExpect(jsonPath("$.data.allergyDetails").value("견과류 알레르기"));
    }

    @Test
    @DisplayName("건강 프로필을 업데이트할 수 있다")
    void updateHealthProfile() throws Exception {
        // given
        UserHealthProfileDto.UpdateHealthProfileRequest request = UserHealthProfileDto.UpdateHealthProfileRequest.builder()
                .sleepHours(9)
                .exerciseFrequencyPerWeek(4)
                .build();

        UserHealthProfile updatedProfile = UserHealthProfile.builder()
                .id(new UserHealthProfile.UserHealthProfileId(1L))
                .userId(new User.UserId(1L))
                .birthYear(1990)
                .sleepHours(9)
                .exerciseFrequencyPerWeek(4)
                .smokingHabit(UserHealthProfileEntity.SmokingHabit.NON_SMOKER)
                .drinkingHabit(UserHealthProfileEntity.DrinkingHabit.SOCIAL_DRINKER)
                .hasAllergies(false)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .phoneNumber("010-1234-5678")
                .password("password")
                .name("테스트 사용자")
                .email("test@example.com")
                .role(UserEntity.Role.USER)
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        given(userHealthProfileService.updateHealthProfile(any(User.UserId.class), any(UserHealthProfile.class)))
                .willReturn(updatedProfile);

        // when & then
        mockMvc.perform(put("/api/user/health-profile")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sleepHours").value(9))
                .andExpect(jsonPath("$.data.exerciseFrequencyPerWeek").value(4));
    }

    @Test
    @DisplayName("건강 프로필을 삭제할 수 있다")
    void deleteHealthProfile() throws Exception {
        // given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .phoneNumber("010-1234-5678")
                .password("password")
                .name("테스트 사용자")
                .email("test@example.com")
                .role(UserEntity.Role.USER)
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        // when & then
        mockMvc.perform(delete("/api/user/health-profile")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(userHealthProfileService).deleteHealthProfile(any(User.UserId.class));
    }

    @Test
    @DisplayName("유효하지 않은 데이터로 건강 프로필 생성 시 400 에러가 발생한다")
    void createHealthProfile_InvalidData() throws Exception {
        // given
        UserHealthProfileDto.CreateHealthProfileRequest request = UserHealthProfileDto.CreateHealthProfileRequest.builder()
                .birthYear(1800) // 유효하지 않은 연도
                .sleepHours(25)  // 유효하지 않은 수면시간
                .exerciseFrequencyPerWeek(8) // 유효하지 않은 운동 횟수
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .phoneNumber("010-1234-5678")
                .password("password")
                .name("테스트 사용자")
                .email("test@example.com")
                .role(UserEntity.Role.USER)
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        // when & then
        mockMvc.perform(post("/api/user/health-profile")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
