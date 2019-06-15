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
    Spinner batches_spinner;

    List<Programs> programsList = new ArrayList<>();
    List<String> programsName = new ArrayList<>();
    String programID;


    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDB = new DatabaseHelper(this);

        et_firstname = findViewById(R.id.et_signup_firstname);
        et_lastname = findViewById(R.id.et_signup_lastname);
        et_username = findViewById(R.id.et_signup_username);
        et_email = findViewById(R.id.et_signup_email);
        et_phone = findViewById(R.id.et_signup_phone);
        et_password = findViewById(R.id.et_signup_password);
        et_con_password = findViewById(R.id.et_signup_con_password);
//        et_batch_id = findViewById(R.id.et_signup_batch_id);

        programs_spinner = findViewById(R.id.et_signup_programs);
        batches_spinner = findViewById(R.id.et_signup_batch);


        btn_signup = findViewById(R.id.btn_signup_user);
        tv_login_acc = findViewById(R.id.tv_login_acc);

        //signup onClick
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                registerUser();
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
//                getBatches();
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

                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.i("SAARegResponse", response);

                loginAccount();
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
                params.put("batch_id", st_batch_id);

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getPrograms() {

        JsonArrayRequest programRequest = new JsonArrayRequest(Request.Method.GET, Config.PROGRAMS_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("SSAResponse", String.valueOf(response));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(programRequest);

    }
}

