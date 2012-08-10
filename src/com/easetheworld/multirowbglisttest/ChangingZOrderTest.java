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

package com.easetheworld.multirowbglisttest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easetheworld.multirowbglisttest.CanvasView.OnDrawListener;

import dev.easetheworld.paintanimator.CanvasLayerManager;
import dev.easetheworld.paintanimator.CanvasLayerManager.CanvasLayer;
import dev.easetheworld.paintanimator.PaintAnimator;
import dev.easetheworld.paintanimator.PaintAnimatorSet;

public class ChangingZOrderTest extends Activity {
	
	private CanvasView mCanvasView;
	private CanvasLayerManager mCanvasLayerManager;
	
	private Paint mPaintR;
	private Paint mPaintB;
	
	private PaintAnimatorSet mPaintAnimatorSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons_canvas);
        
        TextView text1 = (TextView)findViewById(android.R.id.text1);
        text1.setText("Change the z-order of overlapped layers by alpha and drawing order.");
        
        Button button1 = (Button)findViewById(android.R.id.button1);
        button1.setText("Blue to the top.");
        button1.setOnClickListener(mClickListener);
        
        Button button2 = (Button)findViewById(android.R.id.button2);
        button2.setText("Red to the top.");
        button2.setOnClickListener(mClickListener);
        
        mCanvasView = (CanvasView)findViewById(R.id.canvas);
        mCanvasView.setOnClickListener(mClickListener);
        mCanvasView.setOnDrawListener(mDrawListener);
        
        mPaintR = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintR.setColor(Color.RED);
        mPaintR.setAlpha(0xff);
        
        mPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintB.setColor(Color.BLUE);
        mPaintB.setAlpha(0x80);
        
        mPaintAnimatorSet = new PaintAnimatorSet();
        mPaintAnimatorSet.add(PaintAnimator.ofAlpha(mPaintR, 0x80));
        mPaintAnimatorSet.add(PaintAnimator.ofAlpha(mPaintB, 0xff));
        mPaintAnimatorSet.setDuration(1000);
        mPaintAnimatorSet.setOnInvalidateListener(new PaintAnimatorSet.OnInvalidateListener() {
			@Override
			public void onInvalidate() {
				mCanvasView.invalidate();
			}
        });
        
		mCanvasLayerManager = new CanvasLayerManager();
		mCanvasLayerManager.add(new CanvasLayer() {
			@Override
			public void onDraw(View v, Canvas canvas) {
				int ux = v.getWidth() / 9;
				int uy = v.getHeight() / 9;
				canvas.drawRect(ux*1, uy*3, ux*4, uy*6, mPaintR);
				canvas.drawRect(ux*5, uy*3, ux*8, uy*6, mPaintR);
			}

			@Override
			public float getZOrder() {
				return 1f - mPaintAnimatorSet.getAnimatedFraction();
			}
		});
		
		mCanvasLayerManager.add(new CanvasLayer() {
			@Override
			public void onDraw(View v, Canvas canvas) {
				int ux = v.getWidth() / 9;
				int uy = v.getHeight() / 9;
				canvas.drawRect(ux*3, uy*1, ux*6, uy*4, mPaintB);
				canvas.drawRect(ux*3, uy*5, ux*6, uy*8, mPaintB);
			}

			@Override
			public float getZOrder() {
				return mPaintAnimatorSet.getAnimatedFraction();
			}
		});
    }
    
    private CanvasView.OnDrawListener mDrawListener = new OnDrawListener() {
    	@Override
    	public void onDraw(View v, Canvas canvas) {
    		mCanvasLayerManager.draw(v, canvas);
    	}
    };
    
    private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case android.R.id.button1:
				mPaintAnimatorSet.animate(true);
				break;
			case android.R.id.button2:
				mPaintAnimatorSet.animate(false);
				break;
			case R.id.canvas:
				Toast.makeText(v.getContext(), "Clicked.", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}