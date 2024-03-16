package com.example.save4fun.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.save4fun.model.User;
import com.example.save4fun.util.Constant;

public class DBUsersHelper extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "users";

    public DBUsersHelper(Context context) {
        super(context, Constant.DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("create table users(username text primary key, password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        long result = db.insert(USERS_TABLE, null, contentValues);
        return result != -1;
    }

    public boolean isUserExisted(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from users where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean authenticate(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor =
                db.rawQuery("select * from users where username = ? and password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }
}
