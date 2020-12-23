package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userAuthPassword;
    private Button registerButton;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String USER = "user";
    private static final String TAG = "Register";
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.pass);
        userAuthPassword = findViewById(R.id.pass2);
        registerButton = findViewById(R.id.register);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

        //register button
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //take the values
                String user = userName.getText().toString();
                String email = userEmail.getText().toString().toLowerCase();
                System.out.println("-------------------------------> " +  email);
                String password = userPassword.getText().toString();
                String passwordAuth = userAuthPassword.getText().toString();
                //if one of the fields is empty, try again
                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordAuth)){
                    Toast.makeText(getApplicationContext(), "Not valid, try again",
                            Toast.LENGTH_LONG).show();
                    return;
                }if(!password.equals(passwordAuth)){
                    Toast.makeText(getApplicationContext(), "Not valid, try again",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //create new user
                newUser = new User(user, email, password);
                //add the user to database
                registerUser(email, password);
            }
        });
    }

    public void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure" , task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser){
        //create UID to the user
        String keyId = mDatabase.push().getKey();
        //add the user to the database
        mDatabase.child(mAuth.getUid()).setValue(newUser);
        //go to home activity
        Intent HomeIntent = new Intent(this, HomeActivity.class);
        startActivity(HomeIntent);
    }
}