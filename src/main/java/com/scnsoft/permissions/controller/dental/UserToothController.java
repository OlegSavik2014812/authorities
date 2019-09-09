package com.scnsoft.permissions.controller.dental;

import com.scnsoft.permissions.dto.dental.ComplaintDTO;
import com.scnsoft.permissions.dto.dental.TreatmentDTO;
import com.scnsoft.permissions.dto.dental.UserToothDTO;
import com.scnsoft.permissions.service.dentistry.ComplaintService;
import com.scnsoft.permissions.service.dentistry.TreatmentService;
import com.scnsoft.permissions.service.dentistry.UserToothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "user_teeth")
@RequestMapping("user_teeth")
@RequiredArgsConstructor
public class UserToothController {
    private final UserToothService userToothService;
    private final ComplaintService complaintService;
    private final TreatmentService treatmentService;

    @PostMapping()
    public ResponseEntity postUserTooth(@RequestBody UserToothDTO entity) {
        UserToothDTO save1 = userToothService.save(entity);
        return ResponseEntity.ok(Map.of("userTooth", save1));
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
    public UserToothDTO getUserTooth(@RequestParam Long id) {
        return userToothService.findById(id).orElse(UserToothDTO.builder().build());
    }

    @PostMapping("/delete")
    public void deleteById(@RequestParam Long id) {
        userToothService.deleteById(id);
    }

    @GetMapping("{id}")
    public List<UserToothDTO> getUserTeeth(@PathVariable("id") Long id) {
        return userToothService.getUserTeeth(id);
    }
}