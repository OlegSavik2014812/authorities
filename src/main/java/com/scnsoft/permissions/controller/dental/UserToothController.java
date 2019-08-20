package com.scnsoft.permissions.controller.dental;

import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.service.dentistry.ComplaintService;
import com.scnsoft.permissions.service.dentistry.TreatmentService;
import com.scnsoft.permissions.service.dentistry.UserToothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "user_teeth")
@RequestMapping("user_teeth")
public class UserToothController {
    private final UserToothService userToothService;
    private final ComplaintService complaintService;
    private final TreatmentService treatmentService;

    @Autowired
    public UserToothController(UserToothService userToothService, ComplaintService complaintService, TreatmentService treatmentService) {
        this.userToothService = userToothService;
        this.complaintService = complaintService;
        this.treatmentService = treatmentService;
    }

    @PostMapping()
    public ResponseEntity postUserTooth(@RequestBody UserToothDTO entity) {
        Long save = userToothService.save(entity);
        return ResponseEntity.ok(Map.of("userToothId", save));
    }

    @PostMapping("/complain")
    public void complain(@RequestBody ComplaintDTO complaintDTO) {
        complaintService.complain(complaintDTO);
    }

    @PostMapping("/treat")
    public void treat(@RequestBody TreatmentDTO treatmentDTO) {
        treatmentService.treat(treatmentDTO);
    }

    @GetMapping()
    public UserToothDTO getUserTooth(@RequestParam(value = "id") Long id) {
        return userToothService.findById(id).orElse(UserToothDTO.builder().build());
    }

    @GetMapping("{id}")
    public List<UserToothDTO> getUserTeeth(@PathVariable("id") Long id) {
        return userToothService.getUserTeeth(id);
    }
}