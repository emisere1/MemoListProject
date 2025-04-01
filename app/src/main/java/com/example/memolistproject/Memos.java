package com.example.memolistproject;

public class Memos {
    /*
     private static final String CREATE_TABLE_MEMOS = "create table memos (memo_id integer primary key autoincrement, "
            + "memotitle text not null, memodescription text not null, memodate text, memopriority text);"; // initial memo db records

     */
   private int memoID;
   private String memoSubject;
   private String memoDescription;
   private String memoDate;
   private int memoPriority;

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

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public int getMemoPriority() {
        return memoPriority;
    }

    public void setMemoPriority(int memoPriority) {
        this.memoPriority = memoPriority;
    }

}
