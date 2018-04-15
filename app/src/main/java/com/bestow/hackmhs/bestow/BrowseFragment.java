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

import java.util.ArrayList;

public class BrowseFragment extends Fragment {

    private ListView listView;
    public static ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse,null);

        listView = view.findViewById(R.id.BrowseFragment_ListView_listView);

        Bitmap tester = BitmapFactory.decodeResource(getResources(),R.drawable.camera);
        items = new ArrayList<>();
        items.add(new Item("TestName","TestDescription", "New York City", tester));
        items.add(new Item("TestName","TestDescription", "New York City", tester));
        items.add(new Item("TestName","TestDescription", "New York City", tester));
        items.add(new Item("TestName","TestDescription", "New York City", tester));
        ItemAdapter adapter = new ItemAdapter(getActivity(),R.layout.item_layout,items);

        listView.setAdapter(adapter);

        //TODO: REFRESH FROM FIREBASE
        return view;
    }
}
