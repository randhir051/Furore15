package com.cse.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */
public class EventType {
    int image, numEvents;
    String name;

    public EventType(String name, int numEvents, int image) {
        this.image = image;
        this.name = name;
        this.numEvents = numEvents;
    }
}
