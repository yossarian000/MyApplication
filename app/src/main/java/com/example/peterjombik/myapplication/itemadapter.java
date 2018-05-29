package com.example.peterjombik.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class itemadapter extends BaseAdapter {
    private Context mContext;
    private List<ItemObject> myList;
    private LayoutInflater layoutinflater;

    //private ArrayAdapter<ItemObject> myList;

    public itemadapter(Context c, int simple_list_item_1, List<ItemObject> images) {
    //public itemadapter(Context c, ArrayAdapter<ItemObject> images) {
        this.mContext = c;
        layoutinflater =(LayoutInflater)c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        myList = images;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
//
//
//    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView imageView;

        ViewHolder listViewHolder;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            //imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);

            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.mything_item, parent, false);
            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.textView);
            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.imageView);
            listViewHolder.dataInListView = (TextView) convertView.findViewById(R.id.dataView);

            listViewHolder.imageInListView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //listViewHolder.imageInListView.setLayoutParams(new ViewGroup.LayoutParams(25,25));

            convertView.setTag(listViewHolder);

        } else {
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.textInListView.setText(myList.get(position).getName());
        listViewHolder.dataInListView.setText(myList.get(position).getTopic());

        int imageResourceId = this.mContext.getResources().getIdentifier(myList.get(position).getImageResource(), "drawable", this.mContext.getPackageName());
        listViewHolder.imageInListView.setImageResource(imageResourceId);

        return convertView;
    }

    static class ViewHolder{
        TextView textInListView;
        ImageView imageInListView;
        TextView dataInListView;
    }
}
