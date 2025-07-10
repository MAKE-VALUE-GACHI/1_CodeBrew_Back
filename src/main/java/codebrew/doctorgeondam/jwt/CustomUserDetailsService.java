package codebrew.doctorgeondam.jwt;

import codebrew.doctorgeondam.domain.User;
import codebrew.doctorgeondam.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userService.findByPhoneNumber(phoneNumber);
        return CustomUserDetails.from(user.toUserEntity());
    }
}
