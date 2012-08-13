/*
 * Copyright (C) 2012 EaseTheWorld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * https://github.com/EaseTheWorld/MultirowBgListTest
 */

package com.easetheworld.multirowbglisttest;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class OnScrollSpeedChangedListener implements OnScrollListener {
	
	private int mScrollState;
	private long mMinTimeInterval;
	private long mPreviousTime;
	
	private View mMiddleView;
	
	private final float mDensity;
	
	public OnScrollSpeedChangedListener(int minTimeMsInterval) {
		mMinTimeInterval = minTimeMsInterval;
		mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
		mDensity = Resources.getSystem().getDisplayMetrics().density;
	}
	
	/**
	 * @param (dip difference / time difference)
	 */
	protected abstract void onScrollSpeedChanged(float speed);

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) // after scroll start
			checkScrollSpeedIfNecessary(view, false, true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE && scrollState != mScrollState) { // scroll start
			mAbsoluteScrollOffset = getAbsoluteScrollOffset(view);
			mPreviousTime = SystemClock.uptimeMillis();
		}
		mScrollState = scrollState;
		
		// send delay message
		mScrollStopHandler.removeMessages(0);
		switch(scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE: // scroll end
			checkScrollSpeedIfNecessary(view, true, true);
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // to check fling stopped by touch down.
			checkScrollSpeedIfNecessary(view, true, false);
			mScrollStopHandler.sendMessageDelayed(Message.obtain(mScrollStopHandler, 0, view), mMinTimeInterval); // to check fling stopped by touch down.
			break;
		case OnScrollListener.SCROLL_STATE_FLING: // fling will call onScroll, so delay message is not necessary.
			break;
		}
	}
	
	private Handler mScrollStopHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			checkScrollSpeedIfNecessary((AbsListView)msg.obj, true, true);
		}
	};
	
//	private void resetDiff(long currentTime) {
//		mMiddleViewLastDiffSum = 0;
//		mPreviousTime = currentTime;
//	}
//	
//	private void saveMiddleView(AbsListView view) {
//		mMiddleViewPosition = (view.getFirstVisiblePosition() + view.getLastVisiblePosition()) / 2;
//		mMiddleView = view.getChildAt(mMiddleViewPosition - view.getFirstVisiblePosition());
//		mMiddleViewTop = mMiddleView.getTop();
//	}
	
	private int mAbsoluteScrollOffset;
	
	private int getAbsoluteScrollOffset(AbsListView view) {
		return view.getFirstVisiblePosition()*view.getChildAt(0).getHeight() - view.getChildAt(0).getTop();
	}
	
	private void checkScrollSpeedIfNecessary(AbsListView view, boolean force, boolean dispatchCallback) {
		
//		// update offset middle view diff
//		int topDiff = 0;
//		if (view.getPositionForView(mMiddleView) == mMiddleViewPosition) { // middle child view is still same view.
//			int top = mMiddleView.getTop();
//			topDiff = top - mMiddleViewTop;
//		} else {
//			android.util.Log.e("nora", "We missed the middle view");
//		}
//		mLastDiffSum += topDiff;
//		saveMiddleView(view);
		
		long currentTime = SystemClock.uptimeMillis();
		long timeInterval = currentTime - mPreviousTime;
		if (force || timeInterval > mMinTimeInterval) {
			int absoluteScrollOffset = getAbsoluteScrollOffset(view);
			int diff = absoluteScrollOffset - mAbsoluteScrollOffset;
			mAbsoluteScrollOffset = absoluteScrollOffset;
			mPreviousTime = currentTime;
			if (dispatchCallback) {
				if (timeInterval != 0)
					onScrollSpeedChanged((float)diff / timeInterval / mDensity);
				if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE && diff != 0) // if scroll stopped, call one more for speed 0
					onScrollSpeedChanged(0);
			}
		}
	}
}