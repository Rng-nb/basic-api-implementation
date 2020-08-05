package com.thoughtworks.rslist.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWords;
    @Valid
    @NotNull
    private User user;

    public RsEvent(String eventName, String keyWords, User user) {
        this.eventName = eventName;
        this.keyWords = keyWords;
        this.user = user;
    }

    public RsEvent() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
