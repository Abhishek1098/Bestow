package com.bestow.hackmhs.bestow;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView imageViewProfilePicture;
    private TextView textViewName;
    private ListView listView;
    private ArrayList<Item> items;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile,null);

        imageViewProfilePicture = view.findViewById(R.id.ProfileFragment_ImageView_ProfilePic);
        textViewName = view.findViewById(R.id.ProfileFragment_TextView_Name);
        listView = view.findViewById(R.id.ProfileFragment_ListView_ItemList);

        (view.findViewById(R.id.ProfileFragment_Button_EditProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateProfileActivity.class));
            }
        });//

        setView();

        (view.findViewById(R.id.ProfileFragment_Button_signOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        Bitmap tester = BitmapFactory.decodeResource(getResources(),R.drawable.ic_browse);
        items = new ArrayList<>();
            items.add(new Item("test","blue t shirt", "nyc", tester));
        ItemAdapter adapter  = new ItemAdapter(getActivity(),R.layout.item_layout,items);
        listView.setAdapter(adapter);

        return view;
    }

    public void setView(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            if(firebaseUser.getPhotoUrl() != null){
                Glide.with(this).load(firebaseUser.getPhotoUrl().toString()).into(imageViewProfilePicture);
            }else{
                imageViewProfilePicture.setImageResource(R.drawable.ic_default);
            }
            if(firebaseUser.getDisplayName() != null){
                textViewName.setText(firebaseUser.getDisplayName());
            }
        }
    }
}
