package codebrew.doctorgeondam.exception;

public class CommunicationException extends CoreException {
    
    public static final CommunicationException EXTERNAL_API_ERROR = new CommunicationException(ErrorCode.EXTERNAL_API_ERROR);
    public static final CommunicationException SMS_SEND_FAILED = new CommunicationException(ErrorCode.SMS_SEND_FAILED);
    public static final CommunicationException EMAIL_SEND_FAILED = new CommunicationException(ErrorCode.EMAIL_SEND_FAILED);
    
    private CommunicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
