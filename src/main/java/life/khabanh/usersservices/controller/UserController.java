package life.khabanh.usersservices.controller;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/new-user")
    User createUser(@RequestBody UserCreationRequest request) {
        return userService.createUser(request);
    }
}
