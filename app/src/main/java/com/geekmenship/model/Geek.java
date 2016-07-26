package com.geekmenship.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Geek implements Serializable {

    private static long serialVersionUID = 0L;

    private int id;
    private String profilePicture = "ic_action_picture.png";
    private String firstName;
	private String lastName;
	private String email;
    private String description;
    private String location;
    private String password;
    private boolean eventOrganiser;
    private String communityName;
    private String communityDescription;
    private String text;
    private Bitmap bitmap;
    private String encodedString;
    private int type;

    public int getType() {
        return type;
    }

    public Geek() {
    }

    public Geek(int type) {
        this.type = type;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location;  }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getEventOrganiser() {
        return eventOrganiser;
    }

    public void setEventOrganiser(boolean eventOrganiser) {
        this.eventOrganiser = eventOrganiser;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    public String getEncodedString() { return encodedString; }

    public void setEncodedString(String encodedString) { this.encodedString = encodedString; }

    public boolean isEventOrganiser() { return eventOrganiser; }

    public String getCommunityName() { return communityName; }

    public void setCommunityName(String communityName) { this.communityName = communityName; }

    public String getCommunityDescription() { return communityDescription; }

    public void setCommunityDescription(String communityDescription) {
        this.communityDescription = communityDescription;
    }

    @Override
    public String toString() {
        return "Geek{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", password='" + password + '\'' +
                ", eventOrganiser='" + eventOrganiser + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", bitmap='" + bitmap + '\'' +
                ", encodedString='" + encodedString + '\'' +
                ", communityName='" + communityName + '\'' +
                ", communityDescription='" + communityDescription + '\'' +
                '}';
    }
}
