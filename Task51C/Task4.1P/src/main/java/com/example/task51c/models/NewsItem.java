package com.example.task51c.models;

import java.io.Serializable;

public class NewsItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String imageUrl; //resource drawable name or URL
    private String category; //"Football", "Basketball", "Cricket"
    private boolean isFeatured; //for horizontal RecyclerView

    //Constructor, getters, setters
    public NewsItem(int id, String title, String description, String imageUrl,String category, boolean isFeatured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isFeatured = isFeatured;
    }

    //Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public boolean isFeatured() { return isFeatured; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
    public void setFeatured(boolean featured) { isFeatured = featured; }
}
