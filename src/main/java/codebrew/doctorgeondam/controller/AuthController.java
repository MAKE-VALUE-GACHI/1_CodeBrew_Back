package codebrew.doctorgeondam.controller;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import codebrew.doctorgeondam.controller.dto.AuthDto;
import codebrew.doctorgeondam.controller.dto.AuthDto.SignupResponse;
import codebrew.doctorgeondam.service.auth.AuthSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthSaveService authSaveService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthDto.SignupResponse>> signup(
        @RequestBody AuthDto.SignupRequest request
    ) {
        Long saveUser = authSaveService.save(request.toUser());
        return ApiResponse.ok(new SignupResponse(saveUser));
    }
}
