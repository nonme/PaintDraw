package com.nonme.drawandpaint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment paintingFragment = fm.findFragmentById(R.id.painting_fragment),
                toolBarFragment = fm.findFragmentById(R.id.tool_bar_fragment);

        if(paintingFragment == null) {
            paintingFragment = new PaintingFragment();
            fm.beginTransaction()
                    .add(R.id.painting_fragment, paintingFragment)
                    .commit();
        }
        if(toolBarFragment == null) {
            toolBarFragment = new ToolBarFragment();
            fm.beginTransaction()
                    .add(R.id.tool_bar_fragment, toolBarFragment)
                    .commit();
        }
    }
}
