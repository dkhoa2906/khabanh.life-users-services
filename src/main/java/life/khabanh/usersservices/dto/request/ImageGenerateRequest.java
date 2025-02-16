package life.khabanh.usersservices.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageGenerateRequest {
    @NotNull
    MultipartFile faceImage;

    @NotNull
    MultipartFile hairShape;

    @NotNull
    MultipartFile hairColor;
}