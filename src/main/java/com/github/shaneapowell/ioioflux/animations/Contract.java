package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.RGB;

/**********************************************************
 *
 ***********************************************************/
public class Contract extends Expand
{

	/******************************************************
	 *
	 * @param colors Conrary to the Expand animation, these colors contract inwards from the outer ring.
	 *               The color order here is just as critical to understand.  The 0th element in the array
	 *               of colors, is the first one to be lit on the outer most ring.  Think of it like the
	 *               array of colors is pulled back into view from outside the rings.
	 * @param startRingIndex
	 * @param endRingIndex
	 * @param secondsPerCycle
	 * @param cycles
	 *******************************************************/
	public Contract(int startRingIndex, int endRingIndex, double secondsPerCycle, int cycles, RGB ... colors)
	{
		super(startRingIndex, endRingIndex, secondsPerCycle, cycles, colors);
		setReverse(true);
	}

	/******************************************************
	 *
	 *******************************************************/
	public Contract(Contract c)
	{
		super(c);
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new Contract(this);
	}
}
