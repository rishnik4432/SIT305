package com.example.task91p;
public class Post {
    private int id;
    private String title;
    private String description;
    private String category;
    private String type;
    private byte[] image;
    private String dateTime;
    private double latitude;
    private double longitude;

    // Constructor without id (for new posts)
    public Post(String title, String description, String category, String type,
                byte[] image, String dateTime, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.image = image;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Full constructor with id
    public Post(int id, String title, String description, String category, String type,
                byte[] image, String dateTime, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.image = image;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
