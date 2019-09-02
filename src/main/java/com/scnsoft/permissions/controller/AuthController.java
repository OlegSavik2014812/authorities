package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.permission.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("signIn")
    public ResponseEntity signIn(@Validated @NotNull @RequestBody UserDTO user) {
        return ResponseEntity.ok(authenticationService.signIn(user));
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@Validated @NotNull @RequestBody UserDTO user) {
        return ResponseEntity.ok(authenticationService.signUp(user));
    }
}