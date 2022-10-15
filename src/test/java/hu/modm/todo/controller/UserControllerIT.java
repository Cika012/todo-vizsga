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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        var result = sendCreateUserWithPost(expectedEmail)
                .expectStatus().isCreated()
                .expectBody(UserDto.class).value(e -> assertEquals(expectedEmail, e.getEmail()))
                .returnResult().getResponseBody();
        assertNotNull(result.getId());
    }

    @Test
    void createUser_withInvalidEmail_shouldReturnBadRequest() {
        sendCreateUserWithPost("emai @aasdf.com")
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Email is not valid");
    }

    @Test
    void createUser_withEmptyEmail_shouldReturnBadRequest() {
        sendCreateUserWithPost("")
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Email cannot be empty");
    }


    @Test
    void createTodo_withValidData_shouldReturnCreated() {
        long userId = createUser("sample@asdf.com");
        String expectedDescription = "Make a test";

        sendCreateTodoWithPost(userId, expectedDescription, LocalDate.now().plusDays(1), Importance.IMPORTANT)
                .expectStatus().isCreated()
                .expectBody(ToDoDto.class).value(e -> assertEquals(expectedDescription, e.getDescription()));
    }

    @Test
    void createTodo_withEmptyDescription_shouldReturnBadRequest() {
        long userId = createUser("sample@asdf.com");

        sendCreateTodoWithPost(userId, "", LocalDate.now().plusDays(1), Importance.IMPORTANT)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Description cannot be empty");
    }

    @Test
    void createTodo_withPastDate_shouldReturnBadRequest() {
        long userId = createUser("sample@asdf.com");

        sendCreateTodoWithPost(userId, "Watch Back to the future", LocalDate.now().minusDays(2), Importance.IMPORTANT)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Deadline must be in the future");
    }

    @Test
    void createTodo_withPresentDate_shouldReturnBadRequest() {
        long userId = createUser("sample@asdf.com");

        sendCreateTodoWithPost(userId, "Watch Back to the future", LocalDate.now(), Importance.IMPORTANT)
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("violations[0].message").isEqualTo("Deadline must be in the future");
    }

    @Test
    void createTodo_withNotExistingUser_shouldReturnNotFound() {
        long userId = 1;
        sendCreateTodoWithPost(userId, "Buy cat food", LocalDate.now().plusDays(1), Importance.IMPORTANT)
                .expectStatus().isNotFound()
                .expectBody().jsonPath("detail").isEqualTo("User not found with id: " + userId);
    }

    @Test
    void getToDos_existingUserWithOneTodo_shouldReturnTodoDtoList() {
        var userId = createUser("sample@email.com");
        sendCreateTodoWithPost(userId, "task 1", LocalDate.now().plusDays(1), Importance.NON_URGENT);

        getTodosOfUser(userId)
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(1)
                .value(e -> assertEquals("task 1", e.get(0).getDescription()));
    }

    @Test
    void getToDos_existingUserWithThreeTodos_shouldReturnListInImportanceOrder() {
        var userId = createUser("sample@email.com");
        sendCreateTodoWithPost(userId, "task 1", LocalDate.now().plusDays(1), Importance.NON_URGENT);
        sendCreateTodoWithPost(userId, "task 2", LocalDate.now().plusDays(2), Importance.URGENT);
        sendCreateTodoWithPost(userId, "task 3", LocalDate.now().plusDays(2), Importance.IMPORTANT);
        getTodosOfUser(userId)
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(3)
                .value(e -> assertEquals(Importance.URGENT, e.get(0).getImportance()))
                .value(e -> assertEquals(Importance.IMPORTANT, e.get(1).getImportance()))
                .value(e -> assertEquals(Importance.NON_URGENT, e.get(2).getImportance()));
    }

    @Test
    void getToDos_onNewTodosWithStatusFilteredToDONE_shouldReturnEmptyList() {
        var userId = createUser("sample@email.com");
        sendCreateTodoWithPost(userId, "task 1", LocalDate.now().plusDays(1), Importance.NON_URGENT);

        webClient.get()
                .uri("/api/users/{id}/todos?status=DONE", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(0);
    }

    @Test
    void getToDos_onNewTodosWithStatusFilteredTo_NOT_STARTED_shouldReturnListContainingOnlyItemsWithStatus_NOT_STARTED() {
        var userId = createUser("sample@email.com");
        sendCreateTodoWithPost(userId, "task 1", LocalDate.now().plusDays(1), Importance.NON_URGENT);

        webClient.get()
                .uri("/api/users/{id}/todos?status=NOT_STARTED", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(1)
                .value(e -> assertEquals(Status.NOT_STARTED, e.get(0).getStatus()));
    }

    @Test
    void getToDos_FilteredTo_DONE_shouldReturnListContainingOnlyItemsWithStatus_DONE() {
        var userId = createUser("sample@email.com");
        sendCreateTodoWithPost(userId, "task 1", LocalDate.now().plusDays(1), Importance.NON_URGENT);
        var todoDto = sendCreateTodoWithPost(userId, "task 2", LocalDate.now().plusDays(1), Importance.NON_URGENT)
                .expectBody(ToDoDto.class)
                .returnResult().getResponseBody();

        var changeStatusCommand = new ChangeStatusCommand(Status.DONE);
        webClient
                .put()
                .uri("/api/todos/{id}", todoDto.getId())
                .bodyValue(changeStatusCommand)
                .exchange();

        webClient.get()
                .uri("/api/users/{id}/todos?status=DONE", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(1)
                .value(e -> assertEquals(Status.DONE, e.get(0).getStatus()));
    }

    @Test
    void getToDos_existingUserWithNoTodos_shouldReturnEmptyList() {
        var userId = createUser("sample@email.com");

        getTodosOfUser(userId)
                .expectStatus().isOk()
                .expectBodyList(ToDoDto.class)
                .hasSize(0);
    }

    @Test
    void getToDos_notExistingUser_shouldReturnNotFound() {
        var userId = 1;

        getTodosOfUser(userId)
                .expectStatus().isNotFound()
                .expectBody().jsonPath("detail").isEqualTo("User not found with id: " + userId);
    }

    private WebTestClient.ResponseSpec getTodosOfUser(long userId) {
        return webClient.get()
                .uri("/api/users/{id}/todos", userId)
                .exchange();
    }

    private long createUser(String email) {
        var userResult = webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(email))
                .exchange()
                .expectBody(UserDto.class)
                .returnResult().getResponseBody();

        return userResult.getId();
    }

    private WebTestClient.ResponseSpec sendCreateTodoWithPost(long userId, String description, LocalDate deadLine, Importance importance) {
        var createTodoCommand = new CreateTodoCommand(
                description,
                deadLine,
                importance);

        return webClient
                .post()
                .uri("/api/users/{id}/todos", userId)
                .bodyValue(createTodoCommand)
                .exchange();
    }

    private WebTestClient.ResponseSpec sendCreateUserWithPost(String email) {
        return webClient
                .post()
                .uri("/api/users")
                .bodyValue(new CreateUserCommand(email))
                .exchange();
    }
}
