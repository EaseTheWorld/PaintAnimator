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

import java.util.LinkedList;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;


public class PaintAnimatorSet {
	
	private ValueAnimator mAnimator;
	
	private LinkedList<PaintAnimator> mList = new LinkedList<PaintAnimator>();
	
	public PaintAnimatorSet() {
		mAnimator = ValueAnimator.ofFloat(0f, 1f);
		mAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				for (PaintAnimator ppa : mList)
					ppa.setFractionAndInvalidate(valueAnimator.getAnimatedFraction());
				if (mOnInvalidateListener != null)
					mOnInvalidateListener.onInvalidate();
			}
		});
	}
	
	public void add(PaintAnimator paintAnimator) {
		mList.add(paintAnimator);
	}
	
	public void remove(PaintAnimator paintAnimator) {
		mList.remove(paintAnimator);
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
	
	public void setDuration(long duration) {
		mAnimator.setDuration(duration);
	}
	
	public long getDuration() {
		return mAnimator.getDuration();
	}
	
	public static interface OnInvalidateListener {
		void onInvalidate();
	};
	
	private OnInvalidateListener mOnInvalidateListener;
	
	public void setOnInvalidateListener(OnInvalidateListener listener) {
		mOnInvalidateListener = listener;
	}
}