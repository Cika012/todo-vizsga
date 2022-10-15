package hu.modm.todo.controller;

import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.entity.enums.Status;
import hu.modm.todo.service.ToDosService;
import hu.modm.todo.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UsersService usersService;
    private ToDosService toDosService;

    @GetMapping("{id}")
    public UserDto findUserById(@PathVariable("id") long id) {
        return usersService.findUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserCommand command, UriComponentsBuilder uri) {
        var user = usersService.createUser(command);
        return ResponseEntity
                .created(uri.path("/api/users/{id}").buildAndExpand(user.getId()).toUri())
                .body(user);
    }

    @PostMapping("{userId}/todos")
    public ResponseEntity<ToDoDto> createToDo(@PathVariable("userId") long userId,
                                              @Valid @RequestBody CreateTodoCommand command,
                                              UriComponentsBuilder uri) {
        var toDoDto = toDosService.createTodo(userId, command);
        return ResponseEntity
                .created(uri.path("/api/users/{userId}/todo/{todoId}").buildAndExpand(userId, toDoDto.getId()).toUri())
                .body(toDoDto);
    }

    @GetMapping("{userId}/todos")
    public List<ToDoDto> getToDos(@PathVariable("userId") long id, @RequestParam("status") Optional<Status> status) {
        var toDoList =  toDosService.getTodosOfUser(id, status);
        if(toDoList == null) return new ArrayList<>();
        else
            return toDoList;
    }
}
