package com.example.farh.popmovies.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.farh.popmovies.Models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farh on 08/11/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "favMovie";
    private static final String TABLE_NAME = "movies";

    private static final String KEY_ID = "id";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_MOV_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BACKDROP = "backdrop";
    private static final String KEY_VOTE = "vote";
    private static final String KEY_DATE = "date";
    private SQLiteDatabase db;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_POSTER + " TEXT," + KEY_OVERVIEW + " TEXT," + KEY_MOV_ID + " TEXT," + KEY_TITLE + " TEXT," +
                KEY_BACKDROP + " TEXT," + KEY_VOTE + " TEXT," + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRow(String poster , String overView , String movID , String title , String backdrop ,
                          String vote , String date){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_POSTER , poster);
        values.put(KEY_OVERVIEW , overView);;
        values.put(KEY_MOV_ID , movID);
        values.put(KEY_TITLE , title);
        values.put(KEY_BACKDROP , backdrop);
        values.put(KEY_VOTE , vote);
        values.put(KEY_DATE , date);
        db.insert(TABLE_NAME , null , values);
        db.close();
    }
    public void deleteRow(String movID){
        db = this.getReadableDatabase();
        db.delete(TABLE_NAME , KEY_MOV_ID + " =?" , new String[]{movID});
        db.close();
    }
    public List<Movie> getFavMovies(){
        db = this.getReadableDatabase();
        List<Movie> movieList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{
                Movie movie = new Movie();
                movie.setPosterPath(cursor.getString(1));
                movie.setOverview(cursor.getString(2));
                movie.setId(cursor.getString(3));
                movie.setTitle(cursor.getString(4));
                movie.setBackdrop(cursor.getString(5));
                movie.setVoteAverage(cursor.getString(6));
                movie.setDate(cursor.getString(7));
                movieList.add(movie);
            }while (cursor.moveToNext());
        }
        return movieList;
    }

    public boolean ifExist(String movID){
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_MOV_ID}, KEY_MOV_ID + " =?", new String[]{movID}, null, null, null);
        if (cursor != null && cursor.moveToNext()){
            return true;
        }else {
            return false;
        }
    }
}
