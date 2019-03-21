package com.Test;

public class ProductionVideoModel {
    private String[] command;
    private int duration;

    public ProductionVideoModel(String[] command, int duration){
        this.command=command;
        this.duration=duration;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        duration = duration;
    }


}
