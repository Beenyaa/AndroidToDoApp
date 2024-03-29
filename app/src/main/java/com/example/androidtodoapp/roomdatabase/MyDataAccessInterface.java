package com.example.androidtodoapp.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDataAccessInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ToDoListTable toDoListTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ToDoListTable toDoListTable);

    @Delete()
    void delete(ToDoListTable toDoListTable);

    @Query("select * from to_do_table")
    List<ToDoListTable> collectList();

}
