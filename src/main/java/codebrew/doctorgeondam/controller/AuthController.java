package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import codebrew.doctorgeondam.controller.dto.AuthDto;
import codebrew.doctorgeondam.controller.dto.AuthDto.SignupResponse;
import codebrew.doctorgeondam.exception.AuthException;
import codebrew.doctorgeondam.jwt.CustomUserDetails;
import codebrew.doctorgeondam.jwt.JwtService;
import codebrew.doctorgeondam.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입, 로그인, 토큰 관리 API")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자 계정을 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 등록된 전화번호 또는 이메일")
    })
    public ResponseEntity<ApiResponse<AuthDto.SignupResponse>> signup(
        @Valid @RequestBody AuthDto.SignupRequest request
    ) {
        Long saveUser = authService.saveUser(request.toUser());
        return ApiResponse.ok(new SignupResponse(saveUser));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "잘못된 인증 정보")
    })
    public ResponseEntity<ApiResponse<AuthDto.LoginResponse>> login(
            @Valid @RequestBody AuthDto.LoginRequest request
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        AuthDto.LoginResponse response = new AuthDto.LoginResponse(
                accessToken,
                refreshToken,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getRole().name()
        );

        return ApiResponse.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 입력 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰")
    })
    public ResponseEntity<ApiResponse<AuthDto.RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody AuthDto.RefreshTokenRequest request
    ) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);
        
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        
        if (jwtService.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            
            AuthDto.RefreshTokenResponse response = new AuthDto.RefreshTokenResponse(
                    newAccessToken,
                    newRefreshToken
            );
            
            return ApiResponse.ok(response);
        }
        
        throw AuthException.INVALID_TOKEN;
    }

    @PostMapping("/send-auth-code")
    @Operation(summary = "인증 코드 전송", description = "휴대폰 번호로 SMS 인증 코드를 전송합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 전화번호 형식"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "SMS 전송 실패")
    })
    public ResponseEntity<ApiResponse<Void>> sendAuthCode(
            @Valid @RequestBody AuthDto.SendAuthCodeRequest request
    ) {
        authService.sendAuthCode(request.getPhoneNumber());
        return ApiResponse.ok();
    }

    @PostMapping("/verify-auth-code")
    @Operation(summary = "인증 코드 확인", description = "전송된 SMS 인증 코드를 확인합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 코드 확인 완료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 인증 코드 또는 만료된 코드"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "인증 코드를 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<AuthDto.VerifyAuthCodeResponse>> verifyAuthCode(
            @Valid @RequestBody AuthDto.VerifyAuthCodeRequest request
    ) {
        boolean isVerified = authService.verifyAuthCode(
                request.getPhoneNumber(),
                request.getAuthCode()
        );
        
        AuthDto.VerifyAuthCodeResponse response = new AuthDto.VerifyAuthCodeResponse(isVerified);
        return ApiResponse.ok(response);
    }
}
