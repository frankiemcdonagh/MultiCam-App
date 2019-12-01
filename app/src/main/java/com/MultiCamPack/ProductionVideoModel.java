package com.MultiCamPack;

import android.os.Parcel;
import android.os.Parcelable;


public class ProductionVideoModel implements Parcelable {
    private String[] command;
    private Double duration;

    public ProductionVideoModel(String[] command, Double duration){
        this.command=command;
        this.duration=duration;
    }

    protected ProductionVideoModel(Parcel in) {
        command = in.createStringArray();
        duration = in.readDouble();
    }
    public static final Creator<ProductionVideoModel> CREATOR = new Creator<ProductionVideoModel>() {
        @Override
        public ProductionVideoModel createFromParcel(Parcel in) {
            return new ProductionVideoModel(in);
        }

        @Override
        public ProductionVideoModel[] newArray(int size) {
            return new ProductionVideoModel[size];
        }
    };

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        duration = duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(command);
        dest.writeDouble(duration);
    }
}
