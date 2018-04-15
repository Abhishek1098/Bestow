package com.bestow.hackmhs.bestow;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private ImageView imageViewProfilePicture;
    private TextView textViewName;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile,null);

        imageViewProfilePicture = view.findViewById(R.id.ProfileFragment_ImageView_ProfilePic);
        textViewName = view.findViewById(R.id.ProfileFragment_TextView_Name);

        (view.findViewById(R.id.ProfileFragment_Button_EditProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateProfileActivity.class));
            }
        });

        setView();

        return view;
    }

    public void setView(){
        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.getDisplayName() != null){
            textViewName.setText(firebaseUser.getDisplayName().toString());
        }

        if(firebaseUser.getPhotoUrl() != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), firebaseUser.getPhotoUrl());
                imageViewProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imageViewProfilePicture.setImageResource(R.drawable.ic_default);
        }

    }
}
