package com.example.mobileproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users_data";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "fatherName";
    public static final String COL4 = "surName";
    public static final String COL5 = "gender";
    public static final String COL6 = "nationalID";
    public static final String COL7 = "dob";

    /* Constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Code runs automatically when the dB is created */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+"  TEXT, "+COL3+
                " TEXT,  "+COL4+"  TEXT, "+COL5+"  TEXT, "+COL6+"  TEXT, "+COL7+"  TEXT)";
        db.execSQL(createTable);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* Basic function to add data. REMEMBER: The fields
       here, must be in accordance with those in
       the onCreate method above.
    */
    public boolean addData(String id, String name,String fatherName,String surName, String dob,String nationalID, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, fatherName);
        contentValues.put(COL4, surName);
        contentValues.put(COL5, gender);
        contentValues.put(COL6, nationalID);
        contentValues.put(COL7, dob);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data are inserted incorrectly, it will return -1
        if(result == -1) {return false;} else {return true;}
    }
    public boolean addDataWithoutID(String name,String fatherName,String surName, String dob,String nationalID, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, fatherName);
        contentValues.put(COL4, surName);
        contentValues.put(COL5, gender);
        contentValues.put(COL6, nationalID);
        contentValues.put(COL7, dob);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data are inserted incorrectly, it will return -1
        if(result == -1) {return false;} else {return true;}
    }

    /* Returns only one result */
    public Cursor structuredQuery(int ID) {
        SQLiteDatabase db = this.getReadableDatabase(); // No need to write
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1,
                        COL2, COL3}, COL1 + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }


    public Cursor getSpecificResult(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME+"Where ID="+ID,null);
        return data;
    }

    // Return everything inside the dB
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public boolean editRow(String ID, String name,String fatherName, String surName,  String dob,String nationalID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(!name.equals(""))
                db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL2+"= '"+name+"' WHERE "+COL1+"="+ID);
            if(!fatherName.equals(""))
                db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL3+"='"+fatherName+"' WHERE "+COL1+"="+ID);
            if(!surName.equals(""))
                db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL4+"='"+surName+"' WHERE "+COL1+"="+ID);
            if(!nationalID.equals(""))
                db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL6+"='"+nationalID+"' WHERE "+COL1+"="+ID);
            if(!dob.equals(""))
                db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL7+"='"+dob+"' WHERE "+COL1+"="+ID);
        }
        catch (Exception e){
            return false;
        }

        return true;

    }

    public boolean deleteRow(String id){
        try{
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from "+TABLE_NAME+" where id ="+id);}
        catch (Exception e){
            return false;
        }

        return true;


    }
}
