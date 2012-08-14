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
 * https://github.com/EaseTheWorld/PaintAnimatorTest
 */

package com.easetheworld.paintanimatortest;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.easetheworld.paintanimatortest.CanvasLayerManager.CanvasLayer;

import dev.easetheworld.animator.AnimatorPlayer;
import dev.easetheworld.animator.PaintAnimator;

public class WatermarkListView extends ListView {
	
	private AnimatorPlayer mAnimatorPlayer;
	private Paint mListBgPaint;
	private Paint mItemBgPaint;
	private Paint mItemFgPaint;
	
	public WatermarkListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setCacheColorHint(Color.TRANSPARENT); // to use listview's bg
		
		mAnimatorPlayer = new AnimatorPlayer(300);
		
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
				invalidateViews();
			}
		});
		
		setOnScrollListener(new OnScrollSpeedChangedListener(300) {
			
			private float[] mStepThresholds = new float[] {0.7f, 1.2f};
			
			private int convertToTime(float speed) {
				if (speed < 0) speed = -speed;
				int step = Arrays.binarySearch(mStepThresholds, speed);
				if (step < 0)
					step = -step - 1;
				return step * mAnimatorPlayer.getDuration() / mStepThresholds.length;
			}
			
			@Override
			protected void onScrollSpeedChanged(float speed) {
				mAnimatorPlayer.playTo(convertToTime(speed));
			}
		});
	}
	
	public Paint getItemBgPaint() {
		return mItemBgPaint;
	}
	
	public Paint getItemFgPaint() {
		return mItemFgPaint;
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