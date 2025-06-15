package codebrew.doctorgeondam.repository.jpa;

import codebrew.doctorgeondam.domain.User.UserId;
import codebrew.doctorgeondam.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UserId> {

}
