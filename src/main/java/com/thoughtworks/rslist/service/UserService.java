package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    public int insertUser(User user) {
        UserDto userDto = userRepository.save(UserDto.builder().userName(user.getName()).age(user.getAge())
                .email(user.getEmail()).gender(user.getGender()).phone(user.getPhone())
                .voteNum(user.getVoteNum()).build());
        return userDto.getId();
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

    public void deleteUserById(int id) {
        userRepository.deleteAllById(id);
    }
}
