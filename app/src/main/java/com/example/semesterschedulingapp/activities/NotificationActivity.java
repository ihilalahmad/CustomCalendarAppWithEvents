package com.example.semesterschedulingapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.SharedPrefManager;
import com.example.semesterschedulingapp.adapter.NotificationAdapter;
import com.example.semesterschedulingapp.helpers.DatabaseHelper;
import com.example.semesterschedulingapp.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private List<NotificationModel> notificationModelList;

    String task_title;
    String task_details;
    Bundle notification_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //if the user is not logged in
        //starting the login activity
        isUserLoggedIn();

        myDB = new DatabaseHelper(this);

        notificationModelList = new ArrayList<>();
        mAdapter = new NotificationAdapter(notificationModelList);

        mRecyclerView = findViewById(R.id.notification_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        gettingExtraIntents();
        getTaskDetailsFromDB();
   }

    private void isUserLoggedIn(){

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void insertTaskInDB(String title, String detials){

       boolean result = myDB.insertTask(title,detials);

       if (result){

           Toast.makeText(NotificationActivity.this,"Task Inserted",Toast.LENGTH_SHORT).show();
       }else {
           Toast.makeText(NotificationActivity.this,"Insertion Error",Toast.LENGTH_SHORT).show();

       }

    }

    private void gettingExtraIntents(){

        notification_data = getIntent().getExtras();

        if (notification_data != null)
        {
            task_title = notification_data.getString("title");
            task_details = notification_data.getString("details");

            insertTaskInDB(task_title,task_details);

            Log.i("SSANotificationData", task_title+ " " +task_details);
        }
    }

    private void getTaskDetailsFromDB(){

        Cursor taskDetails = myDB.getTaskDetails();

        if (taskDetails.getCount() > 0){

            while (taskDetails.moveToNext()){

                notificationModelList.add(new NotificationModel(
                        taskDetails.getString(1),
                        taskDetails.getString(2)
                ));
            }
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotificationActivity.this,HomeActivity.class));
    }
}
