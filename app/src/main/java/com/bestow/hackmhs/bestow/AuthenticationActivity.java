package com.bestow.hackmhs.bestow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        final EditText editTextUsername = findViewById(R.id.AuthenticationActivity_EditText_username);
        final EditText editTextPassword = findViewById(R.id.AuthenticationActivity_EditText_password);

        firebaseAuth = FirebaseAuth.getInstance();

        (findViewById(R.id.AuthenticationActivity_Button_createAccount)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim());
            }
        });

        (findViewById(R.id.AuthenticationActivity_TextView_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void createUser(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "PLEASE FILL EVERYTHING", Toast.LENGTH_LONG).show();
            return;
        }
        if(!(Patterns.EMAIL_ADDRESS.matcher(username).matches())){
            Toast.makeText(getApplicationContext(), "PLEASE USE VALID EMAIL", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length() < 6){
            Toast.makeText(getApplicationContext(), "PASSWORD NEEDS TO BE AT LEAST 6 CHARACTERS", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "USER CREATED", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "ACCOUNT IS ALREADY REGISTERED", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "AUTHENTICATION FAILED", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

}
