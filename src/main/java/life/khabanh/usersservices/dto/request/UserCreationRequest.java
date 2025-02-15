package life.khabanh.usersservices.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Schema(description = "User's email", example = "example@gmail.com")
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid email format.")
    String email;

    @Schema(description = "User's password", example = "Khabanh@UIT2025")
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password;

    @Schema(description = "User's first name", example = "Khoa")
    @NotBlank(message = "First name cannot be blank.")
    String firstName;

    @Schema(description = "User's last name", example = "Cao")
    @NotBlank(message = "Last name cannot be blank.")
    String lastName;

    @Schema(description = "User's date of birth", example = "2005-06-29")
    @NotBlank(message = "Date of birth cannot be blank.")
    @Past(message = "Date of birth must be a past date.")
    LocalDate dateOfBirth;

    @Schema(description = "Invite code", example = "INVITE_CODE_2025")
    String inviteCode;
}
