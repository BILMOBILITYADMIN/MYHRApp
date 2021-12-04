package Utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
/**
 * @author shridharjoshi
 *
 */
public class MyGridView extends GridView {

	/**
	 * @param context
	 * @param attrs
	 */
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public MyGridView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	private OnNoItemClickListener listener;
	public interface OnNoItemClickListener
	{
		void onNoItemClick();
	}

	public void setOnNoItemClickListener(OnNoItemClickListener listener)
	{
		this.listener = listener;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		// The pointToPosition() method returns -1 if the touch event
		// occurs outside of a child View.
		// Change the MotionEvent action as needed. Here we use ACTION_DOWN
		// as a simple, naive indication of a click.
		if (pointToPosition((int) event.getX(), (int) event.getY()) == -1
				&& event.getAction() == MotionEvent.ACTION_UP)
		{
			if (listener != null)
			{
				listener.onNoItemClick();
			}
		}
		return super.dispatchTouchEvent(event);
	}
}
