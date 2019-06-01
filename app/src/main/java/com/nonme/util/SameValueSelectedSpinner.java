package com.nonme.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * This spinner triggers event OnItemSeleceted even when the already selected item is selected again.
 * This fixes the bug when using spinner after using dropper.
 * Contributions to @melquiades
 */
public class SameValueSelectedSpinner extends Spinner {
    public SameValueSelectedSpinner(Context context)
    { super(context); }

    public SameValueSelectedSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public SameValueSelectedSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }
}
