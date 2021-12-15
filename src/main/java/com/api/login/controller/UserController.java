package com.api.login.controller;

import com.api.login.entity.User;
import com.api.login.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity getByName(@PathVariable String name) {
        User user = (User) service.loadUserByUsername(name);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        User user = service.getById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity postUser(@RequestBody User user) {
        service.postUser(user);
        return ResponseEntity.ok().build();
    }

}
