package com.MultiCamPack;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/*
    FFMpegService, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    programming experts, https://www.youtube.com/watch?v=0akRhC9njlg.
    Gokhan Celikkaya, https://stackoverflow.com/questions/20325615/android-concatenate-two-videos.
    Yuri, accessed from https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory.
 */

//See above re: programming experts
public class TrimAndConcatService extends Service {
    FFmpeg ffMpeg;
    FFmpeg fFmpegConcat;

    ArrayList<ProductionVideoModel> productionVideoModelArrayList = new ArrayList<>();
    int[] progressValues = new int[0];
    TrimAndConcatService.Callbacks activity;

    public MutableLiveData<Integer> percentage;
    IBinder myBinder = new TrimAndConcatService.LocalBinder();

    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null){

            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                productionVideoModelArrayList = bundle.getParcelableArrayList("videoSelections");
                progressValues = new int[productionVideoModelArrayList.size()];
            }
            for(int counter = 0; counter < productionVideoModelArrayList.size(); counter++) {
                ProductionVideoModel pvm = productionVideoModelArrayList.get(counter);
                String[] command = pvm.getCommand();
                Double duration = pvm.getDuration();
                try {
                    LoadFFMpegBinary();
                    executeFFmpegCommand(command, duration, counter);
                } catch (FFmpegNotSupportedException e) {
                    e.printStackTrace();
                } catch (FFmpegCommandAlreadyRunningException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void executeFFmpegCommand(final String[] command, final Double duration, final int counter) throws FFmpegCommandAlreadyRunningException {
        ffMpeg.execute(command, new ExecuteBinaryResponseHandler(){
            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.i("FFMpeg command failed:",message);
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
                    int progress = (int)((timeInSec/duration)*100);
                    progressValues[counter] = progress;
                    percentage.setValue(getTotalProgressValue());
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                if(percentage.getValue() != null && percentage.getValue() == 50){
                    try {
                        File Destination = CreateDestination();
                        String[] Command = GetConcatCommand(Destination);
                        ExecuteFFmpegConcat(Command);
                    } catch (FFmpegCommandAlreadyRunningException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private File CreateDestination() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/ConcatVideos");
        if(!folder.exists()){
            folder.mkdir();
        }
        String fileExt = ".mp4";
        Random r = new Random();
        int randomInt =  r.nextInt((1000 - 1) + 1) + 1;
        String videoName = "concatVideo" + randomInt;
        return new File(folder, videoName+fileExt);
    }

    private String[] GetConcatCommand(File Destination) {
        final ArrayList<String> paths = new ArrayList<>();
        final ArrayList<String> commandList = new ArrayList<>();
        //See above re: Yuri
        String path = Environment.getExternalStorageDirectory().toString()+"/TempSelectionVideos";
        File f = new File(path);
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            paths.add(file[i].toString());
        }
        //See above re: Gokhan Celikkaya
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++)
        {
            commandList.add("-i");
            commandList.add(paths.get(i));

            sb.append("[").append(i).append(":0] [").append(i).append(":1]");
        }
        sb.append(" concat=n=").append(paths.size()).append(":v=1:a=1 [v] [a]");
        commandList.add("-filter_complex");
        commandList.add(sb.toString());
        commandList.add("-map");
        commandList.add("[v]");
        commandList.add("-map");
        commandList.add("[a]");
        commandList.add("-preset");
        commandList.add("ultrafast");
        commandList.add(Destination.toString());

        sb = new StringBuilder();
        for (String str : commandList)
        {
            sb.append(str).append(" ");
        }

        return commandList.toArray(new String[commandList.size()]);
    }

    private void ExecuteFFmpegConcat(String[] command) throws FFmpegCommandAlreadyRunningException {
        ffMpeg.execute(command, new ExecuteBinaryResponseHandler(){
            @Override
            public void onFailure(String message) {

                Log.i("Concat command failed:",message);
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
            }

            @Override
            public void onProgress(String message) {
                if(message.contains("javautil")){percentage.setValue(percentage.getValue() + 10);}
                if(message.contains("Input #0")){percentage.setValue(percentage.getValue() + 10);}
                if(message.contains("Output #0")){percentage.setValue(percentage.getValue() + 10);}
                if(message.contains("Stream mapping:")){percentage.setValue(percentage.getValue() + 10);}
                if(message.contains("Qavg:")){
                    percentage.setValue(100);
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
    private int getTotalProgressValue(){
        int total = 0;
        for (int progressValue : progressValues) {
            total += progressValue;
        }
        int trimTotal = total/progressValues.length;
        trimTotal = trimTotal / 2;
        return trimTotal;
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
        if(ffMpeg != null) { return; }

        ffMpeg = FFmpeg.getInstance(this);
        ffMpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
            @Override
            public void onFailure() {
                Log.i("FFMpeg binary", "Failed to load binary");
            }

            @Override
            public void onSuccess() {
                Log.i("FFMpeg binary", "Successfully loaded binary");
            }

            @Override
            public void onStart() {
                Log.i("FFMpeg binary", "Started to load binary");
            }

            @Override
            public void onFinish() {
                Log.i("FFMpeg binary", "Finished to load binary");
            }
        });
    }

    public TrimAndConcatService(){
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class LocalBinder extends Binder
    {
        public TrimAndConcatService getServiceInstance()
        {
            return TrimAndConcatService.this;
        }
    }

    public void registerClient(Activity activity){
        this.activity = (TrimAndConcatService.Callbacks)activity;
    }

    public MutableLiveData<Integer> getPercentages() {
        return percentage;
    }

    public interface Callbacks{
        void updateClient(float data);
    }
}
