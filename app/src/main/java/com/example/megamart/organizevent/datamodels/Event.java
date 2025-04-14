package com.example.megamart.organizevent.datamodels;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private String date;
    private String location;
    private String description;

    private  int id;

    public Event(String name, String date, String location, String description,int id) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getId(){
        return id;
    }


}
