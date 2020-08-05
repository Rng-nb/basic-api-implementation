package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

  private List<User> userList = new ArrayList<User>();
  private List<RsEvent> rsEventList = initRsEvent();

  private List<RsEvent> initRsEvent() {

    List<RsEvent> rsEvent = new ArrayList<RsEvent>();
    User userInit = new User("xiaowang", 19,"male", "xw@thoughtwork.com", "11111111111");

    userList.add(userInit);
    rsEvent.add(new RsEvent("第一条事件", "无标签", userInit));

    userInit = new User("xiaoming",18,"female", "xm@thoughtworks.com", "12222222222");

    userList.add(userInit);
    rsEvent.add(new RsEvent("第二条事件", "无标签", userInit));

    userInit = new User("xiaoli",  40,"male", "xl@thoughtworks.com", "13333333333");
    userList.add(userInit);
    rsEvent.add(new RsEvent("第三条事件", "无标签", userInit));

    return rsEvent;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable int index) {
    return rsEventList.get(index -1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventListStartEnd(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if(start != null && end != null) {
      return rsEventList.subList(start - 1, end);
    }
    return rsEventList;
  }

  @GetMapping("/rs/user")
  public List<User> getUserList() {
    return userList;
  }

  @PostMapping("/rs/event")
  public void insertRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    rsEventList.add(rsEvent);
    User insertUser = rsEvent.getUser();
    boolean isContains = false;
    for (int i = 0; i < userList.size(); ++i) {
      if(userList.get(i).getName().equals(insertUser.getName())) {
        isContains = true;
      }
    }
    if(!isContains)
      userList.add(insertUser);
  }

  @PatchMapping("/rs/update/{index}")
  public void updateRsEventByIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    if(rsEvent.getEventName() != null && rsEvent.getKeyWords() != null) {
      rsEventList.set(index - 1, rsEvent);
    } else if(rsEvent.getEventName() == null) {
      rsEventList.get(index -1).setKeyWords(rsEvent.getKeyWords());
    } else {
      rsEventList.get(index - 1).setEventName(rsEvent.getEventName());
    }
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    rsEventList.remove(index - 1);
  }
}
