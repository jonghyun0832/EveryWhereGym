package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @Expose
    @SerializedName("email") private String email;

    @Expose
    @SerializedName("cnum") private String cnum;

    @Expose
    @SerializedName("nickname") private boolean nameDuplicate;

    @Expose
    @SerializedName("success") private boolean success;

    @Expose
    @SerializedName("user_id") private String user_id;

    @Expose
    @SerializedName("user_name") private String user_name;

    @Expose
    @SerializedName("user_img") private String user_img;

    @Expose
    @SerializedName("user_trainer") private String user_trainer;



//    @Expose
//    @SerializedName("password") private String password;
//
//    @Expose
//    @SerializedName("nickname") private String nickname;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public boolean isNameDuplicate() {
        return nameDuplicate;
    }

    public void setNameDuplicate(boolean nameDuplicate) {
        this.nameDuplicate = nameDuplicate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public String getUser_trainer() {
        return user_trainer;
    }

    public void setUser_trainer(String user_trainer) {
        this.user_trainer = user_trainer;
    }
}
