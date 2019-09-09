package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ToothConverter;
import com.scnsoft.permissions.dto.dental.ToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class ToothService extends BaseCrudService<Tooth, ToothDTO, Long> {
    private final ToothRepository toothRepository;
    private final ToothConverter toothConverter;

    public ToothService(ToothRepository repository, ToothConverter converter) {
        super(repository, converter);
        this.toothRepository = repository;
        this.toothConverter = converter;
    }

    @Override
    public ToothDTO save(ToothDTO entityDTO) {
        throw new UnsupportedOperationException("Unable to create another tooth. Human jaw can't support more than 32 teeth");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Unable to delete general tooth entity. Tooth entity is for UserTooth entity usage ");
    }

    public ToothDTO getTooth(Long toothNumber) {
        return toothRepository.findById(toothNumber)
                .map(toothConverter::toDTO)
                .orElseThrow(() -> new UnsupportedOperationException("Invalid tooth number. Unable to load tooth"));
    }
}