package com.Test;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class AddVideosActivity extends AppCompatActivity {

    Uri selectedUri;
    ListView videoList;
    CustomAdapter customAdapter;
    ArrayList<VideoListItemModel> videoArray;
    ArrayList<VideoListItemModel> newArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_videos);
        videoList = findViewById(R.id.videoList);
        videoArray = new ArrayList<>();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle != null) {
            newArray = bundle.getParcelableArrayList("list");
        }
        String mainVideo = i.getStringExtra("VideoPath");
        String startTime = i.getStringExtra("Average Time");
        if(mainVideo != null){
            for(VideoListItemModel video : newArray)
            {
                //Todo: Set start time not working.
                if(mainVideo.equals(video.getName())){
                    video.setStartTime(startTime);
                    break;
                }
            }
        }
        if(newArray != null) videoArray.addAll(newArray);
        customAdapter = new CustomAdapter(this, R.layout.addvideo_item, videoArray);
        videoList.setAdapter(customAdapter);

    }

    public void addVideo(View v)
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");

        startActivityForResult(i, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            selectedUri = data.getData();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(), selectedUri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);

            retriever.release();
        }
    }

    public void btnConvertVideos(View view) {
        ArrayList<String> realVideoPaths = new ArrayList<>();
        ArrayList<Integer> durations = new ArrayList<>();
        if(videoArray != null) {
            for (VideoListItemModel video : videoArray) {
                String videoName = video.getName();
                Uri videoUri = Uri.parse(videoName);

                int startTime = Integer.parseInt(video.getStartTime());

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(), videoUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int timeInMillisec = Integer.parseInt(time);
                int durationAfterStartTime = timeInMillisec - startTime;
                durations.add(durationAfterStartTime);



                retriever.release();
                realVideoPaths.add(getRealPathFromUri(getApplicationContext(), videoUri));
            }
            Collections.sort(durations);
            int endVideoDuration = durations.get(0);
        }
    }
    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally{
            if(cursor!=null)
            {
                cursor.close();
            }
        }

    }
}
