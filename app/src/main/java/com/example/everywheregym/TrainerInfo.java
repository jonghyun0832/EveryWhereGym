package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainerInfo {

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("user_name") private String user_name;

    @Expose
    @SerializedName("user_img") private String user_img;

    @Expose
    @SerializedName("tr_img") private String tr_img;

    @Expose
    @SerializedName("tr_intro") private String tr_intro;

    @Expose
    @SerializedName("tr_expert") private String tr_expert;

    @Expose
    @SerializedName("tr_career") private String tr_career;

    @Expose
    @SerializedName("tr_certify") private String tr_certify;

    @Expose
    @SerializedName("success") private boolean success;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getTr_img() {
        return tr_img;
    }

    public void setTr_img(String tr_img) {
        this.tr_img = tr_img;
    }

    public String getTr_intro() {
        return tr_intro;
    }

    public void setTr_intro(String tr_intro) {
        this.tr_intro = tr_intro;
    }

    public String getTr_expert() {
        return tr_expert;
    }

    public void setTr_expert(String tr_expert) {
        this.tr_expert = tr_expert;
    }

    public String getTr_career() {
        return tr_career;
    }

    public void setTr_career(String tr_career) {
        this.tr_career = tr_career;
    }

    public String getTr_certify() {
        return tr_certify;
    }

    public void setTr_certify(String tr_certify) {
        this.tr_certify = tr_certify;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
