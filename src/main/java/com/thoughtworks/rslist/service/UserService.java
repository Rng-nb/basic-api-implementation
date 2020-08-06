package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public int insertUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setVoteNum(user.getVoteNum());
        userRepository.save(userDto);
        return userDto.getId() + 1;
    }

    public List<User> getUserList() {
        List<UserDto> userDtoList = userRepository.findAll();
        List<User> userList = userDtoList.stream().map(
                    item -> new User(item.getUserName(), item.getAge(), item.getGender(),
                            item.getEmail(), item.getPhone())).collect(Collectors.toList());
        return userList;
    }

    public User getUserById(int id) {
        UserDto userDto = userRepository.getAllById(id);
        User userReturn = new User(userDto.getUserName(), userDto.getAge(), userDto.getGender(),
                userDto.getEmail(), userDto.getPhone());
        return userReturn;
    }
}
