package com.example.shoplist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class displayShopListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter listAdapter;
    ArrayList<Item> items = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shop_list);

        //take the name of the list that the user made
        String listId = getIntent().getStringExtra("key");
        System.out.println(listId);

        listView = (ListView)findViewById(R.id.myList);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);



    }
}