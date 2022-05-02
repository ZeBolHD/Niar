package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
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

            if(networkConnected()) {
                if(user_field.getText().toString().equals("ачуна")){

                    Intent intent = new Intent(MainActivity.this, weather_error.class);
                    intent.putExtra("error", "easter");

                    startActivity(intent);
                }
                else if (user_field.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                } else {
                    String city = user_field.getText().toString().trim();
                    String key = "94c711ac5cd4f8c5b1da48fae97afb5a";
                    String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=10&appid=" + key + "&units=metric&lang=ru";

                    new getData().execute(url);
                    //startActivity(new Intent(MainActivity.this, weather_result.class));
                }
            }else{
                Toast.makeText(getApplicationContext(), "Отсутствует подключение к интернету", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean networkConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            return networkInfo.isConnected();
        }else{
            return false;
        }
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

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result != null) {

                Intent intent = new Intent(MainActivity.this, weather_result.class);

                intent.putExtra("jsonData", result);
                startActivity(intent);

            }else{
                String error = "city error";

                Intent intent = new Intent(MainActivity.this, weather_error.class);
                intent.putExtra("error", error);
                startActivity(intent);
            }
        }
    }
}