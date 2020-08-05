package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RsControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    public void get_String_from_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void get_String_from_list_with_index() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void get_String_from_list_with_start_and_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void insertRsEvent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);

        User userInsert = new User("xiaoning", 27, "female", "xn@thoughtworks.com", "15555555555");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWords", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWords", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void updateRsEventByIndex() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        RsEvent rsEvent = new RsEvent("更新eventName", "更新keyWords", null);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/update/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("更新eventName")))
                .andExpect(jsonPath("$.keyWords", is("更新keyWords")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent(null, "只更新keyWords", null);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/update/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWords", is("只更新keyWords")))
                .andExpect(status().isOk());

        rsEvent = new RsEvent("只更新eventName", null, null);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(patch("/rs/update/3").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("只更新eventName")))
                .andExpect(jsonPath("$.keyWords", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void deleteRsEventByIndex() throws Exception {
        mockMvc.perform(delete("/rs/delete/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWords", is("只更新keyWords")))
                .andExpect(jsonPath("$[1].eventName", is("只更新eventName")))
                .andExpect(jsonPath("$[1].keyWords", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyWords", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void insertRsEventShouldHasEventNameAndKeyWordsAndUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        User userInsert = new User("xiaoning", 27,"female",  "xn@thoughtworks.com", "15555555555");

        RsEvent rsEvent = new RsEvent(null, "经济", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", null, userInsert);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent = new RsEvent("猪肉涨价了", "经济", null);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    public void insertRsEventShouldNotInsertUserNameIfContains() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        User userInsert = new User("xiaoning", 27,"female",  "xn@thoughtworks.com", "15555555555");
        RsEvent rsEvent = new RsEvent("5G", "Internet", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[1].user_name", is("xiaoming")))
                .andExpect(jsonPath("$[2].user_name", is("xiaoli")))
                .andExpect(jsonPath("$[3].user_name", is("xiaoning")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].eventName", is("5G")))
                .andExpect(jsonPath("$[3].keyWords", is("Internet")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void insertRsEventShouldInsertUserNameIfNotContains() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        User userInsert = new User("xiaoan", 27, "female", "xa@thoughtworks.com", "16666666666");
        RsEvent rsEvent = new RsEvent("5G+", "Internet", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[1].user_name", is("xiaoming")))
                .andExpect(jsonPath("$[2].user_name", is("xiaoli")))
                .andExpect(jsonPath("$[3].user_name", is("xiaoning")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[3].eventName", is("5G")))
                .andExpect(jsonPath("$[3].keyWords", is("Internet")))
                .andExpect(jsonPath("$[4].eventName", is("5G+")))
                .andExpect(jsonPath("$[4].keyWords", is("Internet")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void shouldReturnCreatedWithIndex() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        User userInsert = new User("xiaoA", 23, "male", "xA@thoughtworks.com", "11234567890");
        RsEvent rsEvent = new RsEvent("xiaoARs", "User", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("index", "6"));
    }

    @Test
    @Order(11)
    public void shouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[0].user_age", is(19)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is("xw@thoughtworks.com")))
                .andExpect(jsonPath("$[0].user_phone", is("11111111111")))
                .andExpect(jsonPath("$[0]", not(hasKey("voteNum"))))
                .andExpect(jsonPath("$[5].user_name", is("xiaoA")))
                .andExpect(jsonPath("$[5].user_age", is(23)))
                .andExpect(jsonPath("$[5].user_gender", is("male")))
                .andExpect(jsonPath("$[5].user_email", is("xA@thoughtworks.com")))
                .andExpect(jsonPath("$[5].user_phone", is("11234567890")))
                .andExpect(jsonPath("$[5]", not(hasKey("voteNum"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void returnErrorWithInvalidIndex() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    @Order(13)
    public void returnErrorWithInvalidUserInfo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        User userInsert = new User("xiaoA", 123, "male", "xA@thoughtworks.com", "11234567890");
        RsEvent rsEvent = new RsEvent("xiaoARs", "User", userInsert);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    @Order(14)
    public void returnErrorWithInvalidStartOrEnd() throws Exception {
        mockMvc.perform(get("/rs/list?start=-1&end=100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }
}