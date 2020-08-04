package com.thoughtworks.rslist.api.domain;

import java.security.PrivateKey;

public class RsEvent {
    private String eventName;
    private String keyWords;

    public RsEvent(String eventName, String keyWords) {
        this.eventName = eventName;
        this.keyWords = keyWords;
    }

    public RsEvent() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getkeyWords() {
        return keyWords;
    }

    public void setkeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
