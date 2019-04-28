package com.Test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new Binary(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void btnCreateProject(View view) {
        Intent intent = new Intent(this, AddVideosActivity.class);
        startActivity(intent);
    }

    public void btnPreviousProject(View view) {
        Intent intent = new Intent(this, ProductionScreenActivity.class);
        startActivity(intent);
    }

    public void btnListOfMergedVideos(View view) {
        Intent intent = new Intent(this, ListOfMergedVideosActivity.class);
        startActivity(intent);
    }
}
