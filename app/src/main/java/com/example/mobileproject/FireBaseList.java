package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseList extends AppCompatActivity {
    ArrayList<Student> studentList= new ArrayList<Student>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_list);
        Log.d("Talal","In fire base list 1");
        Log.d("Talal","out of populate list");
        TextView temperature =(TextView) findViewById(R.id.temprature);
        TextView humidity=(TextView) findViewById(R.id.humidity);
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);
        Button cancelSearch=(Button) findViewById(R.id.diaplayAll);
        Button searchByID=(Button) findViewById(R.id.searchBtnFB);
        EditText searchByIDText=(EditText) findViewById(R.id.searchByIDFB);
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(FireBaseList.this);
        SharedPreferences.Editor editor = sp.edit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url,temperature,humidity,this,weatherIcon);
        Button mainMenu= (Button) findViewById(R.id.mainMenuBtn);
        searchByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateList(searchByIDText.getText().toString());
            }
        });
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateList("");

            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FireBaseList.this,MainActivity.class));
            }
        });
        populateList("");

    }
    public void populateList(String ID){
        Log.d("Talal","In populate list");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference().child("Student");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();
                try{
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(ID.equals("")){
                        studentList.add(new Student(
                                snapshot.child("id").getValue().toString(),
                                snapshot.child("name").getValue().toString(),
                                snapshot.child("fatherName").getValue().toString(),
                                snapshot.child("surName").getValue().toString(),
                                snapshot.child("nationalID").getValue().toString(),
                                snapshot.child("dob").getValue().toString(),
                                snapshot.child("gender").getValue().toString()));
                        }else{
                            if(snapshot.child("id").getValue().toString().equals(ID)){
                                studentList.add(new Student(
                                        snapshot.child("id").getValue().toString(),
                                        snapshot.child("name").getValue().toString(),
                                        snapshot.child("fatherName").getValue().toString(),
                                        snapshot.child("surName").getValue().toString(),
                                        snapshot.child("nationalID").getValue().toString(),
                                        snapshot.child("dob").getValue().toString(),
                                        snapshot.child("gender").getValue().toString()));
                            }
                        }

                    }
                    setUpListView();
                }
                catch(Error error){
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Talal","Error in populating list from database");
            }
        });
    }


    public void setUpListView(){
        Log.d("Talal","In setup list");
        listView=(ListView)findViewById(R.id.fireBaseList);
        CellAdapter adapter= new CellAdapter(getApplicationContext(),0,studentList,FireBaseList.this);
        listView.setAdapter(adapter);
    }
}
