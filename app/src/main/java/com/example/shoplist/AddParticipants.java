package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddParticipants extends AppCompatActivity {

    //Database object
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUser;
    private FirebaseAuth firebaseAuth;

    private EditText participantEmail;
    private Button addParticipantsButton;
    private String listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();

        //the email that the user want to add
        participantEmail = findViewById(R.id.addParticipants_email);
        //add participant button ok
        addParticipantsButton = (Button) findViewById(R.id.addParticipans_button);
        //get the list uid from the previous intent
        listId = getIntent().getStringExtra("listUid");

        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the email that the user want to add
                String email = participantEmail.getText().toString();
                //add the participant to the list
                addParticipantToTheList(email);
            }
        });
    }

    private void addParticipantToTheList(String email) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result = "";
                //take the list from the database
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User currUser = snap.getValue(User.class);
                    //check if exists user with this email
                    if (currUser.getEmail().equals(email)) {
                        //get the userID of the user
                        result = snap.getKey();
                        //if this user dosent exists in the specific list -> add his userID to the shoplist
                        if (!currUser.getShopListUID().contains(listId)) {
                            currUser.getShopListUID().add(listId);
                            mDatabase.child(result).setValue(currUser);
                            Toast.makeText(AddParticipants.this, "The user added", Toast.LENGTH_LONG).show();
                        } else {
                            //the user is already exists in the list
                            Toast.makeText(AddParticipants.this, "The user already exists", Toast.LENGTH_LONG).show();
                        }
                        //go back to the previous intent
                        Intent intent = new Intent(AddParticipants.this, AddListActivity.class);
                        startActivity(intent);
                    }
                }
                //the email is invalid (doesnt exists in users)
                if (result.isEmpty()) {
                    Toast.makeText(AddParticipants.this, "The user doesnt exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}