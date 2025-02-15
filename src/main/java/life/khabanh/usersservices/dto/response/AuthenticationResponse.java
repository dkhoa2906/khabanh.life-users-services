package life.khabanh.usersservices.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {

    @Schema(description = "Indicates whether the user is authenticated", example = "true")
    boolean isAuthenticated;

    @Schema(description = "JWT token used for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token;

    @Schema(description = "JWT refresh token used to obtain a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken;
}
