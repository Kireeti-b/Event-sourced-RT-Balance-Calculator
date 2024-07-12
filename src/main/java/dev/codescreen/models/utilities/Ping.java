package dev.codescreen.models.utilities;

import java.time.LocalDateTime;

public class Ping {
    private final LocalDateTime serverTime;

    public Ping(){
        this.serverTime = LocalDateTime.now();
    }

    public String getServerTime(){
        return this.serverTime.toString();
    }
}
