package hu.modm.todo.service;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.repository.UserNotFoundException;
import hu.modm.todo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class UsersService {
    private UsersRepository repository;
    private UserMapper userMapper;

    public UserDto createUser(CreateUserCommand command){
        var user = userMapper.toEntity(command);
        repository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto findUserById(long id) {
        return userMapper.toDto(repository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }
}
