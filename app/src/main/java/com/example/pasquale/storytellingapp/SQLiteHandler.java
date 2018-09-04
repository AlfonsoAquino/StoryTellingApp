package com.example.pasquale.storytellingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "storytelling";

    // Login table name
    private static final String TABLE_NAME = "album";
    private static final String TABLE_TABLE = "vignette";
    private static final String TABLE_Statistics = "statistica";


    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nome";
    private static final String KEY_PATH = "path";
    private static final String KEY_TIPO = "tipo";
    private static final String KEY_ID_ALBUM = "idAlbum";
    private static final String KEY_ID_PAZIENTE = "idPaziente";
    private static final String KEY_ORDINE = "ordine";
    private static final String KEY_numCorr = "numCorrette";
    private static final String KEY_numErr = "numSbagliate";

    private ArrayList<Statistica> statistiche;
    private ArrayList<Album> albums;
    private ArrayList<Vignetta> vignette;

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        albums=new ArrayList<>();
        vignette=new ArrayList<>();
        statistiche=new ArrayList<>();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PATH + " TEXT ," + KEY_TIPO + " TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_vignette_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PATH + " TEXT,"
                + KEY_ORDINE + " TEXT ," + KEY_ID_ALBUM + " TEXT)";
        db.execSQL(CREATE_vignette_TABLE);
        String CREATE_STATISTICA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_Statistics + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_numCorr + " TEXT,"
                + KEY_numErr + " TEXT ," + KEY_ID_ALBUM + " TEXT,"+ KEY_ID_PAZIENTE + " TEXT)";
        db.execSQL(CREATE_STATISTICA_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addAlbum(int id,String name, String path, int tipo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Name
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PATH, path); // Email
        values.put(KEY_TIPO, tipo); // Email

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New album inserted into sqlite");
    }

    public void addVignetta(int idAlbum, String path, int ordine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ALBUM, idAlbum); // Name
        values.put(KEY_PATH, path); // Email
        values.put(KEY_ORDINE, ordine); // Email

        // Inserting Row
        db.insert(TABLE_TABLE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New vignetta inserted into sqlite");
    }

    public void addStatistica(String idPaziente,String idAlbum, String numCorr, String numErr) {

        SQLiteDatabase db = this.getReadableDatabase();
        int corr=0;
        int err=0;
        int id=0;
        //controllo se esiste già una corrispondenza album/paziente
        String selectQuery = "SELECT "+KEY_ID+","+KEY_numErr+","+KEY_numCorr+" FROM " + TABLE_Statistics+" WHERE idAlbum = "+idAlbum+" and idPaziente = "+idPaziente;

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0) {

            // Move to first row
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                corr=Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_numCorr)));
                err=Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_numErr)));

                cursor.moveToNext();
            }
            cursor.close();
            corr+=Integer.parseInt(numCorr);
            err+=Integer.parseInt(numErr);

            String strFilter = "id=" + id;
            ContentValues args = new ContentValues();
            args.put(KEY_numCorr, corr);
            args.put(KEY_numErr, err);
            db.update(TABLE_Statistics, args, strFilter, null);

            Log.d(TAG, "Row updated into sqlite");

        }else {

            ContentValues values = new ContentValues();
            values.put(KEY_ID_ALBUM, idAlbum); // Name
            values.put(KEY_ID_PAZIENTE, idPaziente); // Name
            values.put(KEY_numCorr, numCorr); // Email
            values.put(KEY_numErr, numErr); // Email

            // Inserting Row
            db.insert(TABLE_Statistics, null, values);

            Log.d(TAG, "New statistic inserted into sqlite");
        }
        // Closing database connection
        db.close();
        Log.d(TAG, "oooooooooooo");

    }

    public ArrayList<Statistica> getStatisticsDetails() {
        String selectQuery = "SELECT  * FROM " + TABLE_Statistics;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Statistica statistica;
        while (!cursor.isAfterLast()){

            statistica=new Statistica(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_numCorr))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_numErr))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID_ALBUM))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID_PAZIENTE))));
            Log.i(TAG,"------------->"+statistica.toString());

            cursor.moveToNext();
            statistiche.add(statistica);
        }

        cursor.close();
        db.close();
        // return user
//        Log.d(TAG, "Fetching user from Sqlite: " + albums.size());

        return statistiche;
    }


    /**
     * Getting user data from database
     * */
    public ArrayList<Album> getAlbumDetails() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Album album;
        while (!cursor.isAfterLast()){

            album=new Album(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_TIPO))));
            Log.i(TAG,"------------->"+album.toString());

            cursor.moveToNext();
            albums.add(album);
        }

        cursor.close();
        db.close();
        // return user
//        Log.d(TAG, "Fetching user from Sqlite: " + albums.size());

        return albums;
    }


    public ArrayList<Vignetta> getVignettaDetails() {
        String selectQuery = "SELECT  * FROM " + TABLE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Vignetta vign;
        while (!cursor.isAfterLast()){

            vign=new Vignetta(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID_ALBUM))),
                    cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ORDINE))));
            Log.i(TAG,"------------->"+vign.toString());

            cursor.moveToNext();
            vignette.add(vign);
        }

        cursor.close();
        db.close();
        // return user
//        Log.d(TAG, "Fetching user from Sqlite: " + albums.size());

        return vignette;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteAlbum() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}