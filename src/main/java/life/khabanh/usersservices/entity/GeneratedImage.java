package life.khabanh.usersservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "generated_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId;

    @Lob
    byte[] hairSwapResult;

    @Lob
    byte[] hairSwapVisualize;
}
