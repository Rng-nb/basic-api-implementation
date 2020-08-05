package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

  private List<RsEvent> rsEventList = initRsEvent();

  private List<RsEvent> initRsEvent() {
    List<RsEvent> rsEvent = new ArrayList<RsEvent>();
    rsEvent.add(new RsEvent("第一条事件", "无标签"));
    rsEvent.add(new RsEvent("第二条事件", "无标签"));
    rsEvent.add(new RsEvent("第三条事件", "无标签"));
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

  @PostMapping("/rs/event")
  public void insertRsEvent(@RequestBody RsEvent rsEvent) {
    rsEventList.add(rsEvent);
  }

  @PatchMapping("/rs/update/{index}")
  public void updateRsEventByIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    if(rsEvent.getEventName() != null && rsEvent.getkeyWords() != null) {
      rsEventList.set(index - 1, rsEvent);
    } else if(rsEvent.getEventName() == null) {
      rsEventList.get(index -1).setkeyWords(rsEvent.getkeyWords());
    } else {
      rsEventList.get(index - 1).setEventName(rsEvent.getEventName());
    }
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    rsEventList.remove(index - 1);
  }
}
