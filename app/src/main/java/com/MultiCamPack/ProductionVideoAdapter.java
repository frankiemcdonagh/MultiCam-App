package com.MultiCamPack;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Objects;


public class ProductionVideoAdapter extends ArrayAdapter<ProductionPathModel> {
    public ArrayList<ProductionPathModel> list;
    private Context mContext;
    int mResource;
    public ProductionVideoAdapter(Context context, int resource, ArrayList<ProductionPathModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        list = objects;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        Uri path = Uri.parse(Objects.requireNonNull(getItem(position)).getPath());
        final ImageView iv  = convertView.findViewById(R.id.production_video_item);

        Bitmap bmThumbnail;

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path.toString(), MediaStore.Video.Thumbnails.MINI_KIND);

        iv.setImageBitmap(bmThumbnail);


        return convertView;
    }
}
