package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.ToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import org.springframework.stereotype.Component;

@Component
public class ToothConverter implements EntityConverter<Tooth, ToothDTO> {
    private final ToothRepository toothRepository;

    public ToothConverter(ToothRepository toothRepository) {
        this.toothRepository = toothRepository;
    }

    @Override
    public ToothDTO toDTO(Tooth entity) {
        ToothDTO toothDTO = new ToothDTO();
        toothDTO.setId(entity.getId());
        toothDTO.setType(entity.getType().toString());
        return toothDTO;
    }

    @Override
    public Tooth toPersistence(ToothDTO entity) {
        return toothRepository.findById(entity.getId()).orElse(null);
    }
}