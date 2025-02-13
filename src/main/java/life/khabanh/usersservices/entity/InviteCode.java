package life.khabanh.usersservices.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Table(name = "invite_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;

    // "01" for adding credits only
    // "11" for adding Admin role and credits
    // "21" for adding Tester role and credits
    String type;

    @Builder.Default
    int creditAdd = 0;

}
