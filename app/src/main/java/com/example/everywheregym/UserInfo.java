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

    //    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }

    //    @Expose
//    @SerializedName("") private String email;


}
