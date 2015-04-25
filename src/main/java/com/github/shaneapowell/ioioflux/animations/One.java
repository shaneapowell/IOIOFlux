package com.github.shaneapowell.ioioflux.animations;


import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;

/************************************************************
 * Light up a single pixel.  The other pixels in the Flux Cap are not touched.
 ***********************************************************/
public class One extends TimedAnimation
{
	protected RGB mColor = RGB.BLACK;
	private FluxCap.ARM mArm;
	private int mRing;

	/**********************************************************
	 *
	 * @param color
	 ***********************************************************/
	public One(RGB color, double secondsPerCycle, int cycles, FluxCap.ARM arm, int ring)
	{
		super(secondsPerCycle, cycles);
		this.mColor = color;
		this.mArm = arm;
		this.mRing = ring;
	}

	/********************************************
	 *
	 *********************************************/
	public One(One o)
	{
		super(o);
		this.mArm = o.mArm;
		this.mRing = o.mRing;
	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new One(this);
	}

	/********************************************
	 *
	 *********************************************/
	@Override
	public void onNewCycle()
	{
		// Do Nothing
	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	@Override
	public void write(FluxCap flux)
	{
		writePixel(flux, this.mArm, mRing, this.mColor);
	}
}
