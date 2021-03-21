package com.example.androidtodoapp.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ToDoListTable.class}, version = 1 )
public abstract class MyRoomDatabase extends RoomDatabase{

    public abstract MyDataAccessInterface myDataAccessInterface();

    public static MyRoomDatabase myInstance;

    public static MyRoomDatabase getInstance(Context context){
        if (myInstance == null){
            myInstance = Room.databaseBuilder(context, MyRoomDatabase.class, "DatabaseName")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return myInstance;
    }

}
