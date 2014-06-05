package com.steinerize.android.colortemperature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class FilterView extends View {
	
	private boolean attached;
	private int a;
	private int r;
	private int b;
	private int g;

	public FilterView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawARGB(this.a, this.r, this.b, this.g);
	}
	
	public FilterView setColor(int a, int r, int b, int g) {
		this.a = a;
		this.r = r;
		this.b = b;
		this.g = g;
		
		this.invalidate();
		return this;
	}
	
	public boolean isAttached() {
		return this.attached;
	}
	
	public FilterView attach(WindowManager wm) {
		if (this.isAttached()) return this;
		
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
				
		int flags = LayoutParams.FLAG_NOT_FOCUSABLE | 
				LayoutParams.FLAG_NOT_TOUCH_MODAL | 
				LayoutParams.FLAG_NOT_TOUCHABLE |
				LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	
		LayoutParams layoutParams = new LayoutParams(size.x, size.y + 200, 0, 0, 
				LayoutParams.TYPE_SYSTEM_OVERLAY, flags, PixelFormat.TRANSPARENT); 
		
		this.attached = true;
		wm.addView(this, layoutParams);
		return this;
	}
	
	public FilterView detach(WindowManager wm) {
		wm.removeView(this);
		this.attached = false;
		return this;
	}

}
