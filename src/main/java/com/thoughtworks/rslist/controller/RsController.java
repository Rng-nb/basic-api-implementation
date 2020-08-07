package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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
  @Autowired
  RsService rsService;

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEventByIndex(@PathVariable int index) {
    if(index <= 0 || index > rsService.getRsEventListSize()) {
      throw new RsEventInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsService.getRsEventByIndex(index));
  }

  @GetMapping("/rs/list")
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

  @PostMapping("/rs/event")
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

  @PatchMapping("/rs/update/{index}")
  public void updateRsEventByIndex(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    rsService.updateRsEventListByIndex(index, rsEvent);
  }

  @DeleteMapping("/rs/delete/{index}")
  public void deleteRsEventByIndex(@PathVariable int index) {
    rsService.deleteRsEventFromListByIndex(index);
  }
}
