package com.example.everywheregym;

import android.app.Application;

public class PublicValues extends Application {
    private String tv;

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }
}
