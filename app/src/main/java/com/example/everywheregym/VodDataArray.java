package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VodDataArray {

    @Expose
    @SerializedName("resultArray") private ArrayList<VodData> vodDataArray;

    public ArrayList<VodData> getVodDataArray() {
        return vodDataArray;
    }

    public void setVodDataArray(ArrayList<VodData> vodDataArray) {
        this.vodDataArray = vodDataArray;
    }
}
