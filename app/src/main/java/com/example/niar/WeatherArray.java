package com.example.niar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

class Weather extends Temp{

    private URL url = null;
    private HttpURLConnection connection = null;
    private InputStreamReader is = null;
    private BufferedReader reader = null;
    private StringBuilder buffer = new StringBuilder();
    public String result = null;
    private String weatherArray;
    public String city;

    Instant instant;
    LocalDateTime ldt;

    Gson gson = new Gson();

    public void setCity(String city) {
        this.city = city;
    }

    public void getInfo(){
        try {

            String adress = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=5&appid=94c711ac5cd4f8c5b1da48fae97afb5a&units=metric";

            url = new URL(adress);
            connection = (HttpURLConnection) url.openConnection();

            is = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(is);

            while ((result = reader.readLine()) != null) {
                buffer.append(result).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        weatherArray = buffer.toString();
        currentWeatherForToday();
    }

    public void currentWeatherForToday() {
        if(weatherArray!=null){
            try {

                JSONObject jsonObject = new JSONObject(weatherArray);
                Temp temp_today = gson.fromJson(jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").toString(), Temp.class);
                temp_max = temp_today.temp_max;
                temp_min = temp_today.temp_min;

                instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(0).getInt("dt"));
                ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
                int day_c = ldt.getDayOfMonth();

                ArrayList<Integer> dateArray = new ArrayList<>();

                for(int i = 0; i!=5; i++){
                    instant = Instant.ofEpochSecond(jsonObject.getJSONArray("list").getJSONObject(i).getInt("dt"));

                    //JSON формат всегда выводит время в UTC

                    ldt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
                    int day = ldt.getDayOfMonth();
                    System.out.println(ldt);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

