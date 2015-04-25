package com.github.shaneapowell.ioioflux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**********************************************************
 *
 ***********************************************************/
public class FluxCapView extends RelativeLayout
{


	private FluxCap mFluxCap;

	private View mCenterAnchorView;

	private PixelView[] mBottomArm;
	private PixelView[] mLeftArm;
	private PixelView[] mRightArm;

	private int mPixelPadding = 8;

	/******************************************************
	 * Don't create a new view, instead have the FluxCap object
	 * create one for you.
	 * @param context
	 * @param attrs
	 *******************************************************/
	protected FluxCapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setBackgroundColor(Color.BLACK);
	}


	/******************************************************
	 *
	 * @param fluxCap
	 *******************************************************/
	protected void setFluxCap(FluxCap fluxCap)
	{
		this.mFluxCap = fluxCap;
		createPixels(fluxCap.getArmLength());
	}

	/******************************************************
	 *
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 *******************************************************/
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

		/* A flux cap pixel is sized such that the width and height can fit 2 arms each.
		 * With 1 more for each end, and 1 more for the center */
		int pixelWidth =  width / ((mFluxCap.getArmLength() * 2) + 3) - (mPixelPadding);
		int pixelPadd = 1;

		int anchorWidth = pixelWidth;
		int anchorHeight = pixelWidth;

		for (int index = 0; index < getChildCount(); index++)
		{
			View child = getChildAt(index);
			child.getLayoutParams().height = pixelWidth;
			child.getLayoutParams().width = pixelWidth;
			child.setPadding(pixelPadd, pixelPadd, pixelPadd, pixelPadd);
		}

		/* Reset the anchor size now */
		this.mCenterAnchorView.getLayoutParams().width = anchorWidth;
		this.mCenterAnchorView.getLayoutParams().height = anchorHeight;
		this.mCenterAnchorView.setPadding(0, 0, 0, 0);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/******************************************************
	 *
	 *******************************************************/
	private void createPixels(int armLength)
	{
		this.mCenterAnchorView = new View(getContext(), null);
		this.mBottomArm = new PixelView[armLength];
		this.mLeftArm = new PixelView[armLength];
		this.mRightArm = new PixelView[armLength];


		PixelView pixel = null;
		RelativeLayout.LayoutParams pixelParams;

		/* Center Anchor */
		pixelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.mCenterAnchorView.setId(ViewIdGenerator.generateViewId());
		pixelParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		this.addView(this.mCenterAnchorView, pixelParams);

		int anchorId;

		/* Bottom */
		for (int index = 0; index < armLength; index++)
		{
			pixel = new PixelView(getContext(), null);
			pixelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			pixelParams.setMargins(0, mPixelPadding, 0, mPixelPadding);
			pixel.setId(ViewIdGenerator.generateViewId());
			this.mBottomArm[index] = pixel;

			anchorId = index == 0 ? this.mCenterAnchorView.getId() : this.mBottomArm[index-1].getId();

			pixelParams.addRule(RelativeLayout.BELOW, anchorId);
			pixelParams.addRule(RelativeLayout.ALIGN_LEFT, anchorId);
			this.addView(pixel, pixelParams);
		}

		/* Left */
		for (int index = 0; index < armLength; index++)
		{
			pixel = new PixelView(getContext(), null);
			pixelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			pixelParams.setMargins(0, 0, mPixelPadding, 0);
			pixel.setId(ViewIdGenerator.generateViewId());
			this.mLeftArm[index] = pixel;

			anchorId = index == 0 ? this.mCenterAnchorView.getId() : this.mLeftArm[index-1].getId();

			pixelParams.addRule(RelativeLayout.ABOVE, anchorId);
			pixelParams.addRule(RelativeLayout.LEFT_OF, anchorId);
			this.addView(pixel, pixelParams);
		}

		/* Right */
		for (int index = 0; index < armLength; index++)
		{
			pixel = new PixelView(getContext(), null);
			pixelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			pixelParams.setMargins(mPixelPadding, 0, 0, 0);
			pixel.setId(ViewIdGenerator.generateViewId());
			this.mRightArm[index] = pixel;

			anchorId = index == 0 ? this.mCenterAnchorView.getId() : this.mRightArm[index-1].getId();

			pixelParams.addRule(RelativeLayout.ABOVE, anchorId);
			pixelParams.addRule(RelativeLayout.RIGHT_OF, anchorId);
			this.addView(pixel, pixelParams);
		}


	}



	/******************************************************
	 *
	 * @param canvas
	 *******************************************************/
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);


		for (int index = 0; index < mFluxCap.getArmLength(); index++)
		{
			updatePixel(FluxCap.ARM.BOTTOM, mBottomArm, index);
			updatePixel(FluxCap.ARM.LEFT, mLeftArm, index);
			updatePixel(FluxCap.ARM.RIGHT, mRightArm, index);
		}


		/* Draw the tube lines */
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);


		PixelView lb = this.mLeftArm[0];
		PixelView lt = this.mLeftArm[this.mLeftArm.length - 1];
		PixelView rb = this.mRightArm[0];
		PixelView rt = this.mRightArm[this.mLeftArm.length - 1];
		PixelView bt = this.mBottomArm[0];
		PixelView bb = this.mBottomArm[this.mLeftArm.length - 1];

		int pWidth = lb.getWidth();
		int hWidth = pWidth / 2;

		/* Left Tube, clockwise starting the point closest to the middle */
//		canvas.drawLine(lb.getRight(), lb.getBottom(), lb.getLeft(), lb.getBottom(), paint);
		canvas.drawLine(lb.getLeft() + hWidth, lb.getBottom() + hWidth, lt.getLeft() - hWidth, lt.getBottom() - hWidth, paint);
		canvas.drawLine(lt.getLeft() - hWidth, lt.getBottom() - hWidth, lt.getLeft() + hWidth, lt.getTop() - hWidth, paint);
		canvas.drawLine(lt.getLeft() + hWidth, lt.getTop() - hWidth, lb.getRight() + hWidth, lb.getTop() + hWidth, paint);
		canvas.drawLine(lb.getRight() + hWidth, lb.getTop() + hWidth, lb.getLeft() + hWidth, lb.getBottom() + hWidth, paint);

		/* Right Tube, starting at center point */
		canvas.drawLine(rb.getLeft() - hWidth, rb.getTop() + hWidth, rt.getLeft() + hWidth, rt.getTop() - hWidth, paint);
		canvas.drawLine(rt.getLeft() + hWidth, rt.getTop() - hWidth, rt.getRight() + hWidth, rt.getTop() + hWidth, paint);
		canvas.drawLine(rt.getRight() + hWidth, rt.getTop() + hWidth, rb.getRight() - hWidth, rb.getBottom() + hWidth, paint);
		canvas.drawLine(rb.getRight() - hWidth, rb.getBottom() + hWidth, rb.getLeft() - hWidth, rb.getTop() + hWidth, paint);

		/* Bottom Tube */
		hWidth /= 2;
		canvas.drawLine(bt.getLeft() - hWidth, bt.getTop() - hWidth, bt.getRight() + hWidth, bt.getTop() - hWidth, paint);
		canvas.drawLine(bt.getRight() + hWidth, bt.getTop() - hWidth, bb.getRight() + hWidth, bb.getBottom() + hWidth, paint);
		canvas.drawLine(bb.getRight() + hWidth, bb.getBottom() + hWidth, bb.getLeft() - hWidth, bb.getBottom() + hWidth, paint);
		canvas.drawLine(bb.getLeft() - hWidth, bb.getBottom() + hWidth, bt.getLeft() - hWidth, bt.getTop() - hWidth, paint);



	}


	/******************************************************
	 *
	 * @param arm
	 * @param pixels
	 * @param index
	 *******************************************************/
	private void updatePixel(FluxCap.ARM arm, PixelView[] pixels, int index)
	{
		PixelView pixelView = pixels[index];
		Pixel pixel = mFluxCap.getPixel(arm, index);
		RGB rgb = pixel. getColor();
		pixelView.setPixelColor(rgb.color());
		pixelView.invalidate();
	}

}
