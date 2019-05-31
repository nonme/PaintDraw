package com.nonme.drawandpaint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FragmentManager fm = getSupportFragmentManager();
        Fragment paintingFragment = fm.findFragmentById(R.id.painting_fragment);
        if(paintingFragment == null) {
            paintingFragment = new PaintingFragment();
            fm.beginTransaction()
                    .add(R.id.painting_fragment, paintingFragment)
                    .commit();
        }
    }
}
