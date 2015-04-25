package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.PixelMask;
import com.github.shaneapowell.ioioflux.RGB;

/**********************************************************
 * The main abstract simple animation class that does nothing more than
 * provide the ability to set a Mask, and to name this animation.
 * At the lowest level, an animation does nothing. Yeah.. go figure.
 * Not until the next abstract class above this one does any
 * actual pixel manipulation occur.
 ***********************************************************/
public abstract class Animation
{
	/* The toString name, by default it's set to the class name of this animation. */
	private String mName;

	/* This mask used to identify which pixels are turned on, and off.  */
	private PixelMask mMask = null;


	/******************************************************
	 *
	 *******************************************************/
	public Animation()
	{
		this.mName = this.getClass().getSimpleName();

	}


	/******************************************************
	 * Copy Constructor
	 * @param a
	 *******************************************************/
	public Animation(Animation a)
	{
		this.mName = a.mName;
		this.mMask = a.mMask == null ? null : new PixelMask(a.mMask);
	}

	/******************************************************
	 * Use the copy constructor to implement this copy method.
	 * @return
	 *******************************************************/
	public abstract Animation copy();

	/******************************************************
	 * Tell this animation to take a step through it's sequence.
	 *
	 * @param flux
	 * @param fps the animations speed in frames per second
	 *******************************************************/
	public abstract void step(FluxCap flux, int fps);


	/******************************************************
	 * Write the state of this animation to the Flux Capacitor.
	 * it's the responsibility of every concrete animation to
	 * enforce the use of the pixel mask.  A easy way to do that is
	 * to use the writePixel(..) method supplied here for each pixel.
	 * It automatically enforces the mask.
	 * @param flux
	 *******************************************************/
	public abstract void write(FluxCap flux);


	/******************************************************
	 * A quick and easy means to check the mask and write a color
	 * to a pixel if it's not masked off.
	 * @param flux
	 * @param arm
	 * @param ring
	 *******************************************************/
	public void writePixel(FluxCap flux, FluxCap.ARM arm, int ring, RGB color)
	{
		if (this.getMask() == null || this.getMask().isOn(arm, ring))
		{
			flux.getPixel(arm, ring).setColor(color);
		}
	}

	/******************************************************
	 * When an animation has finished it's sequence of steps, this
	 * will return true.  False while there are more steps to run.
	 * @return
	 *******************************************************/
	public abstract boolean isFinished();


	/******************************************************
	 * Internally reset yourself as to allow the steps to re-run.
	 * Teh result of this method should be that isFinished() will
	 * return false once again.
	 *******************************************************/
	public abstract void reset();


	/******************************************************
	 *
	 * @param mask
	 * @return
	 *******************************************************/
	public Animation setMask(PixelMask mask)
	{
		this.mMask = mask;
		return this;
	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public PixelMask getMask()
	{
		return this.mMask;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	public String toString()
	{
		return getName();
	}

	/******************************************************
	 * Set the optional name of this animation, and return this.
	 *******************************************************/
	public Animation setName(String name)
	{
		this.mName = name;
		return this;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public String getName()
	{
		return mName;
	}

}
