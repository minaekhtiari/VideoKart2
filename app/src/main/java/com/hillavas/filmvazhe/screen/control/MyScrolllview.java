package com.hillavas.filmvazhe.screen.control;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by arashjahani on 11/24/2016 AD.
 */

public class MyScrolllview extends NestedScrollView {

    public interface ScrollViewListener {
        void onScrollChanged(
                MyScrolllview myNestedScrolllview, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public MyScrolllview(Context context) {
        super(context);
    }

    public MyScrolllview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrolllview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
