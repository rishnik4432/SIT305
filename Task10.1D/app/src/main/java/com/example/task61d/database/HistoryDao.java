package com.example.task61d.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface HistoryDao {
    @Insert
    void insert(HistoryEntity entity);

    @Query ("SELECT * FROM history ORDER BY timestamp DESC")
    List<HistoryEntity> getAll();

    @Query("DELETE FROM history")
    void deleteAll();
}
