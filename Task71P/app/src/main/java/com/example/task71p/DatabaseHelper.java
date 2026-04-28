package com.example.task71p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lostandfound.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "posts";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_CATEGORY = "category";
    private static final String COL_TYPE = "type";
    private static final String COL_IMAGE = "image";
    private static final String COL_DATETIME = "datetime";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_TYPE + " TEXT, " +
                COL_IMAGE + " BLOB, " +
                COL_DATETIME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new post
    public boolean addPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, post.getTitle());
        cv.put(COL_DESCRIPTION, post.getDescription());
        cv.put(COL_CATEGORY, post.getCategory());
        cv.put(COL_TYPE, post.getType());
        cv.put(COL_IMAGE, post.getImage());
        cv.put(COL_DATETIME, post.getDateTime());
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    // Get all posts (optionally filter by category)
    public List<Post> getAllPosts(String categoryFilter) {
        List<Post> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = null;
        String[] selectionArgs = null;
        if (categoryFilter != null && !categoryFilter.equals("All")) {
            selection = COL_CATEGORY + "=?";
            selectionArgs = new String[]{categoryFilter};
        }
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, COL_DATETIME + " DESC");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                String cat = cursor.getString(3);
                String type = cursor.getString(4);
                byte[] image = cursor.getBlob(5);
                String dt = cursor.getString(6);
                Post p = new Post(id, title, desc, cat, type, image, dt);
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Delete a post by id
    public boolean deletePost(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        return deletedRows > 0;
    }
}