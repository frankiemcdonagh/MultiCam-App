package com.Test;

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

public class ProgressBarActivity extends AppCompatActivity {
    CircleProgressBar circleProgressBar;
    ArrayList<ProductionVideoModel> productionVideoModelArrayList = new ArrayList<>();
    int[] progressValues = new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareProgressBar();
        processProductionVideos();
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

    private Intent createServiceIntent(ProductionVideoModel pvm){
        String[] command = pvm.getCommand();
        Double duration = pvm.getDuration();

        Intent myIntent = new Intent(ProgressBarActivity.this,FFMpegService.class);
        myIntent.putExtra("duration",String.valueOf(duration));
        myIntent.putExtra("command",command);
        //myIntent.putExtra("destination", path);

        return myIntent;
    }

    private void processProductionVideos(){
        for(int counter = 0; counter < productionVideoModelArrayList.size(); counter++)
        {
            ProductionVideoModel pvm = productionVideoModelArrayList.get(counter);
            final int position = counter;
            final Intent myIntent = createServiceIntent(pvm);
            android.os.Debug.waitForDebugger();
            startService(myIntent);

            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    android.os.Debug.waitForDebugger();
                    FFMpegService.LocalBinder binder = (FFMpegService.LocalBinder)iBinder;
                    FFMpegService service = binder.getServiceInstance();
                    service.registerClient(getParent());

                    final Observer<Integer> resultObserver = new Observer<Integer>() {
                        @Override
                        public void onChanged(@Nullable Integer integer) {
                            if(integer == null) {return;}

                            progressValues[position] = integer;

                            if(integer==100){
                                stopService(myIntent);
                                Toast.makeText(getApplicationContext(),"SINGLE JOB DONE",Toast.LENGTH_LONG).show();
                            }
                            int currentTotal = getTotalProgressValue();
                            if(currentTotal < 100){
                                circleProgressBar.setProgress(currentTotal);
                            } else if(currentTotal == 100) {
                                Toast.makeText(getApplicationContext(),"ALL JOBS DONE",Toast.LENGTH_LONG).show();
                            }

                        }
                    };
                    service.getPercentages().observe(ProgressBarActivity.this,resultObserver);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            getApplicationContext().bindService(myIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    private int getTotalProgressValue(){
        int total = 0;
        for (int progressValue : progressValues) {
            total += progressValue;
        }

        return total / progressValues.length;
    }
}
