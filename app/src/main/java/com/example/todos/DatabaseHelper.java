package com.example.todos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "todoBase.db";
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TodoTable.NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodoTable.Cols.UUID + ", " +
                TodoTable.Cols.TITLE + ", " +
                TodoTable.Cols.DONE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /** CRUD: create*/
    public void createTodo(Todo todo) {
        ContentValues values = getContentValues(todo);
        mDatabase.insert(TodoTable.NAME, null, values);

    }

    /** CRUD: read specific */
    public Todo readTodo(String uuidString) {
        try (TodoCursorWrapper cursorWrapper = queryTodos(
                TodoTable.Cols.UUID + " = ?", new String[] {uuidString})) {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToNext();
            return cursorWrapper.getTodo();
        }
    }

    /** CRUD: read all */
    public List<Todo> readTodos() {
        List<Todo> todos = new ArrayList<>();

        try (TodoCursorWrapper cursorWrapper = queryTodos(null, null)) {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()) {
                todos.add(cursorWrapper.getTodo());
                cursorWrapper.moveToNext();
            }
        }

        return todos;
    }

    /** CRUD: update */
    public void updateTodo(Todo todo) {
        String uuidString = todo.getId().toString();
        ContentValues values = getContentValues(todo);

        mDatabase.update(TodoTable.NAME, values,
                TodoTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    /** CRUD: delete */
    public void deleteTodo(Todo todo) {
        String uuidString = todo.getId().toString();
        mDatabase.delete(TodoTable.NAME,
                TodoTable.Cols.UUID + " = ?", new String[] {uuidString});
    }


    /** three helper methods */

    public void openDataBase() {
        mDatabase = this.getReadableDatabase();
    }

    private TodoCursorWrapper queryTodos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(TodoTable.NAME, null, whereClause, whereArgs,
                null, null, null);

        return new TodoCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Todo todo) {
        ContentValues values = new ContentValues();
        values.put(TodoTable.Cols.UUID, todo.getId().toString());
        values.put(TodoTable.Cols.TITLE, todo.getTitle());
        values.put(TodoTable.Cols.DONE, todo.isDone() ? 1 : 0);

        return values;
    }

}



/** cursor wrapper */
class TodoCursorWrapper extends CursorWrapper {

    public TodoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Todo getTodo() {
        String uuidString = getString(getColumnIndex(TodoTable.Cols.UUID));
        String title = getString(getColumnIndex(TodoTable.Cols.TITLE));
        int isDone = getInt(getColumnIndex(TodoTable.Cols.DONE));

        Todo todo = new Todo(UUID.fromString(uuidString));
        todo.setTitle(title);
        todo.setDone(isDone == 1);

        return todo;
    }
}


/** database schema */
class TodoTable {
    public static final String NAME = "todos";

    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DONE = "done";
    }
}
