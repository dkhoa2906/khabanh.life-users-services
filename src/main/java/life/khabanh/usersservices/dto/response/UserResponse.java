package life.khabanh.usersservices.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    @Schema(description = "User's UUID", example = "8ddc8dc7-fcfa-4bf3-98c2-5c56a6b779b3")
    String id;

    @Schema(description = "User's email", example = "example@gmail.com")
    String email;

    @Schema(description = "User's first name", example = "Khoa")
    String firstName;

    @Schema(description = "User's last name", example = "Cao")
    String lastName;

    @Schema(description = "User's date of birth", example = "2005-06-29")
    LocalDate dateOfBirth;

    @Schema(description = "Invite code", example = "INVITE_CODE_2025")
    String inviteCode;

    @Schema(description = "User's roles", example = "[\"USER\", \"TESTER\"]")
    Set<String> roles;

    @Schema(description = "User's credit balance", example = "10")
    int credit;
}
