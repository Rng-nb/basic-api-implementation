package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rs_event")
public class RsEventDto {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;
    private String keyWords;
    private int voteNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDto userDto;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "rsEventDto")
    private List<VoteDto> voteDtoList;
}
