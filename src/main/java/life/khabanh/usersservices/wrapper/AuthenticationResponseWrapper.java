package life.khabanh.usersservices.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.AuthenticationResponse;

@Schema(name = "AuthenticationResponseWrapper", description = "Response wrapper for authentication")
public class AuthenticationResponseWrapper extends ApiFormResponse<AuthenticationResponse> {
}

