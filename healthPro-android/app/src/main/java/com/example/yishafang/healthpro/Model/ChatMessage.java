package com.example.yishafang.healthpro.Model;

import java.util.Date;
import java.util.Random;

/**
 * Created by yellowstar on 12/9/15.
 */
public class ChatMessage {
    public String id;
    public String body;
    public int senderId;
    public int receiverId;
    public String sUsername;
    public String rUsername;
    public Date timeStamp;

    public ChatMessage(int sid, int rid, String sUsername, String rUsername, String body, Date timeStamp) {
        this.senderId = sid;
        this.receiverId = rid;
        this.sUsername = sUsername;
        this.rUsername = rUsername;
        this.body = body;
        this.timeStamp = timeStamp;
        this.id = "" + new Random().nextInt(1000) + "-" + String.format("%02d", new Random().nextInt(100));
    }

    public ChatMessage(String sUsername, String rUsername, String body, Date timeStamp) {
        this.sUsername = sUsername;
        this.rUsername = rUsername;
        this.body = body;
        this.timeStamp = timeStamp;
        this.id = "" + new Random().nextInt(1000) + "-" + String.format("%02d", new Random().nextInt(100));
    }

    public boolean fromUser(User user) {
        return this.sUsername == user.getUsername();
    }
}
