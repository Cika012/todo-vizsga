package hu.modm.todo.controller;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UsersService service;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserCommand command, UriComponentsBuilder uri) {
        var user = service.createUser(command);
        return ResponseEntity
                .created(uri.path("/api/users/{id}").buildAndExpand(user.getId()).toUri())
                .body(user);
    }
}
