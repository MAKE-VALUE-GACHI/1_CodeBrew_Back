package codebrew.doctorgeondam.repository.jpa;

import codebrew.doctorgeondam.entity.AuthCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthCodeJpaRepository extends JpaRepository<AuthCodeEntity, Long> {

    /**
     * 전화번호와 유효시간으로 인증 코드 조회
     */
    Optional<AuthCodeEntity> findByPhoneNumberAndExpiredAtAfter(String phoneNumber, LocalDateTime now);

    /**
     * 전화번호와 검증 완료 상태, 유효시간으로 인증 코드 조회
     */
    Optional<AuthCodeEntity> findByPhoneNumberAndVerifiedTrueAndExpiredAtAfter(String phoneNumber, LocalDateTime now);

    /**
     * 만료된 인증 코드 삭제
     */
    @Modifying
    @Query("DELETE FROM AuthCodeEntity a WHERE a.expiredAt < :expiredTime")
    int deleteByExpiredAtBefore(@Param("expiredTime") LocalDateTime expiredTime);
}
