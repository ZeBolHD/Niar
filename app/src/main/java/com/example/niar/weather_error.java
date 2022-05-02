package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class weather_error extends AppCompatActivity {

    private TextView error_text;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_error);

        back = findViewById(R.id.back_button);
        error_text = findViewById(R.id.error_text);

        Intent intent = getIntent();

        String error = intent.getStringExtra("error");


        switch (intent.getStringExtra("error")) {
            case "city error":
                error_text.setText("Такого города не существует");
            case "easter":
                error_text.setText("Нормально общайся");
        }

        back.setOnClickListener(view -> startActivity(new Intent(weather_error.this, MainActivity.class)));
    }
}