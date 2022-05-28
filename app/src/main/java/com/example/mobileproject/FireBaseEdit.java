package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FireBaseEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_edit);
        EditText editName= (EditText) findViewById(R.id.addFirstName);
        EditText editFatherName= (EditText) findViewById(R.id.addFatherName);
        EditText editSurname= (EditText) findViewById(R.id.addSurname);
        EditText editNationalID= (EditText) findViewById(R.id.addNationalID);
        EditText editDOB= (EditText) findViewById(R.id.addDOB);
        Button saveChanges= (Button) findViewById(R.id.addFB);
        Button cancel= (Button) findViewById(R.id.cancelFBAdd);
        Button pickDate=(Button) findViewById(R.id.editDateBtn);
        TextView temperature =(TextView) findViewById(R.id.temprature);
        TextView humidity=(TextView) findViewById(R.id.humidity);
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(FireBaseEdit.this);
        SharedPreferences.Editor editor = sp.edit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url,temperature,humidity,this,weatherIcon);
        Button mainMenu= (Button) findViewById(R.id.mainMenuBtn);
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance();
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);
                editDOB.setText(""+dateFormat.format(c.getTime()));
            }
        } ;
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FireBaseEdit.this,d,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FireBaseEdit.this,MainActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FireBaseEdit.this,FireBaseList.class));
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(FireBaseEdit.this);
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref= database.getReference().child("Student");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child("id").getValue().toString().equals(sp.getString("ID","").toString())){
                                Map<String, Object> map = new HashMap<>();
                                if(!editName.getText().toString().equals(""))
                                map.put("name", editName.getText().toString());
                                if(!editFatherName.getText().toString().equals(""))
                                map.put("fatherName", editFatherName.getText().toString());
                                if(!editSurname.getText().toString().equals(""))
                                map.put("surName", editSurname.getText().toString());
                                if(!editDOB.getText().toString().equals(""))
                                map.put("dob", editDOB.getText().toString());
                                if(!editNationalID.getText().toString().equals(""))
                                map.put("nationalID", editNationalID.getText().toString());
                                if(!map.isEmpty())
                                snapshot.getRef().updateChildren(map);
                            }
                        }
                        Toasty.success(getBaseContext(), "Updated "+editName.getText().toString()+" "+editFatherName.getText().toString()+" "+editSurname.getText().toString()+" "+editDOB.getText().toString()+" "+editNationalID.getText().toString(), Toast.LENGTH_SHORT,true).show();
                        // Will return to main page upon clicking because we need the app to renew table
                        startActivity(new Intent(FireBaseEdit.this,FireBaseList.class));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Talal","Error in updating databse");
                    }
                });
            }
        });


    }
}