package codebrew.doctorgeondam.repository.jpa;

import codebrew.doctorgeondam.domain.User.UserId;
import codebrew.doctorgeondam.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    
    Optional<UserEntity> findByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    boolean existsByEmail(String email);
}
