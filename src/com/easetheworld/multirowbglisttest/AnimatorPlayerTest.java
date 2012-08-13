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

package com.easetheworld.multirowbglisttest;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.nineoldandroids.animation.ObjectAnimator;

import dev.easetheworld.animator.AnimatorPlayer;
import dev.easetheworld.animator.PaintAnimator;

/**
 * This application demonstrates the seeking capability of ValueAnimator. The SeekBar in the
 * UI allows you to set the position of the animation. Pressing the Run button will play from
 * the current position of the animation.
 */
public class AnimatorPlayerTest extends Activity {
	
	private CanvasView mCanvasView;
	
	private Paint mPaint;
	private AnimatorPlayer mAnimatorPlayer;
	private RadioGroup mRadioGroup;

    private static final int DURATION = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_player);
        
        mCanvasView = (CanvasView)findViewById(R.id.canvas);
        mCanvasView.setOnDrawListener(mDrawListener);
        
        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(getScaledTextSize(this, 20));
        
        mAnimatorPlayer = new AnimatorPlayer(DURATION);
        
        // paint color
        mAnimatorPlayer.add(PaintAnimator.ofColor(mPaint, Color.RED));
        
        // paint text size
        mAnimatorPlayer.add(PaintAnimator.ofTextSize(mPaint, getScaledTextSize(this, 40)).setInvalidateViews(mCanvasView));
        
        // text x position with linear interpolator
        ObjectAnimator animText1 = ObjectAnimator.ofFloat(findViewById(R.id.text1), "x", 0, getResources().getDisplayMetrics().widthPixels);
        animText1.setInterpolator(null);
        mAnimatorPlayer.add(animText1);
        
        // text x position with accelerate interpolator
        ObjectAnimator animText2 = ObjectAnimator.ofFloat(findViewById(R.id.text2), "x", 0, getResources().getDisplayMetrics().widthPixels);
        animText2.setInterpolator(new AccelerateInterpolator());
        mAnimatorPlayer.add(animText2);
        
        // text x position with decelerate interpolator
        ObjectAnimator animText3 = ObjectAnimator.ofFloat(findViewById(R.id.text3), "x", 0, getResources().getDisplayMetrics().widthPixels);
        animText3.setInterpolator(new DecelerateInterpolator());
        mAnimatorPlayer.add(animText3);
        
	    SeekBar seekBar = (SeekBar) findViewById(android.R.id.progress);
        seekBar.setMax(DURATION);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            	switch(mRadioGroup.getCheckedRadioButtonId()) {
            	case android.R.id.button1:
	            	mAnimatorPlayer.seekTo(progress);
            		break;
            	case android.R.id.button2:
	            	mAnimatorPlayer.playTo(progress);
            		break;
            	}
            }
        });
    }
    
    private CanvasView.OnDrawListener mDrawListener = new CanvasView.OnDrawListener() {
    	@Override
    	public void onDraw(View v, Canvas canvas) {
    		canvas.drawText("Paint", v.getWidth() / 2, v.getHeight() / 2, mPaint);
    	}
    };
	
	private static float getScaledTextSize(Context context, int size) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, displayMetrics);
	}
}