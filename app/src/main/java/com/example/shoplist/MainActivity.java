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

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button addListButton;
    private Button okListButton;
    private EditText listName;

    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    private String listNameString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("\"shopList\"");

        addListButton = (Button)findViewById(R.id.addLists);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddListDialog();

            }
        });
    }


    //build dialog for the customer press the quantity
    public void createAddListDialog(){
        //dialog
        dialogBuilder = new AlertDialog.Builder(this);
        //the layout of the Dialog
        final View layoutAddList = getLayoutInflater().inflate(R.layout.list_name_layout, null);
        //the button on th layout
        listName = (EditText)layoutAddList.findViewById(R.id.plainNameList);
        okListButton = (Button)layoutAddList.findViewById(R.id.okListButton);

        //show the dialog
        dialogBuilder.setView(layoutAddList);
        dialog = dialogBuilder.create();
        dialog.show();

        //Ok button
        okListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listNameString = listName.getText().toString();
                ShopList shopList = new ShopList(listNameString);
                //push the list to the database
                String keyId = mDatabase.push().getKey();
                mDatabase.child(keyId).setValue(shopList);
                //close the dialog
                dialog.dismiss();
                Intent addListIntent = new Intent(MainActivity.this, AddListActivity.class);
                addListIntent.putExtra("key",keyId);
                startActivity(addListIntent);
            }
        });
    }
}