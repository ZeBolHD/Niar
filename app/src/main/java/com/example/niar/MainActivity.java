package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public EditText user_field;
    private Button main_button;
    private ImageButton info_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_button = findViewById(R.id.main_button);
        info_button = findViewById(R.id.info_icon);

        info_button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, info.class);
            startActivity(intent);
        });

        main_button.setOnClickListener(view -> {

            if(user_field.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
            }else {
                String city = user_field.getText().toString();
                String key = "94c711ac5cd4f8c5b1da48fae97afb5a";
                String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=10&appid=" + key + "&units=metric&lang=ru";

                new getData().execute(url);
                //startActivity(new Intent(MainActivity.this, weather_result.class));
            }
        });
    }

    private class getData extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while((line = reader.readLine())!= null){
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }

                try{
                if(reader!=null)
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result != null) {
                try {

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject dayZero = new JSONObject(jsonObject.getJSONArray("list").getJSONObject(0).toString());
                    String weather = dayZero.getJSONArray("weather").getJSONObject(0).getString("description");
                    String weather_description_up = weather.substring(0, 1).toUpperCase() + weather.substring(1).toLowerCase();
                    String city_name = jsonObject.getJSONObject("city").getString("name");
                    String city_name_up = city_name.substring(0, 1).toUpperCase() + city_name.substring(1).toLowerCase();

                    String wpic = dayZero.getJSONArray("weather").getJSONObject(0).getString("icon");

                    Temp temp_today = gson.fromJson(jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").toString(), Temp.class);
                    double temp_max = temp_today.temp_max;
                    double temp_min = temp_today.temp_min;

                    Instant instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(0).getInt("dt"));
                    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
                    int day_c = ldt.getDayOfMonth();

                    ArrayList<Integer> dateArray = new ArrayList<>();

                    for(int i = 0; i!=10; i++){
                        instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(i).getInt("dt"));

                        //JSON формат всегда выводит время в UTC

                        ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
                        int day = ldt.getDayOfMonth();
                        dateArray.add(day);
                    }

                    for(int i = 0; dateArray.get(i)==day_c; i++) {
                        String array = jsonObject.getJSONArray("list").getJSONObject(i).toString();
                        JSONObject wa = new JSONObject(array);

                        double max = wa.getJSONObject("main").getDouble("temp_max");
                        double min = wa.getJSONObject("main").getDouble("temp_min");

                        if(max>temp_max){
                            temp_max = max;
                        }
                        if(min<temp_min){
                            temp_min = min;
                        }
                    }

                    int temp_current = (int) Math.round(dayZero.getJSONObject("main").getDouble("temp"));
                    int feels_like_today = (int) Math.round(dayZero.getJSONObject("main").getDouble("feels_like"));
                    int temp_min_today = (int) Math.round(temp_min);
                    int temp_max_today = (int) Math.round(temp_max);

                    Intent intent = new Intent(MainActivity.this, weather_result.class);

                    intent.putExtra("weather icon", wpic);
                    intent.putExtra("city name", city_name_up);
                    intent.putExtra("description", weather_description_up);
                    intent.putExtra("temp_current", temp_current);
                    intent.putExtra("feels_like_today", feels_like_today);
                    intent.putExtra("temp_min_today", temp_min_today);
                    intent.putExtra("temp_max_today", temp_max_today);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Такого города не существует", Toast.LENGTH_SHORT).show();
            }
        }
    }
}