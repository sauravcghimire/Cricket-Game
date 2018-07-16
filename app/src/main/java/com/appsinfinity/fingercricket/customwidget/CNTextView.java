package com.appsinfinity.fingercricket.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.appsinfinity.fingercricket.R;


/**
 * Created on 7/3/2016.
 *
 * @author saurav
 */
public class CNTextView extends TextView {
    public CNTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CNTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CNTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SCTextView);
            String fontName = a.getString(R.styleable.SCTextView_fontName);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/" + fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
