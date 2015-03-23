package com.example.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */
public class Event {
    int image;
    String name, timing, category, description;

    public Event(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public Event(String id, String name, String timing, String category, String description) {
        this.name = name;
        this.image = image;
    }
}
