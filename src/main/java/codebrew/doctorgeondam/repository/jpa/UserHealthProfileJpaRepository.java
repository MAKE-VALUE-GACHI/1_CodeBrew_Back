package codebrew.doctorgeondam.repository.jpa;

import codebrew.doctorgeondam.entity.UserHealthProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHealthProfileJpaRepository extends JpaRepository<UserHealthProfileEntity, Long> {
    
    Optional<UserHealthProfileEntity> findByUserId(Long userId);
    
    boolean existsByUserId(Long userId);
}
