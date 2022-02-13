package com.example.everywheregym;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PickerDialogMinute extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private int minute;
    private int hour;

    public PickerDialogMinute(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    Button btnConfirm;
    Button btnCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.picker_minute, null);

        btnConfirm = dialog.findViewById(R.id.btn_picker_minute_confirm);
        btnCancel = dialog.findViewById(R.id.btn_picker_minute_cancel);

        final NumberPicker minutePicker = (NumberPicker) dialog.findViewById(R.id.picker_minute_limit);
        final NumberPicker hourPicker = (NumberPicker) dialog.findViewById(R.id.picker_hour_limit);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PickerDialogMinute.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, hourPicker.getValue(), minutePicker.getValue(), 0);
                PickerDialogMinute.this.getDialog().cancel();
            }
        });

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(minute);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(3);
        hourPicker.setValue(hour);

        builder.setView(dialog)
        // Add action buttons
        /*
        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MyYearMonthPickerDialog.this.getDialog().cancel();
            }
        })
        */
        ;

        return builder.create();
    }
}

