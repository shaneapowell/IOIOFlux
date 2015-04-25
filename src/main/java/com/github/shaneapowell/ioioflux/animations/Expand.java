package com.github.shaneapowell.ioioflux.animations;


import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;

import java.util.Arrays;

/************************************************************
 * Expand each ring from the inside out with the provided list of colors.
 ***********************************************************/
public class Expand extends TimedAnimation
{

	private RGB[] mColors;

	private int mStartRingIndex;
	private int mEndRingIndex;

	/* Used to indicate the indexes desire a reverse expand.. or .. contract. */
	private boolean mContract = false;

	/* The moving ring to color index.  This is the ring that the 0th color is to be drawn at.
	* This is an object instead of a literal, because a null indicates to the step() method
	* that a new initial offset is needed.  Usually due to a cycle reset */
	private Integer mRingOffsetIndex = null;

	/******************************************************
	 * Playing with the array of colors can have all sorts of interesting affects with what rings
	 * are left on and in what state..  Adding lots' of OFF to the end can mimic a pause.
	 *
	 * @param colors IN - an array of RGB colors to use during the expand.  If 1 color is provided,
	 *               the only one ring at a time will be lit.  If 6 colors are provided, the rings will
	 *               grow, each ring passing to the next color.  If you want the arms to stay a final color, set the
	 *               last color to that.  If you want them to be pulled off, set the last color to black/off.
	 *               The order of this array is critical to understand.  Since this is an expand animation,
	 *               it represents a look that the rings of the FluxCap are expanding in color outwards
	 *               from the center.  The inside most ring, will be lit with the LAST color in the color
	 *               array first.  Think if it like thte array is pushed .. tail first... into view on
	 *               the flux capacitor starting from the inside ring.
	 * @param startRingIndex - the flux cap ring index to start at.  0 = the first ring.
	 * @param endRingIndex - the flux cap ring index that the last color ends on.  2 means once the last
	 *                    color hits the 3rd ring, it will be finished. -1 mean stop at the last ring.
	 * @param secondsPerCycle
	 * @param cycles
	 *******************************************************/
	public Expand(int startRingIndex, int endRingIndex, double secondsPerCycle, int cycles, RGB ... colors)
	{
		super(secondsPerCycle, cycles);
		this.mColors = colors;
		this.mStartRingIndex = startRingIndex;
		this.mEndRingIndex = endRingIndex;
		onNewCycle();
	}

	/******************************************************
	 *
	 *******************************************************/
	public Expand(Expand e)
	{
		super(e);
		this.mColors = Arrays.copyOf(e.mColors, e.mColors.length);
		this.mStartRingIndex = e.mStartRingIndex;
		this.mEndRingIndex = e.mEndRingIndex;
		this.mContract = e.mContract;
		this.mRingOffsetIndex = e.mRingOffsetIndex;

	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new Expand(this);
	}

	/******************************************************
	 *
	 *******************************************************/
	public void setReverse(boolean r)
	{
		this.mContract = r;
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void onNewCycle()
	{
		this.mRingOffsetIndex = this.mStartRingIndex;

		if (this.mContract == false)
		{
			this.mRingOffsetIndex -= this.mColors.length - 1;
		}
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{


		/* set the start/end index values if a -1 was provided. also direction dependant. */
		if (this.mContract == false && this.mEndRingIndex < 0)
		{
			this.mEndRingIndex = flux.getArmLength() - 1;
		}
		else if (this.mContract && this.mStartRingIndex < 0)
		{
			this.mStartRingIndex = flux.getArmLength() - 1;
		}


		/* Sanity check on index values */
		if (this.mContract == false)
		{
			if (this.mStartRingIndex >= this.mEndRingIndex)
			{
				throw new RuntimeException(String.format("Start Index (%d) must be < End Index (%d)", this.mStartRingIndex, this.mEndRingIndex));
			}
		}
		else
		{
			if (this.mStartRingIndex <= this.mEndRingIndex)
			{
				throw new RuntimeException(String.format("Start Index (%d) must be > End Index (%d)", this.mStartRingIndex, this.mEndRingIndex));
			}
		}

		/* Calculate the steps rate */
		int stepsPerCycle = mColors.length + (mContract == false ? mEndRingIndex - mStartRingIndex : mStartRingIndex - mEndRingIndex);
		int framesPerStep = Math.max(1, getFramesPerCycle(fps) / stepsPerCycle);


		/* Check if a new initial offset is needed */
		if (this.mRingOffsetIndex == null)
		{
			onNewCycle();
		}


		/* Only change the offset if we're not finished, the offset chang is direction dependant */
		if (isFinished() == false)
		{
			if (this.getFrame() % framesPerStep == 0)
			{
				this.mRingOffsetIndex += (this.mContract ? -1 : 1);
			}
		}


		super.step(flux, fps);
	}


	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	public void write(FluxCap flux)
	{

		/* For each color in the color list */
		for (int colorIndex = 0; colorIndex < this.mColors.length; colorIndex++)
		{
			int ringIndex = this.mRingOffsetIndex + colorIndex;

			/* If this color/arm combo it outside the available ring indexes, skip to the next color/arm combo */
			if (ringIndex < 0 || ringIndex >= flux.getArmLength())
			{
				continue;
			}

			RGB color = mColors[colorIndex];

			writePixel(flux, FluxCap.ARM.BOTTOM, ringIndex, color);
			writePixel(flux, FluxCap.ARM.LEFT, ringIndex, color);
			writePixel(flux, FluxCap.ARM.RIGHT, ringIndex, color);

		}

	}
}
