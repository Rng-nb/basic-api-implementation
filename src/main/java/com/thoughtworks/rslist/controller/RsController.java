package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.RsEventInvalidException;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RsController {
  private RsService rsService;

  @Autowired
  public RsController(RsService rsService) {
    this.rsService = rsService;
  }

  @GetMapping("/rsEvent/{rsEventId}")
  public ResponseEntity getRsEventByIndex(@PathVariable int rsEventId) {
    if(rsEventId <= 0 || !rsService.isContainsRsEvent(rsEventId)) {
      throw new RsEventInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsService.getRsEventByIndex(rsEventId));
  }

  @GetMapping("/rsEvent/list")
  public ResponseEntity getRsEventListStartEnd(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
    int maxSize = rsService.getRsEventListSize();
    if(start != null && end != null) {
       if(start <= 0 || end > maxSize) {
         throw new RsEventInvalidException("invalid request param");
       }
      return ResponseEntity.ok(rsService.getRsEventBetweenStartAndEnd(start, end));
    } else if(start != null && start <= 0){
       throw new RsEventInvalidException("invalid request param");
     } else if(end != null &&  end > maxSize) {
       throw new RsEventInvalidException("invalid request param");
     }
    return ResponseEntity.ok(rsService.getWholeRsEventList());
  }

  @PostMapping("/rsEvent")
  public ResponseEntity insertRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if(rsService.isContainsUser(rsEvent)) {
      int index = rsService.insertRsEventToList(rsEvent);
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Index", String.valueOf(index));
      return ResponseEntity.created(null).headers(httpHeaders).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/rsEvent/{rsEventId}")
  public void deleteRsEventByIndex(@PathVariable int rsEventId) {
    rsService.deleteRsEventFromListByIndex(rsEventId);
  }

  @PatchMapping("/rsEvent/{rsEventId}")
  public ResponseEntity updateRsEventById(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
    return  rsService.updateRsEventByRsEventId(rsEventId, rsEvent);
  }
}
