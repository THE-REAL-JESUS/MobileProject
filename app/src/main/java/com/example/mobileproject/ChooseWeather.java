package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ChooseWeather extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_weather);
        TextView temperature =(TextView) findViewById(R.id.temprature);
        TextView humidity=(TextView) findViewById(R.id.humidity);
        EditText city= (EditText) findViewById(R.id.chooseCity);
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);
        Button saveWeather= (Button) findViewById(R.id.saveWeatherBtn);
        Button mainMenu= (Button) findViewById(R.id.mainMenuBtn);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseWeather.this,MainActivity.class));
            }
        });
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(ChooseWeather.this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("weatherCity",sp.getString("weatherCity","berlin"));
        editor.commit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        weather(url,temperature,humidity,this,weatherIcon);
        saveWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("weatherCity",city.getText().toString());
                editor.commit();
                String url="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
                weather(url,temperature,humidity,ChooseWeather.this,weatherIcon);
                Log.d("Talal","In click listener of save Weather button "+sp.getString("weatherCity","berlin"));
                Toasty.success(getBaseContext(), "Successfully changed weather to "+sp.getString("weatherCity","ERROR"), Toast.LENGTH_SHORT,true).show();

            }
        });

    }
    public static void weather(String url, TextView temperature, TextView humidity, Context context, ImageView weatherIcon){

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("Talal",response.toString());
                    JSONObject jsonMain=response.getJSONObject("main");
                    String jsonTimezone=response.getString("name");
                    JSONObject jsonSys = response.getJSONObject("sys");
                    JSONArray jsonWeather = response.getJSONArray("weather");
                    JSONObject jsonWeatherObject = jsonWeather.getJSONObject(0);
                    String weather = jsonWeatherObject.getString("main");
                    temperature.setText(jsonTimezone+" "+jsonMain.getDouble("temp")+"°C");
                    humidity.setText(jsonMain.getDouble("humidity")+"%");

                    if(weather.equals("Clear")){
                        weatherIcon.setImageResource(R.drawable.sun);
                    }
                    else if(weather.equals("Clouds")){
                        weatherIcon.setImageResource(R.drawable.cloudy);
                    }
                    else if(weather.equals("Rain")){
                        weatherIcon.setImageResource(R.drawable.rainy);
                    }
                }

                catch(Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObj);


    }
}