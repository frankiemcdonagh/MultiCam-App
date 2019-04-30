package com.Test;


import java.util.Date;

public class MergedVideoModel implements Comparable<MergedVideoModel> {
    private String path;
    private Date date;
    MergedVideoModel(String _path, Date _date){
        path=_path;
        date=_date;
    }

    public Date getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(MergedVideoModel o) {
        return  getDate().compareTo(o.getDate());
    }
}
