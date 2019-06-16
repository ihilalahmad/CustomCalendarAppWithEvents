package com.example.semesterschedulingapp.model;

public class NotificationModel {

    private String task_title;
    private String task_details;

    public NotificationModel(String task_title, String task_details) {
        this.task_title = task_title;
        this.task_details = task_details;
    }

    public String getTask_title() {
        return task_title;
    }

    public String getTask_details() {
        return task_details;
    }
}
