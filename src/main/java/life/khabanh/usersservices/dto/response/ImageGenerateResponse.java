package life.khabanh.usersservices.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageGenerateResponse {
    @Schema(description = "Result's ID", example = "89ebr-3245tjsdfdsf-3du37f3")
    String resultId;

    @Schema(description = "Generated Image (Base64)", example = "data:image/png;base64,[BASE64_ENCODED_DATA]")
    String result;

    @Schema(description = "Visualization Image (Base64)", example = "data:image/png;base64,[BASE64_ENCODED_DATA]")
    String visualization;
}
