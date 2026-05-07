package com.example.task81c;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "messages")
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @SuppressWarnings("unused")
    public long id;

    public String username;
    public String message;      // the text content
    public boolean isUser;      // true = user, false = bot
    public long timestamp;      // milliseconds

    public ChatMessage(String username, String message, boolean isUser, long timestamp) {
        this.username = username;
        this.message = message;
        this.isUser = isUser;
        this.timestamp = timestamp;
    }
}
