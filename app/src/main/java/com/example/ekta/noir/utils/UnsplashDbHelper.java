package com.example.ekta.noir.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.ekta.noir.model.UnsplashData;

import static com.example.ekta.noir.model.ResponseKeys.IMAGE_COLOR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_CREATED_AT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_HEIGHT;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_ID;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_FULL;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_URLS_REGULAR;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_USER_NAME;
import static com.example.ekta.noir.model.ResponseKeys.IMAGE_WIDTH;


public class UnsplashDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "unsplash_database";
    public static final String UNSPLASH_TABLE = "unsplash_details";

    public UnsplashDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_URL = "CREATE TABLE " + UNSPLASH_TABLE + "("
                + IMAGE_ID + " TEXT PRIMARY KEY,"
                + IMAGE_WIDTH + " INTEGER,"
                + IMAGE_HEIGHT + " INTEGER,"
                + IMAGE_CREATED_AT + " TEXT,"
                + IMAGE_COLOR + " TEXT,"
                + IMAGE_URLS_FULL + " TEXT,"
                + IMAGE_URLS_REGULAR + " TEXT,"
                + IMAGE_USER_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_URL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UNSPLASH_TABLE);
        onCreate(db);
    }

    //METHODS
    public void insertPhotos(UnsplashData photo){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues unsplashValues = new ContentValues();
        unsplashValues.put(IMAGE_ID, photo.getId());
        unsplashValues.put(IMAGE_WIDTH, photo.getWidth());
        unsplashValues.put(IMAGE_HEIGHT, photo.getHeight());
        unsplashValues.put(IMAGE_CREATED_AT, photo.getCreatedAt());
        unsplashValues.put(IMAGE_COLOR, photo.getColor());
        unsplashValues.put(IMAGE_URLS_FULL, photo.getUrlFull());
        unsplashValues.put(IMAGE_URLS_REGULAR, photo.getUrlRegular());
        unsplashValues.put(IMAGE_USER_NAME, photo.getUsername());

        db.insert(UNSPLASH_TABLE, null, unsplashValues);
        db.close();
    }

    public UnsplashData getPhoto(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor unsplashCursor = db.query(UNSPLASH_TABLE,
                new String[]{IMAGE_ID,
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT,
                        IMAGE_CREATED_AT,
                        IMAGE_COLOR,
                        IMAGE_URLS_FULL,
                        IMAGE_URLS_REGULAR,
                        IMAGE_USER_NAME,
                },
                IMAGE_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if(unsplashCursor != null)
            unsplashCursor.moveToFirst();

        UnsplashData photo = new UnsplashData(unsplashCursor.getString(0),
                unsplashCursor.getInt(1),
                unsplashCursor.getInt(2),
                unsplashCursor.getString(3),
                unsplashCursor.getString(4),
                unsplashCursor.getString(5),
                unsplashCursor.getString(6),
                unsplashCursor.getString(7));
        return photo;
    }

    public List<UnsplashData> getAllPhotos(){

        List<UnsplashData> unsplashList = new ArrayList<UnsplashData>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + UNSPLASH_TABLE + "";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                UnsplashData photo = new UnsplashData();
                photo.setId(cursor.getString(0));
                photo.setWidth(cursor.getInt(1));
                photo.setHeight(cursor.getInt(2));
                photo.setCreatedAt(cursor.getString(3));
                photo.setColor(cursor.getString(4));
                photo.setUrlFull(cursor.getString(5));
                photo.setUrlRegular(cursor.getString(6));
                photo.setUsername(cursor.getString(7));
                unsplashList.add(photo);
            }while(cursor.moveToNext());
        }
        return unsplashList;
    }

    public void deletePhoto(UnsplashData photo){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UNSPLASH_TABLE, IMAGE_ID + "=?" , new String[]{String.valueOf(photo.getId())});
        db.close();
    }

    public int numberOfRows(){
        String query = "SELECT  * FROM " + UNSPLASH_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }
}
