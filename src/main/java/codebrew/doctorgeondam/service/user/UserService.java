package codebrew.doctorgeondam.service.user;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.entity.UserEntity;
import codebrew.doctorgeondam.exception.UserException;
import codebrew.doctorgeondam.exception.AuthException;
import codebrew.doctorgeondam.repository.jpa.UserJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long id) {
        UserEntity userEntity = userJpaRepository.findById(id)
                .orElseThrow(() -> UserException.NOT_FOUND);
        
        return User.from(userEntity);
    }

    public User findByPhoneNumber(String phoneNumber) {
        UserEntity userEntity = userJpaRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> UserException.NOT_FOUND);
        
        return User.from(userEntity);
    }

    public User findByEmail(String email) {
        UserEntity userEntity = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> UserException.NOT_FOUND);
        
        return User.from(userEntity);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userJpaRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Transactional
    public User updateUser(Long userId, User updateUser) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> UserException.NOT_FOUND);

        if (updateUser.getEmail() != null && 
            !updateUser.getEmail().equals(userEntity.getEmail()) &&
            userJpaRepository.existsByEmail(updateUser.getEmail())) {
            throw UserException.EMAIL_ALREADY_EXISTS;
        }

        if (updateUser.getName() != null) {
            userEntity.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            userEntity.setEmail(updateUser.getEmail());
        }
        if (updateUser.getBirthDate() != null) {
            userEntity.setBirthDate(updateUser.getBirthDate());
        }
        if (updateUser.getGender() != null) {
            userEntity.setGender(updateUser.getGender());
        }

        UserEntity savedEntity = userJpaRepository.save(userEntity);
        return User.from(savedEntity);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> UserException.NOT_FOUND);

        if (!passwordEncoder.matches(currentPassword, userEntity.getPassword())) {
            throw AuthException.INVALID_CREDENTIALS;
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userJpaRepository.save(userEntity);
    }
    public Optional<UserEntity> loadUserEntityByPhoneNumber(String phoneNumber) {
        return userJpaRepository.findByPhoneNumber(phoneNumber);
    }
}