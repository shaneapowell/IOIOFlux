package com.github.shaneapowell.ioioflux;

import android.graphics.Color;

/**********************************************************
 * A class to represent a single pixel in our FluxCap.
 ***********************************************************/
public class Pixel
{
	public static final int BYTES_PER_PIXEL = 3;
	private static final int R = 0;
	private static final int G = 1;
	private static final int B = 2;

	private RGB mColor = RGB.BLACK;
	private byte[] mRGB = new byte[] {0 ,0, 0};


	/******************************************************
	 * A default pixel is all off.  0,0,0
	 *******************************************************/
	public Pixel()
	{
		this(new RGB(Color.BLACK));
	}


	/******************************************************
	 *
	 *******************************************************/
	public Pixel(RGB color)
	{
		this.setColor(color);
	}

	/******************************************************
	 *
	 * @param color
	 *******************************************************/
	public void setColor(RGB color)
	{
		this.mColor = color;
		this.mRGB[R] = (byte)color.red();
		this.mRGB[G] = (byte)color.green();
		this.mRGB[B] = (byte)color.blue();
	}

	/******************************************************
	 *
	 * @param src
	 *******************************************************/
	public void setColor(Pixel src)
	{
		this.setColor(src.getColor());
	}

	/******************************************************
	 *
	 * @param pixelToAdd
	 *******************************************************/
	public void add(Pixel pixelToAdd)
	{
		int r = this.mColor.red() + pixelToAdd.mColor.red();
		int g = this.mColor.green() + pixelToAdd.mColor.green();
		int b = this.mColor.blue() + pixelToAdd.mColor.blue();
		this.setColor(new RGB(r, g, b));
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public RGB getColor()
	{
		return mColor;
	}

	/******************************************************
	 *
	 * @param dest
	 * @param start
	 * @return the number of bytes added to the dest array.
	 *******************************************************/
	public int write(byte[] dest, int start)
	{
		dest[start] = mRGB[R];
		dest[start+1] = mRGB[G];
		dest[start+2] = mRGB[B];
		return 3;
	}


}
