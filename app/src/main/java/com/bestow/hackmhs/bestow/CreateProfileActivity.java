package com.bestow.hackmhs.bestow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    ImageView imageViewProfile;
    TextView textViewName;

    FirebaseAuth firebaseAuth;

    static final int IMAGE_KEY_CODE = 00;

    String imageDownloadUrl;

    private Uri uriImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        imageViewProfile = findViewById(R.id.CreateProfileActivity_ImageView);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), IMAGE_KEY_CODE);
            }
        });

        final EditText editTextName = findViewById(R.id.CreateProfileActivity_EditText_name);
        (findViewById(R.id.CreateProfileActivity_Button_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation(editTextName.getText().toString().trim(), uriImage);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_KEY_CODE && resultCode == RESULT_OK){
            if(data != null && data.getData() != null){
                uriImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                    imageViewProfile.setImageBitmap(bitmap);
                    uploadUserPhoto(uriImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadUserPhoto(Uri uriImage){
        StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        profileImageReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
            }
        });
    }

    private void saveUserInformation(String name, Uri photoUri){
        if(name.isEmpty()){
            Toast.makeText(getApplicationContext(), "FILL NAME", Toast.LENGTH_LONG);
            return;
        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(photoUri).build();
            firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CreateProfileActivity.this, "Profile Updated!", Toast.LENGTH_LONG);
                    }
                }
            });
        }

    }
}
