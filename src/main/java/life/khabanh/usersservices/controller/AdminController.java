package life.khabanh.usersservices.controller;

import life.khabanh.usersservices.dto.request.InviteCodeCreationRequest;
import life.khabanh.usersservices.dto.response.ApiResponse;
import life.khabanh.usersservices.entity.InviteCode;
import life.khabanh.usersservices.repository.InviteCodeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    InviteCodeRepository inviteCodeRepository;

    @PostMapping("/invite-code")
    public ApiResponse<InviteCode> createInviteCode(@RequestBody InviteCodeCreationRequest request) {
        InviteCode inviteCode = InviteCode.builder()
                .code(request.getCode())
                .type(request.getType())
                .creditAdd(request.getCreditAdd())
                .build();

        return ApiResponse.<InviteCode>builder()
                .result(inviteCodeRepository.save(inviteCode))
                .build();
    }
}
