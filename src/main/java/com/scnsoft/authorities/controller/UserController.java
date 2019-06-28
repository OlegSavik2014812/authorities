package com.scnsoft.authorities.controller;

import com.scnsoft.authorities.dto.UserDTO;
import com.scnsoft.authorities.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("signIn")
    public UserDTO signIn(@RequestBody UserDTO userDTO) {
        return userService.signIn(userDTO);
    }

    @PostMapping("signUp")
    public UserDTO signUp(@RequestBody UserDTO user) {
        return userService.signUp(user);
    }

    @GetMapping("delete/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }

    @GetMapping(path = "assignGroup", params = {"login", "groupName"})
    public UserDTO assignGroup(@RequestParam String login, @RequestParam String groupName) {
        userService.assignGroup(login, groupName);
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }
}
