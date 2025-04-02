package com.example.memolistproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;


public class MainActivity extends AppCompatActivity {
    private Memos currentMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        settingsButtonPressed();
        listMemoButton();
        changeDateButton();
        initChangeDateButton();
        initSaveButton();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.buttonDate);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DateSelectionActivity datePickerDialog = new DateSelectionActivity();
                datePickerDialog.show(fm, "DatePick");
            }

        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSaveMemo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                MemoQueryActivity ds = new MemoQueryActivity(MainActivity.this);
                try {
                    ds.open();
                    if (currentMemo.getMemoID() == -1) {
                        wasSuccessful = ds.insertMemo(currentMemo);

                        if (wasSuccessful) {
                            int newId = ds.getLastMemoId();
                            currentMemo.setMemoID(newId);
                        }

                    } else {
                        wasSuccessful = ds.updateMemo(currentMemo);
                    }
                    ds.close();
                } catch (Exception e) {
                    wasSuccessful = false;
                }
            }
        });
    }

    public void changeDateButton() {
        Button changeDateButton = findViewById(R.id.buttonDate);
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DateSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void settingsButtonPressed(){ // not sure what the settings button is for but this is what i think it is for now
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SortActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });

    }

   /* public void addMemoButton() {
        ImageButton addMemoButton = findViewById(R.id.addMemoButton);
        addMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MemoEntryActivity.this, MemoEntryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    commented out for the current activity so that the button does not do anything
    */

    public void listMemoButton() {
        ImageButton listMemoButton = findViewById(R.id.ListButton);
        listMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


}

