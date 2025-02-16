package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.HairSwapRequest;
import life.khabanh.usersservices.dto.response.HairSwapResultResponse;
import life.khabanh.usersservices.dto.response.InviteCodeResponse;
import life.khabanh.usersservices.entity.InviteCode;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class ImageGenerateService {
//    public HairSwapResultResponse generateHairSwapResult(HairSwapRequest request) {
//
//    }
}
