package com.example.shoplist;

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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class addParticipant {

        public static void addParticipantToTheList(String email, String listId, String type) {

        FirebaseDatabase friend_database = FirebaseDatabase.getInstance();
        DatabaseReference friend_mDatabase = friend_database.getReference("user");
//        FirebaseAuth friend_firebaseAuth = FirebaseAuth.getInstance();

        friend_mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            friend_mDatabase.child(userUid).setValue(currUser);
                            updateShopListPermission(email, userUid, listId, type);
//                            Toast.makeText(addPart.class, "The user added", Toast.LENGTH_LONG).show();
                        } else {
                            //the user is already exists in the list
//                             Toast.makeText(AddParticipants.this, "The user already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //go back to the previous intent
//                        Intent intent = new Intent(AddParticipants.this, AddListActivity.class);
//                        startActivity(intent);
                    }
                }
                //the email is invalid (doesnt exists in users)
                if (userUid.isEmpty()) {
//                         Toast.makeText(AddParticipants.this, "The user doesnt exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private static void updateShopListPermission(String email, String userUid, String listId, String type) {
        FirebaseDatabase friend_database = FirebaseDatabase.getInstance();
        DatabaseReference friend_mShopListPointer = friend_database.getReference("shopList");

        friend_mShopListPointer.addListenerForSingleValueEvent(new ValueEventListener() {
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
                friend_mShopListPointer.child(listId).setValue(shopList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
