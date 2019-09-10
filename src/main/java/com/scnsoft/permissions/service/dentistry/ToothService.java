package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ToothConverter;
import com.scnsoft.permissions.dto.dental.ToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToothService extends BaseCrudService<Tooth, ToothDTO, Long> {
    public ToothService(ToothRepository repository, ToothConverter converter) {
        super(repository, converter);
    }

    @Override
    public ToothDTO save(ToothDTO entityDTO) {
        throw new UnsupportedOperationException("Unable to create another tooth. Human jaw can't support more than 32 teeth");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Unable to delete tooth entity. Tooth entity is only for UserTooth entity usage ");
    }

    @Override
    public Optional<ToothDTO> findById(Long id) {
        if (id < 1L || id > 32L) {
            throw new UnsupportedOperationException("Invalid tooth number. Unable to load tooth");
        }
        return super.findById(id);
    }

    @Override
    public long count() {
        return 32L;
    }
}