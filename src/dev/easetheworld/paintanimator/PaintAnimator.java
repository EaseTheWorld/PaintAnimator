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

package dev.easetheworld.paintanimator;

import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;


public class PaintAnimator {
	
	private static final int COLOR = 1;
	private static final int TEXT_SIZE = 2;
	
	private static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
	private static final FloatEvaluator sFloatEvaluator = new FloatEvaluator();
	
	private final Paint mPaint;
	
	private final int mField;

	private Object mStart;
	
	private Object mEnd;
	
	private View[] mViews;
	
	private long mDuration;
	
	private ValueAnimator mAnimator;

	private PaintAnimator(Paint paint, int field) {
		mPaint = paint;
		mField = field;
	}
	
	public static PaintAnimator ofColor(Paint paint, int... value) {
		PaintAnimator animator = new PaintAnimator(paint, COLOR);
		if (value.length == 1) {
			animator.mStart = animator.getValue();
			animator.mEnd = value[0];
		} else {
			animator.mStart = value[0];
			animator.mEnd = value[value.length - 1];
		}
		return animator;
	}
	
	public static PaintAnimator ofTextSize(Paint paint, float... value) {
		PaintAnimator animator = new PaintAnimator(paint, TEXT_SIZE);
		if (value.length == 1) {
			animator.mStart = animator.getValue();
			animator.mEnd = value[0];
		} else {
			animator.mStart = value[0];
			animator.mEnd = value[value.length - 1];
		}
		return animator;
	}
	
	public PaintAnimator setInvalidateViews(View... views) {
		mViews = views;
		return this;
	}
	
	public PaintAnimator setDuration(long duration) {
		mDuration = duration;
		return this;
	}
	
	public long getDuration() {
		return mDuration;
	}
	
	private boolean mPlayForward = true;
	
	public void animate(boolean playForward) {
		if (mAnimator.isRunning()) {
			if (playForward != mPlayForward)
				mAnimator.reverse();
		} else {
			if (playForward)
				mAnimator.start();
			else
				mAnimator.reverse();
		}
		mPlayForward = playForward;
	}
	
	void setFractionAndInvalidate(float fraction) {
		switch(mField) {
		case COLOR:
			mPaint.setColor((Integer)sArgbEvaluator.evaluate(fraction, (Integer)mStart, (Integer)mEnd));
			break;
		case TEXT_SIZE:
			mPaint.setTextSize(sFloatEvaluator.evaluate(fraction, (Number)mStart, (Number)mEnd));
			break;
		}
		if (mViews != null) {
			for (View v : mViews)
				v.invalidate();
		}
	}
	
	private Object getValue() {
		switch(mField) {
		case COLOR:
			return mPaint.getColor();
		case TEXT_SIZE:
			return mPaint.getTextSize();
		}
		return null;
	}
	
	private void createAnimator() {
		if (mAnimator == null) {
			if (mStart instanceof Integer)
				mAnimator = ValueAnimator.ofInt((Integer)mStart, (Integer)mEnd);
			else if (mStart instanceof Float)
				mAnimator = ValueAnimator.ofFloat((Float)mStart, (Float)mEnd);
			else
				mAnimator = ValueAnimator.ofObject(null , mStart, mEnd);
			if (mDuration > 0)
				mAnimator.setDuration(mDuration);
			mAnimator.setInterpolator(null);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					setFractionAndInvalidate(valueAnimator.getAnimatedFraction());
				}
			});
		}
	}
}