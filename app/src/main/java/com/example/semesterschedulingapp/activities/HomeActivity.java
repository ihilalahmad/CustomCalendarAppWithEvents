package com.example.semesterschedulingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.semesterschedulingapp.Pattern.MySingleton;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    CompactCalendarView calendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    TextView tv_monthName;
    ImageButton btn_nextMonth;
    ImageButton btn_prevMonth;

    public int color = Color.GREEN;
    public Dialog mDailog;

    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        calendarView = findViewById(R.id.compactcalendar_view);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setUseThreeLetterAbbreviation(true);

        eventList = new ArrayList<>();
        mDailog = new Dialog(this);

        tv_monthName = findViewById(R.id.tv_month_name);
        btn_nextMonth = findViewById(R.id.btn_next);
        btn_prevMonth = findViewById(R.id.btn_previous);

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
    }

    private void loadEventsFromApi() {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Config.BASE_URL, null, new Response.Listener<JSONArray>() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject catObj = response.getJSONObject(i);

                        long date = catObj.getLong("event_date");
                        String title = catObj.getString("event_title");
                        String ecolor = catObj.getString("event_color");

                        Log.d("SSA Events", "Date Millis: " + date + ", Title: " + title);

                        Event events = new Event(color,date,title);
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
                VolleyLog.d("VolleyErr", "Error: " + error.getMessage());
                Log.d("VolleyErr", "----- Volley Error -----" + error.toString());


                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("No Network Connection")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                loadEventsFromApi();

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });

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

        if (id == R.id.action_calender) {

            Intent intent = new Intent(HomeActivity.this, CalenderActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_settings) {

            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
