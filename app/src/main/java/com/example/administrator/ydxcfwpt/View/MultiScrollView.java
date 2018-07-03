package com.example.administrator.ydxcfwpt.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/6/20.
 */

public class MultiScrollView extends ScrollView {
    public MultiScrollView(Context context) {
        super(context);

    }

    public MultiScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    private boolean allowChildViewScroll = true;

    public void setAllowChildViewScroll(boolean allowChildViewScroll) {
        this.allowChildViewScroll = allowChildViewScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!allowChildViewScroll){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

  public   interface ScrollViewListener {
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
    }

}