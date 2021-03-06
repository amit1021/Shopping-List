package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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

public class HomeActivity extends AppCompatActivity {

    private final String CHANNEL = "channel-1";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

//    private Button addListButton;
    private Button okListButton;
//    private Button addParticipants;

    private EditText listName;
//    private EditText email_addParticipant;
    ListView listView;
    private ArrayAdapter<ShopList> shopAdapter;

    ListView listViewListShare;
    private ArrayAdapter<ShopList> shopAdapterListShare;

    private Intent addListIntent;


    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUser;
    private DatabaseReference mSharList;


    private FirebaseAuth firebaseAuth;

    private String listNameString;

    private ArrayList<String> listUID = new ArrayList<>();
    private ArrayList<ShopList> Allshop_list = new ArrayList<>();
    private ArrayList<ShopList> Allshop_share = new ArrayList<>();
    private ArrayList<ShareList> share_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //add list intent
        addListIntent = new Intent(HomeActivity.this, AddListActivity.class);
        //initialization
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("shopList");
        mSharList = database.getReference("shareList");
        //init the list of user UID
        initlistUID();
        listView = (ListView) findViewById(R.id.myListView);
        listViewListShare = (ListView) findViewById(R.id.myListShare);

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
                            if(!shop.isShare()){
                                //add the shop list to the list of the shop list that user own
                                Allshop_list.add(shop);
                            } else {
                                Allshop_share.add(shop);
                            }
                        }

                    }
                }
                setAdapter();
                setAdapterListShare();
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
                if(user != null){
                    listUID = user.getShopListUID();
                    initShopListUID();
                    initSharelist();
                }
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
                String listName = Allshop_list.get(i).getName();
                whichActivity(Allshop_list.get(i).getUID(), listName);
            }
        });
    }

    //display all the share list of the user

    private void setAdapterListShare() {
        shopAdapterListShare = new ArrayAdapter<ShopList>(this, R.layout.row, Allshop_share);
        listViewListShare.setAdapter(shopAdapterListShare);
        //if press on one of the list, open the list.
        listViewListShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String listUid = Allshop_share.get(i).getUID();
                Intent displayToRead = new Intent(HomeActivity.this, DisplayShopToRead.class);
                displayToRead.putExtra("key", listUid);
                displayToRead.putExtra("activity", "HomeActivity");
                startActivity(displayToRead);
            }
        });
    }



    private void whichActivity(String listUid, String listName) {

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
                            addListIntent.putExtra("listName", listName);
                            // to know which activity pass the UID
                            addListIntent.putExtra("activity", "HomeActivity");
                            startActivity(addListIntent);
                        }else{
                            Intent DisplayShopToRead = new Intent(HomeActivity.this, DisplayShopToRead.class);
                            DisplayShopToRead.putExtra("activity", "HomeActivity");
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
                if(listNameString.isEmpty()){
                    Toast.makeText(HomeActivity.this, "missing name", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }

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
                addListIntent.putExtra("listName", listNameString);
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
        if(R.id.logout == item.getItemId()) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
        else if(R.id.add_list == item.getItemId()) {
            createAddListDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSharelist (){
        mSharList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //take the list from the database
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ShareList shareList = (ShareList) snap.getValue(ShareList.class);
                    for (int i = 0; i < listUID.size(); i++) {
                        //check if the user have the list
                        if (listUID.get(i) != null && listUID.get(i).equals(shareList.getListUid())) {
                            share_list.add(shareList);
                            }
                        }

                    }
                notification();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void notification(){
        for(int i = 0; i < Allshop_share.size(); i++){
            if (share_list.get(i).isSelected() && !share_list.get(i).isNotification()){
                share_list.get(i).setNotification(true);
                mSharList.child(share_list.get(i).getListUid()).setValue(share_list.get(i));
                createChannel();
                addNotification(CHANNEL);
            }
        }
    }
    private void createChannel()
    {
        NotificationManager mNotificationManager= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mNotificationManager = getSystemService(NotificationManager.class);
        }
        String id = CHANNEL;
        CharSequence name = "Shopping list";
        String description = "some volunteer take you'r shopping list ";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel.setDescription(description);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    private void addNotification(String channel)
    {
        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationBuilder = new Notification.Builder(this, channel);
        } else {
//noinspection deprecation
            notificationBuilder = new Notification.Builder(this);
        }
        Notification notification = notificationBuilder
                .setContentTitle("Shopping list")
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setContentText("Some volunteer take you'r shopping list").build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
//notificationManager.notify(1, notification);
    }



}