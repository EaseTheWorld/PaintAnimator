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
import android.widget.SeekBar;
import dev.easetheworld.paintanimator.PaintAnimator;

/**
 * This application demonstrates the seeking capability of ValueAnimator. The SeekBar in the
 * UI allows you to set the position of the animation. Pressing the Run button will play from
 * the current position of the animation.
 */
public class AnimationSeeking extends Activity {
	
	private CanvasView mCanvasView;
	
	private Paint mPaint;
	private PaintAnimator mPaintAnimator;

    private static final int DURATION = 1500;
    private SeekBar mSeekBar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_seeking);
        
        mCanvasView = (CanvasView)findViewById(R.id.canvas);
        mCanvasView.setOnDrawListener(mDrawListener);
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(getScaledTextSize(this, 20));
        
        mPaintAnimator = PaintAnimator.ofTextSize(mPaint, getScaledTextSize(this, 40));
        mPaintAnimator.setInvalidateViews(mCanvasView);
        mPaintAnimator.setDuration(DURATION);

        findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mPaintAnimator.start();
            }
        });

        mSeekBar = (SeekBar) findViewById(android.R.id.progress);
        mSeekBar.setMax(DURATION);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	            mPaintAnimator.setCurrentPlayTime(progress);
            }
        });
    }
    
    private CanvasView.OnDrawListener mDrawListener = new CanvasView.OnDrawListener() {
    	@Override
    	public void onDraw(View v, Canvas canvas) {
    		canvas.drawText("Paint Animator", v.getWidth() / 2, v.getHeight() / 2, mPaint);
    	}
    };
	
	private static float getScaledTextSize(Context context, int size) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, displayMetrics);
	}
}