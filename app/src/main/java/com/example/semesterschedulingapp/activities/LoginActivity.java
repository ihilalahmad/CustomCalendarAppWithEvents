package com.example.semesterschedulingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.semesterschedulingapp.helpers.DatabaseHelper;
import com.example.semesterschedulingapp.model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText et_email,et_password;
    String st_login_email, st_login_password;
    Button login_btn;
    TextView tv_create_acc;

    String username,password;
    DatabaseHelper myDB;
    public String ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);

        login_btn = findViewById(R.id.btn_login_user);
        tv_create_acc = findViewById(R.id.tv_create_acc);

        //login onClick
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginUser();
                loginStudent();
            }
        });

        tv_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void loginStudent(){

        st_login_email = et_email.getText().toString();
        st_login_password = et_password.getText().toString();

        if (TextUtils.isEmpty(st_login_email)) {
            et_email.setError("Please enter your email");
            et_email.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(st_login_email).matches()) {
            et_email.setError("Enter a valid email");
            et_email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(st_login_password)) {
            et_password.setError("Enter a password");
            et_password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Login_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("SAALoginResponse", response);

                try{

                    JSONObject jsonObject = new JSONObject(response);
                    int success_message = jsonObject.getInt("success");

                    if (success_message == 1) {
                        String access_token = jsonObject.getString("access_token");
                        Users users = new Users(st_login_email, st_login_password, access_token);
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(users);

                        Log.i("SSAToken", success_message + " " + access_token);

                        finish();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                    }else if (success_message == 0){

                        String loginErrMsg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this,loginErrMsg,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SSALoginErr", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",st_login_email);
                params.put("password",st_login_password);

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
