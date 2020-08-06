package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void should_add_userList_when_post_give_userPath() throws Exception {
        User user = new User("wyf",  20,"male", "a@b.com", "11234567890");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void name_should_less_8() throws Exception {
        User user = new User("wyffffffff", 20,"male",  "a@b.com", "11234567890");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(3)
    public void gender_should_not_null() throws Exception {
        User user = new User("wyf",20, null,  "a@b.com", "11234567890");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(4)
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("wyf",  17,"male", "a@b.com", "11234567890");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(5)
    public void mail_should_suit_format() throws Exception {
        User user = new User("wyf", 20, "male", "a.b.com", "11234567890");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(6)
    public void phone_should_suit_format() throws Exception {
        User user = new User("wyf",  20,"male", "a@b.com", "112345678910");
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


    @Test
    @Order(7)
    public void should_register_user() throws Exception {
        User user = new User("dtotest", 20, "male", "a@b.com", "11234567890");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        List<UserDto> userDtos = userRepository.findAll();
        assertEquals(2, userDtos.size());
        assertEquals("dtotest", userDtos.get(1).getUserName());
        assertEquals(20, userDtos.get(1).getAge());
        assertEquals("male", userDtos.get(1).getGender());
        assertEquals("a@b.com", userDtos.get(1).getEmail());
        assertEquals("11234567890", userDtos.get(1).getPhone());
    }

    @Test
    @Order(8)
    public void should_return_user_when_post_give_id() throws Exception {
        User user = new User("insert1", 20, "male", "a@b.com", "11234567890");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserDto> userDtos = userRepository.findAll();
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.user_name", is(userDtos.get(0).getUserName())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/2"))
                .andExpect(jsonPath("$.user_name",is(userDtos.get(1).getUserName())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/3"))
                .andExpect(jsonPath("$.user_name",is(userDtos.get(2).getUserName())))
                .andExpect(status().isOk());
    }
//
//    @Test
//    @Order(9)
//    public void should_delete_user_when_delete_give_id() {
//        mockM
//    }
}
