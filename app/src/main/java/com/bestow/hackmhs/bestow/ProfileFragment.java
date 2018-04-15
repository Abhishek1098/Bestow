package com.bestow.hackmhs.bestow;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shiven Kumar on 4/14/2018.
 */

public class ProfileFragment extends Fragment {


    ImageView profilePic;
    TextView name;
    Button editProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,null);

        profilePic = view.findViewById(R.id.ProfileFragment_ImageView_ProfilePic);
        name = view.findViewById(R.id.ProfileFragment_TextView_Name);
        editProfile = view.findViewById(R.id.ProfileFragment_Button_EditProfile);

        return view;
    }
}
