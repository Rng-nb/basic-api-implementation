package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class UserController {
    private static List<User> userList = new LinkedList<User>();

    @Autowired
    UserRepository userRepository;

    public static void insertUser(User user) {
        userList.add(user);
    }

    public static List<User> getUserList() {
        return userList;
    }
    @PostMapping("/user")
    public void postUserList(@RequestBody @Valid User user) {
        UserDto userDto = new UserDto();
        userDto.setUserName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setVoteNum(user.getVoteNum());
        userRepository.save(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userList);
    }
}
