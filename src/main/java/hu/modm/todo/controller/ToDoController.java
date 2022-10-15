package hu.modm.todo.controller;

import hu.modm.todo.dto.ChangeStatusCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.service.ToDosService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
@Slf4j
public class ToDoController {

    ToDosService service;

    @PutMapping("/{todoId}")
    public ToDoDto changeStatus(@PathVariable("todoId") long todoId,
                                                @RequestBody ChangeStatusCommand command) {
        return service.changeStatus(todoId, command);
    }
}
