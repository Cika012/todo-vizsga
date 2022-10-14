package hu.modm.todo.repository;

import hu.modm.todo.entity.ToDo;
import hu.modm.todo.entity.User;
import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ToDosRepositoryIT {

    @Autowired
    ToDosRepository toDosRepository;
    @Autowired
    UsersRepository usersRepository;



    @Test
     void createToDo() {
        var user = new User("sample@test.com");
        usersRepository.save(user);

        String expectedDescription = "create integration test";

        var todo = new ToDo(null,
                expectedDescription,
                LocalDate.of(2022,10,14),
                Importance.IMPORTANT,
                Status.NOT_STARTED,
                user);

        toDosRepository.save(todo);

        var todos = toDosRepository.findAll();
        assertEquals(1, todos.size());
        assertThat(todos).isNotNull();
        assertThat(todos)
                .extracting(ToDo::getDescription)
                .contains(expectedDescription);
    }
}
