package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerHome extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference VolunteerReference;
    private DatabaseReference shareListReference;
    private FirebaseAuth firebaseAuth;
    private ListView listView;
    private String userUid;
    private ArrayList<ShareList> arrayList;
    private ArrayList<String> listUid;
    private ArrayAdapter<ShareList> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.list_view_volunteer_home);
        arrayList = new ArrayList<>();
        initlistUid();
    }


    private void initlistUid() {
        userUid = firebaseAuth.getCurrentUser().getUid();
        VolunteerReference = firebaseDatabase.getReference().child("volunteer").child(userUid);
        VolunteerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //take the list from the database
                User user = (User) snapshot.getValue(User.class);
                listUid = user.getShopListUID();
                initShareList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initShareList(){
        shareListReference = firebaseDatabase.getReference("\"shareList\"");
        shareListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    for (int i = 0; i < listUid.size(); i++){
                        if (listUid.get(i) != null && listUid.get(i).equals(snap.getKey())){
                            ShareList shareList = (ShareList)snap.getValue(ShareList.class);
                            arrayList.add(shareList);
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

    private void setAdapter(){
        arrayAdapter = new ArrayAdapter<ShareList>(this, R.layout.row, arrayList);
        listView.setAdapter(arrayAdapter);
        System.out.println(arrayList);
        //if press on one of the list, open the list.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //when the customer press on the item
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String shoplistUid = arrayList.get(i).getListUid();
                Intent intent = new Intent(VolunteerHome.this, DisplayShopToRead.class);
                intent.putExtra("key", shoplistUid);
                intent.putExtra("activity", "VolunteerHome");
                startActivity(intent);
            }
        });
    }

    //the menu on the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_volunteer, menu);
        return true;
    }

    //the option on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:

            case R.id.add_list_volunteer_home:
                Intent addList = new Intent(VolunteerHome.this, VolunteerAddList.class);
                startActivity(addList);

//            case R.id.logout:
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}