package com.appsinfinity.fingercricket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.appsinfinity.fingercricket.R;

/**
 * Created by saurav on 3/29/15.
 */
public class ScoreButtonAdapter extends BaseAdapter {
    private Context context;
    private int[] scoreArray;

    public ScoreButtonAdapter(Context context, int[] scoreArray) {
        this.context = context;
        this.scoreArray = scoreArray;
    }

    @Override
    public int getCount() {
        return scoreArray.length;
    }

    @Override
    public Object getItem(int position) {
        return scoreArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= new ViewHolder();
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.button,parent,false);
            viewHolder.button = (Button) convertView;
            viewHolder.button.setFocusable(false);
            viewHolder.button.setClickable(false);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button.setText(""+scoreArray[position]);
        return convertView;
    }

    private class ViewHolder {
        Button button;
    }
}
