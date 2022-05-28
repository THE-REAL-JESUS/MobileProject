package com.example.mobileproject;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CellAdapter extends ArrayAdapter<Student> {
    Context firebaselist;
    public CellAdapter(Context context,int resource ,ArrayList<Student> studentList, Context firebaselist){
        super(context,resource,studentList);
        this.firebaselist=firebaselist;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Student student=getItem(position);



        if(convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.cell,parent,false);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toasty.info(firebaselist, ""+student.getName()+" "+student.getSurName(),
                        Toast.LENGTH_SHORT, true).show();

            }
        });

        TextView ID= convertView.findViewById(R.id.studentName);
        Button addToSQLBtn= convertView.findViewById(R.id.addtoSQLBtn);
        if(firebaselist.getClass().toString().equals("class com.example.mobileproject.SQLList"))
            addToSQLBtn.setVisibility(View.GONE);
        ImageView img= convertView.findViewById(R.id.imageView);
        ImageButton editBtn= convertView.findViewById(R.id.editButton);
        editBtn.setImageResource(R.drawable.edit);
        ImageButton deleteBtn= convertView.findViewById(R.id.imageButton2);
        deleteBtn.setImageResource(R.drawable.delete);
        img.setImageResource(R.drawable.pic);
        String text=student.getID()+"\n"+student.getName()+" "+student.getFatherName()+" "+student.getSurName()+"\n"+student.getDOB()+"\n"+student.getNationalID()+"\n"+student.getGender();
        ID.setText(text);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(firebaselist);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ID",student.getID());
                editor.commit();
                if(!firebaselist.getClass().toString().equals("class com.example.mobileproject.SQLList")){
                Intent intent= new Intent(firebaselist,FireBaseEdit.class);
                firebaselist.startActivity(intent);}else{
                    Intent intent= new Intent(firebaselist,EditSQL.class);
                    firebaselist.startActivity(intent);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(firebaselist);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ID",student.getID());
                editor.commit();
                if(!firebaselist.getClass().toString().equals("class com.example.mobileproject.SQLList")){
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref= database.getReference().child("Student");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child("id").getValue().toString()
                                    .equals(sp.getString("ID","").toString())){
                                snapshot.getRef().removeValue();
                                Toasty.success(firebaselist, "Successfully deleted "+student.getName()+" "+student.getSurName(), Toast.LENGTH_SHORT,true).show();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Talal","Error in deleting entry from database");
                    }
                });}else{
                    DatabaseHelper myDB= new DatabaseHelper(firebaselist);
                    boolean bool=myDB.deleteRow(student.getID());
                    if(bool)
                    Toasty.success(firebaselist, "Successfully deleted "+student.getName()+" "+student.getSurName(), Toast.LENGTH_SHORT,true).show();
                    else Toasty.error(firebaselist, "Failed delete", Toast.LENGTH_SHORT, true).show();

                    firebaselist.startActivity(new Intent(firebaselist, firebaselist.getClass()));

                }
            }
        });

        addToSQLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper myDB= new DatabaseHelper(firebaselist);
                boolean insert=myDB.addData(student.getID(),student.getName(),student.getFatherName(),student.getSurName(),student.getDOB(),student.getNationalID(),student.getGender());
                if(insert)
                    Toasty.success(firebaselist, "Successfully Inserted in SQL "+student.getName()+" "+student.getSurName(), Toast.LENGTH_SHORT,true).show();
                else Toasty.error(firebaselist, "Failed insert(Already in SQL database)", Toast.LENGTH_SHORT, true).show();
            }
        });
        return convertView;

    }
}
