package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class weather_result extends AppCompatActivity {

    private TextView weatherInfo;
    private TextView city_name;
    private ImageView weather_pic;
    private ImageButton back;
    private TextView current_temp;
    private TextView current_description;
    private TextView current_wind;
    private TextView current_temp_min;
    private TextView current_temp_max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_result);

        back = findViewById(R.id.back_button);
        city_name = findViewById(R.id.city_name);
        weatherInfo = findViewById(R.id.weather_info);
        weather_pic = findViewById(R.id.weather_pic);
        current_temp = findViewById(R.id.current_temp);
        current_description = findViewById(R.id.current_description);
        current_wind = findViewById(R.id.current_wind);
        current_temp_min = findViewById(R.id.current_temp_min);
        current_temp_max = findViewById(R.id.current_temp_max);

        putWeatherText();

        back.setOnClickListener(view -> startActivity(new Intent(weather_result.this, MainActivity.class)));
    }

    @SuppressLint("SetTextI18n")
    private void putWeatherText() {

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("jsonData"));
            JSONObject dayZero = new JSONObject(jsonObject.getJSONArray("list").getJSONObject(0).toString());
            String weather = dayZero.getJSONArray("weather").getJSONObject(0).getString("description");
            String weather_description_up = weather.substring(0, 1).toUpperCase() + weather.substring(1).toLowerCase();
            String city_n = jsonObject.getJSONObject("city").getString("name");
            String city_name_up = city_n.substring(0, 1).toUpperCase() + city_n.substring(1).toLowerCase();

            String weather_icon = dayZero.getJSONArray("weather").getJSONObject(0).getString("icon");

            double temp_max = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp_max");
            double temp_min = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp_min");
            int windSpeed = (int) Math.round(jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getDouble("speed"));

            Instant instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(0).getInt("dt"));
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
            int day_c = ldt.getDayOfMonth();

            ArrayList<Integer> dateArray = new ArrayList<>();

            for (int i = 0; i != 10; i++) {
                instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(i).getInt("dt"));

                //JSON формат всегда выводит время в UTC

                ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
                int day = ldt.getDayOfMonth();
                dateArray.add(day);
            }

            for (int i = 0; dateArray.get(i) == day_c; i++) {
                String array = jsonObject.getJSONArray("list").getJSONObject(i).toString();
                JSONObject wa = new JSONObject(array);

                double max = wa.getJSONObject("main").getDouble("temp_max");
                double min = wa.getJSONObject("main").getDouble("temp_min");

                if (max > temp_max) {
                    temp_max = max;
                }
                if (min < temp_min) {
                    temp_min = min;
                }
            }

            int temp_current = (int) Math.round(dayZero.getJSONObject("main").getDouble("temp"));
            int feels_like_today = (int) Math.round(dayZero.getJSONObject("main").getDouble("feels_like"));
            int temp_min_today = (int) Math.round(temp_min);
            int temp_max_today = (int) Math.round(temp_max);

            city_name.setText(city_name_up);

            weatherInfo.setVisibility(View.INVISIBLE);
            weatherInfo.setText(weather_description_up + ("\n") + ("\n")
                    + "Температура: " + temp_current + " C" + ("\n") + ("\n")
                    + "Ощущается как: " + feels_like_today + " C" + ("\n") + ("\n")
                    + "Минимальная температура: " + temp_min_today + " C" + ("\n") + ("\n")
                    + "Максимальная температура: " + temp_max_today + " C");

            current_temp.setText(temp_current + "°");

            current_temp_min.setText(temp_min_today + "°");
            current_temp_max.setText(temp_max_today + "°");

            current_description.setText(weather_description_up);

            current_wind.setText(windSpeed + "м/c");

            wpicSet(weather_icon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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