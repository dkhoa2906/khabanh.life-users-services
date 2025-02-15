package life.khabanh.usersservices.controller;

import life.khabanh.usersservices.dto.request.InviteCodeCreationRequest;
import life.khabanh.usersservices.dto.response.ApiFormResponse;
import life.khabanh.usersservices.dto.response.InviteCodeResponse;
import life.khabanh.usersservices.service.InviteCodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invite-code")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InviteCodeController {
    InviteCodeService inviteCodeService;

    @PostMapping("/new")
    public ApiFormResponse<InviteCodeResponse> createInviteCode(@RequestBody InviteCodeCreationRequest request) {
        return ApiFormResponse.<InviteCodeResponse>builder()
                .result(inviteCodeService.createInviteCode(request))
                .build();
    }
}
