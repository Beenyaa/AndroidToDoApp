package com.example.androidtodoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtodoapp.roomdatabase.MyRoomDatabase;
import com.example.androidtodoapp.roomdatabase.ToDoListTable;

import java.util.List;

public class ToDoListRecyclerAdapter extends RecyclerView.Adapter<ToDoListRecyclerAdapter.MyViewHolder> {
    Context context;
    List<ToDoListTable> toDoListTable;
    MyRoomDatabase myRoomDatabase;
    AlertDialog alertDialog;

    public ToDoListRecyclerAdapter(Context context, List<ToDoListTable> toDoModelList, MyRoomDatabase myRoomDatabase) {
        this.toDoListTable = toDoModelList;
        this.myRoomDatabase = myRoomDatabase;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ToDoListTable toDoModel = toDoListTable.get(position);
        holder.toDoItem.setText(toDoModel.getItem());

        holder.delete.setOnClickListener((v) -> {
            myRoomDatabase.myDataAccessInterface().delete(toDoModel);
            toDoListTable = myRoomDatabase.myDataAccessInterface().collectList();
            notifyDataSetChanged();
        });

        holder.complete.setChecked(toDoModel.isCompleted());

        if(toDoModel.isCompleted()){
            holder.toDoItem.setPaintFlags(holder.toDoItem.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.toDoItem.setPaintFlags(0);
        }

        holder.setItemClickListener(new MyOnClickListener(){
            @Override
            public void onClickListener(View v, int itemPosition) {
                CheckBox checkBox = (CheckBox)v;
                if (checkBox.isChecked()){
                    toDoModel.setCompleted(true);
                }else{
                    toDoModel.setCompleted(false);
                }

                myRoomDatabase.myDataAccessInterface().update(toDoModel);
                myRoomDatabase.myDataAccessInterface().collectList();
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View nView = LayoutInflater.from(context).inflate(R.layout.update_item_layout, null);
                final EditText itemName = nView.findViewById(R.id.itemName);
                itemName.setText(toDoModel.getItem());

                Button backBtn = nView.findViewById(R.id.backBtn);

                backBtn.setOnClickListener((v) -> {
                    alertDialog.dismiss();
                });


                Button updateItem = nView.findViewById(R.id.saveNewItemBtn);
                updateItem.setOnClickListener((v) -> {
                    String item = itemName.getText().toString();
                    if (TextUtils.isEmpty(item)){
                        Toast.makeText(context, "Type in a task to do", Toast.LENGTH_SHORT).show();
                    }else {
                        toDoModel.setItem(item);
                        myRoomDatabase.myDataAccessInterface().update(toDoModel);
                        myRoomDatabase.myDataAccessInterface().collectList();
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }

                });

                builder.setView(nView);
                alertDialog = builder.create();
                alertDialog.show();

            }

        });

    }

    @Override
    public int getItemCount() {
        return toDoListTable.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView toDoItem;
        Button edit, delete;
        CheckBox complete;
        private MyOnClickListener myOnClickListener;

        public MyViewHolder(@NonNull View itemView){
            super((itemView));

            toDoItem = itemView.findViewById(R.id.toDoItem);
            edit = itemView.findViewById(R.id.editBtn);
            delete = itemView.findViewById(R.id.deleteBtn);
            complete = itemView.findViewById(R.id.completeBtn);
            complete.setOnClickListener(this);

        }

        void setItemClickListener(MyOnClickListener myOnClickListener){
            this.myOnClickListener = myOnClickListener;
        }

        @Override
        public void onClick(View v) {
            this.myOnClickListener.onClickListener(v, getLayoutPosition());
        }
    }

}
