package hu.modm.todo.dto;


import hu.modm.todo.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusCommand {
    private Status status;
}
