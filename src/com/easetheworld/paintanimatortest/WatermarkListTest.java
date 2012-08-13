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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.easetheworld.paintanimatortest.WatermarkListView.WatermarkItemTextView;

public class WatermarkListTest extends Activity {
	
	private WatermarkListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watermark_list);
        
        Integer[] data = new Integer[1000];
        for (int i=0; i<data.length; i++)
        	data[i] = i + 1;
        
        mList = (WatermarkListView)findViewById(android.R.id.list);
        mList.setAdapter(new MyAdapter(this, data));
    }

    private static class MyAdapter extends ArrayAdapter<Integer> {

		public MyAdapter(Context context, Integer[] objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WatermarkItemTextView v;
			if (convertView == null) {
				v = new WatermarkItemTextView(parent.getContext());
				v.setTextSize(24);
				v.setSingleLine(true);
			} else {
				v = (WatermarkItemTextView)convertView;
			}
			int data = getItem(position);
			if ((data & 1) == 0)
				v.setText(String.format("%03d is even. Hello world.", data, data));
			else
				v.setText(String.format("%03d is odd. How are you?", data, data));
			WatermarkListView l = (WatermarkListView)parent;
			v.setTextColor(l.getItemFgPaint().getColor());
			return v;
		}
    }
}