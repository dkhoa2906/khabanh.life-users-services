package life.khabanh.usersservices.mapper;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
}
