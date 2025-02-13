package life.khabanh.usersservices.controller;

import life.khabanh.usersservices.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestController {
    @GetMapping
    ApiResponse<String> test() {
        return ApiResponse.<String>builder()
                .result("Test OK")
                .build();
    }
}

