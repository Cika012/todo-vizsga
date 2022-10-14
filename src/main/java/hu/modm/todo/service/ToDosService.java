package hu.modm.todo.service;

import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.entity.enums.Status;
import hu.modm.todo.repository.ToDosRepository;
import hu.modm.todo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ToDosService {
    private UsersRepository usersRepository;
    private ToDoMapper toDoMapper;
    private ToDosRepository toDosRepository;

    public ToDoDto createTodo(long userId, CreateTodoCommand command){
        var todo = toDoMapper.toEntity(command);
        todo.setStatus(Status.NOT_STARTED);
        var user = usersRepository.getReferenceById(userId);
        todo.setUser(user);
        toDosRepository.save(todo);
        return toDoMapper.toDto(todo);
    }
}