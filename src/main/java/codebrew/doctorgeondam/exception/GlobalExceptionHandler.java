package codebrew.doctorgeondam.exception;

import codebrew.doctorgeondam.controller.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 핸들러
 * 
 * CoreException을 상속한 모든 예외를 자동으로 ApiResponse 형태로 변환하여 반환
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * CoreException 처리 (UserException, AuthException, CommunicationException, SystemException)
     * throw UserException.NOT_FOUND; 시 자동으로 errorCode, message가 담긴 ApiResponse 반환
     */
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<Void>> handleCoreException(CoreException e) {
        log.warn("CoreException occurred: {} - {}", e.getErrorCode().getCode(), e.getMessage());
        
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                e.getErrorCode().getCode(),
                                e.getMessage()
                        )
                ));
    }
    
    /**
     * Spring Security BadCredentialsException 처리
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException occurred: {}", e.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                errorCode.getCode(),
                                errorCode.getMessage()
                        )
                ));
    }
    
    /**
     * Spring Security UsernameNotFoundException 처리
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException occurred: {}", e.getMessage());
        
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                errorCode.getCode(),
                                errorCode.getMessage()
                        )
                ));
    }
    
    /**
     * Validation 예외 처리 (@Valid 어노테이션)
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidationException(Exception e) {
        log.warn("Validation exception occurred: {}", e.getMessage());
        
        String message = "입력값이 올바르지 않습니다.";
        
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            if (validException.getBindingResult().hasFieldErrors()) {
                message = validException.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
            }
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            if (bindException.getBindingResult().hasFieldErrors()) {
                message = bindException.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
            }
        }
        
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                errorCode.getCode(),
                                message
                        )
                ));
    }
    
    /**
     * IllegalArgumentException 처리 (기존 코드 호환성)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException occurred: {}", e.getMessage());
        
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                errorCode.getCode(),
                                e.getMessage()
                        )
                ));
    }
    
    /**
     * 기타 예상하지 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(
                        false,
                        null,
                        new ApiResponse.ErrorInfo(
                                errorCode.getCode(),
                                errorCode.getMessage()
                        )
                ));
    }
}
