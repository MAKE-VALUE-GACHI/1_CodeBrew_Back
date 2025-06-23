package codebrew.doctorgeondam.service.sms;

import codebrew.doctorgeondam.exception.CommunicationException;
import codebrew.doctorgeondam.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        try {
            simulateSmsApiCall(phoneNumber, message);
            log.info("SMS 전송 성공: {} -> {}", phoneNumber, message);
        } catch (Exception e) {
            log.error("SMS 전송 실패: {} -> {}", phoneNumber, message, e);
            throw CommunicationException.SMS_SEND_FAILED;
        }
    }

    public String sendAuthCode(String phoneNumber) {
        String authCode = generateAuthCode();
        String message = String.format("[닥터건담] 인증번호는 [%s]입니다.", authCode);
        
        sendSms(phoneNumber, message);
        return authCode;
    }

    private String generateAuthCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private void simulateSmsApiCall(String phoneNumber, String message) {
        if (new Random().nextInt(10) == 0) {
            throw new RuntimeException("SMS API 호출 실패");
        }
        
        if (phoneNumber == null || !phoneNumber.matches("^010-?\\d{4}-?\\d{4}$")) {
            throw SystemException.INVALID_INPUT;
        }
    }
}
