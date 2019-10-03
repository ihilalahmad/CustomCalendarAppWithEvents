package com.example.semesterschedulingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.semesterschedulingapp.Pattern.MySingleton;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.example.semesterschedulingapp.Utils.SharedPrefManager;
import com.example.semesterschedulingapp.model.Courses;
import com.example.semesterschedulingapp.model.Users;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    CompactCalendarView calendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    TextView tv_monthName;
    ImageButton btn_nextMonth;
    ImageButton btn_prevMonth;
    Button btn_all_courses;

    public int color = Color.GREEN;
    public Dialog mDailog;

    List<Event> eventList;
    List<Courses> coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseMessaging.getInstance().subscribeToTopic("All");


        //if the user is not logged in
        //starting the login activity
        isUserLoggedIn();


        calendarView = findViewById(R.id.compactcalendar_view);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setUseThreeLetterAbbreviation(true);

        eventList = new ArrayList<>();
        mDailog = new Dialog(this);

        tv_monthName = findViewById(R.id.tv_month_name);
        btn_nextMonth = findViewById(R.id.btn_next);
        btn_prevMonth = findViewById(R.id.btn_previous);
        btn_all_courses = findViewById(R.id.btn_all_courses);

        //set initial title of month to textview.
        tv_monthName.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));

        loadEventsFromApi();


        // define a listener to receive callbacks when certain events happen.
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {

                addEventsToCalendar(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("Calendar", "Month was scrolled to: " + firstDayOfNewMonth);
                tv_monthName.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        //scroll to next month of year.
        btn_nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollRight();
            }
        });

        //scroll to prev month of year.
        btn_prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollLeft();
            }
        });

        btn_all_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, CoursesActivity.class));
            }
        });

    }

    private void getAllCourses(){

        StringRequest coursesRequest = new StringRequest(Request.Method.POST, Config.COURSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.i("CoursesRes", response);

                try {

                    JSONObject allCourses = new JSONObject(response);
                    coursesList = new ArrayList<>();

                    JSONArray coursesArray = allCourses.getJSONArray("data");
                    Log.i("CoursesRes", String.valueOf(coursesArray));


                    for (int i=0; i<coursesArray.length(); i++){

                        JSONObject coursesObj = coursesArray.getJSONObject(i);

                        String course_id = coursesObj.getString("id");
                        String course_name = coursesObj.getString("course_name");
                        String course_teacher = coursesObj.getString("teacher_name");
                        String course_session = coursesObj.getString("session_name");

                        Courses courses = new Courses(course_id,course_name,course_teacher,course_session);

                        coursesList.add(courses);

                        Log.i("coursesList",course_name);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("coursesErr", error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Users user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                String program_id = user.getUserProgram_id();

                Map<String, String> params = new HashMap<String, String>();
                params.put("program_id", program_id);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(coursesRequest);
    }

    private void isUserLoggedIn(){

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void loadEventsFromApi() {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Config.TASK_URL,null,
                new Response.Listener<JSONArray>() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(JSONArray response) {

                Log.i("SSAResponse", String.valueOf(response));

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject taskObj = response.getJSONObject(i);

                        String task_title = taskObj.getString("task_title");
                        long task_date = taskObj.getLong("task_date");

                        Log.d("SSAEvents", "Task Title: " + task_title);

                        Event events = new Event(color,task_date,task_title);
                        eventList.add(events);
                        calendarView.addEvent(events);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("ResponseErr", error.getMessage());

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Users user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                String token_type = user.getTokenType();
                String access_token = user.getUserToken();

                Log.i("TokenFromModel",token_type+" "+access_token);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization",token_type+" "+access_token);
                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void addEventsToCalendar(Date dateClick){

        mDailog.setContentView(R.layout.event_popup);
        try {

            eventList = calendarView.getEvents(dateClick);
            Log.d("Calendar", "Day was clicked: " + dateClick + " with events " + eventList);

            String title = null;
            String date = dateFormatForDisplaying.format(dateClick);

            for (int i = 0; i < eventList.size(); i++) {

                title = (String) eventList.get(i).getData();
            }

            TextView tv_event_title = mDailog.findViewById(R.id.tv_event_title);
            TextView tv_event_date = mDailog.findViewById(R.id.tv_event_date);
            ImageButton btn_dialog_close = mDailog.findViewById(R.id.btn_event_popup_close);

            if (title == null){
                Toast.makeText(HomeActivity.this, "No Events", Toast.LENGTH_SHORT).show();
            }else {
                tv_event_title.setText(title);
                tv_event_date.setText(date);

                btn_dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDailog.dismiss();
                    }
                });

                mDailog.setCancelable(false);
                mDailog.show();
            }

        } catch (NullPointerException ex) {
            Log.e("EventException","Event Null " + ex.getMessage());

        } catch (IndexOutOfBoundsException ex) {
            Log.e("EventException","IndexOutOfBounds " + ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {


            Intent i = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_tasks) {

            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }



        return super.onOptionsItemSelected(item);
    }

}
