package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.ToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToothConverter implements EntityConverter<Tooth, ToothDTO> {
    private final ToothRepository toothRepository;

    @Override
    public ToothDTO toDTO(Tooth entity) {
        ToothDTO toothDTO = new ToothDTO();
        toothDTO.setId(entity.getId());
        toothDTO.setType(entity.getType().toString());
        return toothDTO;
    }

    @Override
    public Tooth toPersistence(ToothDTO entityDTO) {
        throw new UnsupportedOperationException("Unable to convert tooth to persistence entity");
    }
}