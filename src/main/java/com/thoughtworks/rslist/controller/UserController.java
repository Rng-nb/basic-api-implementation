package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void postUserList(@RequestBody @Valid User user) {
        userService.insertUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getUserById(@PathVariable int userId) {
        User userReturn = userService.getUserById(userId);
        return ResponseEntity.ok(userReturn);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }
}
