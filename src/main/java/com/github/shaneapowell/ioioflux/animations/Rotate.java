package com.github.shaneapowell.ioioflux.animations;


import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;

import java.util.ArrayList;

/************************************************************
 * Expand each ring from the inside out with the provided list of colors.
 ***********************************************************/
public class Rotate extends TimedAnimation
{

	private ArrayList<RGB> mColors = new ArrayList<RGB>();
	private ArrayList<RGB> mRotatedColors;

	private boolean mClockwise = true;

	/******************************************************
	 * rotate clockwise by default.
	 * @param secondsPerCycle
	 * @param cycles
	 *******************************************************/
	public Rotate(RGB bottom, RGB left, RGB right, double secondsPerCycle, int cycles)
	{
		super(secondsPerCycle, cycles);
		mColors.add(bottom);
		mColors.add(left);
		mColors.add(right);
		onNewCycle();
	}

	/***********************************************************
	 *
	 * @param name
	 * @param secondsPerCycle
	 * @param cycles
	 * @param clockwise
	 *************************************************************/
	public Rotate(String name, RGB bottom, RGB left, RGB right, double secondsPerCycle, int cycles, boolean clockwise)
	{
		this(bottom, left, right, secondsPerCycle, cycles);
		setClockwise(clockwise);
	}

	/******************************************************
	 *
	 *******************************************************/
	public Rotate(Rotate r)
	{
		super(r);
		this.mColors = new ArrayList<RGB>(r.mColors);
		this.mRotatedColors = new ArrayList<RGB>(mColors);
		this.mClockwise = r.mClockwise;
	}


	/******************************************************
	 *
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new Rotate(this);
	}

	/******************************************************
	 *
	 *******************************************************/
	public void setClockwise(boolean c)
	{
		this.mClockwise = c;
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void onNewCycle()
	{
		mRotatedColors = new ArrayList<RGB>(mColors);
	}



	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{

		/* We need to step first with this animation so we don't rotate the color on the first frame with the % calc below */
		super.step(flux, fps);

		/* 3 times per cycle rotation */
		if (getFrame() % (getFramesPerCycle(fps) / 3) == 0)
		{
			if (mClockwise)
			{
				mRotatedColors.add(0, mRotatedColors.remove(mRotatedColors.size() - 1));
			}
			else
			{
				mRotatedColors.add(mRotatedColors.remove(0));
			}
		}

	}


	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	public void write(FluxCap flux)
	{

		for (int index = 0; index < flux.getArmLength(); index++)
		{
			writePixel(flux, FluxCap.ARM.BOTTOM, index, mRotatedColors.get(0));
			writePixel(flux, FluxCap.ARM.LEFT, index, mRotatedColors.get(1));
			writePixel(flux, FluxCap.ARM.RIGHT, index, mRotatedColors.get(2));
		}

	}


}
