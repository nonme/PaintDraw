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

public class TextSizePickerFragment extends DialogFragment{
    public interface NumberPickerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    private TextSizePickerFragment.NumberPickerDialogListener mListener;

    private NumberPicker mNumberPicker;
    private SizeView mTextSizeView;
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
        String[] values = new String[51];
        for(int i = 1; i <= 51; ++i)
            values[i-1] = String.valueOf(i+9);
        mNumberPicker.setMinValue(10);
        mNumberPicker.setMaxValue(60);
        mNumberPicker.setDisplayedValues(values);
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTextSizeView.setSize(5*picker.getValue());
                mValue = 5*picker.getValue();
            }
        });
        mTextSizeView = (SizeView) view.findViewById(R.id.brush_size_view);
        mTextSizeView.setItem(SizeView.TEXT);
        mPositiveButton = (Button) view.findViewById(R.id.ok_button);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick(TextSizePickerFragment.this);
            }
        });
        mNegativeButton = (Button) view.findViewById(R.id.cancel_button);
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick(TextSizePickerFragment.this);
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
            mListener = (TextSizePickerFragment.NumberPickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }
}
