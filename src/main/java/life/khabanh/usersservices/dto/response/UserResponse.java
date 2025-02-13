package life.khabanh.usersservices.dto.response;

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
    String id;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String inviteCode;
    Set<String> roles;
    int credit;
}
