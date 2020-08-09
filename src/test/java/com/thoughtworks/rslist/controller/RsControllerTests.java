package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    private String email = "a@b.com";
    private String phone = "11234567890";
    UserDto userDto;
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("male").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("First Event").keyWords("test").userDto(userDto).voteNum(1).build());
        userDto = userRepository.save(UserDto.builder().userName("name_two").age(18).gender("male").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("Second Event").keyWords("test").userDto(userDto).voteNum(2).build());
        userDto = userRepository.save(UserDto.builder().userName("name_third").age(18).gender("male").email(email).phone(phone).voteNum(10).build());
        rsEventRepository.save(RsEventDto.builder().eventName("Third Event").keyWords("test").userDto(userDto).voteNum(3).build());
    }

    @Test
    public void should_return_rsevent_list_when_get_give_rs_list() throws Exception {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        mockMvc.perform(get("/rsEvent/list"))
                .andExpect(jsonPath("$", hasSize(rsEventDtoList.size())))
                .andExpect(jsonPath("$[0].eventName", is(rsEventDtoList.get(0).getEventName())))
                .andExpect(jsonPath("$[0].keyWord", is(rsEventDtoList.get(0).getKeyWords())))
                .andExpect(jsonPath("$[0].id", is(rsEventDtoList.get(0).getId())))
                .andExpect(jsonPath("$[0].voteNum", is(rsEventDtoList.get(0).getVoteNum())))
                .andExpect(jsonPath("$[1].eventName", is(rsEventDtoList.get(1).getEventName())))
                .andExpect(jsonPath("$[1].keyWord", is(rsEventDtoList.get(1).getKeyWords())))
                .andExpect(jsonPath("$[1].id", is(rsEventDtoList.get(1).getId())))
                .andExpect(jsonPath("$[1].voteNum", is(rsEventDtoList.get(1).getVoteNum())))
                .andExpect(jsonPath("$[2].eventName", is(rsEventDtoList.get(2).getEventName())))
                .andExpect(jsonPath("$[2].keyWord", is(rsEventDtoList.get(2).getKeyWords())))
                .andExpect(jsonPath("$[2].id", is(rsEventDtoList.get(2).getId())))
                .andExpect(jsonPath("$[2].voteNum", is(rsEventDtoList.get(2).getVoteNum())))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_rsevent_when_get_give_index() throws Exception {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();

        mockMvc.perform(get("/rsEvent/{rsEventId}", rsEventDtoList.get(0).getId()))
                .andExpect(jsonPath("$.eventName", is(rsEventDtoList.get(0).getEventName())))
                .andExpect(jsonPath("$.keyWord", is(rsEventDtoList.get(0).getKeyWords())))
                .andExpect(jsonPath("$.id", is(rsEventDtoList.get(0).getId())))
                .andExpect(jsonPath("$.voteNum", is(rsEventDtoList.get(0).getVoteNum())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rsEvent/{rsEventId}", rsEventDtoList.get(1).getId()))
                .andExpect(jsonPath("$.eventName", is(rsEventDtoList.get(1).getEventName())))
                .andExpect(jsonPath("$.keyWord", is(rsEventDtoList.get(1).getKeyWords())))
                .andExpect(jsonPath("$.id", is(rsEventDtoList.get(1).getId())))
                .andExpect(jsonPath("$.voteNum", is(rsEventDtoList.get(1).getVoteNum())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rsEvent/{rsEventId}", rsEventDtoList.get(2).getId()))
                .andExpect(jsonPath("$.eventName", is(rsEventDtoList.get(2).getEventName())))
                .andExpect(jsonPath("$.keyWord", is(rsEventDtoList.get(2).getKeyWords())))
                .andExpect(jsonPath("$.id", is(rsEventDtoList.get(2).getId())))
                .andExpect(jsonPath("$.voteNum", is(rsEventDtoList.get(2).getVoteNum())))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_rsevent_when_get_give_start_and_end() throws Exception {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        mockMvc.perform(get("/rsEvent/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is(rsEventDtoList.get(0).getEventName())))
                .andExpect(jsonPath("$[0].keyWord", is(rsEventDtoList.get(0).getKeyWords())))
                .andExpect(jsonPath("$[1].id", is(rsEventDtoList.get(1).getId())))
                .andExpect(jsonPath("$[1].voteNum", is(rsEventDtoList.get(1).getVoteNum())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rsEvent/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(rsEventDtoList.get(1).getId())))
                .andExpect(jsonPath("$[0].voteNum", is(rsEventDtoList.get(1).getVoteNum())))
                .andExpect(jsonPath("$[1].eventName", is(rsEventDtoList.get(2).getEventName())))
                .andExpect(jsonPath("$[1].keyWord", is(rsEventDtoList.get(2).getKeyWords())))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rsEvent/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is(rsEventDtoList.get(0).getEventName())))
                .andExpect(jsonPath("$[1].voteNum", is(rsEventDtoList.get(1).getVoteNum())))
                .andExpect(jsonPath("$[2].id", is(rsEventDtoList.get(2).getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void should_insert_rsevent_when_post_give_rsevent() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rsEvent/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("First Event")))
                .andExpect(jsonPath("$[0].keyWord", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Second Event")))
                .andExpect(jsonPath("$[1].keyWord", is("test")))
                .andExpect(jsonPath("$[2].eventName", is("Third Event")))
                .andExpect(jsonPath("$[2].keyWord", is("test")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
                .andExpect(status().isOk());
    }


    @Test
    public void should_delete_rsEvent_when_delete_give_index() throws Exception {

        mockMvc.perform(delete("/rsEvent/{rsEventId}", rsEventRepository.findAll().get(0).getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rsEvent/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("Second Event")))
                .andExpect(jsonPath("$[0].keyWord", is("test")))
                .andExpect(jsonPath("$[1].eventName", is("Third Event")))
                .andExpect(jsonPath("$[1].keyWord", is("test")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_insert_rsevent_when_post_give_eventname_and_keywords_and_user() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        RsEvent rsEvent = new RsEvent("猪肉涨价了", null, 1);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", null, 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", "经济", 1);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_badrequest_when_post_give_event_username_not_contains() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + 100 +"}";
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_insert_rsevent_when_post_give_event_username_contains() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> rsEventList = rsEventRepository.findAll();
        assertEquals(4, rsEventList.size());
        assertEquals("猪肉涨价了", rsEventList.get(3).getEventName());
        assertEquals("经济", rsEventList.get(3).getKeyWords());
    }

    @Test
    public void should_return_index_when_post_give_rsevent() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userRepository.findAll().get(0).getId() +"}";
        RsEventDto  rsEventDto = RsEventDto.builder().keyWords("经济").eventName("猪肉涨价了").userDto(userRepository.findAll().get(0)).build();
        rsEventDto = rsEventRepository.save(rsEventDto);
        mockMvc.perform(post("/rsEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("index", String.valueOf(rsEventDto.getId() + 1)));
    }

    @Test
    public void should_return_userinfo_when_get_give_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("name_one")))
                .andExpect(jsonPath("$[0].user_age", is(18)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is(email)))
                .andExpect(jsonPath("$[0].user_phone", is(phone)))
                .andExpect(jsonPath("$[2].user_name", is("name_third")))
                .andExpect(jsonPath("$[2].user_age", is(18)))
                .andExpect(jsonPath("$[2].user_gender", is("male")))
                .andExpect(jsonPath("$[2].user_email", is(email)))
                .andExpect(jsonPath("$[2].user_phone", is(phone)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_error_when_get_with_invalid_index() throws Exception {
        mockMvc.perform(get("/rsEvent/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }


    @Test
    public void should_return_error_when_get_give_invalid_start_or_end() throws Exception {
        mockMvc.perform(get("/rsEvent/list?start=-1&end=100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    public void should_delete_rsevent_when_delete_give_user() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        mockMvc.perform(delete("/user/{userId}", userDto.getId()))
                .andExpect(status().isOk());
        assertEquals(3, userRepository.findAll().size());
        assertEquals(3, rsEventRepository.findAll().size());
    }

    @Test
    public void should_return_badrequest_when_update_give_rsevent_id_suit_user_id() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + 100 +"}";
        mockMvc.perform(patch("/rsEvent/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void should_return_badrequest_when_update_give_rsevent_id_no_user_id() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + null +"}";
        mockMvc.perform(patch("/rsEvent/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void should_update_eventname_and_keywords_when_update_give_rsevent_id_suit_user_id() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(patch("/rsEvent/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        rsEventDto = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("猪肉涨价了", rsEventDto.getEventName());
        assertEquals("经济", rsEventDto.getKeyWords());
    }

    @Test
    public void should_only_update_eventname_when_update_give_rsevent_id_suit_user_id() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWords\":null,\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(patch("/rsEvent/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        rsEventDto = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("猪肉涨价了", rsEventDto.getEventName());
        assertEquals("keyWords", rsEventDto.getKeyWords());
    }

    @Test
    public void should_only_update_keyWords_when_update_give_rsevent_id_suit_user_id() throws Exception {
        UserDto userDto = userRepository.save(UserDto.builder().userName("name_four").age(18).gender("male").email("a@b.com").phone(phone).voteNum(10).build());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).build());
        String jsonString = "{\"eventName\":null,\"keyWords\":\"经济\",\"userId\":" + userDto.getId() +"}";
        mockMvc.perform(patch("/rsEvent/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        rsEventDto = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("eventName", rsEventDto.getEventName());
        assertEquals("经济", rsEventDto.getKeyWords());
    }
}