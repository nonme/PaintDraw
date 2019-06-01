package com.nonme.drawandpaint;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.nonme.fragments.NumberPickerFragment;

public class MainActivity extends AppCompatActivity
            implements NumberPickerFragment.NumberPickerDialogListener{
    private Toolbar mToolbar;
    private PaintingFragment mPaintingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //mToolbar = findViewById(R.id.tool_bar);
        //setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setIcon(R.drawable.main_icon2);
        FragmentManager fm = getSupportFragmentManager();
        mPaintingFragment = (PaintingFragment) fm.findFragmentById(R.id.painting_fragment);
        if(mPaintingFragment == null) {
            mPaintingFragment = new PaintingFragment();
            fm.beginTransaction()
                    .add(R.id.painting_fragment, mPaintingFragment)
                    .commit();
        }
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mPaintingFragment.onDialogPositiveClick(dialog);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mPaintingFragment.onDialogNegativeClick(dialog);
    }
}
