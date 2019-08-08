package com.scnsoft.permissions.service;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.converter.UserToothConverter;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserToothService extends BaseCrudService<UserTooth, UserToothDTO, Long> {
    private final UserToothRepository userToothRepository;
    private final UserToothConverter userToothConverter;

    @Autowired
    public UserToothService(UserToothRepository userToothRepository, UserToothConverter userToothConverter) {
        super(userToothRepository, userToothConverter);
        this.userToothRepository = userToothRepository;
        this.userToothConverter = userToothConverter;
    }

    public List<UserToothDTO> getUserTeeth(Long id) {
        return Optional.ofNullable(id)
                .map(userToothRepository::findAllByUserId)
                .map(Lists::newArrayList)
                .map(Collections::unmodifiableList)
                .orElse(Collections.emptyList())
                .stream()
                .map(userToothConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<UserToothDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
