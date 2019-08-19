package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.ComplaintRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import static java.time.LocalDate.now;

@Service
public class ComplaintService extends DentalRequestService<Complaint, ComplaintDTO> {
    public ComplaintService(ComplaintRepository complaintRepository, ComplaintConverter converter, UserToothRepository userToothRepository) {
        super(complaintRepository, converter, userToothRepository);
    }

    public void complain(ComplaintDTO complaintDTO) {
        BiFunction<UserTooth, String, Complaint> converter =
                (tooth, problem) ->
                        Complaint.complain()
                                .about(tooth)
                                .when(now())
                                .describe(problem)
                                .build();
        save(complaintDTO, converter);
    }
}