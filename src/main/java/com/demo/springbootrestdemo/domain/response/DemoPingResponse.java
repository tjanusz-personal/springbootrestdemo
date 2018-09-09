package com.demo.springbootrestdemo.domain.response;


import java.util.Random;

public class DemoPingResponse {

    private String textMessage;
    private long pingId;

    public DemoPingResponse(String textMessage) {
        this.textMessage = textMessage;
        this.pingId = new Random().nextLong();
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getPingId() {
        return pingId;
    }

    public void setPingId(long pingId) {
        this.pingId = pingId;
    }
}
