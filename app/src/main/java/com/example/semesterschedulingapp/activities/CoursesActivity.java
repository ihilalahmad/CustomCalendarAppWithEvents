package com.example.semesterschedulingapp.activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.semesterschedulingapp.Pattern.MySingleton;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.example.semesterschedulingapp.Utils.SharedPrefManager;
import com.example.semesterschedulingapp.adapter.CoursesAdapter;
import com.example.semesterschedulingapp.adapter.NotificationAdapter;
import com.example.semesterschedulingapp.model.Courses;
import com.example.semesterschedulingapp.model.NotificationModel;
import com.example.semesterschedulingapp.model.Users;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CoursesAdapter mAdapter;
    private List<Courses> coursesList;
    private Dialog mDialog;

    //    public String course_id;
    public String course_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mDialog = new Dialog(this);

        getAllCourses();
    }

    private void setListeners() {

        mAdapter = new CoursesAdapter(coursesList, CoursesActivity.this, new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onClick(Courses courses) {

                final String course_id = courses.getCourse_id();
                Log.i("CourseID", course_id);

                final String finalCourse_id = course_id;

                mDialog.setContentView(R.layout.join_course_popup);

                final EditText et_classroom_code = mDialog.findViewById(R.id.et_classroom_code);
                Button btn_enroll_course = mDialog.findViewById(R.id.btn_enroll_course);


                btn_enroll_course.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String course_code = et_classroom_code.getText().toString();

                        StringRequest courseRequest = new StringRequest(Request.Method.POST, Config.VERIFYCODE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject resObj = new JSONObject(response);

                                    if (resObj.optString("success").equals("1")) {

                                        String success_messge = resObj.getString("message");
                                        Toast.makeText(CoursesActivity.this, success_messge, Toast.LENGTH_SHORT).show();
                                        Log.i("SSARegRes", success_messge);

                                        FirebaseMessaging.getInstance().subscribeToTopic(course_id);

                                    } else if (resObj.optString("success").equals("0")) {

                                        String success_messge = resObj.getString("message");
                                        Toast.makeText(CoursesActivity.this, success_messge, Toast.LENGTH_SHORT).show();
                                        Log.i("SSARegResErr", success_messge);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(CoursesActivity.this, "Error in Data Response", Toast.LENGTH_SHORT).show();
                                Log.e("SSA SignUp ERR", error.getMessage());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Users user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                                String token_type = user.getTokenType();
                                String access_token = user.getUserToken();

                                Log.i("TokenForCourse", token_type + " " + access_token);

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", token_type + " " + access_token);
                                return params;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("course_id", finalCourse_id);
                                params.put("course_code", course_code);
                                return params;
                            }
                        };
                        MySingleton.getInstance(CoursesActivity.this).addToRequestQueue(courseRequest);
                    }
                });
                mDialog.show();
            }
        });
    }

    private void initViews() {

//        mAdapter = new CoursesAdapter(coursesList,CoursesActivity.this);
        mRecyclerView = findViewById(R.id.courses_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAllCourses() {

        StringRequest coursesRequest = new StringRequest(Request.Method.POST, Config.COURSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject allCourses = new JSONObject(response);
                    coursesList = new ArrayList<>();

                    JSONArray coursesArray = allCourses.getJSONArray("data");
                    Log.i("CoursesRes", String.valueOf(coursesArray));


                    for (int i = 0; i < coursesArray.length(); i++) {

                        JSONObject coursesObj = coursesArray.getJSONObject(i);

                        String course_id = coursesObj.getString("id");
                        String course_name = coursesObj.getString("course_name");
                        String course_teacher = coursesObj.getString("teacher_name");
                        String course_session = coursesObj.getString("session_name");

                        Courses courses = new Courses(course_id, course_name, course_teacher, course_session);
                        coursesList.add(courses);
                        Log.i("coursesList",course_id +" "+ course_name);
                    }
                    setListeners();
                    initViews();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("coursesErr", error.getMessage());

            }
        }) {
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
}
