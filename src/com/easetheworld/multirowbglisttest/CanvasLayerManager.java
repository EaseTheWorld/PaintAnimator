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

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.view.View;

public class CanvasLayerManager {
	
	private ArrayList<CanvasLayer> mCanvasLayerList;
	
	public CanvasLayerManager() {
		mCanvasLayerList = new ArrayList<CanvasLayer>();
	}

	public final void draw(View v, Canvas canvas) {
		// sort before every draw because z-order can be changed anytime.
		if (mCanvasLayerList.size() == 2) { // most common case
			if (mCanvasLayerList.get(0).compareTo(mCanvasLayerList.get(1)) > 0)
				Collections.swap(mCanvasLayerList, 0, 1);
		} else if (mCanvasLayerList.size() > 2) {
			Collections.sort(mCanvasLayerList);
		}
		for (CanvasLayer obj : mCanvasLayerList)
			obj.onDraw(v, canvas);
	}

	public final void add(CanvasLayer obj) {
		mCanvasLayerList.add(obj);
	}

	public final void remove(CanvasLayer obj) {
		mCanvasLayerList.remove(obj);
	}
	
	public static abstract class CanvasLayer implements Comparable<CanvasLayer> {

		public abstract void onDraw(View v, Canvas canvas);

		protected int getZOrder() {
			return 0; // default z-order is 0
		}

		@Override
		public final int compareTo(CanvasLayer that) {
			final float thisZ = this.getZOrder();
			final float thatZ = that.getZOrder();
			if (thisZ > thatZ)
				return 1;
			else if (thisZ < thatZ)
				return -1;
			else
				return 0;
		}
	}
}