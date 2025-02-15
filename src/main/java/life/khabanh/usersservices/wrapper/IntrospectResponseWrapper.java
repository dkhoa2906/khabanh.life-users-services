package life.khabanh.usersservices.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.AuthenticationResponse;

@Schema(name = "IntrospectResponseWrapper", description = "Response wrapper for introspect")
public class IntrospectResponseWrapper extends ApiFormResponse<AuthenticationResponse> {
}

