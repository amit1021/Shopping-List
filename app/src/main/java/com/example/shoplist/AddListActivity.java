package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddListActivity extends AppCompatActivity {
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;

    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        //take the uid of the list that the user made
        String listId = getIntent().getStringExtra("key");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("shopList").child(listId);


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                items.remove(i);
                //refresh the list (the display list)
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View view){
        EditText input = findViewById(R.id.input);
        String itemText = input.getText().toString();
        EditText quantityItem = findViewById(R.id.quantityItems);
        int quantity = Integer.parseInt(quantityItem.getText().toString());
        Item item = new Item(itemText, quantity);



        if(!itemText.equals("")){
            itemsAdapter.add(item);

            ShopList shopList = new ShopList();

            firebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    shopList =(ShopList) snapshot.getValue();
                    listUID = (ArrayList)snapshot.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("shopListUID").getValue();
                    //add the key if the new list
                    listUID.add(keyId);
                    //Update the list on the database
                    FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getCurrentUser().getUid()).child("shopListUID").setValue(listUID);
                    //pess the shopListUID (keyId to the next activity
                    Intent displayShopList = new Intent(ListActivity.this, displayShopListActivity.class);
                    displayShopList.putExtra("key",keyId);
                    //open activity displayShopListActivity
                    startActivity(displayShopList);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {   }
            });







            input.setText("");
            quantityItem.setText("");
        } else {
          Toast.makeText(getApplicationContext(), "Please enter text", Toast.LENGTH_LONG).show();
        }
    }
}