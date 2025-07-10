package codebrew.doctorgeondam.exception;

public class SystemException extends CoreException {
    
    public static final SystemException INVALID_INPUT = new SystemException(ErrorCode.INVALID_INPUT);
    public static final SystemException MISSING_REQUIRED_FIELD = new SystemException(ErrorCode.MISSING_REQUIRED_FIELD);
    public static final SystemException INTERNAL_SERVER_ERROR = new SystemException(ErrorCode.INTERNAL_SERVER_ERROR);
    
    private SystemException(ErrorCode errorCode) {
        super(errorCode);
    }
}
