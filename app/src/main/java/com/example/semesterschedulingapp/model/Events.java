package com.example.semesterschedulingapp.model;

public class Events {

    private int color;
    private long date;
    private String title;

    public Events(int color, long date, String title) {
        this.color = color;
        this.date = date;
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public long getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}
