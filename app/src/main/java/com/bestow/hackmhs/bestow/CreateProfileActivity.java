package com.bestow.hackmhs.bestow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_KEY = 101;

    private ImageView imageViewPreview;
    private EditText editTextName;

    private Uri uriProfileImage;

    private String profileImageUrl;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        imageViewPreview = findViewById(R.id.CreateProfileActivity_ImageView);
        imageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageSelector();
            }
        });


        editTextName = findViewById(R.id.CreateProfileActivity_EditText_name);
        (findViewById(R.id.CreateProfileActivity_Button_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void saveUserInformation(){
        String displayName = editTextName.getText().toString();

        if (displayName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "FILL NAME!", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && profileImageUrl != null) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageUrl)).build();
            firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(firebaseUser != null){
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setImageSelector(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), SELECT_IMAGE_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_KEY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageViewPreview.setImageBitmap(bitmap);
                createProfileImageUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createProfileImageUrl(){
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            });
        }
    }
}
