package hu.modm.todo.repository;

import hu.modm.todo.entity.ToDo;
import hu.modm.todo.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDosRepository extends JpaRepository<ToDo, Long> {

    @Query("select t from ToDo t where t.user.id = :userId")
    List<ToDo> getToDosOfUser(long userId);

    @Query("select t from ToDo t where t.user.id = :userId and t.status = :status")
    List<ToDo> getToDosOfUserWithStatus(long userId, Status status);
}
