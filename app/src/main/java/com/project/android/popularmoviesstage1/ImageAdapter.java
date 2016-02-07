package com.project.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> imageUrl;
    private LayoutInflater inflater;

    public ImageAdapter(Context ctx, List<String> imagePath) {
        this.mContext = ctx;
        this.imageUrl = imagePath;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return imageUrl.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }
        //Set ImageView
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.picture);

        Picasso.with(mContext).load(imageUrl.get(position)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                imageView.setImageResource(R.drawable.img_not_found);
            }
        });
        return imageView;
    }
}
