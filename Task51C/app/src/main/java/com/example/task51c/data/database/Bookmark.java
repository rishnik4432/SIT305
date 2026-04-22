package com.example.task51c.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "bookmark")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int newsId;
    private String title;
    private String description;
    private String imageUrl;
    private String category;

    public Bookmark(int newsId, String title, String description, String imageUrl,String category) {
        this.newsId = newsId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    //Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNewsId() { return newsId; }
    public void setNewsId(int newsId) { this.newsId = newsId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
