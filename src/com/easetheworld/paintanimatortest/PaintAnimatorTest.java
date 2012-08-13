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
 * https://github.com/EaseTheWorld/PaintAnimatorTest
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
import android.widget.TextView;
import android.widget.Toast;
import dev.easetheworld.animator.PaintAnimator;

public class PaintAnimatorTest extends Activity {
	
	private CanvasView mCanvasView;
	
	private Paint mPaint1;
	private PaintAnimator mPaint1TextSizeAnimator;
	
	private Paint mPaint2;
	private PaintAnimator mPaint2ColorAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkbox_canvas);
        
        TextView text1 = (TextView)findViewById(android.R.id.text1);
        text1.setText("If you use PaintAnimator, each animation is independent.");
        
        mCanvasView = (CanvasView)findViewById(R.id.canvas);
        mCanvasView.setOnClickListener(mClickListener);
        mCanvasView.setOnDrawListener(mDrawListener);
        
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.GREEN);
        mPaint1.setTextAlign(Paint.Align.CENTER);
        mPaint1.setTextSize(getScaledTextSize(this, 30));
        
        mPaint1TextSizeAnimator = PaintAnimator.ofTextSize(mPaint1, getScaledTextSize(this, 60));
        mPaint1TextSizeAnimator.setDuration(1000);
        mPaint1TextSizeAnimator.setInvalidateViews(mCanvasView);
        
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(Color.BLUE);
        mPaint2.setTextAlign(Paint.Align.CENTER);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setTextSize(getScaledTextSize(this, 30));
        
        mPaint2ColorAnimator = PaintAnimator.ofColor(mPaint2, Color.BLUE, Color.YELLOW, Color.RED);
        mPaint2ColorAnimator.setDuration(2000);
        mPaint2ColorAnimator.setInvalidateViews(mCanvasView);
        
        CheckBox button1 = (CheckBox)findViewById(android.R.id.button1);
        button1.setText("Paint1 Text Size : 30 <-> 60");
        button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mPaint1TextSizeAnimator.animate(isChecked);
			}
		});
        
        CheckBox button2 = (CheckBox)findViewById(android.R.id.button2);
        button2.setText("Paint2 Color : Blue <-Yellow-> Red");
        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mPaint2ColorAnimator.animate(isChecked);
			}
		});
        
    }
    
    private CanvasView.OnDrawListener mDrawListener = new CanvasView.OnDrawListener() {
    	@Override
    	public void onDraw(View v, Canvas canvas) {
    		canvas.drawText("Paint1", v.getWidth() / 2, v.getHeight() / 4, mPaint1);
    		canvas.drawText("Paint1", v.getWidth() / 2, v.getHeight() * 3 / 4, mPaint1);
    		canvas.drawText("Paint2", v.getWidth() / 4, v.getHeight() / 2, mPaint2);
    		canvas.drawText("Paint2", v.getWidth() * 3 / 4, v.getHeight() / 2, mPaint2);
    		canvas.drawRect(40, 40, v.getWidth() - 40, v.getHeight() - 40, mPaint2);
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