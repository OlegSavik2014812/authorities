package com.scnsoft.permissions.controller.dental;

import com.scnsoft.permissions.dto.dental.ToothDTO;
import com.scnsoft.permissions.service.dentistry.ToothService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "teeth")
@RequestMapping("teeth")
public class ToothController {
    private final ToothService toothService;

    public ToothController(ToothService toothService) {
        this.toothService = toothService;
    }

    @GetMapping("{id}")
    @Cacheable(cacheNames = "tooth")
    public ToothDTO getTooth(@PathVariable("id") Long id) {
        return toothService.getTooth(id);
    }
}