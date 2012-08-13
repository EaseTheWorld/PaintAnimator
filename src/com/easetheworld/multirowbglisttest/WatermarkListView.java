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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import dev.easetheworld.animator.AnimatorPlayer;
import dev.easetheworld.animator.CanvasLayerManager;
import dev.easetheworld.animator.PaintAnimator;
import dev.easetheworld.animator.CanvasLayerManager.CanvasLayer;

public class WatermarkListView extends ListView {
	
	private AnimatorPlayer mAnimatorPlayer;
	private Paint mListBgPaint;
	private Paint mItemBgPaint;
	private Paint mItemFgPaint;
	
	public WatermarkListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setCacheColorHint(Color.TRANSPARENT); // to use listview's bg
		
		mAnimatorPlayer = new AnimatorPlayer(1000);
		
		mListBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mListBgPaint.setTextAlign(Paint.Align.CENTER);
		mListBgPaint.setColor(Color.RED);
		mListBgPaint.setAlpha(0x60);
		mListBgPaint.setTextSize(100);
		mAnimatorPlayer.add(PaintAnimator.ofAlpha(mListBgPaint, 0xd0));
		
		mItemBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mItemBgPaint.setTextAlign(Paint.Align.CENTER);
		mItemBgPaint.setColor(Color.YELLOW);
		mItemBgPaint.setAlpha(0x60);
		mAnimatorPlayer.add(PaintAnimator.ofAlpha(mItemBgPaint, 0xd0));
		
		mItemFgPaint = new Paint();
		mItemFgPaint.setColor(Color.WHITE);
		mAnimatorPlayer.add(PaintAnimator.ofColor(mItemFgPaint, 0x40ffffff));
		
		mAnimatorPlayer.setOnTimeChangedListener(new AnimatorPlayer.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(int currentTime) {
				android.util.Log.i("nora", "invalidate "+currentTime);
				invalidateViews();
			}
		});
		
        setOnScrollListener(new OnScrollListener() {
        	private boolean mIsScrolling = false;
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				boolean isScrolling = scrollState != OnScrollListener.SCROLL_STATE_IDLE;
				if (isScrolling != mIsScrolling) {
					animate(isScrolling);
					mIsScrolling = isScrolling;
				}
			}
        });
		setOnScrollListener(new OnScrollSpeedChangedListener(300) {
			
			@Override
			protected void onScrollSpeedChanged(float speed) {
				android.util.Log.i("nora", "speed="+speed);
				if (speed < 0) speed = -speed;
				if (speed > 2f)
					speed = 2f;
				int speedTime = (int)((float)mAnimatorPlayer.getDuration() * speed / 2f);
				mAnimatorPlayer.playTo(speedTime);
			}
		});
	}
	
	public Paint getItemBgPaint() {
		return mItemBgPaint;
	}
	
	public Paint getItemFgPaint() {
		return mItemFgPaint;
	}
	
	public void animate(boolean forward) {
//		android.util.Log.i("nora", "ListView animate "+forward);
//		if (forward)
//			mAnimatorSet.setStartDelay(500);
//		else
//			mAnimatorSet.setStartDelay(100);
//		mAnimatorSet.animate(forward);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int middleData = (Integer)getItemAtPosition((getFirstVisiblePosition() + getLastVisiblePosition()) / 2);
		middleData = (middleData - 1) / 100 * 100;
		canvas.drawText(String.format("%03d", middleData), getWidth() / 2, getHeight() / 2, mListBgPaint);
	}
	
	public int getAnimatorDuration() {
		return mAnimatorPlayer.getDuration();
	}
	
	public int getAnimatorCurrentTime() {
		return mAnimatorPlayer.getCurrentTime();
	}
	
	public static class WatermarkItemTextView extends TextView {
		private WatermarkListView mList;
		private CanvasLayerManager mCanvasLayerManager;
		
		public WatermarkItemTextView(Context context) {
			super(context);
			
			mCanvasLayerManager = new CanvasLayerManager();
			mCanvasLayerManager.add(new CanvasLayer() {
				@Override
				public void onDraw(View v, Canvas canvas) {
					onDrawFromSuper(canvas);
				}
	
				@Override
				public int getZOrder() {
					return getWatermarkListView().getAnimatorDuration() - getWatermarkListView().getAnimatorCurrentTime();
				}
			});
			
			mCanvasLayerManager.add(new CanvasLayer() {
				@Override
				public void onDraw(View v, Canvas canvas) {
					final WatermarkListView parent = getWatermarkListView();
					int pos = parent.getPositionForView(v);
					int spanRows = 10;
					int a = (pos % 100) / spanRows * spanRows;
					drawMultirowBackgroundText(canvas, String.format("%02d", a), parent.getItemBgPaint(), spanRows, 8, pos % spanRows, getWidth() / 2);
				}
	
				@Override
				public int getZOrder() {
					return getWatermarkListView().getAnimatorCurrentTime();
				}
			});
		}

		@Override
		protected void onDraw(Canvas canvas) {
//			super.onDraw(canvas);
			mCanvasLayerManager.draw(this, canvas);
		}
		
		private void onDrawFromSuper(Canvas canvas) {
			super.onDraw(canvas);
		}
		
		private WatermarkListView getWatermarkListView() {
			if (mList == null)
				mList = (WatermarkListView)getParent();
			return mList;
		}
		
	    /**
	     * draw text background which is laid across spanRows rows.
	     * the background is drawn through drawRows rows.
	     * to set margin between backgrounds set spanRows bigger than drawRows.
	     * currentRow is the zero-based index in spanRows.
	     * 
	     * y will be calculated from the input.
	     * you can set x.
	     */
	    final private void drawMultirowBackgroundText(Canvas canvas, String text, Paint paint, int spanRows, int drawRows, int currentRow, float x) {
	    	float y = (spanRows - drawRows) * getHeight() / 2 + (drawRows - currentRow) * getHeight();
	    	paint.setTextSize(drawRows * getHeight());
			canvas.drawText(text, x, y - paint.descent() / 2, paint);
	    }
	    
	    /**
	     * draw bitmap background which is laid across spanRows rows.
	     * to set margin between backgrounds set spanRows bigger than drawRows.
	     * currentRow is the zero-based index in spanRows.
	     * 
	     * y will be calculated from the input.
	     * you can set x.
	     */
	    final private void drawMultirowBackgroundBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int spanRows, int currentRow, float x) {
	    	float y = (spanRows * getHeight() - bitmap.getHeight()) / 2 - currentRow * getHeight();
			canvas.drawBitmap(bitmap, x, y, paint);
	    }
	}
}