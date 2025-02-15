package life.khabanh.usersservices.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Schema(description = "User's password", example = "Khabanh@UIT2025")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password;

    @Schema(description = "User's first name", example = "Khoa")
    String firstName;

    @Schema(description = "User's last name", example = "Cao")
    String lastName;

    @Schema(description = "User's date of birth", example = "2005-06-29")
    @Past(message = "Date of birth must be a past date.")
    LocalDate dateOfBirth;
}
