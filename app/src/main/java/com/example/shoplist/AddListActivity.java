package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddListActivity extends AppCompatActivity {
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    private DatabaseReference mDatabasePullList;

    //Add Database object
    private FirebaseDatabase Friend_database;
    private DatabaseReference Friend_mDatabase;
    private DatabaseReference Friend_mShopListPointer;
    private FirebaseAuth Friend_firebaseAuth;


    private ListView listView;
    private Button button;
    private Button addPartButton;

    //dialog add participants
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText email;
    private Button editor;
    private Button reader;
    private Button cancel;

    private String listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        //take the uid of the list that the user made
        listId = getIntent().getStringExtra("key");


        //initialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("\"shopList\"");
        mDatabasePullList = firebaseDatabase.getReference("\"shopList\"");

        //take the uid of the list that the user made
        String whichActivity = getIntent().getStringExtra("activity");
        //if the whichActivity equals to HomeActivity' we came from home activity and the list is already exist
        if (whichActivity != null && (whichActivity.equals("HomeActivity") || whichActivity.equals("display"))) {
            firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Item> itemToSet = new ArrayList<>();
                    //get the arraylist of items to update
                    itemToSet = snapshot.child(listId).getValue(ShopList.class).getItems();
                    //ser the itemList to item to set
                    items = itemToSet;
                    setListner();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            //we want to add a new list
            items = new ArrayList<>();
            setListner();
        }
        //add item button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });
    }

    private void openDialog() {
        //dialog
        dialogBuilder = new AlertDialog.Builder(this);
        //the layout of the Dialog
        final View layoutPermission = getLayoutInflater().inflate(R.layout.dialog_permission, null);

        //the button on th layout
        email = (EditText) layoutPermission.findViewById(R.id.editTextTextPersonName);
        editor = (Button) layoutPermission.findViewById(R.id.editor_button);
        reader = (Button) layoutPermission.findViewById(R.id.reader_button);
        cancel = (Button) layoutPermission.findViewById(R.id.cancel_button);

        //show the dialog
        dialogBuilder.setView(layoutPermission);
        dialog = dialogBuilder.create();
        dialog.show();

        //set editor participants
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_user = email.getText().toString();
                addParticipantToTheList(email_user, listId, "editor");
                dialog.dismiss();
            }
        });
        //set reader participants
        reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_user = email.getText().toString();
                addParticipantToTheList(email_user, listId, "reader");
                dialog.dismiss();
            }
        });
        //close windows
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //set the Adapter to display the list
    public void setListner() {
        itemsAdapter = new ArrayAdapter<>(this, R.layout.row, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    //remove item from the list
    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();
                //the item we want to remove
                Item itemRemove = items.get(i);
                items.remove(i);
                //refresh the list (the display list)
                itemsAdapter.notifyDataSetChanged();
                //remove the item from database
                firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Item> itemsToUpdate = new ArrayList<>();
                        //get the arraylist of items to update
                        itemsToUpdate = snapshot.child(listId).getValue(ShopList.class).getItems();
                        //find the item in the arraylist
                        for (int j = 0; j < itemsToUpdate.size(); j++) {
                            //check if the current item equal to the item we want to remove
                            if (itemsToUpdate.get(j).equal(itemRemove)) {
                                //remove from the arraylist
                                itemsToUpdate.remove(j);
                                //update the database
                                firebaseReference.child(listId).child("items").setValue(itemsToUpdate);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                return true;
            }
        });
    }

    private void addItem(View view) {
        //take the values from the user
        EditText input = findViewById(R.id.input);
        String itemText = input.getText().toString();
        EditText quantityItem = findViewById(R.id.quantityItems);

        String quantityText = quantityItem.getText().toString();
        if (!itemText.equals("") && isLegal(quantityText)) {
//            if (items.contains(item.name)){
//
//
//            }
            int quantity = Integer.parseInt(quantityItem.getText().toString());
            Item item = new Item(itemText, quantity);

            //add the item to the list
            itemsAdapter.add(item);

            //add the item to database
            firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ShopList shopList = new ShopList();
                    //get the items arraylist from the snapshot and put in the new Shoplist object
                    shopList.setItems(snapshot.child(listId).getValue(ShopList.class).getItems());
                    //get the list name from the snapshot and put in the new Shoplist object
                    shopList.setName(snapshot.child(listId).getValue(ShopList.class).getName());
                    //get the list UID from the snapshot and put in the new Shoplist object
                    shopList.setUID(snapshot.child(listId).getValue(ShopList.class).getUID());
                    //get the Permission list from the snapshot and put in the new Shoplist object
                    shopList.setPermissions(snapshot.child(listId).getValue(ShopList.class).getPermissions());
                    //add the new item that the user add to the list
                    shopList.getItems().add(item);
                    //update the database with the new list
                    firebaseReference.child(listId).setValue(shopList);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            //clear the input and quantityItem taxtView
            input.setText("");
            quantityItem.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter text", Toast.LENGTH_LONG).show();
        }
    }

    //check that the quantity isnt emnpy and contain only numbers
    public static boolean isLegal(String str) {
        if (str.equals("")) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > '9' || str.charAt(i) < '0') {
                return false;
            }
        }
        return true;
    }

    //return to the previous page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //if the user press on return to the previous button
        if (item.getTitle().equals("return")) {
            Intent intent = new Intent(AddListActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        //if the user press on add participants button
        if (item.getTitle().equals("הוסף")) {
            openDialog();
        }
        if(item.getTitle().equals("הצג")){
            Intent intent = new Intent(AddListActivity.this, FreindsInTheListActivity.class);
            intent.putExtra("listId", listId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void addParticipantToTheList (String email, String listId, String type) {

        Friend_database = FirebaseDatabase.getInstance();
        Friend_mDatabase = Friend_database.getReference("user");
        Friend_firebaseAuth = FirebaseAuth.getInstance();
        Friend_mShopListPointer = Friend_database.getReference("\"shopList\"");

        Friend_mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUid = "";
                //take the list from the database
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User currUser = snap.getValue(User.class);
                    //check if exists user with this email
                    if (currUser.getEmail().equals(email)) {
                        //get the userID of the user
                        userUid = snap.getKey();
                        //if this user doesnt exists in the specific list -> add his userID to the shoplist
                        if (!currUser.getShopListUID().contains(listId)) {
                            currUser.getShopListUID().add(listId);
                            Friend_mDatabase.child(userUid).setValue(currUser);
                            updateShopListPermission(email, userUid, listId, type);
//                            Toast.makeText(AddParticipants.this, "The user added", Toast.LENGTH_LONG).show();
                        } else {
                            //the user is already exists in the list
                             Toast.makeText(AddListActivity.this, "The user already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //go back to the previous intent
//                        Intent intent = new Intent(AddParticipants.this, AddListActivity.class);
//                        startActivity(intent);
                    }
                }
                //the email is invalid (doesnt exists in users)
                if (userUid.isEmpty()) {
                    Toast.makeText(AddListActivity.this, "The user doesnt exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateShopListPermission(String email, String userUid, String listId, String type) {
        Friend_mShopListPointer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShopList shopList = snapshot.child(listId).getValue(ShopList.class);
                ArrayList<UserPermission> user_permission = shopList.getPermissions();
                if(type.equals("reader")) {
                    UserPermission userPer = new UserPermission(userUid, email, "Reader");
                    user_permission.add(userPer);
                    shopList.setPermissions(user_permission);

                }else{
                    UserPermission userPer = new UserPermission(userUid, email, "Editor");
                    user_permission.add(userPer);
                    shopList.setPermissions(user_permission);
                }
                shopList.setPermissions(user_permission);
                Friend_mShopListPointer.child(listId).setValue(shopList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

