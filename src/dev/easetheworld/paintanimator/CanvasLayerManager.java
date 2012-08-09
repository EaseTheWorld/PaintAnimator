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

package dev.easetheworld.paintanimator;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.view.View;

public class CanvasLayerManager {
	
	private ArrayList<CanvasLayer> mObjectList;
	
	public CanvasLayerManager() {
		mObjectList = new ArrayList<CanvasLayer>();
	}

	public final void draw(View v, Canvas canvas) {
		Collections.sort(mObjectList); // object's z-order can be changed anytime.
		for (CanvasLayer obj : mObjectList)
			obj.onDraw(v, canvas);
	}

	public final void add(CanvasLayer obj) {
		mObjectList.add(obj);
	}

	public final void remove(CanvasLayer obj) {
		mObjectList.remove(obj);
	}
	
	public static abstract class CanvasLayer implements Comparable<CanvasLayer> {

		public abstract void onDraw(View v, Canvas canvas);

		protected float getZOrder() {
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