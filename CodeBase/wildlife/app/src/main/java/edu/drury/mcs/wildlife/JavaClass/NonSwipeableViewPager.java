package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.Serializable;

/**
 * Created by mark93 on 12/11/2016.
 */

public class NonSwipeableViewPager extends ViewPager implements Serializable {
    private boolean enabled;

    public NonSwipeableViewPager(Context context) {
        super(context);
        this.enabled = true;
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onTouchEvent(ev);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;

}

//    private void setMyScroller(Context context) {
//        try {
//            Class<?> viewpager = ViewPager.class;
//            Field scroller = viewpager.getDeclaredField("mScroller");
//            scroller.setAccessible(true);
//            scroller.set(this, new MyScroller(context));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class MyScroller extends Scroller {
//        public MyScroller(Context context) {
//            super(context);
//        }
//
//        @Override
//        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//            super.startScroll(startX, startY, dx, dy, 350 /* 1secs*/);
//        }
//    }
}
