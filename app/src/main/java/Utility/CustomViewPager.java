package Utility;

import android.content.Context;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Deeksha on 05-02-2016.
 */
public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x,
                                int y) {
        if (v instanceof RecyclerView || v instanceof PagerTabStrip) {
            return (true);
        }

        return (super.canScroll(v, checkV, dx, x, y));
    }
}
