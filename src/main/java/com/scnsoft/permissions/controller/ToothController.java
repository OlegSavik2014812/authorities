package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.ToothDTO;
import com.scnsoft.permissions.service.dentistry.UserToothService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "teeth")
@RequestMapping("teeth")
public class ToothController {
    private final UserToothService userToothService;

    public ToothController(UserToothService userToothService) {
        this.userToothService = userToothService;
    }

    @GetMapping("{id}")
    public ToothDTO getTooth(@PathVariable("id") Long id) {
        return userToothService.getTooth(id);
    }
}