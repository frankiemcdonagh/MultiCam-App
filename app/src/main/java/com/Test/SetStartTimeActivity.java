package com.Test;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import java.util.ArrayList;

public class SetStartTimeActivity extends AppCompatActivity {
    VideoView videoView;
    ArrayList<String> StartTimes;
    ArrayAdapter<String> arrayAdapter;
    String videoPath;
    ArrayList<VideoListItemModel> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_start_time);

        //Setting the video path
        videoView = findViewById(R.id.videoViewStartTime);
        Intent i = getIntent();
        videoPath = i.getStringExtra("MainVideo");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        //creating the list and setting the adapter so that it is dynamic
        StartTimes = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, StartTimes);
        ListView listView = findViewById(R.id.listViewStartTimeVideos);
        listView.setAdapter(arrayAdapter);
    }

    public void btnStartTimeClick(View view) {
        //Gets the current time of video and converts to a string
        int startTimeInt = videoView.getCurrentPosition();
        String startTime = Integer.toString(startTimeInt);
        String listItem = startTime;
        StartTimes.add(listItem);
        arrayAdapter.notifyDataSetChanged();
        videoView.seekTo(0);
        videoView.pause();
    }

    public void btnPlayClick(View view) {
        videoView.start();
    }

    public void btnSaveClick(View view) {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            arrayList = bundle.getParcelableArrayList("list");
        }
        int sum = 0;
        int counter = 0;
        int average;
        for(String time : StartTimes)
        {
            counter++;
            int intStartTime = Integer.parseInt(time);
            sum += intStartTime;
        }
        average = sum/counter;
        String stringAverage = Integer.toString(average);
        Intent i = new Intent(this, AddVideosActivity.class);
        i.putExtra("VideoPath", videoPath);
        i.putExtra("Average Time", stringAverage);
        Bundle b = new Bundle();
        b.putParcelableArrayList("list", arrayList);
        //i.putExtras(b);
        this.startActivity(i);
    }
}
