package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    //on the activity
    private EditText quantity;
    private Button ok;
    private Button addList;
    ListView listView;

    //list of the items the user choose
    private ArrayList<Item> shop_list = new ArrayList<>();

    //name of the item the user press
    private String itemName;

    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUser;
    private FirebaseAuth firebaseAuth;

    //list of all the list shop that have to the user
    ArrayList<String> listUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Button addList
        addList = findViewById(R.id.addList);

        //items
        Item bannana = new Item("בננה");
        Item gvina = new Item("גבינה");
        Item hatzil = new Item("חציל");
        Item guyava = new Item("גויבה");
        Item brokoli = new Item("ברוקולי");
        Item hazeHof = new Item("חזה עוף");

        //add item to listView(show on the activity)
        listView = (ListView)findViewById(R.id.listItems);
        ArrayList<Item> items = new ArrayList<>();
        items.add(bannana);
        items.add(gvina);
        items.add(hatzil);
        items.add(guyava);
        items.add(brokoli);
        items.add(hazeHof);

        //display the list??
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);

        //add the shop list the user made(?) to shopList tree
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("shopList");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //when the customer press on the item
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the item are pressed
               itemName = items.get(i).name;
               //to press the quantity
               createNewContactDialog();
            }
        });

        //Button addList
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //push the shop list to the tree
                String keyId = mDatabase.push().getKey();
                mDatabase.child(keyId).setValue(shop_list);

                firebaseAuth = FirebaseAuth.getInstance();
                mCurrentUser = database.getReference();

                mCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //take the list from the database
                        listUID = (ArrayList)snapshot.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("shopListUID").getValue();
                        //add the key if the new list
                        listUID.add(keyId);
                        //Update the list on the database
                        FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getCurrentUser().getUid()).child("shopListUID").setValue(listUID);
                        System.out.println(listUID);
                        //pess the shopListUID (keyId to the next activity
                        Intent displayShopList = new Intent(ListActivity.this, displayShopListActivity.class);
                        displayShopList.putExtra("key",keyId);
                        //open activity displayShopListActivity
                        startActivity(displayShopList);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {   }
                });
            }
        });





    }
    //build dialog for the customer press the quantity
    public void createNewContactDialog(){
        //dialog
        dialogBuilder = new AlertDialog.Builder(this);
        //the layout of the Dialog
        final View contactQuantity = getLayoutInflater().inflate(R.layout.quantity_layout, null);
        //the button on th layout
        quantity = (EditText) contactQuantity.findViewById(R.id.quantityText);
        ok = (Button) contactQuantity.findViewById(R.id.buttonQ);
        //show the dialog
        dialogBuilder.setView(contactQuantity);
        dialog = dialogBuilder.create();
        dialog.show();

        //Ok button
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the quantity
                int quantityUser = Integer.parseInt(quantity.getText().toString());
                //create Item with name and quantity
                Item i = new Item(itemName,quantityUser);
                //add to the list shop
                shop_list.add(i);
                System.out.println(shop_list.toString());
                //close the dialog
                dialog.dismiss();
            }
        });
    }
}