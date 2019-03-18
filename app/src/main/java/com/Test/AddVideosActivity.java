package com.Test;

import android.content.Intent;
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

public class AddVideosActivity extends AppCompatActivity {

    Uri selectedUri;
    ListView videoList;
    CustomAdapter customAdapter;
    ArrayList<VideoListItemModel> videoArray;
    ArrayList<VideoListItemModel> newarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_videos);
        videoList = findViewById(R.id.videoList);
        videoArray = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            videoArray = bundle.getParcelableArrayList("list");
        }
        customAdapter = new CustomAdapter(this, R.layout.addvideo_item, videoArray);
        videoList.setAdapter(customAdapter);

    }

    public void addVideo(View v)
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");

        startActivityForResult(i, 100);
    }

    public void buttonAddTime(View v){
        Log.i("Unique message", "You clicked the button");
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
            long timeInMillisec = Long.parseLong(time );

            retriever.release();

            VideoListItemModel testEntry = new VideoListItemModel(selectedUri.toString(), "0");
            videoArray.add(testEntry);
            customAdapter.notifyDataSetChanged();
            //String newString = "newString";
        }
    }
}
