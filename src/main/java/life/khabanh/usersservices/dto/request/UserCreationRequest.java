package life.khabanh.usersservices.dto.request;

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
    String email;

    @Size(min = 8, message = "Password must be at least 8 characters.")
    String password;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String inviteCode;
}
