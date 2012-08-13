/*
 * Copyright (C) 2010 The Android Open Source Project
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
 */

package dev.easetheworld.animator;

import java.util.LinkedList;

import com.nineoldandroids.animation.ValueAnimator;

public class AnimatorPlayer {
	
	private ValueAnimator mTimeAnimator;
	private LinkedList<ValueAnimator> mList;
	private int mCurrentTime;
	private int mEndTime;
	private int mDuration;
	
	public AnimatorPlayer(int duration) {
		mTimeAnimator = new ValueAnimator();
		mTimeAnimator.setInterpolator(null);
		setTimeRange(0, duration);
		mDuration = duration;
		mTimeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int currentTime = (Integer)valueAnimator.getAnimatedValue();
				seekToInternal(currentTime);
			}
		});
		
		mList = new LinkedList<ValueAnimator>();
	}
	
	public int getDuration() {
		return mDuration;
	}
	
	public int getCurrentTime() {
		return mCurrentTime;
	}
	
//	public void pause() {
//		mTimeAnimator.cancel();
//	}
	
	public void play(boolean forward) {
		if (forward)
			playTo(mDuration);
		else
			playTo(0);
	}
	
	public void playTo(int time) {
		setTimeRange(time);
		mTimeAnimator.start();
	}
	
	private void seekToInternal(int time) {
		for (ValueAnimator animator : mList) {
			animator.setCurrentPlayTime(time);
		}
		mCurrentTime = time;
		if (mOnTimeChangedListener != null)
			mOnTimeChangedListener.onTimeChanged(time);
	}
	
	public void seekTo(int time) {
		seekToInternal(time);
	}
	
	private void setTimeRange(int endTime) {
		setTimeRange(mCurrentTime, endTime);
	}
	
	private void setTimeRange(int startTime, int endTime) {
		mTimeAnimator.cancel();
		mTimeAnimator.setIntValues(startTime, endTime);
		mTimeAnimator.setDuration(Math.abs(endTime - startTime));
		mEndTime = endTime;
	}
	
	public void add(ValueAnimator animator) {
		animator.setDuration(mDuration);
		mList.add(animator);
	}
	
	public void remove(ValueAnimator animator) {
		mList.remove(animator);
	}
	
	public static interface OnTimeChangedListener {
		void onTimeChanged(int currentTime);
	};
	
	private OnTimeChangedListener mOnTimeChangedListener;
	
	public void setOnTimeChangedListener(OnTimeChangedListener listener) {
		mOnTimeChangedListener = listener;
	}
}