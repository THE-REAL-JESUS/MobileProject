package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView temperature =(TextView) findViewById(R.id.temprature);
        TextView humidity=(TextView) findViewById(R.id.humidity);
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);
        Button goToFireBaseList=(Button) findViewById(R.id.goToFireList);
        Button goToAddToFB=(Button) findViewById(R.id.goToAddFirebase);
        Button goToChooseWeather=(Button) findViewById(R.id.goToChooseWeather);
        Button goToSQLList= (Button) findViewById(R.id.goToSQLList);
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = sp.edit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url,temperature,humidity,this,weatherIcon);
        goToSQLList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SQLList.class));
            }
        });
        goToChooseWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChooseWeather.class));
            }
        });
        goToAddToFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddFB.class));
            }
        });
        goToFireBaseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Talal","In on click");
                startActivity(new Intent(MainActivity.this,FireBaseList.class));
            }
        });

    }


}