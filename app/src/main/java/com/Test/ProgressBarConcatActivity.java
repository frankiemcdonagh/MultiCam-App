package com.Test;

import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.ArrayList;

public class ProgressBarConcatActivity extends AppCompatActivity {
    CircleProgressBar circleProgressBar;
    ArrayList<ProductionVideoModel> productionVideoModelArrayList = new ArrayList<>();
    ArrayList<ServiceConnection> connections = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar_concat);
        prepareProgressBar();
        TrimAndConcatVideos();
    }

    private void prepareProgressBar(){
        final Intent i = getIntent();
        if(i == null) { return; }

        setContentView(R.layout.activity_progress_bar);
        circleProgressBar = findViewById(R.id.circleProgressBar);
        circleProgressBar.setMax(100);

        Bundle bundle = i.getExtras();
        if(bundle != null) {
            productionVideoModelArrayList = bundle.getParcelableArrayList("videoSelections");
        }
    }
    private void TrimAndConcatVideos() {
        final Intent myIntent = createServiceIntent();
        startService(myIntent);

        final ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                TrimAndConcatService.LocalBinder binder = (TrimAndConcatService.LocalBinder)iBinder;
                TrimAndConcatService service = binder.getServiceInstance();
                service.registerClient(getParent());
                final Observer<Integer> resultObserver = new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        if(integer < 100){
                            circleProgressBar.setProgress(integer);
                        }
                        if(integer==100){
                            circleProgressBar.setProgress(integer);
                            stopService(myIntent);
                            Toast.makeText(getApplicationContext(), "Trimmed and concat", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                service.getPercentages().observe(ProgressBarConcatActivity.this,resultObserver);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        connections.add(conn);
        getApplicationContext().bindService(myIntent, conn, Context.BIND_AUTO_CREATE);
    }

    private Intent createServiceIntent() {
        Intent myIntent = new Intent(ProgressBarConcatActivity.this,TrimAndConcatService.class);

        Bundle b = new Bundle();
        b.putParcelableArrayList("videoSelections",productionVideoModelArrayList);
        myIntent.putExtras(b);
        //myIntent.putExtra("destination", path);

        return myIntent;
    }
}
