package com.bestow.hackmhs.bestow;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {

    private ListView listView;
    public ArrayList<Item> itemArrayList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse,null);

        listView = view.findViewById(R.id.BrowseFragment_ListView_listView);

        Bitmap tester = BitmapFactory.decodeResource(getResources(),R.drawable.camera);
        itemArrayList = new ArrayList<>();
/*        itemArrayList.add(new Item("TestName","TestDescription", "New York City", tester));
        itemArrayList.add(new Item("TestName","TestDescription", "New York City", tester));
        itemArrayList.add(new Item("TestName","TestDescription", "New York City", tester));
        itemArrayList.add(new Item("TestName","TestDescription", "New York City", tester));*/
        adapter = new ItemAdapter(getActivity(),R.layout.item_layout, itemArrayList);

        listView.setAdapter(adapter);

        //TODO: REFRESH FROM FIREBASE
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemArrayList.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String username = (String) messageSnapshot.child("username").getValue();
                    String description  = (String) messageSnapshot.child("description").getValue();
                    String city = (String) messageSnapshot.child("city").getValue();

                    itemArrayList.add(new Item(username, description, city, ""));

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
