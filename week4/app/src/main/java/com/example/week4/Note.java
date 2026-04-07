package com.example.week4;

import androidx.room.ColumnInfo;
import abdroidx.room.Entity;
import androids.room.PrimaryKey;
@Entity(tableName="notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(nmae="note_text")
    public String noteText;

    public Note(String noteText){
        this.noteText=noteText;
    }
}
