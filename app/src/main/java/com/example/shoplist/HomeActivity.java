package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private Button addListButton;
    private Button okListButton;
    private Button addParticipants;

    private EditText listName;
    private EditText email_addParticipant;
    ListView listView;
    private ArrayAdapter<ShopList> shopAdapter;

    private Intent addListIntent;


    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUser;

    private FirebaseAuth firebaseAuth;

    private String listNameString;

    private ArrayList<String> listUID = new ArrayList<>();
    private ArrayList<ShopList> Allshop_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //add list intent
        addListIntent = new Intent(HomeActivity.this, AddListActivity.class);
        //initialization
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("\"shopList\"");
        //init the list of user UID
        initlistUID();
        listView = (ListView) findViewById(R.id.myListView);
    }

    private void initShopListUID() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //take the list from the database
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ShopList shop = (ShopList) snap.getValue(ShopList.class);
                    for (int i = 0; i < listUID.size(); i++) {
                        //check if the user have the list
                        if (listUID.get(i) != null && listUID.get(i).equals(shop.getUID())) {
                            //add the shop list to the list of the shop list that user own
                            Allshop_list.add(shop);
                        }

                    }
                }
                setAdapter();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void initlistUID() {
        firebaseAuth = FirebaseAuth.getInstance();
        String userUID = firebaseAuth.getCurrentUser().getUid();
        mCurrentUser = database.getReference().child("user").child(userUID);

        mCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //take the list from the database
                User user = (User) snapshot.getValue(User.class);
                listUID = user.getShopListUID();
                initShopListUID();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //display all the list of the user
    private void setAdapter() {
        shopAdapter = new ArrayAdapter<ShopList>(this, R.layout.row, Allshop_list);
        listView.setAdapter(shopAdapter);
        //if press on one of the list, open the list.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //when the customer press on the item
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                whichActivity(Allshop_list.get(i).getUID());
                //pass the UID to addListActivity, to show and edit the list in the activity
//                addListIntent.putExtra("key", Allshop_list.get(i).getUID());
//
//                // to know which activity pass the UID
//                addListIntent.putExtra("activity", "HomeActivity");
//                startActivity(addListIntent);
            }
        });
    }

    private void whichActivity(String listUid) {

        firebaseAuth = FirebaseAuth.getInstance();
        String userUID = firebaseAuth.getCurrentUser().getUid();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //take the list from the database
                ArrayList<UserPermission> permissions = (ArrayList<UserPermission>) snapshot.child(listUid).getValue(ShopList.class).getPermissions();
                for(UserPermission per : permissions)
                    if(per.getUserUid().equals(userUID)){
                        if(per.getRole().equals("Editor")){
                            addListIntent.putExtra("key", listUid);
                            // to know which activity pass the UID
                            addListIntent.putExtra("activity", "HomeActivity");
                            startActivity(addListIntent);
                        }else{
                            Intent DisplayShopToRead = new Intent(HomeActivity.this, DisplayShopToRead.class);
                            DisplayShopToRead.putExtra("key", listUid);
                            startActivity(DisplayShopToRead);

                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //build dialog for the customer press the quantity
    public void createAddListDialog() {
        //dialog
        dialogBuilder = new AlertDialog.Builder(this);
        //the layout of the Dialog
        final View layoutAddList = getLayoutInflater().inflate(R.layout.list_name_layout, null);
        //the button on th layout
        listName = (EditText) layoutAddList.findViewById(R.id.plainNameList);
        okListButton = (Button) layoutAddList.findViewById(R.id.okListButton);

        //show the dialog
        dialogBuilder.setView(layoutAddList);
        dialog = dialogBuilder.create();
        dialog.show();

        //Ok button -> create the new list
        okListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listNameString = listName.getText().toString();
                String keyId = mDatabase.push().getKey();


                //initialization
                firebaseAuth = FirebaseAuth.getInstance();
                String userUID = firebaseAuth.getCurrentUser().getUid();
                mCurrentUser = database.getReference().child("user").child(userUID);

                //update the shop list UID of the user
                mCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //take the list from the database
                        User user = (User) snapshot.getValue(User.class);
                        listUID = user.getShopListUID();
                        //add the key if the new list
                        listUID.add(keyId);
                        //Update the list on the database
                        mCurrentUser.setValue(user);
                        //create user permission as "Editor"
                        UserPermission userPermission = new UserPermission(userUID, user.getEmail(), "Editor");
                        //create object shoplist
                        ShopList shopList = new ShopList(listNameString, keyId);
                        //add the user permission to shoplist object
                        shopList.getPermissions().add(userPermission);
                        System.out.println("  -------------------------------mDatabase.toString----------------------------------  " + mDatabase.toString());
                        //push the list to the database
                        mDatabase.child(keyId).setValue(shopList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                //close the dialog
                dialog.dismiss();
                addListIntent.putExtra("key", keyId);
                startActivity(addListIntent);
            }
        });
    }


    //the menu on the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    //the option on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            case R.id.add_list:
                createAddListDialog();

        }
        return super.onOptionsItemSelected(item);
    }
}