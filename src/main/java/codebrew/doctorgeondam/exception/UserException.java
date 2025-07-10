package codebrew.doctorgeondam.exception;

public class UserException extends CoreException {
    
    public static final UserException NOT_FOUND = new UserException(ErrorCode.USER_NOT_FOUND);
    public static final UserException USER_NOT_FOUND = new UserException(ErrorCode.USER_NOT_FOUND);
    public static final UserException ALREADY_EXISTS = new UserException(ErrorCode.USER_ALREADY_EXISTS);
    public static final UserException PHONE_NUMBER_ALREADY_EXISTS = new UserException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
    public static final UserException EMAIL_ALREADY_EXISTS = new UserException(ErrorCode.EMAIL_ALREADY_EXISTS);
    public static final UserException HEALTH_PROFILE_NOT_FOUND = new UserException(ErrorCode.HEALTH_PROFILE_NOT_FOUND);
    public static final UserException HEALTH_PROFILE_ALREADY_EXISTS = new UserException(ErrorCode.HEALTH_PROFILE_ALREADY_EXISTS);
    
    private UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
