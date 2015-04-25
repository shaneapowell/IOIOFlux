package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.PixelMask;

import java.util.ArrayList;

/**********************************************************
 * Provides the ability to group several animations into a single
 * animation. it's the job of the concrete implementation to
 * determine exactly what to do with the group of animations.
 * For example, the SequentialGroup runs the groups in order.
 * The AdditionGroup combineds them together.
 ***********************************************************/
public abstract class Group extends Animation
{

	public static final int CYCLE_FOREVER = -1;

	private int mCycles = 0;
	private int mCycle;

	protected ArrayList<Animation> mAnimations = new ArrayList<Animation>();


	/******************************************************
	 *
	 * @param cycles The number of cycles to run this animation group through.  CYCLE_FOREVER does just what it says.
	 *******************************************************/
	public Group(int cycles)
	{
		super();
		this.mCycles = cycles;
	}


	/******************************************************
	 *
	 * @param g
	 *******************************************************/
	public Group(Group g)
	{
		super(g);
		this.mCycles = g.mCycles;
		this.mCycle = g.mCycle;
		for (Animation a : g.mAnimations)
		{
			this.mAnimations.add(a.copy());
		}
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void reset()
	{

		this.mCycle = 0;
		if (this.mAnimations != null)
		{
			for (Animation a : this.mAnimations)
			{
				a.reset();
			}
		}

	}

	/******************************************************
	 *
	 * @param mask
	 * @return
	 *******************************************************/
	@Override
	public Animation setMask(PixelMask mask)
	{
		super.setMask(mask);

		for (Animation anim : this.mAnimations)
		{
			anim.setMask(mask);
		}

		return this;
	}

	/******************************************************
	 *
	 *******************************************************/
	public void incrementCycle()
	{
		this.mCycle++;

		if (isFinished() == false)
		{
			if (this.mAnimations != null)
			{
				for (Animation a : this.mAnimations)
				{
					a.reset();
				}
			}
		}
	}

	/******************************************************
	 *
	 * @param anim
	 * @return this group.
	 *****************************************************/
	public Group add(Animation anim)
	{
		this.mAnimations.add(anim);
		return this;
	}



	/******************************************************
	 *
	 * @return true if more steps are needed for this animation to finish.  False if this animation is finished and can be discarded.
	 * If a false is returned, nothing will be sent out to the LEDs.
	 *******************************************************/
	public boolean isFinished()
	{
		if (this.mCycles > -1 && this.mCycle >= this.mCycles)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
