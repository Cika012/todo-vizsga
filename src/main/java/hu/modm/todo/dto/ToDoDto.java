package hu.modm.todo.dto;

import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDto {
    private Long id;
    private String description;
    private LocalDate deadline;
    private Importance importance;
    private Status status;
}
