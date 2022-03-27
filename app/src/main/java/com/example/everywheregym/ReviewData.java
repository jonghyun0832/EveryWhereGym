package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("rv_id") private String rv_id;

    @Expose
    @SerializedName("trainer_id") private String trainer_id;

    @Expose
    @SerializedName("rv_text") private String rv_text;

    @Expose
    @SerializedName("rv_score") private String rv_score;

    @Expose
    @SerializedName("img_path") private String img_path;

    @Expose
    @SerializedName("user_name") private String name;

    @Expose
    @SerializedName("rv_date") private String date;

    @Expose
    @SerializedName("rv_title") private String rv_title;

    @Expose
    @SerializedName("rv_total_score") private float rv_total_score;

    @Expose
    @SerializedName("success") private boolean success;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRv_id() {
        return rv_id;
    }

    public void setRv_id(String rv_id) {
        this.rv_id = rv_id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getRv_text() {
        return rv_text;
    }

    public void setRv_text(String rv_text) {
        this.rv_text = rv_text;
    }

    public String getRv_score() {
        return rv_score;
    }

    public void setRv_score(String rv_score) {
        this.rv_score = rv_score;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRv_title() {
        return rv_title;
    }

    public void setRv_title(String rv_title) {
        this.rv_title = rv_title;
    }

    public float getRv_total_score() {
        return rv_total_score;
    }

    public void setRv_total_score(float rv_total_score) {
        this.rv_total_score = rv_total_score;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
