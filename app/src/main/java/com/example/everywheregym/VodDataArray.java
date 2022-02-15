package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VodDataArray {

    @Expose
    @SerializedName("resultArray") private ArrayList<VodData> vodDataArray;

    @Expose
    @SerializedName("isSuccess") private boolean success;

    @Expose
    @SerializedName("cursor") private String cursor;

    @Expose
    @SerializedName("end") private boolean end;

    public ArrayList<VodData> getVodDataArray() {
        return vodDataArray;
    }

    public void setVodDataArray(ArrayList<VodData> vodDataArray) {
        this.vodDataArray = vodDataArray;
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
