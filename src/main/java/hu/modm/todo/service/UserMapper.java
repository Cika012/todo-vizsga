package hu.modm.todo.service;

import hu.modm.todo.dto.*;
import hu.modm.todo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(CreateUserCommand command);
    UserDto toDto(User entity);


}
