package com.example.todos;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddTodoFragment extends BottomSheetDialogFragment {
    public static final String TAG = "AddTodoFragment";

    private EditText mEditTodo;
    private Button mBtnSave;
    private boolean isUpdate = false;

    private DatabaseHelper mDatabaseHelper;

    public static AddTodoFragment newInstance() {
        return new AddTodoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_todo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditTodo = Objects.requireNonNull(getView()).findViewById(R.id.et_add);
        mBtnSave = getView().findViewById(R.id.btn_save);
        mBtnSave.setEnabled(false);
        mBtnSave.setTextColor(Color.GRAY);

        mDatabaseHelper = new DatabaseHelper(getActivity());
        mDatabaseHelper.openDataBase();

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String title = bundle.getString("title");
            mEditTodo.setText(title);
        }

        mEditTodo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    mBtnSave.setEnabled(false);
                    mBtnSave.setTextColor(Color.GRAY);
                } else {
                    mBtnSave.setEnabled(true);
                    mBtnSave.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdate) {
                    String uuidString = bundle.getString("uuidString");
                    Todo todo = mDatabaseHelper.readTodo(uuidString);
                    todo.setTitle(mEditTodo.getText().toString());
                    mDatabaseHelper.updateTodo(todo);
                    Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();
                } else {
                    String todoTitle = mEditTodo.getText().toString();
                    Todo todo = new Todo(todoTitle);
                    mDatabaseHelper.createTodo(todo);
                    Toast.makeText(getActivity(), "new todo added", Toast.LENGTH_SHORT).show();
                }

                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
