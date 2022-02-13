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

public class PickerDialogNumber extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private int num;

    public PickerDialogNumber(int num){
        this.num = num;
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

        View dialog = inflater.inflate(R.layout.picker, null);

        btnConfirm = dialog.findViewById(R.id.btn_pk_confirm);
        btnCancel = dialog.findViewById(R.id.btn_picker_cancel);

        final NumberPicker limitPicker = (NumberPicker) dialog.findViewById(R.id.picker_limit);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PickerDialogNumber.this.getDialog().cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, limitPicker.getValue(), 0, 0);
                PickerDialogNumber.this.getDialog().cancel();
            }
        });

        limitPicker.setMinValue(1);
        limitPicker.setMaxValue(20);
        limitPicker.setValue(num);

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

