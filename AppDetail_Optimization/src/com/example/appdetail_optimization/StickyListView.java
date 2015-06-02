package com.example.appdetail_optimization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.appdetail_optimization.AppDetailFragment.StickyScrollCallBack;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class StickyListView extends ListView {
	private StickyScrollCallBack scrollCallBack;

	public StickyListView(Context context) {
		this(context, null);
	}

	public StickyListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StickyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOverScrollMode(OVER_SCROLL_ALWAYS);
		this.setOnScrollListener(mOnScrollListener);
	}

	public void setScrollCallBack(StickyScrollCallBack scrollCallBack) {
		this.scrollCallBack = scrollCallBack;
	}

	private final OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem == 0) {
				View firstView = getChildAt(0);
				if (null != firstView) {
					int firstTop = firstView.getTop();
					if (firstTop < -AppDetailFragment.STICKY_HEIGHT1) {
						firstTop = -AppDetailFragment.STICKY_HEIGHT1;
					}
					scrollCallBack.onScrollChanged(firstTop);
				}
			}
			else if (firstVisibleItem < 6) {
				scrollCallBack.onScrollChanged(-AppDetailFragment.STICKY_HEIGHT1);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("LeiTest", "onScrollStateChanged=" + scrollState);

			// scrollState=0 代表滑动结束
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				animScrollY();
			}
		}
	};

	private void animScrollY() {
		int offsetDistance = 0, firstTop = 0;
		if (getFirstVisiblePosition() == 0) {
			View firstView = getChildAt(0);
			if (firstView != null) {
				firstTop = firstView.getTop();
				if (firstTop < -AppDetailFragment.STICKY_HEIGHT1 / 2) {
					offsetDistance = -AppDetailFragment.STICKY_HEIGHT1;
				}
			}

			if (firstTop != offsetDistance) {
				new AnimUiThread(firstTop, offsetDistance).start();
			}
		}
	}

	public void invalidScroll() {

	}

	public int getFirstViewScrollTop() {
		if (getFirstVisiblePosition() == 0) {
			View firstView = getChildAt(0);
			if (null != firstView) {
				return -firstView.getTop();
			}
		}
		return Integer.MAX_VALUE;
	}

	class AnimUiThread extends Thread {
		private int fromPos, toPos;

		public AnimUiThread(int fromPos, int toPos) {
			this.fromPos = fromPos;
			this.toPos = toPos;
		}

		@Override
		public void run() {
			int num = 10; // 十次循环到位
			for (int i = 0; i < num; i++) {
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				int tempPos = fromPos + (toPos - fromPos) * (i + 1) / num;
				Message msg = uiHandler.obtainMessage();
				msg.what = tempPos;
				msg.sendToTarget();
			}
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		uiHandler = null;
		super.onDetachedFromWindow();
	}

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int pos = msg.what;
			setSelectionFromTop(0, pos);
		};
	};
}
