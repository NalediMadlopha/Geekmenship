package com.geekmenship.model;

        import android.graphics.Bitmap;

        import java.io.Serializable;

public class Event implements Serializable {

    private static long serialVersionUID = 1L;

    private int id;
    private String communityName;
    private String title;
    private String description;
    private String venue;
    private String date;
    private String time;
    private String photo = "ic_action_picture.png";
    private Bitmap bitmap;
    private String encodedString;
    private int type;

    public int getType() {
        return type;
    }

    public Event() {
    }

    public Event(int type) {
        this.type = type;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getVenue() { return venue; }

    public void setVenue(String venue) { this.venue = venue; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getEncodedString() { return encodedString; }

    public void setEncodedString(String encodedString) { this.encodedString = encodedString; }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", venue=" + venue + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", photo='" + photo + '\'' +
                ", bitmap='" + bitmap + '\'' +
                ", encodedString='" + encodedString + '\'' +
                '}';
    }
}
