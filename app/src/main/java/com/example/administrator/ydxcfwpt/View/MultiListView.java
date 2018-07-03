package com.example.administrator.ydxcfwpt.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/6/20.
 */

public class MultiListView extends ListView {
    private ScrollView scrollView;
    private boolean notAllowParentScroll = true;

    public void setNotAllowParentScroll(boolean notAllowParentScroll) {
        this.notAllowParentScroll = notAllowParentScroll;
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public MultiListView(Context context) {
        super(context);
    }

    public MultiListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(notAllowParentScroll){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

}