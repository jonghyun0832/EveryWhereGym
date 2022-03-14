package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LiveData {

    @Expose
    @SerializedName("live_id") private String live_id;

    @Expose
    @SerializedName("live_date") private String live_date;

    @Expose
    @SerializedName("live_title") private String live_title;

    @Expose
    @SerializedName("live_start_hour") private String live_start_hour;

    @Expose
    @SerializedName("live_start_minute") private String live_start_minute;

    @Expose
    @SerializedName("live_spend_time") private String live_spend_time;

    @Expose
    @SerializedName("live_calorie") private String live_calorie;

    @Expose
    @SerializedName("live_join") private String live_join;

    @Expose
    @SerializedName("live_limit_join") private String live_limit_join;

    @Expose
    @SerializedName("live_material") private String live_material;

    @Expose
    @SerializedName("uploader_id") private String uploader_id;

    @Expose
    @SerializedName("uploader_name") private String uploader_name;

    @Expose
    @SerializedName("uploader_img") private String uploader_img;

    @Expose
    @SerializedName("alarm_num") private String alarm_num;

    @Expose
    @SerializedName("trainer_score") private String trainer_score;

    @Expose
    @SerializedName("user_name") private String user_name;

    @Expose
    @SerializedName("open") private String open;

    @Expose
    @SerializedName("enable") private String enable;

    @Expose
    @SerializedName("success") private boolean success;

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getLive_date() {
        return live_date;
    }

    public void setLive_date(String live_date) {
        this.live_date = live_date;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getLive_start_hour() {
        return live_start_hour;
    }

    public void setLive_start_hour(String live_start_hour) {
        this.live_start_hour = live_start_hour;
    }

    public String getLive_start_minute() {
        return live_start_minute;
    }

    public void setLive_start_minute(String live_start_minute) {
        this.live_start_minute = live_start_minute;
    }

    public String getLive_spend_time() {
        return live_spend_time;
    }

    public void setLive_spend_time(String live_spend_time) {
        this.live_spend_time = live_spend_time;
    }

    public String getLive_calorie() {
        return live_calorie;
    }

    public void setLive_calorie(String live_calorie) {
        this.live_calorie = live_calorie;
    }

    public String getLive_join() {
        return live_join;
    }

    public void setLive_join(String live_join) {
        this.live_join = live_join;
    }

    public String getLive_limit_join() {
        return live_limit_join;
    }

    public void setLive_limit_join(String live_limit_join) {
        this.live_limit_join = live_limit_join;
    }

    public String getLive_material() {
        return live_material;
    }

    public void setLive_material(String live_material) {
        this.live_material = live_material;
    }

    public String getUploader_id() {
        return uploader_id;
    }

    public void setUploader_id(String uploader_id) {
        this.uploader_id = uploader_id;
    }

    public String getUploader_name() {
        return uploader_name;
    }

    public void setUploader_name(String uploader_name) {
        this.uploader_name = uploader_name;
    }

    public String getUploader_img() {
        return uploader_img;
    }

    public void setUploader_img(String uploader_img) {
        this.uploader_img = uploader_img;
    }

    public String getAlarm_num() {
        return alarm_num;
    }

    public void setAlarm_num(String alarm_num) {
        this.alarm_num = alarm_num;
    }

    public String getTrainer_score() {
        return trainer_score;
    }

    public void setTrainer_score(String trainer_score) {
        this.trainer_score = trainer_score;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
