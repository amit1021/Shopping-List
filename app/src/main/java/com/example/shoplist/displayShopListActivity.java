package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayShopListActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<Item> items = new ArrayList<>();

    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shop_list);

        database = FirebaseDatabase.getInstance();

        //take the name of the list that the user made
        String listId = getIntent().getStringExtra("key");

           listView = (ListView) findViewById(R.id.myList);


        mDatabase = database.getReference().child("shopList");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                items = (ArrayList) snapshot.child(listId).getValue();
                System.out.println("in   :" + items);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {   }

        });

        System.out.println(items);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //when the customer press on the item
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //get the item are pressed
//                itemName = items.get(i).name;
//                //to press the quantity
//                createNewContactDialog();
            }
        });

    }
}