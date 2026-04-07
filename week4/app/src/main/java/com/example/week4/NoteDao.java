package com.example.week4;

import androidx.room.Insert;
import androidx.room.Query;
public interface NoteDao {

    @Insertvoid insert(Note note);

}
