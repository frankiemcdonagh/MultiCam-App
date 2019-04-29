package com.Test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class ListOfMergedVideosActivity extends AppCompatActivity {

    MergedVideoAdapter mergedVideoAdapter;
    ArrayList<MergedVideoModel> mergedVideoModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_merged_videos);
        ListView listOfVideos = findViewById(R.id.listMergedVideos);
        fillArrayList();

        mergedVideoAdapter = new MergedVideoAdapter(this, R.layout.merged_video_list_item, mergedVideoModels);
        listOfVideos.setAdapter(mergedVideoAdapter);
    }

    private void fillArrayList() {
        String path = Environment.getExternalStorageDirectory().toString()+"/ConcatVideos";
        File f = new File(path);
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            String videoPath = file[i].toString();
            MergedVideoModel mvm = new MergedVideoModel(videoPath);
            mergedVideoModels.add(mvm);
        }
    }
}
