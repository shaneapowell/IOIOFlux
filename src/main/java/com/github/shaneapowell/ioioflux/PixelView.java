package com.github.shaneapowell.ioioflux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**********************************************************
 *
 **********************************************************/
public class PixelView extends FrameLayout
{
	private int mPixelColor = Color.GREEN;

	/******************************************************
	 *
	 * @param context
	 * @param attrs
	 *******************************************************/
	public PixelView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setWillNotDraw(false);
	}

	/******************************************************
	 *
	 * @param color
	 *******************************************************/
	public void setPixelColor(int color)
	{
		this.mPixelColor = color;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int getRadius()
	{
		/* Find the best fit for a circle */
		int r = getWidth() / 2 - getPaddingLeft() - getPaddingRight();
		r = Math.min(r, (getHeight() / 2 - getPaddingTop() - getPaddingBottom()));
		return r;
	}

	/******************************************************
	 *
	 * @param canvas
	 *******************************************************/
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		/* Find the best fit for a circle */
		int r = getRadius();


		Paint paint = new Paint();
		paint.setColor(mPixelColor);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, paint);


		paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, paint);

//		paint.setColor(Color.GREEN);
//		canvas.drawRect(1, 1, getWidth()-1, getHeight()-1, paint);

	}
}
