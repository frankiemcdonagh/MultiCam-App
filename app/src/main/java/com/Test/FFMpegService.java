package com.Test;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FFMpegService extends Service {
    FFmpeg ffMpeg;
    int duration;
    String[] command;
    Callbacks activity;

    public MutableLiveData<Integer> percentage;
    IBinder myBinder = new LocalBinder();

    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            duration = Integer.parseInt(intent.getStringExtra("duration"));
            command = intent.getStringArrayExtra("command");
            try {
                LoadFFMpegBinary();
                executeFFmpegCommand();
            } catch (FFmpegNotSupportedException e) {
                e.printStackTrace();
            } catch (FFmpegCommandAlreadyRunningException e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void executeFFmpegCommand() throws FFmpegCommandAlreadyRunningException {
        ffMpeg.execute(command, new ExecuteBinaryResponseHandler(){
            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
            }

            @Override
            public void onProgress(String message) {
                String[] arr;
                if(message.contains("time=")){
                    arr = message.split("time=");
                    String yalo = arr[1];

                    String abikamha[] = yalo.split(":");
                    String[] yeanda = abikamha[2].split(" ");
                    String seconds = yeanda[0];
                    int hours = Integer.parseInt(abikamha[0]);
                    hours = hours * 3600;
                    int min = Integer.parseInt(abikamha[1]);
                    float sec = Float.valueOf(seconds);
                    float timeInSec = hours+min+sec;

                    percentage.setValue((int)((timeInSec/duration)*1000));

                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                percentage.setValue(100);
            }
        });


    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            LoadFFMpegBinary();
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
        percentage = new MutableLiveData<>();
    }

    private void LoadFFMpegBinary() throws FFmpegNotSupportedException {
        if(ffMpeg == null)
        {
            ffMpeg = FFmpeg.getInstance(this);

        }
        ffMpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public FFMpegService(){
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class LocalBinder extends Binder
    {
        public FFMpegService getServiceInstance()
        {
            return FFMpegService.this;
        }
    }

    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    public MutableLiveData<Integer> getPercentages() {
        return percentage;
    }

    public interface Callbacks{
        void updateClient(float data);
    }
}