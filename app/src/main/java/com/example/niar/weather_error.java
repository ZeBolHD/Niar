package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class weather_error extends AppCompatActivity {

    private TextView error_text;
    private ImageButton back;
    private String error;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_error);

        back = findViewById(R.id.back_button);
        error_text = findViewById(R.id.error_text);

        Intent intent = getIntent();
        error = intent.getStringExtra("error");

        switch (error){
            case "city":
                error_text.setText("Такого города не существует");
                break;
            case "easter":
                error_text.setText("Нормально общайся");
                break;
        }

        intent.removeExtra("error");

        back.setOnClickListener(view -> startActivity(new Intent(weather_error.this, MainActivity.class)));
    }
}