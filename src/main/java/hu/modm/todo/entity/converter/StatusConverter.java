package hu.modm.todo.entity.converter;

import hu.modm.todo.entity.enums.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public Status convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return Stream.of(Status.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}
