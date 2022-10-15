package hu.modm.todo.service;

import hu.modm.todo.dto.ChangeStatusCommand;
import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.entity.enums.Status;
import hu.modm.todo.repository.ToDoNotFoundException;
import hu.modm.todo.repository.ToDosRepository;
import hu.modm.todo.repository.UserNotFoundException;
import hu.modm.todo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class ToDosService {
    private UsersRepository usersRepository;
    private ToDoMapper toDoMapper;
    private ToDosRepository toDosRepository;

    @Transactional
    public ToDoDto createTodo(long userId, CreateTodoCommand command){
        var todo = toDoMapper.toEntity(command);
        todo.setStatus(Status.NOT_STARTED);
        var user = usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        todo.setUser(user);
        toDosRepository.save(todo);
        return toDoMapper.toDto(todo);
    }

    public ToDoDto changeStatus(long todoId, ChangeStatusCommand command) {
        var todoEntity = toDosRepository.findById(todoId).orElseThrow(()->new ToDoNotFoundException("ToDo not found with id: " + todoId));
        todoEntity.setStatus(command.getStatus());
        toDosRepository.save(todoEntity);
        return toDoMapper.toDto(todoEntity);
    }
}