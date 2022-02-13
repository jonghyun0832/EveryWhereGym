package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LiveData {

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
    @SerializedName("live_limit_join") private String live_limit_join;

    @Expose
    @SerializedName("live_material") private String live_material;

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("success") private boolean success;


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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
