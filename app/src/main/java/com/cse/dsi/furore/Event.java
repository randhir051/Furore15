package com.cse.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */
public class Event {
    int image;
    String id, name, timing;

//    public Event(String name, int image) {
//        this.name = name;
//        this.image = image;
//    }

    public Event(String id, String name, String timing) {
        this.name = name;
        this.id = id;
        this.timing = timing;
        this.image = R.drawable.images;
    }
}
