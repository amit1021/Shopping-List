package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button myListButton,makeListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myListButton = (Button)findViewById(R.id.myListButton);
        makeListButton = (Button)findViewById(R.id.makeListButton);

        myListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myListIntent = new Intent(HomeActivity.this, myList.class);
                startActivity(myListIntent);
            }
        });

        makeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addListActivity = new Intent(HomeActivity.this, ListActivity.class);
                startActivity(addListActivity);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:


            case R.id.logout:


        }
        return super.onOptionsItemSelected(item);
    }
}