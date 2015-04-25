package com.github.shaneapowell.ioioflux.animations;


import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;

/************************************************************
 *
 ***********************************************************/
public class Solid extends TimedAnimation
{
	protected RGB mColor = RGB.BLACK;

	/**********************************************************
	 *
	 * @param color
	 ***********************************************************/
	public Solid(RGB color, double secondsPerCycle, int cycles)
	{
		super(secondsPerCycle, cycles);
		this.mColor = color;
	}

	/******************************************************
	 * Build a one frame, one cycle solid animation
	 * @param color
	 *******************************************************/
	public Solid(RGB color)
	{
		this(color, ONE_FRAME, 1);
	}

	/********************************************
	 *
	 *********************************************/
	public Solid(Solid s)
	{
		super(s);
		this.mColor = new RGB(s.mColor);
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new Solid(this);
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
		for (int index = 0; index < flux.getArmLength(); index++)
		{
			writePixel(flux, FluxCap.ARM.BOTTOM, index, this.mColor);
			writePixel(flux, FluxCap.ARM.LEFT, index, this.mColor);
			writePixel(flux, FluxCap.ARM.RIGHT, index, this.mColor);
		}

	}
}
