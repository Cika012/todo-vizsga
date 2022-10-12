package hu.modm.todo.repository;
import hu.modm.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDosRepository extends JpaRepository<User, Long> {
}
