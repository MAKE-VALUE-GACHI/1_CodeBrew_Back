package codebrew.doctorgeondam.controller.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 통합된 API 응답 클래스
 * success, data, error 구조로 일관된 응답 형태 제공
 */
@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorInfo error;

    public ApiResponse(boolean success, T data, ErrorInfo error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /**
     * 데이터와 함께 성공 응답 생성
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, data, null));
    }

    /**
     * 데이터 없이 성공 응답 생성
     */
    public static ResponseEntity<ApiResponse<Void>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    /**
     * 에러 응답 생성
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status)
            .body(new ApiResponse<>(false, null, new ErrorInfo(errorCode, message)));
    }

    /**
     * 에러 정보를 담는 내부 클래스
     */
    public record ErrorInfo(String errorCode, String message) {
    }
}
