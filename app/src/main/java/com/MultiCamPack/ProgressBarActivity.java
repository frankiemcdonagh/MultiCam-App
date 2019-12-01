package com.MultiCamPack;

import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.ArrayList;
/*
    ProgressBarActivity, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    programming experts, https://www.youtube.com/watch?v=0akRhC9njlg
 */
public class ProgressBarActivity extends AppCompatActivity {
    CircleProgressBar circleProgressBar;
    ArrayList<ProductionVideoModel> productionVideoModelArrayList = new ArrayList<>();
    ArrayList<ServiceConnection> connections = new ArrayList<>();
    int[] progressValues = new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareProgressBar();
        processProductionVideos();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        for (ServiceConnection c : connections) {
            getApplicationContext().unbindService(c);
        }
    }

    private void prepareProgressBar(){
        final Intent i = getIntent();
        if(i == null) { return; }

        setContentView(R.layout.activity_progress_bar);
        circleProgressBar = findViewById(R.id.circleProgressBar);
        circleProgressBar.setMax(100);

        Bundle bundle = i.getExtras();
        if(bundle != null) {
            productionVideoModelArrayList = bundle.getParcelableArrayList("productionList");
            progressValues = new int[productionVideoModelArrayList.size()];
        }
    }

    private Intent createServiceIntent(){

        Intent myIntent = new Intent(ProgressBarActivity.this,FFMpegService.class);

        Bundle b = new Bundle();
        b.putParcelableArrayList("productionList",productionVideoModelArrayList);
        myIntent.putExtras(b);
        //myIntent.putExtra("destination", path);

        return myIntent;
    }

    private void processProductionVideos(){
        final Intent myIntent = createServiceIntent();
        startService(myIntent);

        final ServiceConnection conn = new ServiceConnection() {
            @Override
            //See above re: programming experts
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                FFMpegService.LocalBinder binder = (FFMpegService.LocalBinder)iBinder;
                FFMpegService service = binder.getServiceInstance();
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
                            Toast.makeText(getApplicationContext(), "Videos Trimmed Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ProgressBarActivity.this, ProductionScreenActivity.class);
                            startActivity(i);
                        }
                    }
                };
                service.getPercentages().observe(ProgressBarActivity.this,resultObserver);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        connections.add(conn);
        getApplicationContext().bindService(myIntent, conn, Context.BIND_AUTO_CREATE);
    }
}
