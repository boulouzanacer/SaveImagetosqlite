package com.example.saveimagetosqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mac Air on 12/04/2019.
 */
public class DATABASE extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Database version
    private static final String DATABASE_NAME = "image_to_sqlite"; //Database name

    private Context mContext;

    //Constructor DATABASE
    public DATABASE(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

        mContext = context;
      // context.deleteDatabase("image_to_sqlite");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.v("TRACKKK","================>  ONCREATE EXECUTED");


        db.execSQL("CREATE TABLE IF NOT EXISTS Image(IMAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE BLOB)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.v("TRACKKK","================>  ON UPGRADE EXECUTED");

    }

    ///////////////////////////////////////////////////////////////////////////////
    //////                             Functions                             //////
    ///////////////////////////////////////////////////////////////////////////////



    //=============================== FUNCTION TO INSERT INTO Produits TABLE ===============================
    public void Insert_into_image(PostData_image image){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IMAGE", image.image);

        db.insert("Image", null, values);
    }


    //============================== FUNCTION SELECT Produits FROM Produits TABLE ===============================
    public ArrayList<PostData_image> select_image_from_database(String querry){
        ArrayList<PostData_image> images = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_image image = new PostData_image();

                image.id_image = cursor.getInt(cursor.getColumnIndex("IMAGE_ID"));
                image.image = cursor.getBlob(cursor.getColumnIndex("IMAGE"));

                images.add(image);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return images;
    }

    public long getDatabaseSize(){
        File f = mContext.getDatabasePath(DATABASE_NAME);
        return  f.length();
    }
}