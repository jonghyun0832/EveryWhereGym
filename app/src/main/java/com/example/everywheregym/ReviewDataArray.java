package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewDataArray {

    @Expose
    @SerializedName("resultArray") private ArrayList<ReviewData> reviewDataArray;

    @Expose
    @SerializedName("isSuccess") private boolean success;

    @Expose
    @SerializedName("cursor") private String cursor;

    @Expose
    @SerializedName("end") private boolean end;

    public ArrayList<ReviewData> getReviewDataArray() {
        return reviewDataArray;
    }

    public void setReviewDataArray(ArrayList<ReviewData> reviewDataArray) {
        this.reviewDataArray = reviewDataArray;
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
