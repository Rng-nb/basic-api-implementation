package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
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
    private UserRepository userRepository;
    private RsEventRepository rsEventRepository;
    @Autowired
    public UserService(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    public int insertUser(User user) {
        UserDto userDto = userRepository.save(UserDto.builder().userName(user.getName()).age(user.getAge())
                .email(user.getEmail()).gender(user.getGender()).phone(user.getPhone())
                .voteNum(user.getVoteNum()).build());
        return userDto.getId();
    }

    public List<User> getUserList() {
        List<UserDto> userDtoList = userRepository.findAll();
        List<User> userList = userDtoList.stream().map(
                    item -> User.builder().name(item.getUserName()).age(item.getAge()).gender(item.getGender())
                            .email(item.getEmail()).phone(item.getPhone()).build()).collect(Collectors.toList());
        return userList;
    }

    public User getUserById(int id) {
        UserDto userDto = userRepository.getAllById(id);
        User userReturn = User.builder().name(userDto.getUserName()).age(userDto.getAge()).gender(userDto.getGender())
                .email(userDto.getEmail()).phone(userDto.getPhone()).voteNum(userDto.getVoteNum()).build();
        return userReturn;
    }

    public void deleteUserById(int id) {
        userRepository.deleteAllById(id);
    }
}
