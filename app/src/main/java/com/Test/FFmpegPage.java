package com.Test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class FFmpegPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        InitFFmpeg();
    }
    private void InitFFmpeg() {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {

                @Override
                public void onStart() {

                }

                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(), "Library failed to load",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Library loaded successfully",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }
    private void executeFFmpeg(String[] cmd) {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(String message){

                }

                @Override
                public void onFailure(String message) {
                    //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"This failed:" + message, Toast.LENGTH_LONG).show();
                    Log.i("mymessage", message);
                }

                @Override
                public void onSuccess(String message) {
                    //Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"This worked", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // FFMpeg already running
        }
    }

    public void BtnFFmpegClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            Uri selectedUri = data.getData();
            File folder = new File(Environment.getExternalStorageDirectory() + "/MultiCamVideos");
            if(!folder.exists())
            {
                folder.mkdir();
            }
            String filePrefix = "NewVideo";
//        File path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_MOVIES);
//
//        if(path != null) {
//            Log.i("media-path", path.getPath());
//            File[] files = path.listFiles();
//            for (File f : files) {
//                Log.i("media-file", f.getAbsolutePath());
//            }
//        }


            String fileExt = ".mp4";

            File dest = new File(folder,filePrefix + fileExt);

            //String[] command = new String[]{"-ss","1000","-y","-i",originalPath,"-t","3000","-vcodec","mpeg4","-b:v","2097152",
            //        "-b:a","4800","-ac","2","-ar","22050",dest.getAbsolutePath()};
            String[] command = new String[] {"-i",selectedUri.toString(),"-an",dest.getAbsolutePath()};


            executeFFmpeg(command);
        }
    }
}