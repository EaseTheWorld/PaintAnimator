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

public class PaintAnimatorSet2 extends PaintAnimator2 {
	
	private LinkedList<PaintAnimator2> mList = new LinkedList<PaintAnimator2>();
	
	public PaintAnimatorSet2() {
		super();
		setFloatValues(0f, 1f);
		addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				for (PaintAnimator2 ppa : mList)
					ppa.setCurrentPlayTime((mPlayForward ? valueAnimator.getCurrentPlayTime() : (valueAnimator.getDuration() - valueAnimator.getCurrentPlayTime())));
				if (mViews != null) {
					for (View v : mViews)
						v.invalidate();
				}
			}
		});
	}

	@Override
	public ValueAnimator setDuration(long duration) {
		for (PaintAnimator2 ppa : mList)
			ppa.setDuration(duration);
		return super.setDuration(duration);
	}

	public void add(PaintAnimator2 paintAnimator) {
		paintAnimator.setDuration(getDuration());
		mList.add(paintAnimator);
	}
	
	public void remove(PaintAnimator2 paintAnimator) {
		mList.remove(paintAnimator);
	}
}