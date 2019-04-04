package com.example.semesterschedulingapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semesterschedulingapp.R;
import com.example.semesterschedulingapp.helpers.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    EditText et_username,et_password;
    CardView login_btn;
    TextView tv_create_acc;

    String username,password;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        et_username = findViewById(R.id.et_login_username);
        et_password = findViewById(R.id.et_login_password);

        login_btn = findViewById(R.id.login_cardView);
        tv_create_acc = findViewById(R.id.tv_create_acc);

        //login onClick
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
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
        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
    }

    private void loginUser() {
        String name = et_username.getText().toString();
        String pass = et_password.getText().toString();
        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Complete All Fields!", Toast.LENGTH_SHORT).show();

        } else {

            Cursor cursor = myDB.getDetails();
            while (cursor.moveToNext()) {
                username = cursor.getString(3);
                password = cursor.getString(5);
            }

            if (name.equals(username) && pass.equals(password)) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {

                Toast.makeText(MainActivity.this, "Username or Password is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
