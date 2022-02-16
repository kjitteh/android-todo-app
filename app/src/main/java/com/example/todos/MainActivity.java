package com.example.todos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TodoAdapter mAdapter;
    private List<Todo> mTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mTodoList = new ArrayList<>();
        mTodoList.add(new Todo("My first todo"));
        mTodoList.add(new Todo("My first todo"));
        mTodoList.add(new Todo("Finish assignments before school resumes"));
        mTodoList.add(new Todo("Buy a new dairy for recording journals"));
        mTodoList.add(new Todo("My first todo thanks"));
        mTodoList.add(new Todo("My first todos"));
        mTodoList.add(new Todo("My first todo all the way"));


        mAdapter = new TodoAdapter(this, mTodoList);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }



    private class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {
        private List<Todo> mTodos;
        private Context mContext;

        public TodoAdapter(Context context, List<Todo> todos) {
            mContext = context;
            mTodos = todos;
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
    }

    private class TodoHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_todo);
        }
        public void bind(Todo todo) {
            mCheckBox.setChecked(todo.isChecked());
            mCheckBox.setText(todo.getTitle());
        }
    }
}