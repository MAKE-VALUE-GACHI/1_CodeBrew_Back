package codebrew.doctorgeondam.exception;

public class AuthException extends CoreException {
    
    public static final AuthException INVALID_CREDENTIALS = new AuthException(ErrorCode.INVALID_CREDENTIALS);
    public static final AuthException ACCESS_TOKEN_EXPIRED = new AuthException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    public static final AuthException REFRESH_TOKEN_EXPIRED = new AuthException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    public static final AuthException INVALID_TOKEN = new AuthException(ErrorCode.INVALID_TOKEN);
    public static final AuthException UNAUTHORIZED_ACCESS = new AuthException(ErrorCode.UNAUTHORIZED_ACCESS);
    
    public static final AuthException CODE_NOT_FOUND = new AuthException(ErrorCode.AUTH_CODE_NOT_FOUND);
    public static final AuthException CODE_EXPIRED = new AuthException(ErrorCode.AUTH_CODE_EXPIRED);
    public static final AuthException CODE_MISMATCH = new AuthException(ErrorCode.AUTH_CODE_MISMATCH);
    
    private AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
