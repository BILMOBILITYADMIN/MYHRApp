package Utility;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Harshitha on 05-02-2016.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    ListView list;
    private GestureDetector gestureDetector;
    private Context context;

    public OnSwipeTouchListener(Context ctx, ListView list) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        context = ctx;
        this.list = list;
    }

    public OnSwipeTouchListener() {
        super();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight(int pos) {

    }

    public void onSwipeLeft(int pos) {

    }

    public void onClick() {

    }

    public void onDoubleClick() {

    }

    public void onLongClick() {

    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 10;
        private static final int SWIPE_VELOCITY_THRESHOLD = 10;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        private int getPostion(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null && e2 != null) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight(getPostion(e1));
                    else
                        onSwipeLeft(getPostion(e1));

                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }

    }
}
