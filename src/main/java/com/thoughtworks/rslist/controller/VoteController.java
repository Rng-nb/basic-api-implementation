package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.service.VoteService;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class VoteController {
    private VoteService voteService;
    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/voteRecord")
    public ResponseEntity getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId, @RequestParam(required = false) Integer pageIndex) {
        if(pageIndex == null)
            return ResponseEntity.ok(voteService.getVoteRecord(userId, rsEventId));
        else
            return ResponseEntity.ok(voteService.getVoteRecordByPageIndex(userId, rsEventId, pageIndex));
    }

    @PostMapping("/rsEvent/vote/{rsEventId}")
    public void voteRsEventById(@PathVariable int rsEventId, @RequestBody Vote vote) {
        voteService.voteRsEventById(rsEventId, vote);
    }

    @GetMapping("/vote/time")
    public ResponseEntity getVoteByTimeStartAndEnd(@RequestParam String timeStart, @RequestParam String timeEnd) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTimeStart = LocalDateTime.parse(timeStart, dateTimeFormatter);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(timeEnd, dateTimeFormatter);
        return  ResponseEntity.ok(voteService.getVoteByTimeStartAndEnd(localDateTimeStart, localDateTimeEnd));
    }
}
