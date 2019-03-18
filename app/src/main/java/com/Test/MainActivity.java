package com.Test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void btnProductionClick(View view) {
        Intent intent = new Intent(this, Production.class);
        startActivity(intent);
    }

    public void BtnFFmpegClick(View view) {
        Intent intent = new Intent(this, FFmpegPage.class);
        startActivity(intent);
    }
    public void BtnAddVideosClick(View view) {
        Intent intent = new Intent(this, AddVideosActivity.class);
        startActivity(intent);
    }
    public void BtnSetStartTimeClick(View view) {
        Intent intent = new Intent(this, SetStartTimeActivity.class);
        startActivity(intent);
    }

}
