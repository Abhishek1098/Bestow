package com.bestow.hackmhs.bestow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editTextUsername = findViewById(R.id.LoginActivity_EditText_username);
        final EditText editTextPassword = findViewById(R.id.LoginActivity_EditText_password);

        firebaseAuth = FirebaseAuth.getInstance();

        (findViewById(R.id.LoginActivity_Button_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim());
            }
        });

        (findViewById(R.id.LoginActivity_TextView_createAccount)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
            }
        });
    }

    private void loginUser(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "PLEASE FILL EVERYTHING", Toast.LENGTH_LONG).show();
            return;
        }
        if(!(Patterns.EMAIL_ADDRESS.matcher(username).matches())){
            Toast.makeText(getApplicationContext(), "USERNAME MUST BE AN EMAIL", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length() < 6){
            Toast.makeText(getApplicationContext(), "PASSWORD NEEDS TO BE AT LEAST 6 CHARACTERS", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "SUCCESSFUL LOGIN", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "FAIL LOGIN", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
