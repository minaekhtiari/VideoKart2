package com.hillavas.filmvazhe.screen.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by arashjahani on 10/12/2016 AD.
 */

public class MovieGridView extends GridView {

    public interface ScrollViewListener {
        void onScrollChanged(
                MovieGridView gridScrollview, int x, int y, int oldx, int oldy);
    }

    private MovieGridView.ScrollViewListener scrollViewListener = null;

    public MovieGridView(Context context) {
        super(context);
    }

    public MovieGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(MovieGridView.ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

}
