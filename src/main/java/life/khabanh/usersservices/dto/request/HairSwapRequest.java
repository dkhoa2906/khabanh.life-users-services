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
public class HairSwapRequest {
    @NotNull
    MultipartFile face_image;

    @NotNull
    MultipartFile hair_shape;

    @NotNull
    MultipartFile hair_color;

    @NotNull
    String token;
}