package com.Test;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

/*
    MainScreenActivity, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    Yuri, accessed from https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory.
 */

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }
    public void btnInfo(View view) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainScreenActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_main_help, null);
        mbuilder.setView(mView);
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }
    public void btnCreateProject(View view) {
        Intent intent = new Intent(this, AddVideosActivity.class);
        startActivity(intent);
    }

    public void btnPreviousProject(View view) {
        try {
            //See above re: Yuri
            String path = Environment.getExternalStorageDirectory().toString() + "/TempProductionVideos";
            Log.d("Files", "Path: " + path);
            File f = new File(path);
            File file[] = f.listFiles();
            Log.d("Files", "Size: " + file.length);
            Intent intent = new Intent(this, ProductionScreenActivity.class);
            startActivity(intent);

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "No previous project available", Toast.LENGTH_LONG).show();
        }
    }

    public void btnListOfMergedVideos(View view) {
        try {
            //See above re: Yuri
            String path = Environment.getExternalStorageDirectory().toString() + "/ConcatVideos";
            Log.d("Files", "Path: " + path);
            File f = new File(path);
            File file[] = f.listFiles();
            Log.d("Files", "Size: " + file.length);
            Intent intent = new Intent(this, ListOfMergedVideosActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "No Merged Videos available", Toast.LENGTH_LONG).show();
        }
    }
}
