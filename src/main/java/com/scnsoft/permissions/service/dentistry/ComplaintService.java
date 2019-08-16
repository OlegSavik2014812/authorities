package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.repository.dentistry.ComplaintRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComplaintService extends BaseCrudService<Complaint, ComplaintDTO, Long> {

    private final UserToothRepository userToothRepository;
    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository repository, ComplaintConverter converter, UserToothRepository userToothRepository) {
        super(repository, converter);
        this.complaintRepository = repository;
        this.userToothRepository = userToothRepository;
    }

    public void complain(ComplaintDTO complaintDTO) {
        String problem = complaintDTO.getDescription();
        if (Objects.isNull(problem) || problem.trim().isEmpty()) {
            return;
        }
        Long userToothId = complaintDTO.getUserToothId();
        LocalDate date = Optional.ofNullable(complaintDTO.getDate()).orElseGet(LocalDate::now);
        userToothRepository.findById(userToothId)
                .ifPresent(tooth ->
                        complaintRepository.save(Complaint
                                .complain()
                                .on(tooth)
                                .describe(problem)
                                .when(date)
                                .build())
                );
    }

    @Override
    public Collection<ComplaintDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}