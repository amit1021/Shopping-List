package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerAddList<DialogBuilder> extends AppCompatActivity {
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private ArrayAdapter<ShareList> arrayAdapter;
    private ArrayList<ShareList> arrayList;

    private FirebaseAuth firebaseAuth;
    private String userUid;

    //create dialog
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    //button
    private Button addShareList;
    private Button cancelShareList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_add_list);
        listView = (ListView) findViewById(R.id.listview_volunteer);
        arrayList = new ArrayList<>();
        //initialization
        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
//        userReference = firebaseDatabase.getReference("\"volunteer\"").child(userUid);
        userReference = firebaseDatabase.getReference().child("volunteer").child(userUid);
        databaseReference = firebaseDatabase.getReference("shareList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    ShareList shareList = snap.getValue(ShareList.class);
                    if(!shareList.isSelected()){
                        arrayList.add(shareList);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter() {
        arrayAdapter = new ArrayAdapter<ShareList>(this, R.layout.volunteer_row, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openDialog(i);
            }
        });
    }

    private void openDialog(int i) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View layoutAddShareList = getLayoutInflater().inflate(R.layout.dialog_add_share_list, null);
        addShareList = (Button) layoutAddShareList.findViewById(R.id.add_button_share);
        cancelShareList = (Button) layoutAddShareList.findViewById(R.id.cancel_button_share);

        //show the dialog
        dialogBuilder.setView(layoutAddShareList);
        dialog = dialogBuilder.create();
        dialog.show();

        cancelShareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        addShareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listUid = arrayList.get(i).getListUid();
                arrayList.get(i).setSelected(true);
                databaseReference.child(listUid).setValue(arrayList.get(i));
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = (User) snapshot.getValue(User.class);
                        ArrayList<String> arrayListUid = user.getShopListUID();
                        arrayListUid.add(listUid);
                        userReference.setValue(user);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    //the menu on the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_list_volunteer, menu);
        return true;
    }

    //the option on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.return_ic:
                Intent intent = new Intent(VolunteerAddList.this, VolunteerHome.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}