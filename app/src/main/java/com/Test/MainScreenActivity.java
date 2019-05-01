package com.Test;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

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
        Intent intent = new Intent(this, ProductionScreenActivity.class);
        startActivity(intent);
    }

    public void btnListOfMergedVideos(View view) {
        Intent intent = new Intent(this, ListOfMergedVideosActivity.class);
        startActivity(intent);
    }
}
