package hu.modm.todo.dto;

import hu.modm.todo.entity.enums.Importance;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateTodoCommand {
    private String description;
    private LocalDate deadline;
    private Importance importance;
}
