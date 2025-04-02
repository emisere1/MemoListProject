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
import android.widget.RadioGroup;
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

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSaveMemo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioGroupPriority = findViewById(R.id.prioritygroup);
                int selectedPriorityId = radioGroupPriority.getCheckedRadioButtonId();

                int priority = Memos.PRIORITY_LOW;
                if (selectedPriorityId == R.id.radioHigh) {
                    priority = Memos.PRIORITY_HIGH;
                } else if (selectedPriorityId == R.id.radioMedium) {
                    priority = Memos.PRIORITY_MEDIUM;
                } else if (selectedPriorityId == R.id.radioLow) {
                    priority = Memos.PRIORITY_LOW;
                }

                currentMemo.setMemoPriority(priority);

                MemoQueryActivity ds = new MemoQueryActivity(MainActivity.this);
                try {
                    ds.open();
                    boolean wasSuccessful;
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
                    Log.e("DatabaseError", "Error in database operation", e);
                }
            }
        });
    }


    private void initTextChangedEvents() {
        final EditText etMemoSubject = findViewById(R.id.editMemoTitle);
        etMemoSubject.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoSubject(etMemoSubject.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText etMemoDescription = findViewById(R.id.editMemoDescription);
        etMemoDescription.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoDescription(etMemoDescription.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
                currentMemo = ds.getSpecificMemo(memoId);
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Load memo failed", Toast.LENGTH_LONG).show();
            currentMemo = new Memos();
        }

        EditText editMemoSubject = findViewById(R.id.editMemoTitle);
        EditText editMemoDescription = findViewById(R.id.editMemoDescription);
        EditText editMemoDate = findViewById(R.id.editMemoDate);
        RadioButton radioHigh = findViewById(R.id.radioHigh);
        RadioButton radioMedium = findViewById(R.id.radioMedium);
        RadioButton radioLow = findViewById(R.id.radioLow);

        editMemoSubject.setText(currentMemo.getMemoSubject());
        editMemoDescription.setText(currentMemo.getMemoDescription());
        if (currentMemo.getMemoDate() != null) {
            editMemoDate.setText(DateFormat.format("MM/dd/yyyy", currentMemo.getMemoDate().getTimeInMillis()).toString());
        }

        int priority = currentMemo.getMemoPriority();
        if (priority == Memos.PRIORITY_HIGH) {
            radioHigh.setChecked(true);
        } else if (priority == Memos.PRIORITY_MEDIUM) {
            radioMedium.setChecked(true);
        } else {
            radioLow.setChecked(true);
        }
    }
}