package codebrew.doctorgeondam.service.auth;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSaveService {

    private final UserJpaRepository userJpaRepository;

    public Long save(User user) {
        UserEntity saveUser = userJpaRepository.save(user.toUserEntity());
        return saveUser.getId();
    }
}
