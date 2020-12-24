package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class AddListActivity extends AppCompatActivity {
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    private DatabaseReference mShareListReference;

    private ListView listView;
    private Button button;
    private Button shareList;

    //dialog share list
    private AlertDialog.Builder shareListDialogBuilder;
    private AlertDialog shareListDialog;
    private EditText nameContact;
    private EditText addressContact;
    private EditText phoneContact;
    private Button sendShareList;
    private String listName;

    private ShopList shopList;

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
        shareList = findViewById(R.id.shareList_menu);

        //take the uid of the list that the user made
        listId = getIntent().getStringExtra("key");
        //take the list name
        listName = getIntent().getStringExtra("listName");

        //initialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("\"shopList\"");
        mShareListReference = firebaseDatabase.getReference("\"shareList\"");

        //take the uid of the list that the user made
        String whichActivity = getIntent().getStringExtra("activity");
        //if the whichActivity equals to HomeActivity' we came from home activity and the list is already exist
        if (whichActivity != null && (whichActivity.equals("HomeActivity") || whichActivity.equals("display"))) {
            firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Item> itemToSet = new ArrayList<>();
                    // get the arraylist of items to update
                    itemToSet = snapshot.child(listId).getValue(ShopList.class).getItems();
                    // get the shoplist
                    shopList = snapshot.child(listId).getValue(ShopList.class);
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

    private void openDialogShareList(){
        //dialog
        shareListDialogBuilder = new AlertDialog.Builder(this);
        //the layout of the Dialog
        final View layoutShareList = getLayoutInflater().inflate(R.layout.dialog_sharelist, null);
        //the button on th layout
        nameContact = (EditText) layoutShareList.findViewById(R.id.name_contact);
        addressContact = (EditText) layoutShareList.findViewById(R.id.address_contact);
        phoneContact = (EditText) layoutShareList.findViewById(R.id.phone_contact);
        sendShareList = (Button) layoutShareList.findViewById(R.id.send_sharelist_button);
        //show the dialog
        shareListDialogBuilder.setView(layoutShareList);
        shareListDialog = shareListDialogBuilder.create();
        shareListDialog.show();

        sendShareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_contact = nameContact.getText().toString();
                String address_contact = addressContact.getText().toString();
                String phone_contact = phoneContact.getText().toString();
                //create object shareList
                ShareList shareList = new ShareList(name_contact, address_contact, phone_contact, listId, listName);
                mShareListReference.child(listId).setValue(shareList);
                shareListDialog.dismiss();
                closeListToEdit();
            }
        });
    }

    private void closeListToEdit() {
        shopList.setShare(true);
        System.out.println(shopList.isShare());
        firebaseReference.child(listId).setValue(shopList);
    }

    private void openDialogPermission() {
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
                String email_user = email.getText().toString().toLowerCase();
                addParticipant.addParticipantToTheList(email_user, listId, "editor");
                dialog.dismiss();
            }
        });
        //set reader participants
        reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_user = email.getText().toString();
                addParticipant.addParticipantToTheList(email_user, listId, "reader");
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
            openDialogPermission();
        }

        if(item.getTitle().equals("הצג")){
            Intent intent = new Intent(AddListActivity.this, FreindsInTheListActivity.class);
            intent.putExtra("listId", listId);
            startActivity(intent);
        }

        //if the user prees on share list
        if(item.getTitle().equals("שתף")){
            openDialogShareList();
        }

        return super.onOptionsItemSelected(item);
    }
}

