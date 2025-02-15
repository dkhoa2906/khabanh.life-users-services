package life.khabanh.usersservices.mapper;

import life.khabanh.usersservices.dto.request.UserCreationRequest;
import life.khabanh.usersservices.dto.request.UserUpdateRequest;
import life.khabanh.usersservices.dto.response.UserResponse;
import life.khabanh.usersservices.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}