package com.example.everywheregym;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {
    @Expose
    @SerializedName("name") private String name;

    @Expose
    @SerializedName("hobby") private String hobby;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHobby()
    {
        return hobby;
    }

    public void setHobby(String hobby)
    {
        this.hobby = hobby;
    }
}