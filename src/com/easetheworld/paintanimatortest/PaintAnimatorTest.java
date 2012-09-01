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
 * https://github.com/EaseTheWorld/PaintAnimator
 */

package com.easetheworld.paintanimatortest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import dev.easetheworld.animator.PaintAnimator;

public class PaintAnimatorTest extends Activity {
	
	private CanvasView mCanvasView;
	
	private Paint mPaint1;
	private Paint mPaint2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_animator);
        
        mCanvasView = (CanvasView)findViewById(R.id.canvas);
        mCanvasView.setOnClickListener(mClickListener);
        mCanvasView.setOnDrawListener(mDrawListener);
        
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.BLUE);
        mPaint1.setTextAlign(Paint.Align.CENTER);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setTextSize(getScaledTextSize(this, 30));
        
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(Color.GREEN);
        mPaint2.setTextAlign(Paint.Align.CENTER);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setTextSize(getScaledTextSize(this, 30));
        
        final PaintAnimator paint1ColorAnimator = PaintAnimator.ofColor(mPaint1, Color.BLUE, Color.YELLOW, Color.RED);
        paint1ColorAnimator.setDuration(1000);
        paint1ColorAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkColor)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint1ColorAnimator.animate(isChecked);
			}
		});
        
        final PaintAnimator paint1TextSizeAnimator = PaintAnimator.ofTextSize(mPaint1, getScaledTextSize(this, 45));
        paint1TextSizeAnimator.setDuration(1000);
        paint1TextSizeAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkTextSize)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint1TextSizeAnimator.animate(isChecked);
			}
		});
        
        final PaintAnimator paint1StrokeWidthAnimator = PaintAnimator.ofStrokeWidth(mPaint1, 4f);
        paint1StrokeWidthAnimator.setDuration(1000);
        paint1StrokeWidthAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkStrokeWidth)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint1StrokeWidthAnimator.animate(isChecked);
			}
		});
        
        final PaintAnimator paint2AlphaAnimator = PaintAnimator.ofAlpha(mPaint2, 0x40);
        paint2AlphaAnimator.setDuration(1000);
        paint2AlphaAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkAlpha)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint2AlphaAnimator.animate(isChecked);
			}
		});
        
        final PaintAnimator paint2TextScaleXAnimator = PaintAnimator.ofTextScaleX(mPaint2, 2f);
        paint2TextScaleXAnimator.setDuration(1000);
        paint2TextScaleXAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkTextScaleX)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint2TextScaleXAnimator.animate(isChecked);
			}
		});
        
        final PaintAnimator paint2TextSkewXAnimator = PaintAnimator.ofTextSkewX(mPaint2, 1f);
        paint2TextSkewXAnimator.setDuration(1000);
        paint2TextSkewXAnimator.setInvalidateViews(mCanvasView);
        
        ((CheckBox)findViewById(R.id.checkTextSkewX)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				paint2TextSkewXAnimator.animate(isChecked);
			}
		});
        
    }
    
    private CanvasView.OnDrawListener mDrawListener = new CanvasView.OnDrawListener() {
    	@Override
    	public void onDraw(View v, Canvas canvas) {
    		canvas.drawText("Hello", v.getWidth() / 4, v.getHeight() / 3, mPaint1);
    		canvas.drawText("World", v.getWidth() / 4, v.getHeight() * 2 / 3, mPaint1);
    		canvas.drawRect(40, 40, v.getWidth() / 2 - 20, v.getHeight() - 40, mPaint1);
    		
    		canvas.drawText("Hello", v.getWidth() * 3 / 4, v.getHeight() / 3, mPaint2);
    		canvas.drawText("World", v.getWidth() * 3 / 4, v.getHeight() * 2 / 3, mPaint2);
    		canvas.drawRect(v.getWidth() / 2 + 20, 40, v.getWidth() - 40, v.getHeight() - 40, mPaint2);
    	}
    };
    
    private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.canvas:
				Toast.makeText(v.getContext(), "Clicked.", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private static float getScaledTextSize(Context context, int size) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, displayMetrics);
	}
}