package hu.modm.todo.repository;
import hu.modm.todo.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDosRepository extends JpaRepository<ToDo, Long> {
}
