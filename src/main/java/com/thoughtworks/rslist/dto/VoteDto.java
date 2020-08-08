package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vote")
public class VoteDto {
    @Id
    @GeneratedValue
    private int id;
    private LocalDateTime localDateTime;
    private int votNum;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDto userDto;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventDto rsEventDto;

}
