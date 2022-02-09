package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchDataArray {

    @Expose
    @SerializedName("resultArray") private ArrayList<SearchData> searchDataArray;

    @Expose
    @SerializedName("isSuccess") private boolean Success;

    public ArrayList<SearchData> getSearchDataArray() {
        return searchDataArray;
    }

    public void setSearchDataArray(ArrayList<SearchData> searchDataArray) {
        this.searchDataArray = searchDataArray;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }
}
