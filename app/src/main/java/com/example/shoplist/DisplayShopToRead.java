package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    private ListView listView;
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shop_to_read);
        //take the listUid from Home activity
        listUid = getIntent().getStringExtra("key");
        System.out.println("key -------------------------- " + listUid);
        //init
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("\"shopList\"");
        //init listView
        listView = findViewById(R.id.listViewReader);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.toString() + " ----------");
                ShopList shop = (ShopList)snapshot.child(listUid).getValue(ShopList.class);
                items = shop.getItems();
                itemsAdapter = new ArrayAdapter<Item>(DisplayShopToRead.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(itemsAdapter);

//                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemToDisplay);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}