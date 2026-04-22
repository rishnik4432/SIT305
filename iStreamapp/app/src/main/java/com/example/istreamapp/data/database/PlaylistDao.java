package com.example.istreamapp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    void insert(PlaylistItem item);

    @Delete
    void delete(PlaylistItem item);

    @Query("SELECT * FROM playlist WHERE userId = :userId")
    List<PlaylistItem> getPlaylistForUser(int userId);
}