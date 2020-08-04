package com.thoughtworks.rslist.api.domain;

import java.security.PrivateKey;

public class RsEvent {
    private String eventName;
    private String ketyWords;

    public RsEvent(String eventName, String ketyWords) {
        this.eventName = eventName;
        this.ketyWords = ketyWords;
    }

    public RsEvent() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKetyWords() {
        return ketyWords;
    }

    public void setKetyWords(String ketyWords) {
        this.ketyWords = ketyWords;
    }
}
