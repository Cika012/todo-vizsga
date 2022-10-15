package hu.modm.todo.controller;

import hu.modm.todo.dto.*;
import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;
import hu.modm.todo.repository.ToDosRepository;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoControllerIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ToDosRepository toDosRepository;

    @BeforeEach
    void deleteAll() {
        toDosRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    void changeStatus_shouldUpdateStatusAndReturnOK() {
        long userId = createUser();
        ToDoDto todoResult = createTodo(userId);
        long todoId = todoResult.getId();

        var changeStatusCommand = new ChangeStatusCommand(Status.WORKING_ON_IT);

        webClient
                .put()
                .uri("/api/todos/{id}", todoId)
                .bodyValue(changeStatusCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ToDoDto.class).value(e -> assertEquals(changeStatusCommand.getStatus(), e.getStatus()));
    }

    @Test
    void changeStatus_OnNotExistingTodo_shouldReturnNotFound() {
        var changeStatusCommand = new ChangeStatusCommand(Status.WORKING_ON_IT);

        webClient
                .put()
                .uri("/api/todos/1")
                .bodyValue(changeStatusCommand)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("detail").isEqualTo("ToDo not found with id: " + 1);
    }

    private ToDoDto createTodo(long userId) {
        var createTodoCommand = new CreateTodoCommand(
                "Make a test",
                LocalDate.now().plusDays(1),
                Importance.IMPORTANT);

       return webClient
                .post()
                .uri("/api/users/{id}/todos", userId)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ToDoDto.class).value(e -> assertEquals(createTodoCommand.getDescription(), e.getDescription()))
                .returnResult().getResponseBody();
    }

    private long createUser() {
        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand("sample@asdf.com"))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();
        return userResult.getId();
    }
}
