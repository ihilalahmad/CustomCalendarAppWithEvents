package com.example.semesterschedulingapp.model;

public class Courses {

    private String course_id;
    private String course_name;
    private String course_teacher;
    private String course_session;


    public Courses(String course_id, String course_name, String course_teacher, String course_session) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_teacher = course_teacher;
        this.course_session = course_session;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_teacher() {
        return course_teacher;
    }

    public String getCourse_session() {
        return course_session;
    }
}
