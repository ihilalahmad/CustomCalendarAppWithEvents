package com.example.semesterschedulingapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.semesterschedulingapp.R;

public class SearchActivity extends AppCompatActivity {
    MenuItem myActionMenuItem;
    SearchView searchView;
    FragmentManager fm;

    TextView tv_searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tv_searchTerm = findViewById(R.id.tv_searchTerm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);


        myActionMenuItem = menu.findItem(R.id.action_search);
        myActionMenuItem.expandActionView();

        searchView = (SearchView) myActionMenuItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();
                ft.commit();

                tv_searchTerm.setText(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
}
