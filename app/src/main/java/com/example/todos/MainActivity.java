package com.example.todos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private List<Todo> mTodoList;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private TodoAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.openDataBase();

        mRecyclerView = findViewById(R.id.rv_todo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFloatingActionButton = findViewById(R.id.fab_add);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTodoFragment.newInstance().show(getSupportFragmentManager(), AddTodoFragment.TAG);
            }
        });

        updateUI();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TodoItemTouchHelper(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void updateUI() {
        List<Todo> todos = mDatabaseHelper.readTodos();

        if (mAdapter == null) {
            mAdapter = new TodoAdapter(this, todos, mDatabaseHelper);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTodos(todos);
        }

    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        List<Todo> todos = mDatabaseHelper.readTodos();
        Collections.reverse(todos);
        mAdapter.setTodos(todos);
        mAdapter.notifyDataSetChanged();
    }


    class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {
        private Context mContext;
        private List<Todo> mTodos;
        private DatabaseHelper mHelper;

        public TodoAdapter(Context context, List<Todo> todos, DatabaseHelper helper) {
            mContext = context;
            mTodos = todos;
            mHelper = helper;
        }


        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_todo, parent, false);
            return new TodoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
            Todo todo = mTodos.get(position);
            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return mTodos.size();
        }

        public void setTodos(List<Todo> todos) {
            mTodos = todos;
        }

        public MainActivity getContext() {
            return (MainActivity) mContext;
        }

        public void deleteItem(int position) {
            Todo todo = mTodos.get(position);
            mHelper.deleteTodo(todo);
            mTodos.remove(position);
            notifyItemChanged(position);

        }

        public void editItem(int position) {
            Todo todo = mTodos.get(position);
            Bundle args = new Bundle();
            args.putString("uuidString", todo.getId().toString());
            args.putString("title", todo.getTitle());
            AddTodoFragment fragment = new AddTodoFragment();
            fragment.setArguments(args);
            fragment.show(getContext().getSupportFragmentManager(), AddTodoFragment.TAG);
        }
    }


    class TodoHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;

        public TodoHolder(View view) {
            super(view);
            mCheckBox = view.findViewById(R.id.cb_todo);
        }

        public void bind(Todo todo) {
            mCheckBox.setText(todo.getTitle());
            mCheckBox.setChecked(todo.isDone());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    todo.setDone(b);
                    mDatabaseHelper.updateTodo(todo);
                }
            });
        }
    }
}