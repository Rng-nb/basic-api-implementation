package com.thoughtworks.rslist.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventView {
    private String eventName;
    private String keyWord;
    private int id;
    private int voteNum;
}
