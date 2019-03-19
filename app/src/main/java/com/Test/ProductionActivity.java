package com.Test;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.util.ArrayList;

public class ProductionActivity extends AppCompatActivity {

    public  int currentposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.listViewVideos);
        final VideoView videoView = findViewById(R.id.videoViewPlayer);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        final ArrayList<String> varray = new ArrayList<>();
        varray.add("video1.mp4");
        varray.add("video2.mp4");
        final ArrayList<String> RecordedChanges = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, varray);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoposition = varray.get(position);
                if(videoposition.equals("video1.mp4")){

                    currentposition = videoView.getCurrentPosition();
                    RecordedChanges.add("video1" + " - " + currentposition);
                    Log.d("ChangesList",RecordedChanges.toString());
                    String videopath = "android.resource://" + getPackageName() + "/" + R.raw.video1;

                    Uri uri = Uri.parse(videopath);
                    videoView.setVideoURI(uri);
                    videoView.pause();
                    videoView.seekTo(currentposition);
                    videoView.start();



                }
                else{
                    currentposition = videoView.getCurrentPosition();
                    RecordedChanges.add("video2" + " - " + currentposition);
                    Log.d("ChangesList",RecordedChanges.toString());
                    String videopath = "android.resource://" + getPackageName() + "/" + R.raw.video2;

                    Uri uri = Uri.parse(videopath);
                    videoView.setVideoURI(uri);
                    videoView.pause();
                    videoView.seekTo(currentposition);
                    videoView.start();



                }


            }
        });

    }



}
