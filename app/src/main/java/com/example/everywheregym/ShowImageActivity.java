package com.example.everywheregym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowImageActivity extends AppCompatActivity {

    ImageView iv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        iv_show = findViewById(R.id.imageView_show);


        Intent intent = getIntent();
        String img_url = intent.getStringExtra("img_url");

        Glide.with(this).load(img_url).into(iv_show);

    }
}