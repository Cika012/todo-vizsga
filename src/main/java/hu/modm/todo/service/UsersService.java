package hu.modm.todo.service;

import hu.modm.todo.dto.CreateUserCommand;
import hu.modm.todo.dto.UserDto;
import hu.modm.todo.entity.User;
import hu.modm.todo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class UsersService {
    private UsersRepository repository;
    private UserMapper mapper;

    public UserDto createUser(CreateUserCommand command){
        var user = mapper.toEntity(command);
        repository.save(user);
        return mapper.toDto(user);
    }
}
