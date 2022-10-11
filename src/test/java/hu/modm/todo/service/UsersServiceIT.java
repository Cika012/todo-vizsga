package hu.modm.todo.service;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsersServiceIT {

    @Autowired
    UsersService service;

    @Test
    void createUserTest() {
        var command = new CreateUserCommand("sample@gmail.com");
        var resultDto = service.createUser(command);
        assertEquals("sample@gmail.com", resultDto.getEmail());
    }
}