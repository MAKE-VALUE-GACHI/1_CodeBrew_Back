package codebrew.doctorgeondam.service.auth;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.entity.AuthCodeEntity;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.exception.UserException;
import codebrew.doctorgeondam.exception.AuthException;
import codebrew.doctorgeondam.repository.jpa.AuthCodeJpaRepository;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import codebrew.doctorgeondam.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final AuthCodeJpaRepository authCodeJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    
    private static final int AUTH_CODE_EXPIRY_MINUTES = 5;

    public Long saveUser(User user) {
        if (userJpaRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw UserException.PHONE_NUMBER_ALREADY_EXISTS;
        }
        
        if (user.getEmail() != null && userJpaRepository.existsByEmail(user.getEmail())) {
            throw UserException.EMAIL_ALREADY_EXISTS;
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        UserEntity saveUser = userJpaRepository.save(user.toUserEntity());
        return saveUser.getId();
    }

    public void sendAuthCode(String phoneNumber) {
        authCodeJpaRepository.findByPhoneNumberAndExpiredAtAfter(phoneNumber, LocalDateTime.now())
                .ifPresent(authCodeJpaRepository::delete);

        String authCode = smsService.sendAuthCode(phoneNumber);
        
        AuthCodeEntity authCodeEntity = AuthCodeEntity.builder()
                .phoneNumber(phoneNumber)
                .code(authCode)
                .expiredAt(LocalDateTime.now().plusMinutes(AUTH_CODE_EXPIRY_MINUTES))
                .verified(false)
                .build();
        
        authCodeJpaRepository.save(authCodeEntity);
        log.info("인증 코드 전송 및 저장 완료: {}", phoneNumber);
    }

    public boolean verifyAuthCode(String phoneNumber, String inputCode) {
        AuthCodeEntity authCodeEntity = authCodeJpaRepository
                .findByPhoneNumberAndExpiredAtAfter(phoneNumber, LocalDateTime.now())
                .orElseThrow(() -> AuthException.CODE_NOT_FOUND);

        if (authCodeEntity.isVerified()) {
            throw AuthException.CODE_EXPIRED;
        }

        if (!authCodeEntity.getCode().equals(inputCode)) {
            throw AuthException.CODE_MISMATCH;
        }

        authCodeEntity.setVerified(true);
        authCodeJpaRepository.save(authCodeEntity);
        
        log.info("인증 코드 검증 완료: {}", phoneNumber);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean isPhoneNumberVerified(String phoneNumber) {
        return authCodeJpaRepository
                .findByPhoneNumberAndVerifiedTrueAndExpiredAtAfter(phoneNumber, LocalDateTime.now())
                .isPresent();
    }

    public void cleanupExpiredAuthCodes() {
        int deletedCount = authCodeJpaRepository.deleteByExpiredAtBefore(LocalDateTime.now());
        log.info("만료된 인증 코드 정리 완료: {} 개", deletedCount);
    }
}