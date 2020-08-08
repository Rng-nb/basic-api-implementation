package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private RsEventRepository rsEventRepository;
    private UserRepository userRepository;
    @Autowired
    public VoteService(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    public List<Vote> getVoteRecord(int userId, int rsEventId) {
        List<Vote> voteRecord = voteRepository.findVoteFromUserIdAndRsEventId(userId, rsEventId).stream().map(
                item -> Vote.builder().userId(item.getUserDto().getId()).rsEventId(item.getRsEventDto().getId())
                        .localDateTime(item.getLocalDateTime()).voteNum(item.getVotNum()).build())
                .collect(Collectors.toList());
        return voteRecord;
    }

    public List<Vote> getVoteRecordByPageIndex(int userId, int rsEventId, Integer pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        List<Vote> voteRecordPage = voteRepository.findVoteFromUserIdAndRsEventIdAndPage(userId, rsEventId, pageable)
                .stream().map(item -> Vote.builder().userId(item.getUserDto().getId()).rsEventId(item.getRsEventDto().getId())
                        .localDateTime(item.getLocalDateTime()).voteNum(item.getVotNum()).build())
                .collect(Collectors.toList());
        return voteRecordPage;
    }

    public ResponseEntity voteRsEventById(int rsEventId, Vote vote) {
        Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserDto> userDto = userRepository.findById(vote.getUserId());
        if(!rsEventDto.isPresent() || !userDto.isPresent() || vote.getVoteNum() > userDto.get().getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }
        VoteDto voteDto = VoteDto.builder().userDto(userDto.get()).rsEventDto(rsEventDto.get()).votNum(vote.getVoteNum())
                .localDateTime(vote.getLocalDateTime()).build();
        voteRepository.save(voteDto);
        UserDto user = userDto.get();
        user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
        userRepository.save(user);
        RsEventDto rsEvent = rsEventDto.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(rsEvent);
        return ResponseEntity.ok().build();
    }

    public List<Vote> getVoteByTimeStartAndEnd(LocalDateTime timeStart, LocalDateTime timeEnd) {
        List<VoteDto> voteDtoList = voteRepository.findByStartTimeAndEndTime(timeStart, timeEnd);
        List<Vote> voteList = voteDtoList.stream().map( item -> Vote.builder().userId(item.getUserDto().getId())
                .rsEventId(item.getRsEventDto().getId()).voteNum(item.getVotNum()).localDateTime(item.getLocalDateTime()).build())
                .collect(Collectors.toList());
        return voteList;
    }
}
