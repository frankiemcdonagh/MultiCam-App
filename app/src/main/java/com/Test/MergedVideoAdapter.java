package com.Test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MergedVideoAdapter extends ArrayAdapter<MergedVideoModel> {
    public ArrayList<MergedVideoModel> list;
    private Context mContext;
    int mResource;
    public MergedVideoAdapter(Context context, int resource, ArrayList<MergedVideoModel> objects) {
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

        final String VideoPath = getItem(position).getPath();

        final ImageView iv  = convertView.findViewById(R.id.mergedVideoThumbnail);
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(VideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
        iv.setImageBitmap(bmThumbnail);

        TextView textViewDate = convertView.findViewById(R.id.txtDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = getItem(position).getDate();

        textViewDate.setText(sdf.format(date));


        TextView textViewName = convertView.findViewById(R.id.txtVideoName);
        File file = new File(VideoPath);
        textViewName.setText(file.getName());

        Button buttonDelete = convertView.findViewById(R.id.item_delete_merged_video);
        Button buttonPlay = convertView.findViewById(R.id.btnPlay_merged_video);
        Button buttonShare = convertView.findViewById(R.id.btnShare_merged_video);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete Video")
                        .setMessage("Are you sure you want to delete the video?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(VideoPath);
                                file.delete();
                                list.remove(position);
                                notifyDataSetChanged();
                            }

                        }).create().show();
            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PlayMergedVideoActivity.class);
                i.putExtra("VideoPath", VideoPath);
                mContext.startActivity(i);
            }
        });
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(VideoPath));
                sendIntent.setType("video/mp4*");
                mContext.startActivity(Intent.createChooser(sendIntent,"Send video via:"));
            }
        });
        return convertView;
    }
}
