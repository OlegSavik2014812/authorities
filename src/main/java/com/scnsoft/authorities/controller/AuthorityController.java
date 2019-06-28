package com.scnsoft.authorities.controller;

import com.scnsoft.authorities.dto.AuthorityDTO;
import com.scnsoft.authorities.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("authorities")
public class AuthorityController {
    private final AuthService authService;

    public AuthorityController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("get")
    public List<AuthorityDTO> getAuthorities() {
        return authService.findAll();
    }

    @PostMapping("postAuthority")
    public AuthorityDTO postAuthority(@RequestBody AuthorityDTO authorityDTO) {
        return authService.saveEntity(authorityDTO).orElseThrow(RuntimeException::new);
    }

    @GetMapping("{id}")
    public AuthorityDTO getById(@RequestParam Long id) {
        return authService.findById(id).orElseThrow(RuntimeException::new);
    }
}
