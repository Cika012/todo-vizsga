package hu.modm.todo.service;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.entity.User;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Autowired
    UserMapper mapper;

    @Mock
    UsersRepository usersRepository;

    @InjectMocks
    UsersService service;

    @Test
    void createUserTest() {
        var createUserCommand = new CreateUserCommand("sample@email.com");

        Mockito.when(usersRepository.save((User)notNull())).thenAnswer(i -> {
            ((User)i.getArguments()[0]).setId((long)1);
            return i.getArguments()[0];
            }
        );
        var userDto = service.createUser(createUserCommand);

    }
}