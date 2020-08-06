package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.controller.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RsService {

    @Autowired
    UserService userService;

    private List<RsEvent> rsEventList;

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEventListInit = new LinkedList<RsEvent>();
        rsEventListInit.add(new RsEvent("第一条事件", "无标签",
                new User("xiaowang", 19,"male", "xw@thoughtworks.com", "11111111111")));
        rsEventListInit.add(new RsEvent("第二条事件", "无标签",
                new User("xiaoming",18,"female", "xm@thoughtworks.com", "12222222222")));
        rsEventListInit.add(new RsEvent("第三条事件", "无标签",
                new User("xiaoli",  40,"male", "xl@thoughtworks.com", "13333333333")));

        userService.insertUser(new User("xiaowang", 19,"male", "xw@thoughtworks.com", "11111111111"));
        userService.insertUser(new User("xiaoming",18,"female", "xm@thoughtworks.com", "12222222222"));
        userService.insertUser(new User("xiaoli",  40,"male", "xl@thoughtworks.com", "13333333333"));
        return rsEventListInit;
    }

    public RsEvent getRsEventByIndex(int index) {
        return rsEventList.get(index - 1);
    }

    public List<RsEvent> getWholeRsEventList() {
        return rsEventList;
    }
    public List<RsEvent> getRsEventBetweenStartAndEnd(int start, int end) {
        return rsEventList.subList(start - 1, end);
    }

    public int getRsEventListSize() {
        return rsEventList.size();
    }

    public int insertRsEventToList(RsEvent rsEvent) {
        rsEventList.add(rsEvent);
        User userInsert = rsEvent.getUser();
        boolean isContains = false;
        for (int i = 0; i < userService.getUserList().size(); ++i) {
            if(userService.getUserList().get(i).getName().equals(userInsert.getName())) {
                isContains = true;
            }
        }
        if(!isContains)
            userService.getUserList().add(userInsert);

        return rsEventList.indexOf(rsEvent) + 1;
    }

    public void updateRsEventListByIndex(int index, RsEvent rsEvent) {
        if(rsEvent.getEventName() != null && rsEvent.getKeyWords() != null) {
            rsEventList.set(index - 1, rsEvent);
        } else if(rsEvent.getEventName() == null) {
            rsEventList.get(index -1).setKeyWords(rsEvent.getKeyWords());
        } else {
            rsEventList.get(index - 1).setEventName(rsEvent.getEventName());
        }
    }

    public void deleteRsEventFromListByIndex(int index) {
        rsEventList.remove(index - 1);
    }

}
