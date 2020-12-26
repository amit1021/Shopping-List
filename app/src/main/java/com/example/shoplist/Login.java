package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button buttonRegister;

    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private EditText email, password;
    private Button buttonLogin;
    private String  userUid;
    private boolean answer;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            userUid = user.getUid();
            init(userUid);
        }
    }

    private void init(String userUid) {
        answer = false;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("volunteer");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.getKey().equals(userUid)) {
                        answer = true;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (answer) {
                    Toast.makeText(Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), VolunteerHome.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //register button
        buttonRegister = (Button) findViewById(R.id.loginRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        //login button
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.loginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    user = mFirebaseAuth.getCurrentUser();
                    userUid = user.getUid();
                    init(userUid);

                } else {
                    Toast.makeText(Login.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString();
                String passwordUser = password.getText().toString();
                if (emailUser.isEmpty()) {
                    email.setError("Please Enter Email Id");
                    email.requestFocus();
                } else if (passwordUser.isEmpty()) {
                    password.setError("Please Enter Password Id");
                    password.requestFocus();
                } else if (emailUser.isEmpty() && passwordUser.isEmpty()) {
                    Toast.makeText(Login.this, "Fileds are empty", Toast.LENGTH_SHORT).show();
                } else if (!(emailUser.isEmpty() && passwordUser.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            } else {
                                user = mFirebaseAuth.getCurrentUser();
                                userUid = user.getUid();
                                init(userUid);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}