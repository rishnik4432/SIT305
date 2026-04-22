package com.example.task51c.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insert(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("SELECT * FROM bookmark")
    List<Bookmark> getAllBookmarks();

    @Query("SELECT * FROM bookmark WHERE newsId = :newsId LIMIT 1")
    Bookmark getBookmarkByNewsId(int newsId);
}
