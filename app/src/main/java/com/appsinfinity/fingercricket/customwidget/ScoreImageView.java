package com.appsinfinity.fingercricket.customwidget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by macbook on 6/8/15.
 */
public class ScoreImageView extends ImageView {
    public ScoreImageView(Context context) {
        super(context);
    }

    public ScoreImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Display display;
        if (Build.VERSION.SDK_INT > 17) {
            display = getDisplay();
        } else {
            display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        }
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        setMeasuredDimension((int)(width/3.0), (int)(width/3.0));
    }
}
