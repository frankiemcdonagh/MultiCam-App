package com.Test;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayMergedVideoActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_merged_video);
        videoView = findViewById(R.id.merged_video_video_view);
        mediaController = new MediaController(this);
        Intent intent = getIntent();
        String videoPath = intent.getExtras().getString("VideoPath");
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}
