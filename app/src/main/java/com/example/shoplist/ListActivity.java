package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText quantity;
    private Button ok;
    private Button addList;
    ListView listView;
    private ArrayList<Item> shop_list = new ArrayList<>();
    private String it;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private FirebaseUser User;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //items
        Item bannana = new Item("בננה");
        Item gvina = new Item("גבינה");
        Item hatzil = new Item("חציל");
        Item guyava = new Item("גויבה");
        Item brokoli = new Item("ברוקולי");
        Item hazeHof = new Item("חזה עוף");


        listView = (ListView)findViewById(R.id.listItems);
        ArrayList<Item> items = new ArrayList<>();
        items.add(bannana);
        items.add(gvina);
        items.add(hatzil);
        items.add(guyava);
        items.add(brokoli);
        items.add(hazeHof);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);

        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("shopList");

        //String UID = User.getUid();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //when the customer press on the item
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the item are pressed
               it = items.get(i).name;
               createNewContactDialog();
            }
        });

        addList = findViewById(R.id.addList);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyId = mDatabase.push().getKey();
                mDatabase.child(keyId).setValue(shop_list);



            }
        });





    }
    //build dialog for the customer press the quantity
    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactQuantity = getLayoutInflater().inflate(R.layout.quantity_layout, null);
        quantity = (EditText) contactQuantity.findViewById(R.id.quantityText);
        ok = (Button) contactQuantity.findViewById(R.id.buttonQ);

        dialogBuilder.setView(contactQuantity);
        dialog = dialogBuilder.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the quantity
                int quantityUser = Integer.parseInt(quantity.getText().toString());
                Item i = new Item(it,quantityUser);
                shop_list.add(i);
                System.out.println(shop_list.toString());

                //close the dialog
                dialog.dismiss();
            }
        });
    }
}