package com.example.semesterschedulingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.semesterschedulingapp.Pattern.MySingleton;
import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.Utils.Config;
import com.example.semesterschedulingapp.helpers.DatabaseHelper;
import com.example.semesterschedulingapp.model.Programs;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText et_firstname, et_lastname, et_username, et_email, et_phone, et_password, et_con_password, et_batch_id;
    String st_firstName, st_lastName, st_userName, st_email, st_phone, st_password, st_con_password, st_batch_id;
    Button btn_signup;
    TextView tv_login_acc;
    Spinner programs_spinner;
    ProgressBar signup_progressbar;

    List<Programs> programsList;
    List<String> programsName = new ArrayList<>();
    int selected_program;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        et_firstname = findViewById(R.id.et_signup_firstname);
        et_lastname = findViewById(R.id.et_signup_lastname);
        et_username = findViewById(R.id.et_signup_username);
        et_email = findViewById(R.id.et_signup_email);
        et_phone = findViewById(R.id.et_signup_phone);
        et_password = findViewById(R.id.et_signup_password);
        et_con_password = findViewById(R.id.et_signup_con_password);

        signup_progressbar = findViewById(R.id.signup_progressbar);

        programs_spinner = findViewById(R.id.et_signup_programs);


        btn_signup = findViewById(R.id.btn_signup_user);
        tv_login_acc = findViewById(R.id.tv_login_acc);

        signup_progressbar.setVisibility(View.GONE);
        //signup onClick
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                registerUser();
                signup_progressbar.setVisibility(View.VISIBLE);
                registerStudent();
            }
        });
        //login onClick
        tv_login_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        getPrograms();
        programs_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_program = programsList.get(position).getProgram_id();

                FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(selected_program));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void loginAccount() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    private void registerStudent() {

        st_firstName = et_firstname.getText().toString();
        st_lastName = et_lastname.getText().toString();
        st_userName = et_username.getText().toString();
        st_email = et_email.getText().toString();
        st_phone = et_phone.getText().toString();
        st_password = et_password.getText().toString();
        st_con_password = et_con_password.getText().toString();
//        st_batch_id = et_batch_id.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SAARegResponse", response);

                try{

                    JSONObject registrationResponse = new JSONObject(response);

                    if (registrationResponse.optString("success").equals("1")){
                        signup_progressbar.setVisibility(View.GONE);

                        String success_messge = registrationResponse.getString("message");
                        Toast.makeText(RegisterActivity.this, success_messge, Toast.LENGTH_SHORT).show();
                        Log.i("SSARegRes",success_messge);
                        loginAccount();

                    }else if (registrationResponse.optString("success").equals("0")){
                        signup_progressbar.setVisibility(View.GONE);
                        String error_messge = registrationResponse.getString("message");
                        Toast.makeText(RegisterActivity.this, error_messge, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SSA SignUp ERR", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", st_firstName);
                params.put("last_name", st_lastName);
                params.put("username", st_email);
                params.put("email", st_email);
                params.put("phone", st_phone);
                params.put("password", st_password);
                params.put("password_confirmation", st_con_password);
                params.put("batch_id", String.valueOf(selected_program));

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getPrograms() {

        StringRequest programRequest = new StringRequest(Request.Method.GET, Config.PROGRAMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONObject programObj = new JSONObject(response);
                    programsList = new ArrayList<>();

                    JSONArray programArray = programObj.getJSONArray("data");

                    for (int i=0; i<programArray.length(); i++){

                        JSONObject programsName = programArray.getJSONObject(i);

                        int program_id = programsName.getInt("id");
                        String program_name = programsName.getString("program_name");
                        Log.i("SSAPrograms",program_id+ " " +program_name);

                        Programs programs = new Programs(program_id,program_name);
                        programsList.add(programs);
                    }

                    for (int j=0; j<programsList.size(); j++){

                        programsName.add(programsList.get(j).getProgram_name());

                        Log.i("SSAProgramNames", String.valueOf(programsName));
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, programsName);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    programs_spinner.setAdapter(spinnerArrayAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SSAProgramsErr", error.getMessage());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(programRequest);

    }
}

