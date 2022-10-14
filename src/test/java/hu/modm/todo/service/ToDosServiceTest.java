package hu.modm.todo.service;

import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.entity.ToDo;
import hu.modm.todo.entity.User;
import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;
import hu.modm.todo.repository.ToDosRepository;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToDosServiceTest {

    @Mock
    ToDoMapper mapper;

    @Mock
    UsersRepository usersRepository;
    @Mock
    ToDosRepository toDosRepository;

    @InjectMocks
    ToDosService service;

    @Test
    void createTodo() {
        var user = new User(1L, "asd@asd.com", null);
        var command = new CreateTodoCommand("Sample todo", LocalDate.now().plusDays(1), Importance.IMPORTANT);
        var entity = new ToDo();
        entity.setDescription(command.getDescription());
        entity.setDeadline(command.getDeadline());
        entity.setImportance(command.getImportance());

        when(usersRepository.findById(any())).thenReturn(Optional.of(user));
        when(mapper.toEntity(any(CreateTodoCommand.class))).thenReturn(entity);
        when(mapper.toDto(any(ToDo.class))).thenAnswer(
                x->{
                    var todo = ((ToDo) x.getArguments()[0]);
                    ToDoDto toDoDto = new ToDoDto();
                    toDoDto.setDeadline(todo.getDeadline());
                    toDoDto.setDescription((todo.getDescription()));
                    toDoDto.setStatus(todo.getStatus());
                    toDoDto.setImportance(todo.getImportance());
                    toDoDto.setId((long)1);
                    return toDoDto;
                }
        );

        when(toDosRepository.save(any(ToDo.class))).thenAnswer(i -> {
                    ((ToDo)i.getArguments()[0]).setId(1L);
                    return i.getArguments()[0];
                }
        );

        var todo = service.createTodo(1, command);

        assertNotNull(todo.getId());
        assertEquals(1L, todo.getId());
        assertEquals(command.getDescription(), todo.getDescription());
        assertEquals(Status.NOT_STARTED, todo.getStatus());
    }
}