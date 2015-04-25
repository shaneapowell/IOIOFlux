package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.FluxCap;


/**********************************************************
 * A group of animations that run in sequence on after another.
 * The next animation in the group is NOT started until the
 * previous one finishes.
 ***********************************************************/
public class SequentialGroup extends Group
{

	private int mAnimationIndex = 0;

	/******************************************************
	 *
	 * @param cycles
	 *******************************************************/
	public SequentialGroup(int cycles)
	{
		super(cycles);
	}


	/******************************************************
	 *
	 * @param g
	 *******************************************************/
	public SequentialGroup(SequentialGroup g)
	{
		super(g);
		this.mAnimationIndex = g.mAnimationIndex;
	}

	/****************************************************
	 *
	 * @return
	 *****************************************************/
	@Override
	public Animation copy()
	{
		return new SequentialGroup(this);
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void reset()
	{
		super.reset();
		this.mAnimationIndex = 0;
	}


	/******************************************************
	 *
	 * @param flux
	 * @param fps the animations speed in frames per second
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{
		Animation anim = this.mAnimations.get(mAnimationIndex);
		anim.step(flux, fps);

		if (anim.isFinished())
		{
			stepToNextAnimation();
		}

	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	@Override
	public void write(FluxCap flux)
	{
		Animation anim = this.mAnimations.get(mAnimationIndex);
		if (anim != null)
		{
			anim.write(flux);
		}
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	private Animation stepToNextAnimation()
	{
		this.mAnimationIndex++;
		if (this.mAnimationIndex >= this.mAnimations.size())
		{
			this.mAnimationIndex = 0;
			incrementCycle();
		}

		Animation anim = this.mAnimations.get(this.mAnimationIndex);
		anim.reset();
		return anim;

	}



}
