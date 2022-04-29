package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class weather_result extends AppCompatActivity {

    private TextView weatherInfo;
    private TextView city_name;
    private ImageView weather_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_result);

        city_name = findViewById(R.id.city_name);
        weatherInfo = findViewById(R.id.weather_info);
        weather_pic = findViewById(R.id.weather_pic);

        putWeatherText();
    }

    @SuppressLint("SetTextI18n")
    private void putWeatherText() {

        Intent intent = getIntent();

        String weather_icon = intent.getStringExtra("weather icon");
        String city_name_up = intent.getStringExtra("city name");
        String description = intent.getStringExtra("description");
        int temp_current = intent.getIntExtra("temp_current", 0);
        int feels_like_today = intent.getIntExtra("feels_like_today", 0);
        int temp_min_today = intent.getIntExtra("temp_min_today", 0);
        int temp_max_today = intent.getIntExtra("temp_max_today", 0);

        city_name.setText(city_name_up);

        weatherInfo.setText(description + ("\n") + ("\n")
                + "Температура: " + temp_current + " C" + ("\n") + ("\n")
                + "Ощущается как: " + feels_like_today + " C" + ("\n") + ("\n")
                + "Минимальная температура: " + temp_min_today + " C" + ("\n") + ("\n")
                + "Максимальная температура: " + temp_max_today + " C");
        wpicSet(weather_icon);
    }

    private void wpicSet(String n) {
        switch (n) {
            case "01d":
                weather_pic.setImageResource(R.drawable.ic_day_clear);
                break;

            case "01n":
                weather_pic.setImageResource(R.drawable.ic_night_clear);
                break;

            case "02d":
                weather_pic.setImageResource(R.drawable.ic_day_sonny_overcast);
                break;

            case "02n":
                weather_pic.setImageResource(R.drawable.ic_night_overcast);
                break;

            case "03d":
                weather_pic.setImageResource(R.drawable.ic_cloudy);
                break;

            case "03n":
                weather_pic.setImageResource(R.drawable.ic_cloudy);
                break;

            case "04d":
                weather_pic.setImageResource(R.drawable.ic_day_cloudy);
                break;

            case "04n":
                weather_pic.setImageResource(R.drawable.ic_night_cloudy);
                break;

            case "09d":
                weather_pic.setImageResource(R.drawable.ic_day_showers);
                break;

            case "09n":
                weather_pic.setImageResource(R.drawable.ic_night_showers);
                break;

            case "10d":
                weather_pic.setImageResource(R.drawable.ic_day_rain);
                break;

            case "10n":
                weather_pic.setImageResource(R.drawable.ic_night_rain);
                break;

            case "11d":
                weather_pic.setImageResource(R.drawable.ic_day_thunderstorm);
                break;

            case "11n":
                weather_pic.setImageResource(R.drawable.ic_night_thunderstorm);
                break;

            case "13d":
                weather_pic.setImageResource(R.drawable.ic_day_snow);
                break;

            case "13n":
                weather_pic.setImageResource(R.drawable.ic_night_snow);
                break;

            case "50d":
                weather_pic.setImageResource(R.drawable.ic_day_fog);
                break;

            case "50n":
                weather_pic.setImageResource(R.drawable.ic_night_fog);
                break;

            default:
                weather_pic.setImageResource(R.drawable.ic_not_found);
        }
    }
}