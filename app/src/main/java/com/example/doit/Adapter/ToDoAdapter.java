package com.example.doit.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.doit.AddNewTask;
import com.example.doit.MainActivity;
import com.example.doit.Model.ToDoModel;
import com.example.doit.R;
import com.example.doit.Utils.DatabaseHandler;
//What is adapter in Android with example?
//An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set. See also: ArrayAdapter.
import java.security.AccessController;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;
   //cslled constructor
    public ToDoAdapter(DatabaseHandler db,MainActivity activity){
        this.db=db;
        this.activity=activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int ViewType){
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,
                parent,
                false);

        return new ViewHolder(itemView);
    }

          public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
           db.openDatabase();
      final   ToDoModel item=todoList.get(position);
                holder.task.setText(item.getTask());
                holder.task.setChecked(toBoolean(item.getStatus()));
                holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         if(isChecked){
                             db.updateStatus(item.getId(),1);
                         }
                         db.updateStatus(item.getId(),0);
                    }
                });
          }
          public int getItemCount(){
                 return todoList.size();
          }
          private boolean toBoolean(int n){
        return n!=0;
          }

          public void setTasks(List<ToDoModel>todoList){
        this.todoList=todoList;
        notifyDataSetChanged();
          }
          public Context getContext(){
        return activity;
          }
          public void deleteItem(int position){
        ToDoModel item= todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
          }
          public void editItem(int position){
           ToDoModel item = todoList.get(position);
           Bundle bundle = new Bundle();
           bundle.putInt("Id",item.getId());
           bundle.putString("task",item.getTask());
              AddNewTask fragment=new AddNewTask();
              fragment.setArguments(bundle);
              fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);

          }
    public static class ViewHolder extends RecyclerView.ViewHolder{
     CheckBox task;
       ViewHolder(View view){
           super(view);
           task=view.findViewById(R.id.todoCheckBox);

       }
    }
}
