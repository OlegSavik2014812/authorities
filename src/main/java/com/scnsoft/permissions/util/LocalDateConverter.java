package com.scnsoft.permissions.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDate date) {
        return Timestamp.valueOf(date.atStartOfDay());
    }

    @Override
    public LocalDate convertToEntityAttribute(Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalDate();
    }
}
