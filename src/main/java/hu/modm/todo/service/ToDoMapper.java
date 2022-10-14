package hu.modm.todo.service;

import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.entity.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ToDoMapper {
    ToDo toEntity(CreateTodoCommand command);
    ToDoDto toDto(ToDo entity);
}
