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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ListView;
import dev.easetheworld.paintanimator.PaintAnimatorSet;
import dev.easetheworld.paintanimator.PaintAnimator;

public class AniListView extends ListView {
	
	private PaintAnimatorSet mAnimatorSet;
	private Paint mListPaint;
	private Paint mItemBgPaint;
	
	public AniListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setCacheColorHint(Color.TRANSPARENT); // to use listview's bg
		
		mAnimatorSet = new PaintAnimatorSet();
		
		mListPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mListPaint.setTextSize(40);
		mListPaint.setTextAlign(Paint.Align.CENTER);
		mListPaint.setColor(0xff0000ff);
		mAnimatorSet.add(PaintAnimator.ofColor(mListPaint, 0x000000ff));
//		mListPaint.setColor(0x408080ff);
//		mAnimatorSet.add(PaintAnimator.ofColor(mListPaint, 0xff0000ff));
//		mAnimatorSet.add(PaintAnimator.ofTextSize(mListPaint, 80));
		
		mItemBgPaint = new Paint();
		mItemBgPaint.setTextAlign(Paint.Align.CENTER);
		mItemBgPaint.setColor(0x40ff8080);
		mAnimatorSet.add(PaintAnimator.ofColor(mItemBgPaint, 0xc0ff0000));
		
		mAnimatorSet.setDuration(1000);
		mAnimatorSet.setOnInvalidateListener(new PaintAnimatorSet.OnInvalidateListener() {
			@Override
			public void onInvalidate() {
				invalidateViews();
			}
		});
	}
	
	public Paint getItemPaint() {
		return mItemBgPaint;
	}
	
	public void animate(boolean forward) {
		mAnimatorSet.animate(forward);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText("List", getWidth() / 2, getHeight() / 2, mListPaint);
	}
}