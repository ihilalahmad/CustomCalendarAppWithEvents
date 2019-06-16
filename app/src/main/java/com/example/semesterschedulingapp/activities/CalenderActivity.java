package com.example.semesterschedulingapp.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.helpers.DatabaseHelper;

public class CalenderActivity extends AppCompatActivity {
    CalendarView simpleCalendarView;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        myDialog = new Dialog(this);

        simpleCalendarView = findViewById(R.id.simpleCalendarView);
        simpleCalendarView.setFocusedMonthDateColor(Color.RED);
        simpleCalendarView.setUnfocusedMonthDateColor(Color.BLUE);
        simpleCalendarView.setSelectedWeekBackgroundColor(Color.RED);
        simpleCalendarView.setWeekSeparatorLineColor(Color.GREEN);


        // perform setOnDateChangeListener event on CalendarView
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                int mon = month + 1;
                String event_date = year+"-"+mon+"-"+dayOfMonth;
                Toast.makeText(getApplicationContext(), event_date, Toast.LENGTH_LONG).show();

                createEvent(event_date);
            }
        });
    }

    private void createEvent(final String ev_date){

        myDialog.setContentView(R.layout.popup_create_event); // Set popup layout

        Button closeButton = myDialog.findViewById(R.id.close_button);
        Button submit = myDialog.findViewById(R.id.btn_submit_event);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        final EditText title = myDialog.findViewById(R.id.et_event_title);
        final EditText date = myDialog.findViewById(R.id.et_event_date);
        date.setText(ev_date);
        final EditText description = myDialog.findViewById(R.id.et_event_description);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String event_title = title.getText().toString();
                final String event_date = date.getText().toString();
                final String event_description = description.getText().toString();

                myDialog.dismiss();
            }
        });

        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
    }

}
