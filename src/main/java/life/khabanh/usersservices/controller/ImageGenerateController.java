package life.khabanh.usersservices.controller;

import life.khabanh.usersservices.dto.request.ImageGenerateRequest;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.ImageGenerateResponse;
import life.khabanh.usersservices.service.ImageGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/hair-swap")
@RequiredArgsConstructor
public class ImageGenerateController {
    private final ImageGenerateService imageGenerateService;

    @PostMapping(value = "/generate", consumes = "multipart/form-data")
    public ApiFormResponse<ImageGenerateResponse> generateHairSwapResult(
            @RequestPart("face_image") MultipartFile faceImage,
            @RequestPart("hair_shape") MultipartFile hairShape,
            @RequestPart("hair_color") MultipartFile hairColor
    ) throws IOException {
        ImageGenerateRequest request = ImageGenerateRequest.builder()
                .faceImage(faceImage)
                .hairShape(hairShape)
                .hairColor(hairColor)
                .build();

        ImageGenerateResponse response = imageGenerateService.generate(request);

        return ApiFormResponse.<ImageGenerateResponse>builder()
                .result(response)
                .build();
    }
}

