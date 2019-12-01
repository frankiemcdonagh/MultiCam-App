package com.MultiCamPack;

import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/*
    CustomAdapter, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    Yuri, accessed from https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory.
 */

public class ListOfMergedVideosActivity extends AppCompatActivity {

    MergedVideoAdapter mergedVideoAdapter;
    ArrayList<MergedVideoModel> mergedVideoModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_merged_videos);
        ListView listOfVideos = findViewById(R.id.listMergedVideos);
        setToolbar();
        fillArrayList();


        Collections.sort(mergedVideoModels, Collections.reverseOrder());
        mergedVideoAdapter = new MergedVideoAdapter(this, R.layout.merged_video_list_item, mergedVideoModels);
        listOfVideos.setAdapter(mergedVideoAdapter);
    }
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
    public void btnInfo(View view) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(ListOfMergedVideosActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_merged_videos, null);
        mbuilder.setView(mView);
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }

    //See above re: Yuri
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
