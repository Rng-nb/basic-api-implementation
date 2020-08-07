package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
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

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RsControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    private String email = "a@b.com";
    private String phone = "11234567890";
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("First Event").keyWords("test").userDto(userDto).build());
        userDto = userRepository.save(UserDto.builder().userName("name_two").age(18).gender("mail").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("Second Event").keyWords("test").userDto(userDto).build());
        userDto = userRepository.save(UserDto.builder().userName("name_third").age(18).gender("mail").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("Third Event").keyWords("test").userDto(userDto).build());
    }
    @Test
    @Order(1)
    public void should_return_rsevent_list_when_get_give_rs_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("First Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("Second Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("Third Event")))
                .andExpect(jsonPath("$[2].keyWords", is("test")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_return_rsevent_when_get_give_index() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("First Event")))
                .andExpect(jsonPath("$.keyWords", is("test")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("Second Event")))
                .andExpect(jsonPath("$.keyWords", is("test")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("Third Event")))
                .andExpect(jsonPath("$.keyWords", is("test")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_return_rsevent_when_get_give_start_and_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("First Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Second Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("Second Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Third Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("First Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Second Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(jsonPath("$[2].eventName", is("Third Event")))
                .andExpect(jsonPath("$[2].keyWords", is("test")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void should_insert_rsevent_when_post_give_rsevent() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("First Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Second Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(jsonPath("$[2].eventName", is("Third Event")))
                .andExpect(jsonPath("$[2].keyWords", is("test")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWords", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void should_update_rsevent_when_patch_give_index() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        RsEvent rsEvent = new RsEvent("updateEventName", "updateKeyWords", 1);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/update/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("updateEventName")))
                .andExpect(jsonPath("$.keyWords", is("updateKeyWords")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent(null, "onlyUpdateKeyWords", 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/update/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("Second Event")))
                .andExpect(jsonPath("$.keyWords", is("onlyUpdateKeyWords")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent("onlyUpdateEventName", null, 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/update/3").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("onlyUpdateEventName")))
                .andExpect(jsonPath("$.keyWords", is("test")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void should_delete_rsEvent_when_delete_give_index() throws Exception {
        mockMvc.perform(delete("/rs/delete/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("Second Event")))
                .andExpect(jsonPath("$[0].keyWords", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Third Event")))
                .andExpect(jsonPath("$[1].keyWords", is("test")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void should_insert_rsevent_when_post_give_eventname_and_keywords_and_user() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        RsEvent rsEvent = new RsEvent("猪肉涨价了", null, 1);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", null, 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", "经济", 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    public void should_return_badrequest_when_post_give_event_username_not_contains() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + 100 +"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    public void should_insert_rsevent_when_post_give_event_username_contains() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> rsEventList = rsEventRepository.findAll();
        assertEquals(4, rsEventList.size());
        assertEquals("猪肉涨价了", rsEventList.get(3).getEventName());
        assertEquals("经济", rsEventList.get(3).getKeyWords());
    }

    @Test
    @Order(10)
    public void should_return_index_when_post_give_rsevent() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + 1 +"}";

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("index", "4"));
    }

    @Test
    @Order(11)
    public void should_return_userinfo_when_get_give_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("name_one")))
                .andExpect(jsonPath("$[0].user_age", is(18)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is(email)))
                .andExpect(jsonPath("$[0].user_phone", is(phone)))
                .andExpect(jsonPath("$[0]", not(hasKey("voteNum"))))
                .andExpect(jsonPath("$[2].user_name", is("name_third")))
                .andExpect(jsonPath("$[2].user_age", is(18)))
                .andExpect(jsonPath("$[2].user_gender", is("male")))
                .andExpect(jsonPath("$[2].user_email", is(email)))
                .andExpect(jsonPath("$[2].user_phone", is(phone)))
                .andExpect(jsonPath("$[2]", not(hasKey("voteNum"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void should_return_error_when_get_with_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    @Order(13)
    public void should_return_error_when_get_with_invalid_index_userInfo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        RsEvent rsEvent = new RsEvent("xiaoARs", null, 1);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    @Order(14)
    public void should_return_error_when_get_give_invalid_start_or_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=-1&end=100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    public void shoud_delete_rsevent_when_delete_give_user() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        mockMvc.perform(delete("/user/delete/{id}", userDto.getId()))
                .andExpect(status().isOk());
        assertEquals(3, userRepository.findAll().size());
        assertEquals(3, rsEventRepository.findAll().size());
    }
}