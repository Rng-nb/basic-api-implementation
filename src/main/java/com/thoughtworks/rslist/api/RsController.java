package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventInvalidException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsEventList = initRsEvent();
  private List<RsEvent> initRsEvent() {

    List<RsEvent> rsEvent = new LinkedList<RsEvent>();
    User userInit = new User("xiaowang", 19,"male", "xw@thoughtworks.com", "11111111111");

    UserController.insertUser(userInit);
    rsEvent.add(new RsEvent("第一条事件", "无标签", userInit));

    userInit = new User("xiaoming",18,"female", "xm@thoughtworks.com", "12222222222");

    UserController.insertUser(userInit);
    rsEvent.add(new RsEvent("第二条事件", "无标签", userInit));

    userInit = new User("xiaoli",  40,"male", "xl@thoughtworks.com", "13333333333");
    UserController.insertUser(userInit);
    rsEvent.add(new RsEvent("第三条事件", "无标签", userInit));

    return rsEvent;
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventByIndex(@PathVariable int index) {
    if(index <= 0 || index > rsEventList.size()) {
      throw new RsEventInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsEventList.get(index -1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventListStartEnd(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    if(start == null || start < 0 || end == null || end > rsEventList.size()) {
      throw new RsEventInvalidException("invalid request param");

    } else if(start != null && end != null) {
      return ResponseEntity.ok(rsEventList.subList(start - 1, end));
    }
    return ResponseEntity.ok(rsEventList);
  }

  @PostMapping("/rs/event")
  public ResponseEntity insertRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    rsEventList.add(rsEvent);
    User insertUser = rsEvent.getUser();
    boolean isContains = false;
    for (int i = 0; i < UserController.getUserList().size(); ++i) {
      if(UserController.getUserList().get(i).getName().equals(insertUser.getName())) {
        isContains = true;
      }
    }
    if(!isContains)
      UserController.insertUser(insertUser);
    HttpHeaders httpHeaders = new HttpHeaders();
    int index = rsEventList.indexOf(rsEvent) + 1;
    httpHeaders.add("Index", String.valueOf(index));
    return ResponseEntity.created(null).headers(httpHeaders).build();
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
