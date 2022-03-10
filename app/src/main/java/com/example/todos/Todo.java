package com.example.todos;

import java.util.UUID;

public class Todo {
    private UUID mId;
    private String mTitle;
    private boolean mDone;

    public Todo(String title) {
        mId = UUID.randomUUID();
        mTitle = title;
        mDone = false;
    }

    public Todo(UUID uuid) {
        mId = uuid;
        mDone = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }
}
