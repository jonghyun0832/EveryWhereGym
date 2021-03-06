package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiveDataArray {

    @Expose
    @SerializedName("resultArray") private ArrayList<LiveData> liveDataArray;

    @Expose
    @SerializedName("eventArray") private String[][] eventArray;

    @Expose
    @SerializedName("isSuccess") private boolean success;

    @Expose
    @SerializedName("cursor") private String cursor;

    @Expose
    @SerializedName("end") private boolean end;

    public ArrayList<LiveData> getLiveDataArray() {
        return liveDataArray;
    }

    public void setLiveDataArray(ArrayList<LiveData> liveDataArray) {
        this.liveDataArray = liveDataArray;
    }

    public String[][] getEventArray() {
        return eventArray;
    }

    public void setEventArray(String[][] eventArray) {
        this.eventArray = eventArray;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
