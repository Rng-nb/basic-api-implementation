package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RsService {
    private RsEventRepository rsEventRepository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, UserService userService) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public RsEvent getRsEventByIndex(int index) {
        RsEventDto eventDto = rsEventRepository.findById(index).get();
        RsEvent rsEvent = new RsEvent(eventDto.getEventName(), eventDto.getKeyWords(), eventDto.getUserDto().getId());
        return rsEvent;
    }

    public List<RsEvent> getWholeRsEventList() {
        List<RsEvent> rsEventList = rsEventRepository.findAll().stream()
                                    .map(item -> new RsEvent(item.getEventName(), item.getKeyWords(), item.getUserDto().getId()))
                                    .collect(Collectors.toList());
        return rsEventList;
    }
    public List<RsEvent> getRsEventBetweenStartAndEnd(int start, int end) {
        List<RsEvent> rsEventList = rsEventRepository.findAllByStartAndEnd(start, end).stream()
                                    .map(item -> new RsEvent(item.getEventName(), item.getKeyWords(), item.getUserDto().getId()))
                                    .collect(Collectors.toList());
        return rsEventList;
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

    public void updateRsEventListByIndex(int index, RsEvent rsEvent) {
        RsEventDto rsEventDto = rsEventRepository.findById(index).get();
        if(rsEvent.getEventName() != null && rsEvent.getKeyWords() != null) {
            rsEventDto.setEventName(rsEvent.getEventName());
            rsEventDto.setKeyWords(rsEvent.getKeyWords());
        } else if(rsEvent.getEventName() == null) {
            rsEventDto.setKeyWords(rsEvent.getKeyWords());
        } else {
            rsEventDto.setEventName(rsEvent.getEventName());
        }
        rsEventRepository.save(rsEventDto);
    }

    public void deleteRsEventFromListByIndex(int index) {
        rsEventRepository.deleteAllById(index);
    }

    public boolean isContainsUser(RsEvent rsEvent) {
        return  userRepository.findById(rsEvent.getUserId()).isPresent();
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
