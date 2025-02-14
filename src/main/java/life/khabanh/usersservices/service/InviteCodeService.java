package life.khabanh.usersservices.service;

import life.khabanh.usersservices.dto.request.InviteCodeCreationRequest;
import life.khabanh.usersservices.dto.response.InviteCodeResponse;
import life.khabanh.usersservices.entity.InviteCode;
import life.khabanh.usersservices.exception.AppException;
import life.khabanh.usersservices.exception.ErrorCode;
import life.khabanh.usersservices.mapper.InviteCodeMapper;
import life.khabanh.usersservices.repository.InviteCodeRepository;
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
public class InviteCodeService {
    InviteCodeRepository inviteCodeRepository;
    InviteCodeMapper inviteCodeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public InviteCodeResponse createInviteCode(InviteCodeCreationRequest request) {
        if (inviteCodeRepository.existsByCode(request.getCode()))
            throw new AppException(ErrorCode.INVITE_CODE_EXISTED);

        InviteCode inviteCode = inviteCodeMapper.toInviteCode(request);

        return inviteCodeMapper.toInviteCodeResponse(inviteCodeRepository.save(inviteCode));
    }
}
