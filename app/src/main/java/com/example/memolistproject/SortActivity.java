package com.example.memolistproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SortActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sort);
        initDateSettings();
        initPrioritySettings();
        initSubjectSettings();
        initSortDateClick();
        initSortPriorityClick();
        addMemoButton();
        listMemoButton();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
/*
    public void settingsButtonPressed(){ // not sure what the settings button is for but this is what i think it is for now
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SortActivity.this, SortActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });

    }


 */
    public void addMemoButton() {
        ImageButton addMemoButton = findViewById(R.id.addMemoButton);
        addMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SortActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    public void listMemoButton() {
        ImageButton listMemoButton = findViewById(R.id.ListButton);
        listMemoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SortActivity.this, MemoList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }




    private void initDateSettings(){
        String sortBy = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortField", "MemoDate");
        String sortOrder = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortOrder", "ASC");
        Switch sDate = findViewById(R.id.switchDate);
        if (sDate.isChecked()){
            sDate.setChecked(true);
        } else {
            sDate.setChecked(false);
        }
    }
    private void initPrioritySettings(){
        String sortBy = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortField", "MemoPriority");
        String sortOrder = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortOrder", "ASC");
        RadioButton rblow = findViewById(R.id.rbLow);
        RadioButton rbMedium = findViewById(R.id.rbMedium);
        RadioButton rbHigh = findViewById(R.id.rbHigh);
        if (rblow.isChecked()){
            rblow.setChecked(true);
        } else if (rbMedium.isChecked()){
            rbMedium.setChecked(true);
        } else if (rbHigh.isChecked()){
            rbHigh.setChecked(true);
        }
    }
    // Update the SharedPreferences to store multiple sorting criteria
    private void updateSortPreferences(String field, String order) {
        getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE)
                .edit()
                .putString(field, order)
                .apply();
    }

    // Modify the initSortDateClick method
    private void initSortDateClick() {
        Switch sOrder = findViewById(R.id.switchDate);
        sOrder.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateSortPreferences("sortFieldDate", "ASC");
                } else {
                    updateSortPreferences("sortFieldDate", "DESC");
                }
            }
        });
    }

    // Modify the initSortPriorityClick method
    private void initSortPriorityClick() {
        RadioGroup rgPriority = findViewById(R.id.rgPriorty);
        rgPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbLow = findViewById(R.id.rbLow);
                RadioButton rbMedium = findViewById(R.id.rbMedium);
                RadioButton rbHigh = findViewById(R.id.rbHigh);
                if (rbLow.isChecked()) {
                    updateSortPreferences("sortFieldPriority", "ASC");
                } else if (rbMedium.isChecked()) {
                    updateSortPreferences("sortFieldPriority", "ASC");
                } else if (rbHigh.isChecked()) {
                    updateSortPreferences("sortFieldPriority", "ASC");
                }
            }
        });
    }

    // Modify the initSubjectSettings method
    private void initSubjectSettings() {
        EditText etSubject = findViewById(R.id.editTextSBTD);
        String subject = etSubject.getText().toString();
        updateSortPreferences("sortFieldSubject", subject);
    }
}