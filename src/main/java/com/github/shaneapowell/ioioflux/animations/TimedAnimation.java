package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.FluxCap;

/************************************************************
 * An abstract animation class the provides a mechanism to cycle through
 * various cycles of this animation.
 ***********************************************************/
public abstract class TimedAnimation extends Animation
{

	public static final float ONE_FRAME = 0.000000001f;

	private double mSecondsPerCycle = 0;
	private int mNumCycles = 0;

	private int mFrame = 0;
	private int mCycle = 0;


	/**********************************************************
	 * @param secondsPerCycle the number of seconds to pass per cycle.  If you make this a very very small number (0.00001) it will cause a single frame to fire
	 *                        this animation.  The logic in this class will ensure that no matter how small (not zero) this value is, at least one will run.
	 *                        You an use the constant ONE_FRAME
	 * @param cycles the number of cycle loops to perform before returning false on step().  A -1 means to loop for ever.
	 ***********************************************************/
	public TimedAnimation(double secondsPerCycle, int cycles)
	{
		super();
		this.mSecondsPerCycle = secondsPerCycle;
		this.mNumCycles = cycles;
	}

	/******************************************************
	 *
	 *******************************************************/
	public TimedAnimation(TimedAnimation t)
	{
		super(t);
		this.mSecondsPerCycle = t.mSecondsPerCycle;
		this.mNumCycles = t.mNumCycles;
		this.mFrame = t.mFrame;
		this.mCycle = t.mCycle;
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void reset()
	{
		this.mCycle = 0;
		this.mFrame = 0;
		onNewCycle();
	}

	/******************************************************
	 *
	 *******************************************************/
	public void setSecondsPerCycle(float sec)
	{
		this.mSecondsPerCycle = sec;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public double getSecondsPerCycle()
	{
		return this.mSecondsPerCycle;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int getNumCycles()
	{
		return this.mNumCycles;
	}

	/******************************************************
	 * Get the current frame. Remember frames start at 0.
	 * @return
	 *******************************************************/
	public int getFrame()
	{
		return this.mFrame;
	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int getCycle()
	{
		return this.mCycle;
	}

	/******************************************************
	 * When a step detects a new cycle, this method will be called.
	 * 99.9% of the time, the concrete animation will need to
	 * simply reset it's state.
	 *******************************************************/
	public abstract void onNewCycle();


	/******************************************************
	 *
	 * @return true if more steps are needed for this animation to finish.  False if this animation is finished and can be discarded.
	 * If a false is returned, nothing will be sent out to the LEDs.
	 *******************************************************/
	public boolean isFinished()
	{
		if (this.mNumCycles > -1 && this.mCycle >= this.mNumCycles)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/******************************************************
	 *
	 * @param targetFps
	 * @return
	 *******************************************************/
	protected int getFramesPerCycle(int targetFps)
	{
		return Math.max(1, (int)Math.round(this.mSecondsPerCycle * ((double)targetFps)));
	}

	/******************************************************
	 * Does the math and tracking of frames and cycles.
     * this method MUST be called last in the overriding class.
	 * This is because the frame and cycle counters are
	 * calculated and incremented every time this is called.
	 * @param flux
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{

		if (isFinished() == false)
		{
			this.mFrame++;

			if (this.mFrame >= getFramesPerCycle(fps))
			{
				this.mFrame = 0;
				this.mCycle++;
				this.onNewCycle();
			}

		}

		/* Sanity Check */
		if (mFrame < 0 || mFrame >= getFramesPerCycle(fps))
		{
			throw new RuntimeException(String.format("Frame counter [%d] out of range [0-%d]", mFrame, getFramesPerCycle(fps)));
		}

	}
}
