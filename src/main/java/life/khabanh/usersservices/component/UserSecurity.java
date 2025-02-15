package life.khabanh.usersservices.component;

import life.khabanh.usersservices.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import life.khabanh.usersservices.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean isOwner(String userId) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElse(null);

        return user != null && user.getEmail().equals(authenticatedEmail);
    }
}

