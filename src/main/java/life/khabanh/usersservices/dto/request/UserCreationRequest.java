package life.khabanh.usersservices.dto.request;

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
public class UserCreationRequest {

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid email format.")
    String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password;

    @NotBlank(message = "First name cannot be blank.")
    String firstName;

    @NotBlank(message = "Last name cannot be blank.")
    String lastName;

    @Past(message = "Date of birth must be a past date.")
    LocalDate dateOfBirth;

    String inviteCode;
}
