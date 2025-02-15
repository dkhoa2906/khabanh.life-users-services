package life.khabanh.usersservices.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.UserResponse;

@Schema(name = "UserResponseWrapper", description = "Response wrapper for user information")
public class UserResponseWrapper extends ApiFormResponse<UserResponse> {
}

