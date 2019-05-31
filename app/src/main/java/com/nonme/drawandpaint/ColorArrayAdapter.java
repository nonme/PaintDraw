package com.nonme.drawandpaint;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ColorArrayAdapter extends ArrayAdapter<Integer> {
    private Integer[] mColors;
    public ColorArrayAdapter(Context context, Integer[] colors) {
        super(context, R.layout.spinner_row, colors);
        this.mColors = colors;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }
    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(mColors[position]);
        imageView.setLayoutParams(new AbsListView.LayoutParams(
                100, 100));
        return imageView;
    }
}