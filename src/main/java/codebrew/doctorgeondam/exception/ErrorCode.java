package codebrew.doctorgeondam.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    USER_NOT_FOUND("UNF001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("UAE002", "이미 등록된 사용자입니다.", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS("PAE003", "이미 등록된 전화번호입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("EAE004", "이미 등록된 이메일입니다.", HttpStatus.CONFLICT),
    HEALTH_PROFILE_NOT_FOUND("HPN005", "건강 프로필을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HEALTH_PROFILE_ALREADY_EXISTS("HPA006", "이미 등록된 건강 프로필입니다.", HttpStatus.CONFLICT),
    
    INVALID_CREDENTIALS("ICR007", "잘못된 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED("ATE008", "액세스 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("RTE009", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("ITK010", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS("UNA011", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    
    AUTH_CODE_NOT_FOUND("ACN012", "인증 코드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AUTH_CODE_EXPIRED("ACE013", "인증 코드가 만료되었습니다.", HttpStatus.BAD_REQUEST),
    AUTH_CODE_MISMATCH("ACM014", "인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    
    INVALID_INPUT("IIN015", "입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("MRF016", "필수 입력값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("ISE017", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    
    EXTERNAL_API_ERROR("EAE018", "외부 API 호출 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    SMS_SEND_FAILED("SSF019", "SMS 전송에 실패했습니다.", HttpStatus.BAD_GATEWAY),
    EMAIL_SEND_FAILED("ESF020", "이메일 전송에 실패했습니다.", HttpStatus.BAD_GATEWAY);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
