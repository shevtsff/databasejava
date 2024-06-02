package com.example.profiledatabase.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.profiledatabase.ui.MainActivity;

import java.util.ArrayList;

public class DatabaseAdapter {
    DatabaseHelper databaseHelper; //получаем доступ к БД на устройстве
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        this.databaseHelper = new DatabaseHelper(context.getApplicationContext());

    }

    //подключение к бд
    public DatabaseAdapter Open() {
        database = databaseHelper.getReadableDatabase();
        return DatabaseAdapter.this;
    }

    //метод который закрывает подключение к бд
    public void Close() {
        databaseHelper.close();
    }

    //метод для создания записей в бд
    public void insert(Profile profile) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_SNAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_NAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.sName);
        cv.put(DatabaseHelper.COLUMN_IMAGE, profile.sName);


        database.insert(DatabaseHelper.TABLE_PROFILE, null, cv);
    }

    //метод для обновления данных
    public void update(Profile profile) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_SNAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_NAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.sName);
        cv.put(DatabaseHelper.COLUMN_IMAGE, profile.sName);


        database.update(DatabaseHelper.TABLE_PROFILE, cv, "_id = ?", new String[]{String.valueOf(profile._id)});
    }

    //метод для удаления данных в бд(id записи равно значению взятому из нашего массива значений)
    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_PROFILE, "_id = ?", new String[]{String.valueOf(id)});
    }

    //метод возвращающий курсор(объект заполненный данными) заполненный данными
    private Cursor getProfileEntries() {
        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_SNAME, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_IMAGE};
        return database.query(DatabaseHelper.TABLE_PROFILE, columns, null, null, null, null, null);
    }

    //метод извлекающий все записи из бд
    public ArrayList<Profile> profiles() {
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = getProfileEntries();
        while (cursor.moveToNext()) {
            Profile profile = new Profile(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE))
            );
            profiles.add(profile);
        }

        return profiles;
    }

    //метод позволяющий вернуть одну конкретную запись
    public Profile getSingleProfile(long id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PROFILE + " WHERE " + DatabaseHelper.COLUMN_ID + "=?;", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        return new Profile(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE))
        );
    }
}

