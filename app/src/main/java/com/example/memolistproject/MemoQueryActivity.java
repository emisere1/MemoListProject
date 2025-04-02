package com.example.memolistproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
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

    public Memos getSpecificMemo(int memoID) {
        Memos memos = new Memos();
        String query = "SELECT * FROM memos WHERE memo_id = " + memoID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            memos.setMemoID(cursor.getInt(0));
            memos.setMemoSubject(cursor.getString(1));
            memos.setMemoDescription(cursor.getString(2));
            memos.setMemoPriority(cursor.getInt(4));  // Adjusted to correct column index for priority
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(3));  // Corrected to fetch the date as long milliseconds
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
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
                    newMemo.setMemoDate(calendar);
                    newMemo.setMemoPriority(cursor.getInt(4));
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
        ContentValues initialValues = new ContentValues();
        try {
            open();
            initialValues.put("memoSubject", memo.getMemoSubject());
            initialValues.put("memoDescription", memo.getMemoDescription());
            initialValues.put("memoPriority", memo.getMemoPriority());
            initialValues.put("memoDate", memo.getMemoDate().getTimeInMillis());

            long result = database.insert("memos", null, initialValues);
            didSucceed = result > 0;
            if (!didSucceed) {
                Log.e("DatabaseError", "Failed to insert memo, result: " + result);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting memo", e);
        } finally {
            close();
        }
        return didSucceed;
    }

    public boolean updateMemo(Memos memo) {
        if (memo.getMemoDate() == null) {
            Log.e("MemoDB", "Attempt to update memo without a date set");
            return false;
        }

        boolean didSucceed = false;
        try {
            open();
            ContentValues updateValues = new ContentValues();
            updateValues.put("memoSubject", memo.getMemoSubject());
            updateValues.put("memoDescription", memo.getMemoDescription());
            updateValues.put("memoPriority", memo.getMemoPriority());
            updateValues.put("memoDate", memo.getMemoDate().getTimeInMillis());

            String whereClause = "memo_id=?";
            String[] whereArgs = new String[] { String.valueOf(memo.getMemoID()) };

            Log.d("MemoQueryActivity", "Updating memo with ID: " + memo.getMemoID());
            Log.d("MemoQueryActivity", "Memo Subject: " + memo.getMemoSubject());
            Log.d("MemoQueryActivity", "Memo Description: " + memo.getMemoDescription());
            Log.d("MemoQueryActivity", "Memo Date: " + DateFormat.format("MM/dd/yyyy", memo.getMemoDate()).toString());
            Log.d("MemoQueryActivity", "Memo Priority: " + memo.getMemoPriority());

            // Verify if the memo_id exists in the database
            Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM memos WHERE memo_id=?", whereArgs);
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                Log.d("MemoQueryActivity", "Memo ID exists in database: " + (count > 0));
                cursor.close();
            }

            int rowsAffected = database.update("memos", updateValues, whereClause, whereArgs);
            didSucceed = rowsAffected > 0;
            if (didSucceed) {
                Log.d("MemoQueryActivity", "Memo updated successfully, rows affected: " + rowsAffected);
            } else {
                Log.e("MemoQueryActivity", "Failed to update memo, rows affected: " + rowsAffected);
            }
            close();
        } catch (Exception e) {
            Log.e("Database Error", "Error updating memo", e);
            didSucceed = false;
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
