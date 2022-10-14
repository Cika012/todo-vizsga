package hu.modm.todo.dto;

import hu.modm.todo.entity.enums.Importance;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateTodoCommand {
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;
    private Importance importance;
}
