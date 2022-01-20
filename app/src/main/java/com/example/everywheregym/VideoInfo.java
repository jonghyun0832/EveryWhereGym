package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoInfo {

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("title") private String title;


    @Expose
    @SerializedName("category") private String category;

    @Expose
    @SerializedName("difficulty") private String difficulty;

    @Expose
    @SerializedName("video_url") private String video_url;

    @Expose
    @SerializedName("thumbnail") private String thumbnail;

    @Expose
    @SerializedName("length") private long length;

    @Expose
    @SerializedName("success") private boolean success;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
