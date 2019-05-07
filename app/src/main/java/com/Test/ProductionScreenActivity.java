package com.Test;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
    ProductionScreenActivity, created by Frankie McDonagh
    Date:

    This Activity contains code from from:
    Yuri, accessed from https://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory.
    vir us, accessed from https://stackoverflow.com/questions/3936396/how-to-get-duration-of-a-video-file.
    Sohail Zahid, accessed from https://stackoverflow.com/questions/39389031/how-to-delete-all-files-in-a-directory-java-android-app-current-code-is-deletin
 */

public class ProductionScreenActivity extends AppCompatActivity {


    ProductionVideoAdapter productionVideoAdapter;
    ArrayList<ProductionPathModel> productionPathModels;
    public int currentposition;
    public VideoView videoView;
    public String ClickedPath;
    public int counter;
    boolean boolValue = true;
    final ArrayList<String> RecorderPaths = new ArrayList<>();
    final ArrayList<Integer> RecordedStartTimes = new ArrayList<>();
    ArrayList<ProductionVideoModel> productionVideoModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_screen);
        DeleteOldSelections();
        ClickedPath = null;
        RecorderPaths.clear();
        counter = 0;
        CreateDialog();




        ListView list = findViewById(R.id.listViewVideos);
        videoView = findViewById(R.id.videoViewPlayer);
        productionPathModels = new ArrayList<>();
        //See above re: Yuri
        String path = Environment.getExternalStorageDirectory().toString()+"/TempProductionVideos";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            //here populate your listview
            Log.d("Files", "FileName:" + file[i].getName());
            ProductionPathModel ppm = new ProductionPathModel(file[i].toString());
            productionPathModels.add(ppm);
        }
        productionVideoAdapter = new ProductionVideoAdapter(this, R.layout.production_video_item, productionPathModels);
        list.setAdapter(productionVideoAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (ClickedPath == null) {
                        ClickedPath = productionPathModels.get(position).getPath();
                    }
                    if (productionPathModels.get(position).getPath() != ClickedPath || counter == 0) {
                        ClickedPath = productionPathModels.get(position).getPath();
                        currentposition = videoView.getCurrentPosition();
                        RecordedStartTimes.add(currentposition);
                        RecorderPaths.add(ClickedPath);
                        Uri uri = Uri.parse(ClickedPath);
                        videoView.setVideoURI(uri);
                        videoView.pause();
                        videoView.seekTo(currentposition);
                        videoView.start();


                    }

                    counter++;
                }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                openDialog();
            }
        });



    }
    private void CreateDialog() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProductionScreenActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_production_screen, null);
        mbuilder.setView(mView);
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }

        private void openDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Times Saved")
                .setMessage("Do you want to continue?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoView.seekTo(0);
                        videoView.pause();
                        videoView.setVideoURI(null);
                        RecordedStartTimes.clear();
                        RecorderPaths.clear();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productionVideoModelArrayList.clear();
                        DeleteOldSelections();
                        prepareArray();
                        goToNextPage();
                    }

                    private void prepareArray() {
                        ArrayList<String> commands = new ArrayList<>();
                        File folder = new File(Environment.getExternalStorageDirectory() + "/TempSelectionVideos");
                        if(!folder.exists()){
                            folder.mkdir();
                        }
                        for(int i = 0; i < RecordedStartTimes.size(); i++){
                            String videoName = "Selection "+ i ;
                            String fileExt = ".mp4";
                            int startTime;
                            int duration;
                            startTime = RecordedStartTimes.get(i);
                            int i2 = i+1;
                            try{
                                duration = RecordedStartTimes.get(i2) - startTime;
                            }
                            //See above re: vir us
                            catch (Exception e) {
                                Uri videoUri = Uri.parse(RecorderPaths.get(i));
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(getApplicationContext(), videoUri);
                                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                int timeInMillisec = Integer.parseInt(time);
                                duration = timeInMillisec - startTime;
                                retriever.release();
                            }
                            DecimalFormat df = new DecimalFormat(".##");
                            double DoubleStartTime = (double)(startTime) / 1000;
                            DoubleStartTime = Double.parseDouble(df.format(DoubleStartTime));
                            Double DoubleDuration = (double) duration / 1000;
                            DoubleDuration = Double.parseDouble(df.format(DoubleDuration));
                            File dest = new File(folder, videoName+fileExt);
                            String[] Command  = new String[] {"-ss",""+DoubleStartTime,"-y","-i",RecorderPaths.get(i),"-t",""+DoubleDuration,"-vcodec","mpeg4","-b:v","2097152","-b:a","48000","-ac","2","-ar","22050",dest.getAbsolutePath()};
                            ProductionVideoModel pvm = new ProductionVideoModel(Command, DoubleDuration);
                            productionVideoModelArrayList.add(pvm);

                        }
                    }
                    private void goToNextPage() {
                        Intent i = new Intent(ProductionScreenActivity.this, ProgressBarConcatActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelableArrayList("videoSelections",productionVideoModelArrayList);
                        i.putExtras(b);
                        ProductionScreenActivity.this.startActivity(i);
                    }
                }).create().show();


    }
    //See above re: Sohail Zahid
    private void DeleteOldSelections() {
        //get the production folder
        File dir = new File(Environment.getExternalStorageDirectory()+"/TempSelectionVideos");
        //delete children
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }



}
