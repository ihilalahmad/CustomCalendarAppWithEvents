package com.example.semesterschedulingapp.Utils;

public class Config {


    public static String NOTIFICATION_CHANNEL_ID = "101";
//localhost api URL for you laptop, change only ip
//    public static final String BASE_URL = "http://192.168.10.17/semester_activity_schedule/public/api/";

//    public static final String BASE_URL = "http://192.168.10.13/uol_semester/public/api/";

//live api URL, for developer use only.
    public static final String BASE_URL = "https://dev.mraheelkhan.com/uolsemester/public/api/";

    public static final String TASK_URL = BASE_URL +"auth/tasklist";
    public static final String SIGNUP_URL = BASE_URL +"auth/signup";
    public static final String LOGIN_URL = BASE_URL +"auth/login";
    public static final String PROGRAMS_URL = BASE_URL +"getProgramList";
    public static final String BATCHES_URL = BASE_URL +"getBatchesList";
    public static final String COURSES_URL = BASE_URL +"getCoursesList";
    public static final String VERIFYCODE = BASE_URL +"auth/verifycode";


}
