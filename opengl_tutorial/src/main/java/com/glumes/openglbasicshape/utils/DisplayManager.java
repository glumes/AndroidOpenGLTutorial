package com.glumes.openglbasicshape.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by bill on 2015/8/15.
 */
public class DisplayManager {
	
	private static final String TAG = DisplayManager.class.getSimpleName();
	
	private static volatile DisplayManager sInstance;
	
	private int mScreenWidth;
	private int mScreenHeight;
	private float mDensity;
	private float mScaledDensity;
	
	private DisplayManager() {
		
	}

	/**
	 * Init the relevant field
	 */
	public void init(Context context) {
		Point point = new Point();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getSize(point);
		mScreenWidth = point.x;
		mScreenHeight = point.y;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		mDensity = metrics.density;
		mScaledDensity = metrics.scaledDensity;
		Log.d(TAG, "mScreenWidth:" + mScreenWidth + " - mScreenHeight:" + mScreenHeight + " - mDensity:" + mDensity + " - mScaledDensity:" + mScaledDensity);
	}

	/**
	 * After first getInstance() called, you should call {@link #init(Context)} first!
	 */
	public static DisplayManager getInstance() {
		if (sInstance == null) {
			synchronized (DisplayManager.class) {
				if (sInstance == null) {
					sInstance = new DisplayManager();
				}
			}
		}
		return sInstance;
	}

	public int getmScreenWidth() {
		return mScreenWidth;
	}

	public int getmScreenHeight() {
		return mScreenHeight;
	}
}
