package com.nonme.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.nonme.drawandpaint.R;
import com.nonme.views.SizeView;

public class BrushSizePickerFragment extends DialogFragment {
    public interface NumberPickerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    private NumberPickerDialogListener mListener;

    private NumberPicker mNumberPicker;
    private SizeView mSizeView;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private Context mContext;

    private int mValue;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.number_picker, null);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        String[] values = new String[20];
        for(int i = 1; i <= 20; ++i)
            values[i-1] = String.valueOf(i);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(20);
        mNumberPicker.setDisplayedValues(values);
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mSizeView.setSize(5*picker.getValue());
                mValue = 5*picker.getValue();
            }
        });
        mSizeView = (SizeView) view.findViewById(R.id.brush_size_view);
        mSizeView.setItem(SizeView.CIRCLE);
        mPositiveButton = (Button) view.findViewById(R.id.ok_button);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick(BrushSizePickerFragment.this);
            }
        });
        mNegativeButton = (Button) view.findViewById(R.id.cancel_button);
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick(BrushSizePickerFragment.this);
            }
        });
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        return dialog;
    }
    public int getValue() {
        return mValue;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NumberPickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }
}
