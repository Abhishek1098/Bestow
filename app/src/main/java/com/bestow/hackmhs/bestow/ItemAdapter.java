package com.bestow.hackmhs.bestow;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shiven Kumar on 4/14/2018.
 */

public class ItemAdapter extends ArrayAdapter {

    Context con;
    List<Item> itemList;

    public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        con = context;
        itemList = objects;
    }

    @NonNull
    @Override
    public View getView (final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.item_layout,null);
        Item currentItem = itemList.get(position);

        TextView description = layoutView.findViewById(R.id.Item_TextView_Description);
        TextView seller = layoutView.findViewById(R.id.Item_TextView_Seller);
        TextView town = layoutView.findViewById(R.id.Item_TextView_Town);
        ImageView image = layoutView.findViewById(R.id.Item_ImageView_image);

        description.setText(currentItem.getDescription());
        seller.setText(currentItem.getUsername());
        image.setImageBitmap(currentItem.getImage());


        return layoutView;
    }
}
