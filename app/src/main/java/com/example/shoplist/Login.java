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

public class Login extends AppCompatActivity {

    private Button buttonRegister;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText email, password;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //register button
        buttonRegister = (Button)findViewById(R.id.loginRegister);
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
                if(mFirebaseUser != null){
                    Toast.makeText(Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(Login.this, HomeActivity.class);
                    startActivity(home);
                } else {
                    Toast.makeText(Login.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String emailUser = email.getText().toString();
                String passwordUser = password.getText().toString();
                if (emailUser.isEmpty()) {
                    email.setError("Please Enter Email Id");
                    email.requestFocus();
                }
                else if (passwordUser.isEmpty()) {
                    password.setError("Please Enter Password Id");
                    password.requestFocus();
                }
                else if (emailUser.isEmpty() && passwordUser.isEmpty()){
                    Toast.makeText(Login.this, "Fileds are empty", Toast.LENGTH_SHORT).show();
                } else if (!(emailUser.isEmpty() && passwordUser.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToHome = new Intent(Login.this, ListActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Error", Toast.LENGTH_SHORT).show();
                }
                }
        });

    }

    public void openRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


}