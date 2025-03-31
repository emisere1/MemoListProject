package com.example.memolistproject;

public class Memos {
    /*
     private static final String CREATE_TABLE_MEMOS = "create table memos (memo_id integer primary key autoincrement, "
            + "memotitle text not null, memodescription text not null, memodate text, memotext text);"; // initial memo db records

     */
   private int memoID;
   private String memoTitle;
   private String memoDescription;
   private String memoDate;
   private String memoText;

    public int getMemoID() {
        return memoID;
    }

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public String getMemoTitle() {
        return memoTitle;
    }

    public void setMemoTitle(String memoTitle) {
        this.memoTitle = memoTitle;
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

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

}
