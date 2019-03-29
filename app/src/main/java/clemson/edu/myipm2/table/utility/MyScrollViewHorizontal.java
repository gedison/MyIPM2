package clemson.edu.myipm2.table.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyScrollViewHorizontal extends HorizontalScrollView {
	private HorizontalScrollViewListener scrollViewListener = null;

	public MyScrollViewHorizontal(Context context) {
		super(context);
	}

	public MyScrollViewHorizontal(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(HorizontalScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}
}
