package hu.modm.todo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_email", unique = true)
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ToDo> todos;

    public User(String email) {
        this.email = email;
    }
}
