package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class VoteControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    UserDto userDto;
    RsEventDto rsEventDto;

//    @AfterEach
//    void tearDown() {
//        voteRepository.deleteAll();
//        rsEventRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    public void should_get_vote_record_when_get_give_user_and_rsevent_id() throws Exception {
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email("a@b.com").phone("11234567890").voteNum(2).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).voteNum(0).build());
        voteRepository.save(VoteDto.builder().localDateTime(LocalDateTime.now()).votNum(5).userDto(userDto).rsEventDto(rsEventDto).build());
        mockMvc.perform(get("/voteRecord")
                    .param("userId", String.valueOf(userDto.getId()))
                    .param("rsEventId", String.valueOf(rsEventDto.getId())))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                    .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                    .andExpect(jsonPath("$[0].voteNum", is(5)))
                    .andExpect(status().isOk());
    }

    @Test
    public void should_page_vote_record_when_get_give_user_and_rsevent_id_and_page_index() throws Exception {
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email("a@b.com").phone("11234567890").voteNum(10).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).voteNum(0).build());
        for (int i = 0; i < 8; ++i) {
            voteRepository.save(VoteDto.builder().
                    localDateTime(LocalDateTime.now()).votNum(i + 1).userDto(userDto).
                    rsEventDto(rsEventDto).build());
        }
        mockMvc.perform(get("/voteRecord")
                    .param("userId", String.valueOf(userDto.getId()))
                    .param("rsEventId", String.valueOf(rsEventDto.getId()))
                    .param("pageIndex", "2"))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].voteNum", is(6)))
                    .andExpect(jsonPath("$[1].voteNum", is(7)))
                    .andExpect(jsonPath("$[2].voteNum", is(8)))
                    .andExpect(status().isOk());
    }

    @Test
    public void should_vote_when_post_give_votenum_enough() throws Exception {
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email("a@b.com").phone("11234567890").voteNum(8).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).voteNum(1).build());
        Vote vote = Vote.builder().voteNum(3).userId(userDto.getId()).localDateTime(LocalDateTime.now()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

        List<VoteDto> voteDtoList = voteRepository.findAll();
        userDto = userRepository.findById(userDto.getId()).get();
        rsEventDto = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals(1, voteDtoList.size());
        assertEquals(userDto.getId(), voteDtoList.get(0).getUserDto().getId());
        assertEquals(rsEventDto.getId(), voteDtoList.get(0).getRsEventDto().getId());
        assertEquals(rsEventDto.getVoteNum(), 4);
        assertEquals(userDto.getVoteNum(), 5);
    }

    @Test
    public void should_vote_fail_when_post_give_votenum_not_enough() throws Exception {
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email("a@b.com").phone("11234567890").voteNum(3).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).voteNum(1).build());
        Vote vote = Vote.builder().voteNum(5).userId(userDto.getId()).localDateTime(LocalDateTime.now()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_vote_when_get_give_start_and_end_time() throws Exception {
        userDto = userRepository.save(UserDto.builder().userName("name_one").age(18).gender("mail").email("a@b.com").phone("11234567890").voteNum(10).build());
        rsEventDto = rsEventRepository.save(RsEventDto.builder().keyWords("keyWords").eventName("eventName").userDto(userDto).voteNum(1).build());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(int i = 0; i < 8; ++i) {
            LocalDateTime localDateTime = LocalDateTime.parse("2000-12-12 12:12:" + (12 + i), dateTimeFormatter);
            voteRepository.save(VoteDto.builder().rsEventDto(rsEventDto).userDto(userDto).votNum(1).localDateTime(localDateTime).build());
        }
        mockMvc.perform(get("/vote/time")
                        .param("timeStart", "2000-12-12 12:12:15")
                        .param("timeEnd", "2000-12-12 12:12:18"))
                        .andExpect(jsonPath("$", hasSize(4)))
                        .andExpect(jsonPath("$[0].localDateTime", is(LocalDateTime.of(2000,12,12,12,12, 15).toString())))
                        .andExpect(jsonPath("$[1].localDateTime", is(LocalDateTime.of(2000,12,12,12,12,16).toString())))
                        .andExpect(jsonPath("$[2].localDateTime", is(LocalDateTime.of(2000,12,12,12,12,17).toString())))
                        .andExpect(jsonPath("$[3].localDateTime", is(LocalDateTime.of(2000,12,12,12,12,18).toString())))
                        .andExpect(status().isOk());
    }
}
