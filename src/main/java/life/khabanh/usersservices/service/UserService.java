package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setInviteCode(request.getInviteCode());
        return userRepository.save(user);
    }
}
