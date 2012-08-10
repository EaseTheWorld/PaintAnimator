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

import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

public class PaintAnimatorSet extends PaintAnimator {
	
	private LinkedList<PaintAnimator> mList = new LinkedList<PaintAnimator>();
	
	public PaintAnimatorSet() {
		super();
		setFloatValues(0f, 1f);
		addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				long currentTime = valueAnimator.getCurrentPlayTime();
				if (currentTime < 0) currentTime = 0;
				else if (currentTime > valueAnimator.getDuration()) currentTime = valueAnimator.getDuration();
				for (PaintAnimator ppa : mList)
					ppa.setCurrentPlayTime((mPlayForward ? currentTime : (valueAnimator.getDuration() - currentTime)));
				if (mViews != null) {
					for (View v : mViews)
						v.invalidate();
				}
				if (mOnInvalidateListener != null)
					mOnInvalidateListener.onInvalidate();
			}
		});
	}

	@Override
	public ValueAnimator setDuration(long duration) {
		for (PaintAnimator ppa : mList)
			ppa.setDuration(duration);
		return super.setDuration(duration);
	}

	public void add(PaintAnimator paintAnimator) {
		paintAnimator.setDuration(getDuration());
		mList.add(paintAnimator);
	}
	
	public void remove(PaintAnimator paintAnimator) {
		mList.remove(paintAnimator);
	}
	
	public static interface OnInvalidateListener {
		void onInvalidate();
	};
	
	private OnInvalidateListener mOnInvalidateListener;
	
	public void setOnInvalidateListener(OnInvalidateListener listener) {
		mOnInvalidateListener = listener;
	}
}