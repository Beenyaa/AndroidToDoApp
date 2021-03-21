package com.example.androidtodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidtodoapp.roomdatabase.MyRoomDatabase;
import com.example.androidtodoapp.roomdatabase.ToDoListTable;

public class NewItemActivity extends AppCompatActivity {

    private EditText itemName;
    private Button saveNewItem;
    private MyRoomDatabase myRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener((v) -> {
            startActivity(new Intent(NewItemActivity.this, MainActivity.class));
            finish();
        });

        itemName = findViewById(R.id.itemName);

        saveNewItem = findViewById(R.id.saveNewItemBtn);

        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());

        final ToDoListTable toDoListTable = new ToDoListTable();

        saveNewItem.setOnClickListener((v) -> {
            String item = itemName.getText().toString();
            if (TextUtils.isEmpty(item)){
                Toast.makeText(NewItemActivity.this, "Type in a task to do", Toast.LENGTH_SHORT).show();
            }else {
                toDoListTable.setItem(item);
                toDoListTable.setCompleted(false);
                myRoomDatabase.myDataAccessInterface().insert(toDoListTable);
                Toast.makeText(NewItemActivity.this, "Task has been added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NewItemActivity.this, MainActivity.class));
                finish();
            }

        });


    }
}