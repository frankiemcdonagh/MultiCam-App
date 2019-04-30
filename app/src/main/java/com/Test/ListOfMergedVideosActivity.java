package com.Test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ListOfMergedVideosActivity extends AppCompatActivity {

    MergedVideoAdapter mergedVideoAdapter;
    ArrayList<MergedVideoModel> mergedVideoModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_merged_videos);
        ListView listOfVideos = findViewById(R.id.listMergedVideos);

        fillArrayList();


        Collections.sort(mergedVideoModels, Collections.reverseOrder());
        mergedVideoAdapter = new MergedVideoAdapter(this, R.layout.merged_video_list_item, mergedVideoModels);
        listOfVideos.setAdapter(mergedVideoAdapter);
    }

    private void fillArrayList()  {
        String path = Environment.getExternalStorageDirectory().toString()+"/ConcatVideos";
        File f = new File(path);
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            String videoPath = file[i].toString();
            File dateFile = new File(videoPath);
            Date lastModDate = new Date(dateFile.lastModified());
            //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            MergedVideoModel mvm = new MergedVideoModel(videoPath, lastModDate);
            mergedVideoModels.add(mvm);
        }
    }
}
