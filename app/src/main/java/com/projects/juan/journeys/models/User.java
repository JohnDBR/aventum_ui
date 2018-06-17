package com.projects.juan.journeys.models;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String cc;
    private String email;
    private String profile_picture;

    public User(int id, String first_name, String last_name, String cc, String email, String profile_picture){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.cc = cc;
        this.email = email;
        this.profile_picture = profile_picture;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCc() {
        return cc;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }
}
