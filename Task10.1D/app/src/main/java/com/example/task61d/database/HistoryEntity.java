package com.example.task61d.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "history")
public class HistoryEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String screenName; //e.g., "Hint", "Explain"
    public String userInput; //the prompt sent to AI
    public String aiResponse;
    public long timestamp; //System.surrentTimeMillis()
}
