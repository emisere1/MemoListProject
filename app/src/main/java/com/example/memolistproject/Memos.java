package com.example.memolistproject;

import java.util.Calendar;

public class Memos {
    /*
     private static final String CREATE_TABLE_MEMOS = "create table memos (memo_id integer primary key autoincrement, "
            + "memotitle text not null, memodescription text not null, memodate text, memopriority text);"; // initial memo db records

     */
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
//int priotitys for ethan

   private int memoID;
   private String memoSubject;
   private String memoDescription;
   private Calendar memoDate;
   private int memoPriority;

   public Memos() {
       memoID = -1;
       memoDate = Calendar.getInstance();
    }

    public int getMemoID() {
        return memoID;
    }

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public String getMemoSubject() {
        return memoSubject;
    }

    public void setMemoSubject(String memoSubject) {
        this.memoSubject = memoSubject;
    }

    public String getMemoDescription() {
        return memoDescription;
    }

    public void setMemoDescription(String memoDescription) {
        this.memoDescription = memoDescription;
    }

    public Calendar getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(Calendar memoDate) {
        this.memoDate = memoDate;
    }

    public int getMemoPriority() {
        return memoPriority;
    }

    public void setMemoPriority(int memoPriority) {
        this.memoPriority = memoPriority;
    }

}
