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

public class AddFB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fb);
        EditText addName= (EditText) findViewById(R.id.addFirstName);
        EditText addFatherName= (EditText) findViewById(R.id.addFatherName);
        EditText addSurname= (EditText) findViewById(R.id.addSurname);
        EditText addDOB= (EditText) findViewById(R.id.addDOB);
        EditText addNationalID= (EditText) findViewById(R.id.addNationalID);
        EditText addGender= (EditText) findViewById(R.id.addGender);
        Button addBtn= (Button) findViewById(R.id.addFB);
        Button cancelAdd=(Button) findViewById(R.id.cancelFBAdd);
        Button pickDate=(Button) findViewById(R.id.addDateBtn);
        TextView temperature =(TextView) findViewById(R.id.temprature);
        TextView humidity=(TextView) findViewById(R.id.humidity);
        ImageView weatherIcon= (ImageView) findViewById(R.id.weatherIcon);
        final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(AddFB.this);
        SharedPreferences.Editor editor = sp.edit();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+sp.getString("weatherCity","berlin")+"&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url,temperature,humidity,this,weatherIcon);
        Button mainMenu= (Button) findViewById(R.id.mainMenuBtn);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddFB.this,MainActivity.class));
            }
        });
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference().child("Student");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map<String, Object> bigMap = new HashMap<>();//Since firebase is maps inside maps I need to create a map for each entry which contains another map that contains all student info
                        String ID="";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ID=snapshot.child("id").getValue().toString();// To get the ID of the last entry in table
                        }
                        int newID=1;
                        if(!ID.equals(""))
                            newID= Integer.parseInt(ID)+1;

                        Map<String, Object> map = new HashMap<>();
                        bigMap.put(""+newID,map);

                        map.put("id",""+newID);
                        if(!addName.getText().toString().equals(""))
                            map.put("name", addName.getText().toString());
                        else map.put("name", "N/A");
                        if(!addFatherName.getText().toString().equals(""))
                            map.put("fatherName", addFatherName.getText().toString());
                        else map.put("fatherName", "N/A");
                        if(!addSurname.getText().toString().equals(""))
                            map.put("surName", addSurname.getText().toString());
                        else map.put("surName", "N/A");
                        if(!addDOB.getText().toString().equals(""))
                            map.put("dob", addDOB.getText().toString());
                        else map.put("dob", "N/A");
                        if(!addNationalID.getText().toString().equals(""))
                            map.put("nationalID", addNationalID.getText().toString());
                        else map.put("nationalID", "N/A");
                        if(!addGender.getText().toString().equals(""))
                            map.put("gender", addGender.getText().toString());
                        else map.put("gender", "N/A");
                        if(!map.isEmpty())
                            dataSnapshot.getRef().updateChildren(bigMap);
                        Toasty.success(getBaseContext(), "Successfully Inserted in FB "+addName.getText().toString()+" "+addSurname.getText().toString(), Toast.LENGTH_SHORT,true).show();


                        // Will return to main page upon clicking because we need the app to renew table
                        startActivity(new Intent(AddFB.this,MainActivity.class));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Talal","Error in adding into database");
                    }


                });
            }
        });
        cancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddFB.this,MainActivity.class));
            }
        });
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance();
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);
                addDOB.setText(""+dateFormat.format(c.getTime()));
            }
        } ;
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddFB.this,d,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }
}