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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_button;
    private TextView result_info;
    private ImageView weather_pic;

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

                weather_pic.setImageResource(0);

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
                String line = "";

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
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String wpic = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    result_info.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp") + " C" + ("\n")  + ("\n")
                            + "Ощущается как: " + jsonObject.getJSONObject("main").getDouble("feels_like") + " C"  + ("\n") + ("\n")
                            + "Минимальная температура: " + jsonObject.getJSONObject("main").getDouble("temp_min") + " C" + ("\n") + ("\n")
                            + "Максимальная температура: " + jsonObject.getJSONObject("main").getDouble("temp_max") + " C");

                        if (wpic.equals("Clear"))
                            weather_pic.setImageResource(R.drawable.sunny);

                        if (wpic.equals("Clouds"))
                            weather_pic.setImageResource(R.drawable.cloudy);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                result_info.setText("Такого города не существует");
            }
        }
    }
}