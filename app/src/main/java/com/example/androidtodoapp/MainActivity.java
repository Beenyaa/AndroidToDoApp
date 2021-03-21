package com.example.androidtodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.androidtodoapp.roomdatabase.MyRoomDatabase;
import com.example.androidtodoapp.roomdatabase.ToDoListTable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Button addNewItem;
    private RecyclerView recyclerView;
    private List<ToDoListTable> toDoModelList = new ArrayList<>();
    private MyRoomDatabase myRoomDatabase;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("To Do App");

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        addNewItem = findViewById(R.id.addNewItemActivityBtn);
        addNewItem.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, NewItemActivity.class));
            finish();
        });

        myRoomDatabase = MyRoomDatabase.getInstance(getApplicationContext());
        recyclerView = findViewById(R.id.toDoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        collectItems();

    }

    private void collectItems(){
        swipeRefreshLayout.setRefreshing(true);
        toDoModelList = myRoomDatabase.myDataAccessInterface().collectList();
        RecyclerView.Adapter adapter = new ToDoListRecyclerAdapter(MainActivity.this, toDoModelList, myRoomDatabase);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

        collectItems();

    }
}

