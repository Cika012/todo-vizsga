package hu.modm.todo.entity;

import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    private Importance importance;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_STARTED;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
