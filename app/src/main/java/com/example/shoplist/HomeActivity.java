package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button addListButton;
    private Button okListButton;
    private EditText listName;
    ListView listView;

    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUser;
    private FirebaseAuth firebaseAuth;

    private String listNameString;

    private ArrayList<String> listUID;
    private ArrayList<String> shoplistUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("\"shopList\"");

        initShopListUID();
        //createUserShopLists();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            String name = signInAccount.getDisplayName();
            String email = signInAccount.getEmail();

            System.out.println("name.............................." + name + "........................" + email);
        }


        listView = (ListView)findViewById(R.id.myListView);
        addListButton = (Button)findViewById(R.id.addList_button);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddListDialog();

            }
        });
    }

    private void initShopListUID() {


    }

//    private void createUserShopLists() {
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        String userUID = firebaseAuth.getCurrentUser().getUid();
//        mCurrentUser = database.getReference().child("user").child(userUID);
//
//        mCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //take the list from the database
//                User user = (User)snapshot.getValue(User.class);
//                System.out.println("--------------------------------------------1111111" );
//                listUID = user.getShopListUID();
//                ArrayList<String> arr = mDatabase.child("shopList").getKey();
//                for(int i = 0; i < listUID.size(); i++){
//                    System.out.println("-------------------------------------------2222222");
//                    for (String str : mDatabase.getKey()){
//                        System.out.println("--------------------------------------------333333");
//                        System.out.println("Snap--------------" + snap.getKey() + "--------------listUid----------" + listUID.get(i));
//                        if(snap.getKey().equals(listUID.get(i))){
//                            System.out.println("--------------------------------------------" + snap.getKey());
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {   }
//        });
//    }


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

                System.out.println(mDatabase.toString());

//                firebaseAuth = FirebaseAuth.getInstance();
//                String userUID = firebaseAuth.getCurrentUser().getUid();
//                mCurrentUser = database.getReference().child("user").child(userUID);

                mCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //take the list from the database
                        User user = (User)snapshot.getValue(User.class);
                        listUID = user.getShopListUID();
                        //add the key if the new list
                        listUID.add(keyId);
                        System.out.println("Email: " +  user.getEmail());
                        //Update the list on the database
                        mCurrentUser.setValue(user);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {   }
                });

                //close the dialog
                dialog.dismiss();
                Intent addListIntent = new Intent(HomeActivity.this, AddListActivity.class);
                addListIntent.putExtra("key",keyId);
                startActivity(addListIntent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}