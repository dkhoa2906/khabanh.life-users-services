package life.khabanh.usersservices.mapper;

import life.khabanh.usersservices.dto.request.InviteCodeCreationRequest;
import life.khabanh.usersservices.dto.response.InviteCodeResponse;
import life.khabanh.usersservices.entity.InviteCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviteCodeMapper {
    InviteCode toInviteCode(InviteCodeCreationRequest request);
    InviteCodeResponse toInviteCodeResponse(InviteCode inviteCode);
}
