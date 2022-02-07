package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VodHistoryData {

    @Expose
    @SerializedName("resultArray") private ArrayList<VodData> vodData;

    @Expose
    @SerializedName("history_date") private String date;

    public ArrayList<VodData> getVodData() {
        return vodData;
    }

    public void setVodData(ArrayList<VodData> vodData) {
        this.vodData = vodData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
