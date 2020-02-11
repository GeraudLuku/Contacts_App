package com.geraudluku.contactsapp.Models;

public class Contact {
    private String username, email, number, profile_picture;

    public Contact(){}

    public Contact(String username, String email, String number, String profile_picture) {
        this.username = username;
        this.email = email;
        this.number = number;
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
