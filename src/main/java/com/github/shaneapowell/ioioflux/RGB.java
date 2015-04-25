package com.github.shaneapowell.ioioflux;

import android.graphics.Color;

/**********************************************************
 * A convenient immutable class around the android Color/int class.
 ***********************************************************/
public class RGB
{

	public static final RGB OFF         = new RGB(Color.BLACK);
	public static final RGB ON          = new RGB(Color.WHITE);

	public static final RGB BLACK       = OFF;
	public static final RGB WHITE       = ON;
	public static final RGB RED         = new RGB(Color.RED);
	public static final RGB GREEN       = new RGB(Color.GREEN);
	public static final RGB BLUE        = new RGB(Color.BLUE);
	public static final RGB PURPLE      = new RGB(0xff, 0x00, 0xff);
	public static final RGB ORANGE      = new RGB(0xff, 0x66, 0x00);


	private static final int CMAX = 0xff;
	private static final int CMIN = 0x00;

	private int color;


	/******************************************************
	 *
	 * @param color
	 *******************************************************/
	public RGB(RGB color)
	{
		this(color.color);
	}

	/******************************************************
	 *
	 * @param color
	 *******************************************************/
	public RGB(int color)
	{
		this.color = color;
	}

	/******************************************************
	 *
	 * @param r
	 * @param g
	 * @param b
	 *******************************************************/
	public RGB(float r, float g, float b)
	{
		this((int)r, (int)g, (int)b);
	}


	/******************************************************
	 *
	 * @param r
	 * @param g
	 * @param b
	 *******************************************************/
	public RGB(int r, int g, int b)
	{
		this.color = Color.rgb( Math.min(CMAX, Math.max(CMIN, r)),
								Math.min(CMAX, Math.max(CMIN, g)),
								Math.min(CMAX, Math.max(CMIN, b)) );
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int red()
	{
		return Color.red(color);
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int green()
	{
		return Color.green(color);
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int blue()
	{
		return Color.blue(color);
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int color()
	{
		return this.color;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	public String toString()
	{
		return String.format("%#x,%#x,%#x]", Color.red(color), Color.green(color), Color.blue(color));
	}
}
