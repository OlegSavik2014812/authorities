package com.scnsoft.permissions.service.dentistry;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.converter.ToothConverter;
import com.scnsoft.permissions.converter.UserToothConverter;
import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.dto.ToothDTO;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
public class UserToothService extends BaseCrudService<UserTooth, UserToothDTO, Long> {
    private final UserToothRepository userToothRepository;
    private final UserToothConverter userToothConverter;
    private final ToothRepository toothRepository;
    private final ToothConverter toothConverter;
    private final ComplaintConverter complaintConverter;

    @Autowired
    public UserToothService(UserToothRepository userToothRepository,
                            UserToothConverter userToothConverter,
                            ToothRepository toothRepository,
                            ToothConverter toothConverter,
                            ComplaintConverter complaintConverter) {
        super(userToothRepository, userToothConverter);
        this.userToothRepository = userToothRepository;
        this.userToothConverter = userToothConverter;
        this.toothRepository = toothRepository;
        this.toothConverter = toothConverter;
        this.complaintConverter = complaintConverter;
    }

    public List<UserToothDTO> getUserTeeth(Long userId) {
        return Optional.ofNullable(userId)
                .map(userToothRepository::findAllByUserId)
                .map(Lists::newArrayList)
                .map(Collections::unmodifiableList)
                .orElse(Collections.emptyList())
                .stream()
                .map(userToothConverter::toDTO)
                .collect(Collectors.toList());
    }

    public Long save(UserToothDTO userToothDTO) {
        if (Objects.isNull(userToothDTO)) {
            return 0L;
        }
        UserTooth userTooth = Optional.of(userToothDTO)
                .map(userToothConverter::toPersistence)
                .map(userToothRepository::save)
                .orElseGet(UserTooth::new);

        Long id = userTooth.getId();
        UnaryOperator<ComplaintDTO> operator = complaintDTO -> {
            complaintDTO.setUserToothId(id);
            return complaintDTO;
        };
        List<Complaint> complaints = userToothDTO.getComplaints().stream()
                .map(operator)
                .map(complaintConverter::toPersistence).collect(Collectors.toList());
        userTooth.setComplaints(complaints);
        UserTooth complainedTooth = userToothRepository.save(userTooth);
        return complainedTooth.getId();
    }

    public ToothDTO getTooth(Long toothNumber) {
        if (Objects.isNull(toothNumber) || toothNumber < 0 || toothNumber > 32) {
            return null;
        }
        return toothRepository.findById(toothNumber).map(toothConverter::toDTO).orElse(null);
    }

    @Override
    public Collection<UserToothDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
