package com.example.memolistproject;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DatabaseActivity extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MemoListProject.db";
    private static final int DATABASE_VERSION = 1;
    /*
    field values
    memo title string, memo description string, priority selection num
    date ( from calander ) editable)
     */
    private static final String CREATE_TABLE_MEMOS = "create table memos (memo_id integer primary key autoincrement, "
            + "memosubjecttext not null, memodescription text not null, memodate text, memopriority int);"; // initial memo db records
    public DatabaseActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMOS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseActivity.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS memos");
        onCreate(db);
    }
}