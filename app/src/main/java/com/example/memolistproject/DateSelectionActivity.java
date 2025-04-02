package com.example.memolistproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateSelectionActivity extends DialogFragment {

    Calendar selectedDate;

    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DateSelectionActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dateselectionactivity, container);

        if (getDialog() != null) {
            getDialog().setTitle("Select Date");
        }
        selectedDate = Calendar.getInstance();

        final CalendarView cv = view.findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                selectedDate.set(year, month, day);
            }
        });

        Button saveDateButton = view.findViewById(R.id.buttonSelect);
        saveDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(selectedDate);
            }
        });

        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void saveItem(Calendar selectedTime) {
        if (getActivity() instanceof SaveDateListener) {
            SaveDateListener activity = (SaveDateListener) getActivity();
            activity.didFinishDatePickerDialog(selectedTime);
            getDialog().dismiss();
        }
    }
}