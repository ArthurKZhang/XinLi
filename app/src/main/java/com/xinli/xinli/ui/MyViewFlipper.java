package com.xinli.xinli.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

/**
 * Created by zhangyu on 10/26/16.
 */
public class MyViewFlipper extends ViewFlipper {

    public MyViewFlipper(Context context) {
        super(context);
    }

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
    public boolean ontouchEvent(MotionEvent event){

        return true;
    }
}
