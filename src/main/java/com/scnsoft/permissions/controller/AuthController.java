package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.permission.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("signIn")
    public ResponseEntity signIn(@RequestBody UserDTO user) {
        return ResponseEntity.ok(authenticationService.signIn(user));
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserDTO user) {
        return ResponseEntity.ok(authenticationService.signUp(user));
    }
}