package hu.modm.todo.entity.converter;

import hu.modm.todo.entity.enums.Importance;
import hu.modm.todo.entity.enums.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ImportanceConverter implements AttributeConverter<Importance, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Importance status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public Importance convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return Stream.of(Importance.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}
