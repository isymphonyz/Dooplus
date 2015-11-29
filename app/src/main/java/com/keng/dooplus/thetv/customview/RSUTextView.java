package com.keng.dooplus.thetv.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RSUTextView extends TextView {
	
	Typeface tf;
	
	public RSUTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/rsu-light.ttf");
		this.setTypeface(tf);
	}

	public RSUTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/rsu-light.ttf");
		this.setTypeface(tf);
	}

	public RSUTextView(Context context) {
		super(context);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/rsu-light.ttf");
		this.setTypeface(tf);
	}
}
