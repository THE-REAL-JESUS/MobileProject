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

public class EditSQL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sql);
        EditText editName = (EditText) findViewById(R.id.addFirstNameSQL);
        EditText editFatherName = (EditText) findViewById(R.id.addFatherNameSQL);
        EditText editSurname = (EditText) findViewById(R.id.addSurnameSQL);
        EditText editNationalID = (EditText) findViewById(R.id.addNationalIDSQL);
        EditText editDOB = (EditText) findViewById(R.id.addDOBSQL);
        Button saveChanges = (Button) findViewById(R.id.addSQL);
        Button cancel = (Button) findViewById(R.id.cancelSQLAdd);
        Button pickDate = (Button) findViewById(R.id.editDateBtnSQL);
        TextView temperature = (TextView) findViewById(R.id.temprature);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(EditSQL.this);
        SharedPreferences.Editor editor = sp.edit();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + sp.getString("weatherCity", "berlin") + "&appid=9c2c754ed9190deeb0086f19b40aa026&units=metric";
        ChooseWeather.weather(url, temperature, humidity, this, weatherIcon);
        Button mainMenu = (Button) findViewById(R.id.mainMenuBtn);
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance();
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);
                editDOB.setText("" + dateFormat.format(c.getTime()));
            }
        };
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditSQL.this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditSQL.this, MainActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EditSQL.this, FireBaseList.class));
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(EditSQL.this);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("Student");

                DatabaseHelper myDB = new DatabaseHelper(EditSQL.this);

                String id = sp.getString("ID", "").toString();
                myDB.editRow(id, editName.getText().toString(), editFatherName.getText().toString(), editSurname.getText().toString(), editDOB.getText().toString(), editNationalID.getText().toString());
                Map<String, Object> map = new HashMap<>();
                Toasty.success(getBaseContext(), "Updated "+editName.getText().toString()+" "+editFatherName.getText().toString()+" "+editSurname.getText().toString()+" "+editDOB.getText().toString()+" "+editNationalID.getText().toString(), Toast.LENGTH_SHORT,true).show();
                    // Will return to main page upon clicking because we need the app to renew table
                    startActivity(new Intent(EditSQL.this, SQLList.class));
            }
        });


    }
}