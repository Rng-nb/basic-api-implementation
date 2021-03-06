package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String userName;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDto")
    private List<RsEventDto> rsEventList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDto")
    private List<VoteDto> voteList;
}
