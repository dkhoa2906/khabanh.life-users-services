package life.khabanh.usersservices.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic API Response Wrapper")
public class ApiFormResponse<T> {

    @Builder.Default
    @Schema(description = "HTTP status code", example = "200")
    int code = 200;

    @Builder.Default
    @Schema(description = "Response message", example = "OK")
    String message = "OK";

    @Schema(description = "Payload of the response")
    T result;
}

