package com.example.memolistproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MemoList extends AppCompatActivity {

    ArrayList<Memos> memos;
    RecyclerView memoList;
    MemoAdapter memoAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int memoID = memos.get(position).getMemoID();
            Intent intent = new Intent(MemoList.this, MainActivity.class);
            intent.putExtra("memoID", memoID);
            startActivity(intent);
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_list);
        settingsButtonPressed();
        addMemoButton();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        memoList = findViewById(R.id.rvMemo);
        memoList.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(memoList);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            memoAdapter.removeItem(position);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rvMemo), "Memo Deleted", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadMemos();
    }

    private void loadMemos() {
        String sortDate = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldDate", "MemoDate");
        if (sortDate == "ASC"){
            sortDate = "MemoDate ASC";
        }
        else{
            sortDate = "MemoDate DESC";
        }
        String sortPriority = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldPriority", "MemoPriority");
        switch (sortPriority) {
            case "High": // High
                sortPriority = "MemoPriority ASC";
                break;
            case "Medium": // Medium
                sortPriority = "CASE WHEN MemoPriority = 2 THEN 0 ELSE MemoPriority END ASC, MemoPriority DESC";
                break;
            case "Low": // Low
                sortPriority = "MemoPriority DESC";
                break;
            default:
                sortPriority = "MemoPriority ASC";
                break;
        }
        String sortSubject = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldSubject", "MemoSubject");
        String sortOrder = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldOrder", "ASC");
        MemoQueryActivity db = new MemoQueryActivity(this);

        try {
            db.open();
            memos = db.getMemos(sortDate, sortPriority, sortSubject, sortOrder);
            db.close();
            if (memos.size() > 0) {
                memoAdapter = new MemoAdapter(memos, this);
                memoAdapter.setOnItemClickListener(onItemClickListener);
                memoList.setAdapter(memoAdapter);
            } else {
                Intent intent = new Intent(MemoList.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    public void settingsButtonPressed() {
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MemoList.this, SortActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void addMemoButton() {
        ImageButton addMemoButton = findViewById(R.id.addMemoButton);
        addMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MemoList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}

//import android.app.ListActivity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class MemoList extends AppCompatActivity {
//
//    ArrayList<Memos> memos;
//    RecyclerView memoList;
//    MemoAdapter memoAdapter;
//
//    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
//            int position = viewHolder.getAdapterPosition();
//            int memoID = memos.get(position).getMemoID();
//            Intent intent = new Intent(MemoList.this, MainActivity.class);
//            intent.putExtra("memoID", memoID);
//            startActivity(intent);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_memo_list);
//        settingsButtonPressed();
//        addMemoButton();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    public void onResume(){
//        super.onResume();
//        String sortDate = getSharedPreferences("MemoListPreferences" , Context.MODE_PRIVATE).getString("sortfieldDate", "MemoDate");
//        String sortPriority = getSharedPreferences("MemoListPreferences" , Context.MODE_PRIVATE).getString("sortfieldPriority", "MemoPriority");
//        String sortSubject = getSharedPreferences("MemoListPreferences" , Context.MODE_PRIVATE).getString("sortfieldSubject", "MemoSubject");
//        String sortOrder = getSharedPreferences("MemoListPreferences" , Context.MODE_PRIVATE).getString("sortorder", "ASC");
//        MemoQueryActivity db = new MemoQueryActivity(this);
//
//        try{
//            db.open();
//            memos = db.getMemos(sortDate, sortPriority, sortSubject, sortOrder);
//            db.close();
//            if (memos.size() > 0 ){
//                memoList = findViewById(R.id.rvMemo);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//                memoList.setLayoutManager(layoutManager);
//                MemoAdapter memoAdapter = new MemoAdapter(memos, this);
//                memoAdapter.setOnItemClickListener(onItemClickListener);
//                memoList.setAdapter(memoAdapter);
//            }
//            else{
//                Intent intent = new Intent(MemoList.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//        catch (Exception e){
//            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void settingsButtonPressed(){ // not sure what the settings button is for but this is what i think it is for now
//        ImageButton settingsButton = findViewById(R.id.settingsButton);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(MemoList.this, SortActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//
//        });
//
//    }
//
//    public void addMemoButton() {
//        ImageButton addMemoButton = findViewById(R.id.addMemoButton);
//        addMemoButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(MemoList.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//    }

    /*
    public void listMemoButton() {
        ImageButton listMemoButton = findViewById(R.id.ListButton);
        listMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MemoEntryActivity.this, MemoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    commented out for the current activity so that the button does not do anything
     */


