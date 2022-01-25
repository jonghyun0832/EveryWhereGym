package com.example.everywheregym;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VodData {


    @Expose
    @SerializedName("vod_difficulty") private String vod_difficulty;

    @Expose
    @SerializedName("vod_thumbnail") private String vod_thumbnail;

    @Expose
    @SerializedName("vod_time") private String vod_time;

    @Expose
    @SerializedName("vod_uploader_img") private String vod_uploader_img;

    @Expose
    @SerializedName("vod_title") private String vod_title;

    @Expose
    @SerializedName("vod_uploader_name") private String vod_uploader_name;

    @Expose
    @SerializedName("vod_path") private String vod_path;

    @Expose
    @SerializedName("vod_uploader_id") private String vod_uploader_id;

    @Expose
    @SerializedName("vod_id") private String vod_id;

    @Expose
    @SerializedName("vod_category") private String vod_category;

    @Expose
    @SerializedName("vod_explain") private String vod_explain;

    @Expose
    @SerializedName("vod_material") private String vod_materail;

    @Expose
    @SerializedName("success") private boolean success;


    public String getVod_difficulty() {
        return vod_difficulty;
    }

    public void setVod_difficulty(String vod_difficulty) {
        this.vod_difficulty = vod_difficulty;
    }

    public String getVod_thumbnail() {
        return vod_thumbnail;
    }

    public void setVod_thumbnail(String vod_thumbnail) {
        this.vod_thumbnail = vod_thumbnail;
    }

    public String getVod_time() {
        return vod_time;
    }

    public void setVod_time(String vod_time) {
        this.vod_time = vod_time;
    }

    public String getVod_uploader_img() {
        return vod_uploader_img;
    }

    public void setVod_uploader_img(String vod_uploader_img) {
        this.vod_uploader_img = vod_uploader_img;
    }

    public String getVod_title() {
        return vod_title;
    }

    public void setVod_title(String vod_title) {
        this.vod_title = vod_title;
    }

    public String getVod_uploader_name() {
        return vod_uploader_name;
    }

    public void setVod_uploader_name(String vod_uploader_name) {
        this.vod_uploader_name = vod_uploader_name;
    }

    public String getVod_path() {
        return vod_path;
    }

    public void setVod_path(String vod_path) {
        this.vod_path = vod_path;
    }

    public String getVod_uploader_id() {
        return vod_uploader_id;
    }

    public void setVod_uploader_id(String vod_uploader_id) {
        this.vod_uploader_id = vod_uploader_id;
    }

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getVod_category() {
        return vod_category;
    }

    public void setVod_category(String vod_category) {
        this.vod_category = vod_category;
    }

    public String getVod_explain() {
        return vod_explain;
    }

    public void setVod_explain(String vod_explain) {
        this.vod_explain = vod_explain;
    }

    public String getVod_materail() {
        return vod_materail;
    }

    public void setVod_materail(String vod_materail) {
        this.vod_materail = vod_materail;
    }
}
