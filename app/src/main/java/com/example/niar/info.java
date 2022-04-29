package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

public class info extends AppCompatActivity {

    private ImageButton vk_link_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        vk_link_me = findViewById(R.id.vk_me);

        vk_link_me.setOnClickListener(view -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vk.com/zebolhd"));
            startActivity(browserIntent);

        });
    }
}