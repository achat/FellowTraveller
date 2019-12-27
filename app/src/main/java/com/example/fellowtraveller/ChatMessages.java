package com.example.fellowtraveller;

public class ChatMessages {
    private String message, type;
    private boolean seen;
    private long time;


    public ChatMessages(String aMessage, boolean aSeen, long aTime, String aType){

        this.message = aMessage;
        this.seen = aSeen;
        this.time = aTime;
        this.type = aType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public ChatMessages(){

    }
}
