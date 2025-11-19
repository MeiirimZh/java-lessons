package com.example.javalessons;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private final String databasePath;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDatabaseIfNotExists() {
        File dbFile = new File(databasePath);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            copyDatabaseFromAssets();
        }
        Log.d("DB_TEST", "DB SIZE = " + dbFile.length());
    }

    public void forceCopy() {
        try {
            File dbFile = new File(databasePath);
            if (dbFile.exists()) {
                dbFile.delete();
            }
            copyDatabaseFromAssets();
            Log.d("DB_TEST", "Database replaced from assets!");
        } catch (Exception e) {
            Log.e("DB_TEST", "ERROR REPLACING DB: " + e.getMessage());
        }
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream inputStream = context.getAssets().open("databases/" + DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(databasePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getLectureTitles() {
        List<String> titles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT lecture_title FROM lectures ORDER BY lecture_id", null);
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("lecture_title"));
                    titles.add(title);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DB_TEST", "Ошибка при чтении таблицы lectures: " + e.getMessage());
        }

        db.close();
        return titles;
    }
}