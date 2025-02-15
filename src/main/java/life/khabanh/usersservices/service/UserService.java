package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.dto.request.UserUpdateRequest;
import life.khabanh.usersservices.dto.response.UserResponse;
import life.khabanh.usersservices.entity.InviteCode;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.enums.Role;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.exception.ErrorCode;
import life.khabanh.usersservices.mapper.UserMapper;
import life.khabanh.usersservices.repository.InviteCodeRepository;
import life.khabanh.usersservices.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class UserService {
    UserRepository userRepository;
    InviteCodeRepository inviteCodeRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setDateOfCreation(LocalDate.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        // Process invite code
        if (request.getInviteCode() != null && !request.getInviteCode().isEmpty())
            if (inviteCodeRepository.existsByCode(request.getInviteCode())){
                var inviteCode = inviteCodeRepository.findByCode(request.getInviteCode());
                user.setCredit(user.getCredit() + inviteCode.getCreditAdd());

                switch (inviteCode.getType().charAt(0)){
                    case '1':
                        roles.add(Role.ADMIN.name());
                        break;
                    case '2':
                        roles.add(Role.TESTER.name());
                        break;
                }
            }

        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

//    @PostAuthorize("(returnObject.email == authentication.name) or hasRole('ADMIN')")
//    public UserResponse getUser(String id) {
//        return userMapper.toUserResponse(userRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
//    }

    public UserResponse getUser(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated User: " + authentication.getName()); // Debug

        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }



    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userMapper.toUserResponse(userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @PreAuthorize("@userSecurity.isOwner(#id) or hasRole('ADMIN')")
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("@userSecurity.isOwner(#id)")
    public UserResponse deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.deleteById(id);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }
}