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

import com.nineoldandroids.animation.ValueAnimator;

public class PaintAnimator2 extends ValueAnimator {
	
	private static final int COLOR = 1;
	private static final int ALPHA = 2;
	private static final int TEXT_SIZE = 3;
	private static final int TEXT_SCALE_X = 4;
	private static final int TEXT_SKEW_X = 5;
	private static final int STROKE_WIDTH = 6;
	
	private static final ArgbEvaluator2 sArgbEvaluator = new ArgbEvaluator2();
	
	private final Paint mPaint;
	
	private final int mField;
	
	protected View[] mViews;
	
	// default constructor for child class
	protected PaintAnimator2() {
		this(new Paint(), 0);
	}

	private PaintAnimator2(Paint paint, int field) {
		mPaint = paint;
		mField = field;
		setInterpolator(null); // linear interpolator is enough because this is not about position.
		if (field > 0) {
			addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					setValue(valueAnimator.getAnimatedValue());
					if (mViews != null) {
						for (View v : mViews)
							v.invalidate();
					}
				}
			});
		}
	}
	
	private void setFloatValuesHelper(float... value) {
		if (value.length == 1)
			setFloatValues((Float)getValue(), value[0]);
		else
			setFloatValues(value);
	}
	
	private void setIntValuesHelper(int... value) {
		if (value.length == 1)
			setIntValues((Integer)getValue(), value[0]);
		else
			setIntValues(value);
	}
	
	public static PaintAnimator2 ofColor(Paint paint, int... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, COLOR);
		animator.setIntValuesHelper(value);
		animator.setEvaluator(sArgbEvaluator);
		return animator;
	}
	
	public static PaintAnimator2 ofAlpha(Paint paint, int... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, ALPHA);
		animator.setIntValuesHelper(value);
		return animator;
	}
	
	public static PaintAnimator2 ofTextSize(Paint paint, float... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, TEXT_SIZE);
		animator.setFloatValuesHelper(value);
		return animator;
	}
	
	public static PaintAnimator2 ofTextScaleX(Paint paint, float... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, TEXT_SCALE_X);
		animator.setFloatValuesHelper(value);
		return animator;
	}
	
	public static PaintAnimator2 ofTextSkewX(Paint paint, float... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, TEXT_SKEW_X);
		animator.setFloatValuesHelper(value);
		return animator;
	}
	
	public static PaintAnimator2 ofStrokeWidth(Paint paint, int... value) {
		PaintAnimator2 animator = new PaintAnimator2(paint, STROKE_WIDTH);
		animator.setIntValuesHelper(value);
		return animator;
	}
	
	public PaintAnimator2 setInvalidateViews(View... views) {
		mViews = views;
		return this;
	}
	
	protected boolean mPlayForward = false;
	
	public void animate(boolean playForward) {
		boolean isReverse = playForward != mPlayForward;
		mPlayForward = playForward;
		if (!isStarted()) { // if not started, simply start.
			if (playForward)
				start();
			else
				reverse();
		} else {
			if (isReverse) {
				if (isRunning()) // animating (after delay)
					reverse();
				else // during start delay
					cancel();
			}
		}
	}
	
	public void toggle() {
		animate(!mPlayForward);
	}
	
	private Object getValue() {
		switch(mField) {
		case COLOR:
			return mPaint.getColor();
		case ALPHA:
			return mPaint.getAlpha();
		case TEXT_SIZE:
			return mPaint.getTextSize();
		case TEXT_SCALE_X:
			return mPaint.getTextScaleX();
		case TEXT_SKEW_X:
			return mPaint.getTextSkewX();
		case STROKE_WIDTH:
			return mPaint.getStrokeWidth();
		}
		return null;
	}
	
	private void setValue(Object value) {
		switch(mField) {
		case COLOR:
			mPaint.setColor((Integer)value);
			break;
		case ALPHA:
			mPaint.setAlpha((Integer)value);
			break;
		case TEXT_SIZE:
			mPaint.setTextSize((Float)value);
			break;
		case TEXT_SCALE_X:
			mPaint.setTextScaleX((Float)value);
			break;
		case TEXT_SKEW_X:
			mPaint.setTextSkewX((Float)value);
			break;
		case STROKE_WIDTH:
			mPaint.setStrokeWidth((Integer)value);
			break;
		}
	}
}