package com.example.task81c;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface ChatDao {
    @Insert
    void insert(ChatMessage message);

    @Query("SELECT * FROM messages WHERE username = :username ORDER BY timestamp ASC")
    List<ChatMessage> getMessagesForUser(String username);

    @Query("DELETE FROM messages WHERE username = :username")
    @SuppressWarnings("unused")
    void clearMessagesForUser(String username);
}
