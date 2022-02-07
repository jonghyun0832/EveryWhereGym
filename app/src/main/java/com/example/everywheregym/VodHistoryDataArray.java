package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VodHistoryDataArray {

    @Expose
    @SerializedName("resultHisArray") private ArrayList<VodHistoryData> vodHistoryData;

    public ArrayList<VodHistoryData> getVodHistoryData() {
        return vodHistoryData;
    }

    public void setVodHistoryData(ArrayList<VodHistoryData> vodHistoryData) {
        this.vodHistoryData = vodHistoryData;
    }
}
