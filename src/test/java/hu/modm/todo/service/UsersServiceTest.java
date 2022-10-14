package hu.modm.todo.service;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.entity.User;
import hu.modm.todo.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    UserMapper mapper;

    @Mock
    UsersRepository usersRepository;

    @InjectMocks
    UsersService service;

    @Test
    void createUserTest() {
        long expectedId = 1;
        String expectedEmail = "sample@email.com";

        var createUserCommand = new CreateUserCommand(expectedEmail);

        when(mapper.toEntity(any(CreateUserCommand.class))).thenAnswer(x->new User(null, ((CreateUserCommand)x.getArguments()[0]).getEmail()));
        when(mapper.toDto(any(User.class))).thenAnswer(x->new UserDto(((User)x.getArguments()[0]).getId(), ((User)x.getArguments()[0]).getEmail()));

        when(usersRepository.save(any(User.class))).thenAnswer(i -> {
            ((User)i.getArguments()[0]).setId(expectedId);
            return i.getArguments()[0];
            }
        );
        var userDto = service.createUser(createUserCommand);

        assertEquals(expectedId, userDto.getId());
        assertEquals(expectedEmail, userDto.getEmail());
    }
}