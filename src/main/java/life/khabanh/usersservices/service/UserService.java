package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.dto.response.ApiResponse;
import life.khabanh.usersservices.dto.response.UserResponse;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.exception.ErrorCode;
import life.khabanh.usersservices.mapper.UserMapper;
import life.khabanh.usersservices.repository.UserRepository;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public ApiResponse<UserResponse> createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXSITED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .message("User created successfully")
                .result(userMapper.toUserResponse(user))
                .build();

        return apiResponse;
    }
}