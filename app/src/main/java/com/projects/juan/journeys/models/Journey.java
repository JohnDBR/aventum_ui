package com.projects.juan.journeys.models;


import com.beust.klaxon.JSON;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juan on 13/02/18.
 */

public class Journey{
    private int id;
    private String code;
    private String start;
    private String end;
    private int capacity;
    private int price;
    private int duration;
    private String tags;
    private ArrayList<String> students;
    private User driver;
    private JSONArray stops;

    public Journey(JSONObject journey, JSONArray stops, JSONObject driver, JSONArray students) {
        this.students = new ArrayList<>();
        if(journey != null){
            try {
                this.id = journey.getInt("id");
                this.code = journey.getString("code");
                this.start = journey.getString("start");
                this.end = journey.getString("end");
                this.capacity = journey.getInt("capacity");
                this.price = journey.getInt("price");
                this.duration = journey.getInt("duration");
                this.tags = journey.getString("tags");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(stops != null){
            this.stops = stops;
        }else{
            this.stops = new JSONArray();
        }
        try {
            if(driver != null){
                this.driver = new User(driver.getInt("id"), driver.getString("first_name"), driver.getString("last_name"), driver.getString("cc"),
                        driver.getString("email"), driver.getString("profile_picture"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(students != null){
            for(int i = 0; i < students.length(); i++) {
                try {
                    this.students.add(students.getJSONObject(i).getString("first_name").toString() + " " + students.getJSONObject(i).getString("last_name").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getTags() {
        return tags;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public User getDriver() {
        return driver;
    }

    public JSONArray getStops() {
        return stops;
    }
}
