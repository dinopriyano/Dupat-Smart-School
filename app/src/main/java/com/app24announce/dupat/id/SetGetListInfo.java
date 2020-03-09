package com.app24announce.dupat.id;

public class SetGetListInfo {
    String info,time,timestamp;

    public SetGetListInfo() {
    }

    public SetGetListInfo(String info, String time, String timestamp) {
        this.info = info;
        this.time = time;
        this.timestamp = timestamp;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
