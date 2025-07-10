package codebrew.doctorgeondam.exception;

import lombok.Getter;

@Getter
public abstract class CoreException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    protected CoreException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    protected CoreException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
    
    protected CoreException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
    
    protected CoreException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
    }
}
