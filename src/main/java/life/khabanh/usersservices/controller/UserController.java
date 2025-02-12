package life.khabanh.usersservices.controller;

import jakarta.validation.Valid;
import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.dto.response.ApiResponse;
import life.khabanh.usersservices.dto.response.UserResponse;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping("/new")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid  UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();

    }

}
