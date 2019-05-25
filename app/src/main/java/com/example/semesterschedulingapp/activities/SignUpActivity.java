package com.example.semesterschedulingapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.helpers.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    EditText et_firstname, et_lastname, et_username, et_email, et_password;
    CardView signup_btn;
    TextView tv_login_acc;

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
        et_password = findViewById(R.id.et_signup_password);

        signup_btn = findViewById(R.id.signup_cardview);
        tv_login_acc = findViewById(R.id.tv_login_acc);

        //signup onClick
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //login onClick
        tv_login_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });
    }

    private void loginAccount() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }

    private void registerUser() {
        String firstname = et_firstname.getText().toString();
        String lastname = et_lastname.getText().toString();
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        Cursor getUsers;
        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Complete All Field", Toast.LENGTH_SHORT).show();
        } else {
            getUsers = myDB.checkRepeatUser(username);
            if (getUsers != null && getUsers.getCount()>0) {
                Toast.makeText(SignUpActivity.this, "Username Already Taken!", Toast.LENGTH_SHORT).show();
            } else {
                Boolean result = myDB.insertUser(firstname, lastname, username, email, password);
                if (result) {
                    Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));

                    et_firstname.setText("");
                    et_lastname.setText("");
                    et_username.setText("");
                    et_email.setText("");
                    et_password.setText("");
                } else {
                    Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
