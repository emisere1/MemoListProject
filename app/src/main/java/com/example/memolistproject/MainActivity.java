package com.example.memolistproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DateSelectionActivity.SaveDateListener {
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
        initTextChangedEvents();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            initMemo(extras.getInt("memoID"));

        }
        else{
            currentMemo = new Memos();
        }

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
    private void initTextChangedEvents(){
        final EditText memoSubject = findViewById(R.id.editMemoTitle);
        memoSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoSubject(memoSubject.getText().toString());

            }
        });
        final EditText memoDescription = findViewById(R.id.editMemoDescription);
        memoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoDescription(memoDescription.getText().toString());

            }
        });
    }
    private void savePriorityToCurrentMemo() {
        RadioButton radioHigh = findViewById(R.id.radioHigh);
        RadioButton radioMedium = findViewById(R.id.radioMedium);
        RadioButton radioLow = findViewById(R.id.radioLow);

        if (radioHigh.isChecked()) {
            currentMemo.setMemoPriority(Memos.PRIORITY_HIGH);
        } else if (radioMedium.isChecked()) {
            currentMemo.setMemoPriority(Memos.PRIORITY_MEDIUM);
        } else if (radioLow.isChecked()) {
            currentMemo.setMemoPriority(Memos.PRIORITY_LOW);
        }
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSaveMemo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePriorityToCurrentMemo();
                if (currentMemo.getMemoDate() == null) {
                    Log.e("MainActivity", "Memo date is not set. Setting current date as default.");
                    currentMemo.setMemoDate(Calendar.getInstance());  // Set current date as default if not set
                }

                MemoQueryActivity ds = new MemoQueryActivity(MainActivity.this);
                try {
                    ds.open();
                    boolean wasSuccessful;
                    if (currentMemo.getMemoID() == -1) {  // If it's a new memo
                        wasSuccessful = ds.insertMemo(currentMemo);
                        if (wasSuccessful) {
                            int newId = ds.getLastMemoId();
                            currentMemo.setMemoID(newId);
                        }
                    } else {  // Updating an existing memo
                        wasSuccessful = ds.updateMemo(currentMemo);
                    }
                    ds.close();
                } catch (Exception e) {
                    Log.e("DatabaseError", "Error in database operation", e);
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

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        EditText editMemoDate = findViewById(R.id.editMemoDate);
        editMemoDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime).toString());
        currentMemo.setMemoDate(selectedTime);
    }

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
    private void initMemo(int memoId) {
        MemoQueryActivity ds = new MemoQueryActivity(MainActivity.this);
        try {
            ds.open();
            if (memoId != -1) {
                currentMemo = ds.getSpecificMemo(memoId);  // Assuming getSpecificMemo is correctly implemented to fetch a memo
            } else {
                currentMemo = new Memos();  // Create a new memo if no ID is provided or ID is -1
            }
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load memo failed", Toast.LENGTH_LONG).show();
            currentMemo = new Memos();  // Ensure currentMemo is not null even if loading fails
        }

        // Update UI elements with memo data
        EditText editMemoSubject = findViewById(R.id.editMemoTitle);
        EditText editMemoDescription = findViewById(R.id.editMemoDescription);
        EditText editMemoDate = findViewById(R.id.editMemoDate);  // Assuming there's an EditText for date
        RadioButton radioHigh = findViewById(R.id.radioHigh);
        RadioButton radioMedium = findViewById(R.id.radioMedium);
        RadioButton radioLow = findViewById(R.id.radioLow);

        editMemoSubject.setText(currentMemo.getMemoSubject());
        editMemoDescription.setText(currentMemo.getMemoDescription());
        if (currentMemo.getMemoDate() != null) {
            editMemoDate.setText(DateFormat.format("MM/dd/yyyy", currentMemo.getMemoDate().getTimeInMillis()).toString());
        }

        // Set radio buttons according to memo priority using if-else
        int priority = currentMemo.getMemoPriority();
        if (priority == Memos.PRIORITY_HIGH) {
            radioHigh.setChecked(true);
        } else if (priority == Memos.PRIORITY_MEDIUM) {
            radioMedium.setChecked(true);
        } else {
            radioLow.setChecked(true);  // This handles PRIORITY_LOW and any undefined state as a default
        }
    }







}