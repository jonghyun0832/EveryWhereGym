package com.example.everywheregym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchData {

    @Expose
    @SerializedName("search_text") private String search_text;

    @Expose
    @SerializedName("search_date") private String search_date;

    @Expose
    @SerializedName("search_id") private String search_id;


    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }
}
