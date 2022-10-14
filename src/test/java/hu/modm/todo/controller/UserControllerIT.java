package hu.modm.todo.controller;

import hu.modm.todo.dto.CreateTodoCommand;
import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.ToDoDto;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.repository.ToDosRepository;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

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
    void createUser_withValidData_shouldReturnCreated() {
        String expectedEmail = "sample@asdf.com";

        var result = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(expectedEmail))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserDto.class).value(e -> assertEquals(expectedEmail, e.getEmail()))
                .returnResult().getResponseBody();
        assertNotNull(result.getId());
    }

    @Test
    void createUser_withInvalidEmail_shouldReturnBadRequest() {
        var result = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand("emai @aasdf.com"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Email is not valid");

    }

    @Test
    void createUser_withEmptyEmail_shouldReturnBadRequest() {
        webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(""))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Email cannot be empty");
    }


    @Test
    void createTodo_withValidData_shouldReturnCreated() {
        String expectedEmail = "sample@asdf.com";

        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(expectedEmail))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();

        long id = userResult.getId();

        var createTodoCommand = new CreateTodoCommand(
                "Make a test",
                LocalDate.now().plusDays(1),
                Importance.IMPORTANT);

        var todoResult = webClient
                .post()
                .uri("/api/users/{id}/todos", id)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ToDoDto.class).value(e -> assertEquals(createTodoCommand.getDescription(), e.getDescription()))
                .returnResult().getResponseBody();
    }

    @Test
    void createTodo_withEmptyDescription_shouldReturnBadRequest() {
        String expectedEmail = "sample@asdf.com";

        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(expectedEmail))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();

        long id = userResult.getId();

        var createTodoCommand = new CreateTodoCommand("", LocalDate.now().plusDays(1), Importance.IMPORTANT);

        var todoResult = webClient
                .post()
                .uri("/api/users/{id}/todos", id)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Description cannot be empty");
    }

    @Test
    void createTodo_withNotFutureDate_shouldReturnBadRequest() {
        String expectedEmail = "sample@asdf.com";

        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(expectedEmail))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();

        long id = userResult.getId();

        var createTodoCommand = new CreateTodoCommand("Watch Back to the future", LocalDate.now().minusDays(2), Importance.IMPORTANT);

        var todoResult = webClient
                .post()
                .uri("/api/users/{id}/todos", id)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Deadline must be in the future");
    }

    @Test
    void createTodo_withPresentDate_shouldReturnBadRequest() {
        String expectedEmail = "sample@asdf.com";

        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(expectedEmail))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();

        long id = userResult.getId();

        var createTodoCommand = new CreateTodoCommand("Watch Back to the future", LocalDate.now(), Importance.IMPORTANT);

        var todoResult = webClient
                .post()
                .uri("/api/users/{id}/todos", id)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Deadline must be in the future");
    }

    @Test
    void createTodo_withNotExistingUser_shouldReturnNotFound() {
        var createTodoCommand = new CreateTodoCommand("Watch Back to the future", LocalDate.now().plusDays(1), Importance.IMPORTANT);
        long id = 1;
        var todoResult = webClient
                .post()
                .uri("/api/users/{id}/todos", id)
                .bodyValue(createTodoCommand)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("detail").isEqualTo("User not found with id: "+id);
    }
}
