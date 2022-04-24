package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_button;
    private TextView result_info;
    public ImageView weather_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_button = findViewById(R.id.main_button);
        result_info = findViewById(R.id.result_info);
        weather_pic = findViewById(R.id.weather_pic);

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.get().load("http://source.unsplash.com/random").into(weather_pic);

                if(user_field.getText().toString().trim().equals("")) {
                    result_info.setText("Введите текст");
                }else {
                    String city = user_field.getText().toString();
                    String key = "94c711ac5cd4f8c5b1da48fae97afb5a";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    new getData().execute(url);
                }
            }
        });
    }
    private class getData extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
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
                    JSONObject jsonObject = new JSONObject(result);
                    String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    String wpic = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String weaup = weather.substring(0, 1).toUpperCase() + weather.substring(1).toLowerCase();
                    result_info.setText(weaup + ("\n")  + ("\n")
                            + "Температура: " + jsonObject.getJSONObject("main").getDouble("temp") + " C" + ("\n")  + ("\n")
                            + "Ощущается как: " + jsonObject.getJSONObject("main").getDouble("feels_like") + " C"  + ("\n") + ("\n")
                            + "Минимальная температура: " + jsonObject.getJSONObject("main").getDouble("temp_min") + " C" + ("\n") + ("\n")
                            + "Максимальная температура: " + jsonObject.getJSONObject("main").getDouble("temp_max") + " C");

                            wpicset(wpic);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                result_info.setText("Такого города не существует");
            }
        }
        private void wpicset(String n){
            switch (n){
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
}