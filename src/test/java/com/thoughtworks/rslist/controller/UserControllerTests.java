package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void should_add_userList_when_post_give_userPath() throws Exception {
        User user = User.builder().name("wyf").age(20).gender("male").email("a@b.com").phone("11234567890").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void name_should_less_8() throws Exception {
        User user = User.builder().name("wyffffffff").age(20).gender("male").email("a@b.com").phone("11234567890").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(3)
    public void gender_should_not_null() throws Exception {
        User user = User.builder().name("wyf").age(20).gender(null).email("a@b.com").phone("11234567890").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(4)
    public void age_should_between_18_and_100() throws Exception {
        User user = User.builder().name("wyf").age(17).gender("male").email("a@b.com").phone("11234567890").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(5)
    public void mail_should_suit_format() throws Exception {
        User user = User.builder().name("wyf").age(20).gender("male").email("a.b.com").phone("11234567890").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    @Order(6)
    public void phone_should_suit_format() throws Exception {
        User user = User.builder().name("wyf").age(20).gender("male").email("a@b.com").phone("112345678910").build();
        objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


    @Test
    @Order(7)
    public void should_register_user() throws Exception {
        User user = User.builder().name("dtotest").age(20).gender("male").email("a@b.com").phone("11234567890").build();
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
        User user = User.builder().name("insert1").age(20).gender("male").email("a@b.com").phone("11234567890").build();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<UserDto> userDtoList = userRepository.findAll();
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.user_name", is(userDtoList.get(0).getUserName())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/2"))
                .andExpect(jsonPath("$.user_name",is(userDtoList.get(1).getUserName())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/3"))
                .andExpect(jsonPath("$.user_name",is(userDtoList.get(2).getUserName())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void should_delete_user_when_delete_give_id() throws Exception {
        UserDto userDto = UserDto.builder().voteNum(10).phone("11234567890").gender("male").email("a@b.com")
                .age(20).userName("xx").build();
        RsEventDto rsEventDto = RsEventDto.builder().userDto(userDto).eventName("test").voteNum(5).keyWords("123").build();
        userDto = userRepository.save(userDto);
        rsEventDto = rsEventRepository.save(rsEventDto);
        List<UserDto> userDtoListBeforeDelete = userRepository.findAll();
        List<RsEventDto> rsEventDtoListBeforeDelete = rsEventRepository.findAll();
        mockMvc.perform(delete("/user/{userId}", userDto.getId()))
                .andExpect(status().isOk());

        assertEquals(userDtoListBeforeDelete.size() - 1, userRepository.findAll().size());
        assertEquals(rsEventDtoListBeforeDelete.size() - 1, rsEventRepository.findAll().size());

    }
}
