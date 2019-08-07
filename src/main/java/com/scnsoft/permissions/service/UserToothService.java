package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserToothConverter;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserToothService extends BaseCrudService<UserTooth, UserToothDTO, Long> {

    @Autowired
    public UserToothService(UserToothRepository userToothRepository, UserToothConverter userToothConverter) {
        super(userToothRepository, userToothConverter);
    }

    @Override
    public Collection<UserToothDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
