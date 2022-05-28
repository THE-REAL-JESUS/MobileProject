package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SQLList extends AppCompatActivity {

    ArrayList<Student> studentList= new ArrayList<Student>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqllist);
        Log.d("Talal","In fire base list 1");
        Log.d("Talal","out of populate list");
        TextView temperature =(TextView) findViewById(R.id.temprature);//From action bar layout
        TextView humidity=(TextView) findViewById(R.id.humidity);//From action bar layout
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);//From action bar layout
        Button cancelSearch=(Button) findViewById(R.id.diaplayAll);//From search bar layout
        Button searchByID=(Button) findViewById(R.id.searchBtnFB);//From search bar layout
        EditText searchByIDText=(EditText) findViewById(R.id.searchByIDFB);//From search bar layout
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(SQLList.this);
        SharedPreferences.Editor editor = sp.edit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url,temperature,humidity,this,weatherIcon);
        Button mainMenu= (Button) findViewById(R.id.mainMenuBtn);
        searchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateList(searchByIDText.getText().toString());
                setUpListView();
            }
        });
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateList("");
                setUpListView();
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SQLList.this,MainActivity.class));
            }
        });
        populateList("");
        setUpListView();

    }
    public void populateList(String ID){
        Log.d("Talal","In populate list SQL");
        DatabaseHelper myDB= new DatabaseHelper(SQLList.this);
        Cursor cursor= myDB.getListContents();
        if (cursor.moveToFirst()){
            studentList.clear();
            do{
                if(ID.equals(""))
                studentList.add(new Student(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(5),cursor.getString(6),cursor.getString(4)));
                else if(ID.equals(cursor.getString(0))){
                    studentList.add(new Student(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(5),cursor.getString(6),cursor.getString(4)));
                }
            }
            while(cursor.moveToNext());
        }

    }


    public void setUpListView(){
        Log.d("Talal","In setup list");
        listView=(ListView)findViewById(R.id.sqlList);
        CellAdapter adapter= new CellAdapter(getApplicationContext(),0,studentList,SQLList.this);
        listView.setAdapter(adapter);
    }
}