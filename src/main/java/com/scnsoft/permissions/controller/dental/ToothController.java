package com.scnsoft.permissions.controller.dental;

import com.scnsoft.permissions.dto.dental.ToothDTO;
import com.scnsoft.permissions.service.dentistry.ToothService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController(value = "teeth")
@RequestMapping("teeth")
@RequiredArgsConstructor
public class ToothController {
    private final ToothService toothService;

    @GetMapping("{id}")
    @Cacheable(cacheNames = "tooth")
    public ToothDTO getTooth(@PathVariable("id") Long id) {
        return toothService.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("Invalid tooth number. Unable to load tooth"));
    }

    @GetMapping
    @Cacheable(cacheNames = "teeth")
    public List<ToothDTO> getTeeth() {
        return toothService.findAll();
    }
}