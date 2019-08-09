package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.service.dentistry.UserToothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "user_teeth")
@RequestMapping("teeth")
public class UserToothController {
    private final UserToothService userToothService;

    @Autowired
    public UserToothController(UserToothService userToothService) {
        this.userToothService = userToothService;
    }

    @PostMapping()
    public UserToothDTO postGroup(@RequestBody UserToothDTO entity) {
        userToothService.saveEntity(entity);
        return userToothService.findById(entity.getId()).orElseGet(() -> UserToothDTO.builder().build());
    }

    @GetMapping("{id}")
    public List<UserToothDTO> getUserTeeth(@PathVariable("id") Long id) {
        return userToothService.getUserTeeth(id);
    }
}
