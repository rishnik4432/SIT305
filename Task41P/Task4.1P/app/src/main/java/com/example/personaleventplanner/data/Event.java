package com.example.personaleventplanner.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String category;
    private String location;
    private Date dateTime;

    // Constructor (without id – Room will auto-generate)
    public Event(String title, String category, String location, Date dateTime) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTime = dateTime;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDateTime() { return dateTime; }      // ← This was missing
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
}