package com.example.shoplist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        openHome();
        openLogin();
    }

    public void openLogin(){
        Intent login = new Intent(MainActivity.this, Login.class);
        startActivity(login);
    }

    public void openHome(){
        Intent Home = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(Home);
    }
}