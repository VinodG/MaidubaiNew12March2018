package com.winit.maidubai.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.winit.maidubai.utilities.LogUtils;

/**
 * Created by Girish Velivela on 30-09-2016.
 */
public class CustomViewPager extends ViewPager {

    private int min_distance = 100;
    private float downX, downY, upX, upY;
    public boolean swipeFalg = true;
    public Context context;

    // swipeListener for controlling swipe
    private  SwipeListener swipeListener;
    public CustomViewPager(Context context) {
        super(context);
        this.context = context;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeListener(SwipeListener swipeListener){
        this.swipeListener = swipeListener;
    }
/*
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        return swipeFalg;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.debug(LogUtils.LOG_TAG,"CustomViewPager");
        if(swipeListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    //HORIZONTAL SCROLL
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > min_distance) {
                            // left or right
                            if (deltaX < 0) {
                                return swipeListener.onLeftToRightSwipe() || super.onTouchEvent(event);
                            }
                            if (deltaX > 0) {
                                return swipeListener.onLeftToRightSwipe() || super.onTouchEvent(event);
                            }
                        } else {
                            return false;
                        }
                    }
                }
                default:
                    return false;
            }
        }else
            return !swipeFalg || super.onTouchEvent(event);
    }*/

}
