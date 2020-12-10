package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FreindsInTheListActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;

    private ArrayList<UserPermission> userPermissions;
    private ArrayAdapter<UserPermission> arrayAdapter;
    private String listId;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freinds_in_the_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("\"shopList\"");

        listView = (ListView)findViewById(R.id.freind_in_list_listView);

        listId = getIntent().getStringExtra("listId");

        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPermissions = snapshot.child(listId).getValue(ShopList.class).getPermissions();
                arrayAdapter = new ArrayAdapter<>(FreindsInTheListActivity.this, android.R.layout.simple_list_item_1 ,userPermissions);
                listView.setAdapter(arrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //return to the previous page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display_to_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.return_ic:
                Intent intent = new Intent(FreindsInTheListActivity.this, AddListActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}