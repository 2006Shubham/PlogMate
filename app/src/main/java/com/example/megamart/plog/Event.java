package com.example.megamart.plog;

import java.io.Serializable;

public class Event implements Serializable {
    private String event_name, event_date, event_address, event_time, map_link, coordinator_name, mobile_number, event_description,event_image;

    public Event(String event_name, String event_date, String event_address, String event_time,
                 String map_link, String coordinator_name, String mobile_number, String event_description, String eventImage) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_address = event_address;
        this.event_time = event_time;
        this.map_link = map_link;
        this.coordinator_name = coordinator_name;
        this.mobile_number = mobile_number;
        this.event_description = event_description;
        this.event_image  = eventImage;

    }

    // Getters only (no need for setters if not modifying)
    public String getEvent_name() { return event_name; }
    public String getEvent_date() { return event_date; }
    public String getEvent_address() { return event_address; }
    public String getEvent_time() { return event_time; }
    public String getMap_link() { return map_link; }
    public String getCoordinator_name() { return coordinator_name; }
    public String getMobile_number() { return mobile_number; }

    public String getEvent_image() {
        return event_image;
    }

    public String getEvent_description() { return event_description;




    }
}
