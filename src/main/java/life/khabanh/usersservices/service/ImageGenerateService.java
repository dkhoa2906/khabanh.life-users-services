package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.ImageGenerateRequest;
import life.khabanh.usersservices.dto.response.ImageGenerateResponse;
import life.khabanh.usersservices.entity.GeneratedImage;
import life.khabanh.usersservices.entity.User;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.repository.GeneratedImageRepository;
import life.khabanh.usersservices.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import life.khabanh.usersservices.exception.ErrorCode;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageGenerateService {
    GeneratedImageRepository generatedImageRepository;
    UserRepository userRepository;

    RestTemplate restTemplate = new RestTemplate();
    String FASTAPI_URL = "https://55d0-34-19-22-219.ngrok-free.app";

//    @PostAuthorize. from bearer get email, from email get user, from user get creditbalance and if this success the -=1

    public ImageGenerateResponse generate(ImageGenerateRequest request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCredit() < 1) {
            throw new RuntimeException("Not enough credits to generate image");
        }

        byte[] faceImage = request.getFaceImage().getBytes();
        byte[] hairShape = request.getHairShape().getBytes();
        byte[] hairColor = request.getHairColor().getBytes();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("face_image", new ByteArrayResource(faceImage) {
            @Override
            public String getFilename() {
                return "face.png";
            }
        });
        body.add("hair_shape", new ByteArrayResource(hairShape) {
            @Override
            public String getFilename() {
                return "shape.png";
            }
        });
        body.add("hair_color", new ByteArrayResource(hairColor) {
            @Override
            public String getFilename() {
                return "color.png";
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        byte[] result = generateResult(body, headers);
        byte[] visualization = generateVisualization(body, headers);

        var generatedResult = GeneratedImage.builder()
                .userId(user.getId())
                .hairSwapResult(result)
                .hairSwapVisualize(visualization)
                .build();

        String processId = generatedImageRepository.save(generatedResult).getId();
        user.setCredit(user.getCredit() - 1);
        userRepository.save(user);

        return ImageGenerateResponse.builder()
                .resultId(processId)
                .result("data:image/png;base64," + Base64.getEncoder().encodeToString(result))
                .visualization("data:image/png;base64," + Base64.getEncoder().encodeToString(visualization))
                .build();
    }

    private byte[] generateResult(MultiValueMap<String, Object> body, HttpHeaders headers) throws IOException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                FASTAPI_URL.concat("/swap_hair"),
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else throw new IOException("Failed to process image, status: " + response.getStatusCode());

    }

    private byte[] generateVisualization(MultiValueMap<String, Object> body, HttpHeaders headers) throws IOException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                FASTAPI_URL.concat("/swap_hair_visual"),
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else throw new IOException("Failed to process image, status: " + response.getStatusCode());
    }

}
