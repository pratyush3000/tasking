package com.example.doit;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.doit.Model.ToDoModel;
import com.example.doit.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.core.content.ContextCompat;

public class AddNewTask<view> extends BottomSheetDialogFragment {

    public static final String TAG="ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){          //newinstance used to return object of addnewtaskclass
                                                    // so that it can be used in our main activity and we will be able to call the function of newTask from there
        return new AddNewTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState){
        super.onViewCreated(view,savedInstancesState);
        newTaskText=getView().findViewById(R.id.newTaskText);
        newTaskSaveButton=getView().findViewById(R.id.newTaskButton);
        db=new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate=false;
        final Bundle bundle = getArguments();
        if(bundle!=null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                       if(charSequence.toString().equals("")){
                           newTaskSaveButton.setEnabled(false);
                           newTaskSaveButton.setTextColor(Color.GRAY);
                       }
                       else{
                           newTaskSaveButton.setEnabled(true);
                           newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                       }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate=isUpdate;
          newTaskSaveButton.setOnClickListener(new View.OnClickListener(){
              @Override

              public void onClick(View v){
           String text=newTaskText.getText().toString();
                 if(finalIsUpdate){
                     db.update(bundle.getInt("id"),text);
                 }
                 else{
                     ToDoModel task=new ToDoModel();
                     task.setTask(text);
                     task.setStatus(0);
                     db.insertTask(task);
                 }
                 dismiss();
              }
          });

    }
@Override
 public void  onDismiss(DialogInterface dialog){
    Activity activity = getActivity();
    if(activity instanceof DialogCloseListener){
        ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
}
