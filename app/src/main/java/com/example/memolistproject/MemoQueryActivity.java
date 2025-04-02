package com.example.memolistproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class MemoQueryActivity {

    private SQLiteDatabase database;
    private DatabaseActivity dbHelper;

    public ArrayList<String> getMemoSubjectName() {
        ArrayList<String> memoSubjects = new ArrayList<>();
        try {
            String query = "SELECT memosubjecttext FROM memos"; // Corrected field name
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    memoSubjects.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching memo subjects", e);
        }
        return memoSubjects;
    }

    public Memos getSpecificContact(int memoID) {
        Memos memos = new Memos();
        String query = "SELECT * FROM memos WHERE memo_id =" + memoID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            memos = new Memos();
            memos.setMemoID(cursor.getInt(0));
            memos.setMemoSubject(cursor.getString(1));
            memos.setMemoDescription(cursor.getString(2));
            memos.setMemoPriority(cursor.getInt(3));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
            memos.setMemoDate(calendar);

            cursor.close();
        }
        return memos;
    }

    public ArrayList<Memos> getMemos(String sortField, String sortOrder) {
        ArrayList<Memos> memos = new ArrayList<>();
        try {
            String query = "SELECT * FROM memos ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Memos newMemo = new Memos();
                    newMemo.setMemoID(cursor.getInt(0));
                    newMemo.setMemoSubject(cursor.getString(1));
                    newMemo.setMemoDescription(cursor.getString(2));
                    newMemo.setMemoPriority(cursor.getInt(3));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(cursor.getString(4)));
                    newMemo.setMemoDate(calendar);
                    memos.add(newMemo);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Database Error", "Error retrieving memos", e);
        }
        return memos;
    }

    public MemoQueryActivity(Context context) {
        dbHelper = new DatabaseActivity(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertMemo(Memos memo) {
        boolean didSucceed = false;
        try {
            open();
            ContentValues initialValues = new ContentValues();
            initialValues.put("memosubjecttext", memo.getMemoSubject());
            initialValues.put("memodescription", memo.getMemoDescription());
            initialValues.put("memopriority", memo.getMemoPriority());
            initialValues.put("memodate", String.valueOf(memo.getMemoDate().getTimeInMillis()));

            didSucceed = database.insert("memos", null, initialValues) > 0;
            close();
        } catch (Exception e) {
            Log.e("Database Error", "Error inserting memo", e);
        }
        return didSucceed;
    }

    public boolean updateMemo(Memos memo) {
        boolean didSucceed = false;
        try {
            open();
            ContentValues updateValues = new ContentValues();
            updateValues.put("memosubjecttext", memo.getMemoSubject());
            updateValues.put("memodescription", memo.getMemoDescription());
            updateValues.put("memopriority", memo.getMemoPriority());
            updateValues.put("memodate", String.valueOf(memo.getMemoDate().getTimeInMillis()));

            didSucceed = database.update("memos", updateValues, "memo_id=?", new String[]{String.valueOf(memo.getMemoID())}) > 0;
            close();
        } catch (Exception e) {
            Log.e("Database Error", "Error updating memo", e);
        }
        return didSucceed;
    }

    public int getLastMemoId() {
        int lastId = -1;
        try {
            open();
            String query = "SELECT MAX(memo_id) FROM memos";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                lastId = cursor.getInt(0);
            }
            if (cursor != null) cursor.close();
            close();
        } catch (Exception e) {
            Log.e("Database Error", "Error retrieving last memo ID", e);
            lastId = -1;
        }
        return lastId;
    }

}
