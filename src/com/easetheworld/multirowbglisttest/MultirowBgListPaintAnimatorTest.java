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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MultirowBgListPaintAnimatorTest extends Activity {
	
	private AniListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multirow_bg_list_paint_animator);
        
        String[] data = new String[100];
        for (int i=0; i<data.length; i++)
        	data[i] = "Data "+(i+1);
        
        mList = (AniListView)findViewById(android.R.id.list);
        mList.setAdapter(new MyAdapter(this, data));
        mList.setOnScrollListener(new OnScrollListener() {
        	private boolean mIsScrolling = false;
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				boolean isScrolling = scrollState != OnScrollListener.SCROLL_STATE_IDLE;
				if (isScrolling != mIsScrolling) {
					mList.animate(isScrolling);
					mIsScrolling = isScrolling;
				}
			}
        });
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
    	
    	private AniListView mParent;
    	
		public MultirowBackgroundView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// to get current position, use parent ListView.
			if (mParent == null)
				mParent = (AniListView)getParent();
			int pos = mParent.getPositionForView(this);
			
			Paint paint = mParent.getItemPaint();
			
			// text
			int spanRows = 6;
			int a = pos / spanRows * spanRows + 1;
			int b = a + spanRows - 1;
			drawMultirowBackgroundText(canvas, a + "-" + b, paint, spanRows, 4, pos % spanRows, getWidth() / 2);
			
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