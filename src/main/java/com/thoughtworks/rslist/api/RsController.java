package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.api.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventList() {
    return rsEventList;
  }
}
