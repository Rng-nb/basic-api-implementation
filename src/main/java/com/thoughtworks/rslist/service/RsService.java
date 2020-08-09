package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.view.RsEventView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RsService {
    private RsEventRepository rsEventRepository;
    private UserRepository userRepository;

    @Autowired
    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, UserService userService) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    public RsEventView getRsEventByIndex(int rsEventId) {
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
        RsEventView rsEventView = RsEventView.builder().eventName(rsEventDto.getEventName()).keyWord(rsEventDto.getKeyWords())
                .id(rsEventDto.getId()).voteNum(rsEventDto.getVoteNum()).build();
        return rsEventView;
    }

    public List<RsEventView> getWholeRsEventList() {
        List<RsEventView> rsEventViewList = rsEventRepository.findAll().stream()
                                                .map(item -> RsEventView.builder().eventName(item.getEventName()).keyWord(item.getKeyWords())
                                                .id(item.getId()).voteNum(item.getVoteNum()).build()).collect(Collectors.toList());
        return rsEventViewList;
    }
    public List<RsEventView> getRsEventBetweenStartAndEnd(int start, int end) {
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        start = rsEventDtoList.get(start - 1).getId();
        end = rsEventDtoList.get(end - 1).getId();
        List<RsEventView> rsEventViewList = rsEventRepository.findAllByStartAndEnd(start, end).stream()
                                                .map(item -> RsEventView.builder().eventName(item.getEventName()).keyWord(item.getKeyWords())
                                                .id(item.getId()).voteNum(item.getVoteNum()).build()).collect(Collectors.toList());
        return rsEventViewList;
    }

    public int getRsEventListSize() {
        return rsEventRepository.findAll().size();
    }

    public int insertRsEventToList(RsEvent rsEvent) {
        UserDto userDto = userRepository.getAllById(rsEvent.getUserId());
        RsEventDto rsEventDto = rsEventRepository.save(RsEventDto.builder().eventName(rsEvent.getEventName()).keyWords(rsEvent.getKeyWords())
                .userDto(userDto).build());
        return rsEventDto.getId();
    }


    public void deleteRsEventFromListByIndex(int index) {
        rsEventRepository.deleteAllById(index);
    }

    public boolean isContainsUser(RsEvent rsEvent) {
        return  userRepository.findById(rsEvent.getUserId()).isPresent();
    }

    public boolean isContainsRsEvent(int rsEventId) {
        return rsEventRepository.findById(rsEventId).isPresent();
    }

    public ResponseEntity updateRsEventByRsEventId(int rsEventId, RsEvent rsEvent) {
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
        if(rsEvent.getUserId() != rsEventDto.getUserDto().getId()) {
            return ResponseEntity.badRequest().build();
        } else {
            if(rsEvent.getEventName() == null) {
                rsEventDto.setKeyWords(rsEvent.getKeyWords());
                rsEventRepository.save(rsEventDto);
            } else if(rsEvent.getKeyWords() == null) {
                rsEventDto.setEventName(rsEvent.getEventName());
                rsEventRepository.save(rsEventDto);
            } else {
                rsEventDto.setKeyWords(rsEvent.getKeyWords());
                rsEventDto.setEventName(rsEvent.getEventName());
                rsEventRepository.save(rsEventDto);
            }
        }
        return ResponseEntity.ok().build();
    }
}
