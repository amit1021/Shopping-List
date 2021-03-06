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

public class DisplayShopToRead extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef;
    private String listUid;
    private String activity;
    private ListView listView;
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shop_to_read);
        //take the listUid from Home activity
        listUid = getIntent().getStringExtra("key");
        //the activity that reference
        activity = getIntent().getStringExtra("activity");
        //init
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("shopList");
        //init listView
        listView = findViewById(R.id.listViewReader);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShopList shop = (ShopList)snapshot.child(listUid).getValue(ShopList.class);
                items = shop.getItems();
                itemsAdapter = new ArrayAdapter<Item>(DisplayShopToRead.this, R.layout.row, items);
                listView.setAdapter(itemsAdapter);

//                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemToDisplay);

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
            if (R.id.return_ic == item.getItemId()) {
                if (activity!= null && activity.equals("VolunteerHome")) {
                    Intent intent = new Intent(DisplayShopToRead.this, VolunteerHome.class);
                    startActivity(intent);
                }
                else if(activity!= null && activity.equals("HomeActivity")){
                    Intent intent = new Intent(DisplayShopToRead.this, HomeActivity.class);
                    startActivity(intent);
                }
        }
        return super.onOptionsItemSelected(item);
    }

}