package com.example.memolistproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private EditText etSubject;

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

    private void initDateSettings() {
        String sortOrder = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldDate", "ASC");
        Switch sDate = findViewById(R.id.switchDate);
        sDate.setChecked("ASC".equals(sortOrder));
    }

    private void initPrioritySettings() {
        String sortPriority = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldPriority", "ASC");
        RadioButton rbLow = findViewById(R.id.rbLow);
        RadioButton rbMedium = findViewById(R.id.rbMedium);
        RadioButton rbHigh = findViewById(R.id.rbHigh);
        if ("Low".equals(sortPriority)) {
            rbLow.setChecked(true);
        } else if ("Medium".equals(sortPriority)) {
            rbMedium.setChecked(true);
        } else if ("High".equals(sortPriority)) {
            rbHigh.setChecked(true);
        }
    }

    private void updateSortPreferences(String field, String order) {
        getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE)
                .edit()
                .putString(field, order)
                .apply();
    }

    private void initSortDateClick() {
        Switch sOrder = findViewById(R.id.switchDate);
        sOrder.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSortPreferences("sortFieldDate", isChecked ? "ASC" : "DESC");
            }
        });
    }

    private void initSortPriorityClick() {
        RadioGroup rgPriority = findViewById(R.id.rgPriorty);
        rgPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLow) {
                    updateSortPreferences("sortFieldPriority", "Low");
                } else if (checkedId == R.id.rbMedium) {
                    updateSortPreferences("sortFieldPriority", "Medium");
                } else if (checkedId == R.id.rbHigh) {
                    updateSortPreferences("sortFieldPriority", "High");
                }
            }
        });
    }

    private void initSubjectSettings() {
        EditText etSubject = findViewById(R.id.editTextSBTD);
        String subject = getSharedPreferences("MemoListPreferences", Context.MODE_PRIVATE).getString("sortFieldSubject", "");
        etSubject.setText(subject);
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                updateSortPreferences("sortFieldSubject", etSubject.getText().toString());
            }
        });

    }
}