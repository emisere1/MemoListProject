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
            String query = "SELECT memosubject FROM memos"; // Corrected field name
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




//    public ArrayList<Memos> getMemos(String sortDate, String sortPriority, String sortSubject, String sortBy) {
//        ArrayList<Memos> memos = new ArrayList<>();
//        try {
//            String query = "SELECT * FROM memos ORDER BY " + sortDate + " " + sortPriority;
//            Cursor cursor = database.rawQuery(query, null);
//            if (cursor.moveToFirst()) {
//                while (!cursor.isAfterLast()) {
//                    Memos newMemo = new Memos();
//                    newMemo.setMemoID(cursor.getInt(0));
//                    newMemo.setMemoSubject(cursor.getString(1));
//                    newMemo.setMemoDescription(cursor.getString(2));
//                    newMemo.setMemoPriority(cursor.getInt(3));
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(Long.parseLong(cursor.getString(4)));
//                    newMemo.setMemoDate(calendar);
//                    memos.add(newMemo);
//                    cursor.moveToNext();
//                }
//            }
//            cursor.close();
//        } catch (Exception e) {
//            Log.e("Database Error", "Error retrieving memos", e);
//        }
//        return memos;
//    }

    public ArrayList<Memos> getMemos(){
        ArrayList<Memos> memos = new ArrayList<Memos>();
        try{
            String query = "Select * from memos";
            Cursor cursor = database.rawQuery(query, null);
            Memos newMemo;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newMemo = new Memos();
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
            cursor.close();
        }
        catch (Exception e){
            memos = new ArrayList<Memos>();
        }
        return memos;
    }
    public ArrayList<Memos> getMemos(String sortDate, String sortPriority, String sortSubject, String sortOrder) {
        ArrayList<Memos> memos = new ArrayList<>();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM memos");

            if (sortSubject != null && !sortSubject.isEmpty()) {
                queryBuilder.append(" WHERE MemoSubject LIKE '%").append(sortSubject).append("%' OR MemoDescription LIKE '%").append(sortSubject).append("%'");
            }
            // Add sorting criteria
            queryBuilder.append(" ORDER BY ");
            if (sortPriority != null && !sortPriority.isEmpty()) {
                queryBuilder.append(sortPriority).append(", ");
            }
            if (sortDate != null && !sortDate.isEmpty()) {
                queryBuilder.append(sortDate).append(" ");
            }


           // queryBuilder.append(" ").append(sortOrder);

            // Remove the last comma and space
            if (queryBuilder.toString().endsWith(", ")) {
                queryBuilder.setLength(queryBuilder.length() - 2);
            }

            String query = queryBuilder.toString();
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
        try {
            open();  // Ensure this method correctly opens the database

            ContentValues initialValues = new ContentValues();
            initialValues.put("memoSubject", memo.getMemoSubject());
            initialValues.put("memoDescription", memo.getMemoDescription());
            initialValues.put("memoPriority", memo.getMemoPriority());
            initialValues.put("memoDate", memo.getMemoDate().getTimeInMillis()); // Ensure the date is converted to milliseconds

            didSucceed = database.insert("memos", null, initialValues) > 0;
            close();  // Ensure this method correctly closes the database
        } catch (Exception e) {
            Log.e("Database Error", "Error inserting memo", e);
            didSucceed = false;
        }
        return didSucceed;
    }



    public boolean updateMemo(Memos memo) {
        boolean didSucceed = false;
        try {
            open();  // Ensure this method correctly opens the database

            if (memo.getMemoDate() == null) {
                Log.e("MemoDB", "Attempt to update memo without a date set");
                close();
                return false;  // Exit if the date is not set
            }

            ContentValues updateValues = new ContentValues();
            updateValues.put("memoSubject", memo.getMemoSubject());
            updateValues.put("memoDescription", memo.getMemoDescription());
            updateValues.put("memoPriority", memo.getMemoPriority());
            updateValues.put("memoDate", memo.getMemoDate().getTimeInMillis());

            String whereClause = "memo_id=?";
            String[] whereArgs = new String[] { String.valueOf(memo.getMemoID()) };

            didSucceed = database.update("memos", updateValues, whereClause, whereArgs) > 0;
            close();  // Ensure this method correctly closes the database
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
