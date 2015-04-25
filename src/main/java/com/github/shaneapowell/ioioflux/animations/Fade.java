package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;

/************************************************************
 *
 ***********************************************************/
public class Fade extends Solid
{


	private float mFromRed;
	private float mFromGreen;
	private float mFromBlue;

	private float mToRed;
	private float mToGreen;
	private float mToBlue;

	private float mRed;
	private float mGreen;
	private float mBlue;

	private float mRedStep = -1;
	private float mGreenStep = -1;
	private float mBlueStep = -1;

	/**********************************************************
	 *
	 ***********************************************************/
	public Fade(RGB fromColor, RGB toColor, double secondsPerCycle, int cycles)
	{
		super(fromColor, secondsPerCycle, cycles);

		this.mFromRed = fromColor.red();
		this.mFromGreen = fromColor.green();
		this.mFromBlue = fromColor.blue();

		this.mToRed = toColor.red();
		this.mToGreen = toColor.green();
		this.mToBlue = toColor.blue();

		onNewCycle();
	}


	/******************************************************
	 *
	 *******************************************************/
	public Fade(Fade f)
	{
		super(f);

		this.mFromRed = f.mFromRed;
		this.mFromGreen = f.mFromGreen;
		this.mFromBlue = f.mFromBlue;

		this.mToRed = f.mToRed;
		this.mToGreen = f.mToGreen;
		this.mToBlue = f.mToBlue;

		this.mRed = f.mRed;
		this.mGreen = f.mGreen;
		this.mBlue = f.mBlue;

		this.mRedStep = f.mRedStep;
		this.mGreenStep = f.mGreenStep;
		this.mBlueStep = f.mBlueStep;

	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new Fade(this);
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void onNewCycle()
	{
		super.onNewCycle();
		this.mRed = this.mFromRed;
		this.mGreen = this.mFromGreen;
		this.mBlue = this.mFromBlue;
		this.mColor = new RGB((int)this.mFromRed, (int)this.mFromGreen, (int)this.mFromBlue);
	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{

		if (this.mRedStep == -1)
		{
			this.mRedStep = (this.mToRed - this.mFromRed) / ((float) getFramesPerCycle(fps));
			this.mGreenStep = (this.mToGreen - this.mFromGreen) / ((float) getFramesPerCycle(fps));
			this.mBlueStep = (this.mToBlue - this.mFromBlue) / ((float) getFramesPerCycle(fps));
		}


		/* Even if step is called, only modify the animation value if not finished */
		if (!isFinished())
		{
			this.mRed += this.mRedStep;
			this.mGreen += this.mGreenStep;
			this.mBlue += this.mBlueStep;
		}

		this.mColor = new RGB(this.mRed, this.mGreen, this.mBlue);

		super.step(flux, fps);
	}

}
