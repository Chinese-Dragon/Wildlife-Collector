package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeableItemClickListener;

/**
 * Created by mark93 on 4/25/2017.
 */

public class FixSwipeableItemClickListener extends SwipeableItemClickListener {
    public FixSwipeableItemClickListener(Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
