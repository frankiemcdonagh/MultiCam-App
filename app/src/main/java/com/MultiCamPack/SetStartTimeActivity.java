package com.MultiCamPack;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Objects;

public class SetStartTimeActivity extends AppCompatActivity {
    VideoView videoView;
    ArrayList<StartTimeModel> startTimes;
    StartTimesAdapter startTimesAdapter;
    String videoPath;
    ArrayList<VideoListItemModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_start_time);
        setToolbar();
        //Setting the video path
        videoView = findViewById(R.id.videoViewStartTime);
        Intent i = getIntent();
        videoPath = i.getStringExtra("MainVideo");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        //creating the list and setting the adapter so that it is dynamic
        startTimes = new ArrayList<>();
        startTimesAdapter = new StartTimesAdapter(this, R.layout.add_start_time_item, startTimes);
        ListView listView = findViewById(R.id.listViewStartTimeVideos);
        listView.setAdapter(startTimesAdapter);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    public void btnInfo(View view) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(SetStartTimeActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_set_start_time, null);
        mbuilder.setView(mView);
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }

    public void btnStartTimeClick(View view) {
        //Gets the current time of video and converts to a string
        int startTimeInt = videoView.getCurrentPosition();
        StartTimeModel stm = new StartTimeModel(startTimeInt);
        startTimes.add(stm);
        startTimesAdapter.notifyDataSetChanged();
        videoView.seekTo(0);
        videoView.pause();
    }

    public void btnPlayClick(View view) {
        videoView.seekTo(0);
        videoView.start();
    }

    public void btnSaveClick(View view) {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            arrayList = bundle.getParcelableArrayList("list");
        }
        String stringAverage = "0";
        if(startTimes.size() != 0) {
            int sum = 0;
            int counter = 0;
            int average;
            for (StartTimeModel time : startTimes) {
                counter++;
                int intStartTime = time.getStartTime();
                sum += intStartTime;
            }
            //DecimalFormat df = new DecimalFormat("#.##")        average = sum/counter;
            //average = average/1000;
            average = sum / counter;
            stringAverage = Integer.toString(average);
        }
        //stringAverage = df.format(stringAverage);
        Intent i = new Intent(this, AddVideosActivity.class);
        i.putExtra("VideoPath", videoPath);
        i.putExtra("Average Time", stringAverage);
        Bundle b = new Bundle();
        b.putParcelableArrayList("list", arrayList);
        i.putExtras(b);
        this.startActivity(i);
    }
}
