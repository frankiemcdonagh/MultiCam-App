package com.Test;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoListItemModel implements Parcelable {
    private String name;
    private String startTime;

    public VideoListItemModel(String name, String startTime){
        this.name=name;
        this.startTime=startTime;
    }
    public VideoListItemModel(Parcel in){
        name = in.readString();
        startTime = in.readString();
    }

    public static final Creator<VideoListItemModel> CREATOR = new Creator<VideoListItemModel>() {
        @Override
        public VideoListItemModel createFromParcel(Parcel in) {
            return new VideoListItemModel(in);
        }

        @Override
        public VideoListItemModel[] newArray(int size) {
            return new VideoListItemModel[size];
        }
    };

    public String getName(){
        return name;
    }
    public void setName(){
        this.name = name;
    }
    public String getStartTime(){
        return startTime;
    }
    public void setStartTime(){
        this.startTime = startTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(startTime);
    }

}
