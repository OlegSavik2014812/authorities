package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.EntityConverter;
import com.scnsoft.permissions.dto.BaseDentalRequestDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.BaseDentalRequest;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.repository.CrudRepository;

import java.util.function.BiFunction;

public class DentalRequestService<T extends BaseDentalRequest, K extends BaseDentalRequestDTO> extends BaseCrudService<T, K, Long> {
    private final UserToothRepository userToothRepository;
    private final CrudRepository<T, Long> crudRepository;

    public DentalRequestService(CrudRepository<T, Long> repository, EntityConverter<T, K> converter, UserToothRepository userToothRepository) {
        super(repository, converter);
        this.crudRepository = repository;
        this.userToothRepository = userToothRepository;
    }

    void save(K entity, BiFunction<UserTooth, String, T> converter) {
        String description = entity.getDescription();
        if (Strings.isBlank(description)) {
            return;
        }
        userToothRepository.findById(entity.getUserToothId())
                .ifPresent(userTooth ->
                        crudRepository.save(
                                converter.apply(userTooth, description)));

    }
}
