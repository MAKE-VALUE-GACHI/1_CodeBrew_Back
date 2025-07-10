package codebrew.doctorgeondam.controller.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 통합된 API 응답 클래스 success, data, error 구조로 일관된 응답 형태 제공
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorInfo error;

    private ApiResponse(boolean success, T data, ErrorInfo error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // ============ 성공 응답 팩토리 메서드 ============

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

    // ============ 실패 응답 팩토리 메서드 ============

    /**
     * 에러 코드와 메시지로 실패 응답 생성 (400 Bad Request)
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String errorCode, String message) {
        return ResponseEntity.badRequest()
            .body(new ApiResponse<>(false, null, new ErrorInfo(errorCode, message)));
    }

    /**
     * 메시지만으로 실패 응답 생성 (400 Bad Request)
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity.badRequest()
            .body(new ApiResponse<>(false, null, new ErrorInfo("BAD_REQUEST", message)));
    }

    /**
     * HTTP 상태 코드를 지정한 실패 응답 생성
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorCode,
        String message) {
        return ResponseEntity.status(status)
            .body(new ApiResponse<>(false, null, new ErrorInfo(errorCode, message)));
    }

    // ============ 특정 상황별 편의 메서드 ============

    /**
     * 401 Unauthorized 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message);
    }

    /**
     * 403 Forbidden 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }

    /**
     * 404 Not Found 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }

    /**
     * 500 Internal Server Error 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> serverError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", message);
    }

    // ============ Getter 메서드 ============

    /**
     * 에러 여부 확인
     */
    public boolean hasError() {
        return error != null;
    }

    /**
     * 데이터 존재 여부 확인
     */
    public boolean hasData() {
        return data != null;
    }

    // ============ 내부 에러 정보 클래스 ============

    /**
     * 에러 정보를 담는 내부 클래스
     */
    public record ErrorInfo(String errorCode, String message) {

    }
}
