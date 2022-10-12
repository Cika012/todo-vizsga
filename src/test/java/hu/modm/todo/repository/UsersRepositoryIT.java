package hu.modm.todo.repository;

import hu.modm.todo.entity.User;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsersRepositoryIT {

    @Autowired
    UsersRepository repository;

    @Test
    void createUserTest() {
        var user = new User();
        String expectedEmail = "sample@gmail.com";
        user.setEmail(expectedEmail);
        repository.save(user);

        var users = repository.findAll();

        assertThat(users)
                .extracting(User::getEmail)
                .contains(expectedEmail);
    }
}