package com.github.shaneapowell.ioioflux.animations;

import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.Pixel;

import java.util.HashMap;


/**********************************************************
 * Given each provided animation, add the pixel color
 * definitions to each other in order.  A common
 * use for this would be to add a Solid with an Expand.
 * You can make a solid light green color, with an expand
 * red color, to make a green Flux expand with Purple lights.
 * This is done by providing a mock FluxCap to each animations
 * in the call to step(). Then, the mock FluxCap objects have
 * their pixels combined and set in the actual flux cap.
 ***********************************************************/
public class AdditionGroup extends Group
{

	private HashMap<Animation, FluxCap> mMockFluxCaps = new HashMap<Animation, FluxCap>();

	/******************************************************
	 *
	 * @param cycles
	 *******************************************************/
	public AdditionGroup(int cycles)
	{
		super(cycles);
	}


	/******************************************************
	 *
	 *******************************************************/
	public AdditionGroup(AdditionGroup g)
	{
		super(g);
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public Animation copy()
	{
		return new AdditionGroup(this);
	}

	/******************************************************
	 *
	 * @param flux
	 * @param fps the animations speed in frames per second
	 *******************************************************/
	@Override
	public void step(FluxCap flux, int fps)
	{

		if (this.mAnimations.size() <= 0)
		{
			return;
		}

		boolean areAllFinished = true;

		for (int index = 0; index < this.mAnimations.size(); index++)
		{
			Animation anim = this.mAnimations.get(index);
			anim.step(flux, fps);

			if (anim.isFinished() == false)
			{
				areAllFinished = false;
			}
		}


		if (areAllFinished == true)
		{
			this.incrementCycle();
		}


	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	@Override
	public void write(FluxCap flux)
	{

		if (this.mAnimations.size() <= 0)
		{
			return;
		}

		/* Add all the mock flux caps, into a single mock flux cap.  We'll add to the first one, and use it
		as the master final flux to copy into the concrete flux. */
		FluxCap finalFlux = getMockFluxCap(flux, this.mAnimations.get(0));
		this.mAnimations.get(0).write(finalFlux);

		for (int index = 1; index < this.mAnimations.size(); index++)
		{
			/* Write the animation to it's mock flux */
			Animation anim = this.mAnimations.get(index);
			FluxCap mockFlux = getMockFluxCap(flux, anim);
			anim.write(mockFlux);

			/* Add the resulting pixels in this mock flux, to the final Flux */
			for (int pixelIndex = 0; pixelIndex < finalFlux.getAllPixels().length; pixelIndex++)
			{
				Pixel pixel = finalFlux.getPixel(pixelIndex);
				pixel.add(mockFlux.getPixel(pixelIndex));
			}
		}

		/* set the concrete flux cap pixels to the final mock one */
		for (int index = 0; index < flux.getAllPixels().length; index++)
		{
			flux.getPixel(index).setColor(finalFlux.getPixel(index));
		}


	}

	/******************************************************
	 *
	 * @param anim
	 *******************************************************/
	private FluxCap getMockFluxCap(FluxCap concreteFluxCap, Animation anim)
	{
		FluxCap fluxCap;

		if (mMockFluxCaps.containsKey(anim) == false)
		{
			fluxCap = new FluxCap(concreteFluxCap.getPixelMatrix());
			mMockFluxCaps.put(anim, fluxCap);
		}

		fluxCap = mMockFluxCaps.get(anim);

		return fluxCap;
	}


}
