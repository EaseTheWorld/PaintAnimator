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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MultirowBgListTest extends Activity {
	
	private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multirow_bg_list);
        
        String[] data = new String[100];
        for (int i=0; i<data.length; i++)
        	data[i] = "Data "+(i+1);
        
        mList = (ListView)findViewById(android.R.id.list);
        mList.setAdapter(new MyAdapter(this, data));
    }

    private static class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, String[] objects) {
			super(context, 0, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new MultirowBackgroundView(parent.getContext());
			}
			MultirowBackgroundView v = (MultirowBackgroundView)convertView;
			v.setText(getItem(position));
			v.setTextSize(24);
			return convertView;
		}
    }
    
    /**
     * To use multirow background, extend the ListView item view and override onDraw().
     * Call drawMultirowBackgroundText() or drawMultirowBackgroundBitmap() before super.onDraw() 
     * so default draw will overlap the background.
     */
    private static class MultirowBackgroundView extends TextView {
    	
    	private ListView mParent;
    	private Paint mPaint;
    	private Bitmap mBitmap;
    	
		public MultirowBackgroundView(Context context) {
			super(context);
			
            mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setColor(0x40ff0000);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// to get current position, use parent ListView.
			if (mParent == null)
				mParent = (ListView)getParent();
			int pos = mParent.getPositionForView(this);
			
			Paint paint = mPaint;
			// text
			int spanRows = 5;
			int a = pos / spanRows * spanRows + 1;
			int b = a + spanRows - 1;
			drawMultirowBackgroundText(canvas, a + "-" + b, paint, spanRows, 3, pos % spanRows, getWidth() / 2);
			
			// image
			spanRows = 4;
			drawMultirowBackgroundBitmap(canvas, mBitmap, paint, spanRows, pos % spanRows, getWidth() - mBitmap.getWidth());
			
			super.onDraw(canvas);
		}
		
        /**
         * draw text background which is laid across spanRows rows.
         * the background is drawn through drawRows rows.
         * to set margin between backgrounds set spanRows bigger than drawRows.
         * currentRow is the zero-based index in spanRows.
         * 
         * y will be calculated from the input.
         * you can set x.
         */
        final private void drawMultirowBackgroundText(Canvas canvas, String text, Paint paint, int spanRows, int drawRows, int currentRow, float x) {
        	float y = (spanRows - drawRows) * getHeight() / 2 + (drawRows - currentRow) * getHeight();
        	paint.setTextSize(drawRows * getHeight());
			canvas.drawText(text, x, y - paint.descent() / 2, paint);
        }
        
        /**
         * draw bitmap background which is laid across spanRows rows.
         * to set margin between backgrounds set spanRows bigger than drawRows.
         * currentRow is the zero-based index in spanRows.
         * 
         * y will be calculated from the input.
         * you can set x.
         */
        final private void drawMultirowBackgroundBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int spanRows, int currentRow, float x) {
        	float y = (spanRows * getHeight() - bitmap.getHeight()) / 2 - currentRow * getHeight();
			canvas.drawBitmap(bitmap, x, y, paint);
        }
    }
}