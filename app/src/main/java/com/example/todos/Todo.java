package com.example.todos;

import java.util.UUID;

public class Todo {
    private UUID mId;
    private boolean mChecked;
    private String mTitle;

    public Todo(String title) {
        mId = UUID.randomUUID();
        mChecked = false;
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }


    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
