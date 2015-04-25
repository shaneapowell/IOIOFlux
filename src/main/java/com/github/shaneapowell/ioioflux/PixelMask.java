package com.github.shaneapowell.ioioflux;

import java.util.Arrays;
import java.util.HashMap;

/**********************************************************
 *
 ***********************************************************/
public class PixelMask
{
	private HashMap<FluxCap.ARM, boolean[]> mMask = new HashMap<FluxCap.ARM, boolean[]>();

	/******************************************************
	 * Default constructor for internal cloning only.
	 *******************************************************/
	public PixelMask(PixelMask mask)
	{
		boolean[] maskB = mask.mMask.get(FluxCap.ARM.BOTTOM);
		boolean[] maskL = mask.mMask.get(FluxCap.ARM.LEFT);
		boolean[] maskR = mask.mMask.get(FluxCap.ARM.RIGHT);

		maskB = Arrays.copyOf(maskB, maskB.length);
		maskL = Arrays.copyOf(maskB, maskL.length);
		maskR = Arrays.copyOf(maskB, maskR.length);

		this.mMask.put(FluxCap.ARM.BOTTOM, maskB);
		this.mMask.put(FluxCap.ARM.LEFT, maskL);
		this.mMask.put(FluxCap.ARM.RIGHT, maskR);
	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	public PixelMask(FluxCap flux)
	{
		this(flux, true);
	}

	/******************************************************
	 *
	 * @param flux
	 *******************************************************/
	public PixelMask(FluxCap flux, boolean defaultMask)
	{
		this(flux.getPixelMatrix(), defaultMask);
	}

	/******************************************************
	 *
	 * @param matrix
	 *******************************************************/
	public PixelMask(PixelMatrix matrix, boolean defaultMask)
	{
		boolean[] arm = new boolean[matrix.getArmLength()];
		Arrays.fill(arm, defaultMask);
		this.mMask.put(FluxCap.ARM.BOTTOM, Arrays.copyOf(arm, arm.length));
		this.mMask.put(FluxCap.ARM.LEFT, Arrays.copyOf(arm, arm.length));
		this.mMask.put(FluxCap.ARM.RIGHT, Arrays.copyOf(arm, arm.length));
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	public String toString()
	{
		return mMask.toString();
	}



	/******************************************************
	 *
	 * @param arm
	 * @param index
	 * @return
	 *******************************************************/
	public boolean isOn(FluxCap.ARM arm, int index)
	{
		return this.mMask.get(arm)[index];
	}


	/******************************************************
	 *
	 * @param arm
	 * @param index
	 * @param mask
	 *******************************************************/
	public PixelMask set(FluxCap.ARM arm, int index, boolean mask)
	{
		this.mMask.get(arm)[index] = mask;
		return this;
	}

	/*******************************************************
	 *
	 * @param index
	 * @param mask
	 * @return
	 ********************************************************/
	public PixelMask setRing(int index, boolean mask)
	{
		set(FluxCap.ARM.BOTTOM, index, mask);
		set(FluxCap.ARM.LEFT, index, mask);
		set(FluxCap.ARM.RIGHT, index, mask);
		return this;
	}

	/******************************************************
	 * Return a copy of this mask, inverted.  true <> false
	 ******************************************************/
	public PixelMask invert()
	{
		PixelMask copy = new PixelMask(this);

		boolean[] maskB = copy.mMask.get(FluxCap.ARM.BOTTOM);
		boolean[] maskL = copy.mMask.get(FluxCap.ARM.LEFT);
		boolean[] maskR = copy.mMask.get(FluxCap.ARM.RIGHT);

		for (int index = 0; index < maskB.length; index++)
		{
			maskB[index] = !maskB[index];
			maskL[index] = !maskL[index];
			maskR[index] = !maskR[index];
		}

		return copy;
	}

	/******************************************************
	 * OR this mask, the mask of "or" mask.
	 * @param or
	 * @return this mask.
	 *******************************************************/
	public PixelMask or(PixelMask or)
	{
		boolean[] maskB = this.mMask.get(FluxCap.ARM.BOTTOM);
		boolean[] maskL = this.mMask.get(FluxCap.ARM.LEFT);
		boolean[] maskR = this.mMask.get(FluxCap.ARM.RIGHT);

		for (int index = 0; index < maskB.length; index++)
		{
			maskB[index] |= maskB[index];
			maskL[index] |= maskL[index];
			maskR[index] |= maskR[index];
		}

		return this;
	}


	/******************************************************
	 *  AND the the and mask to this mask.
	 * @param and
	 * @return this mask
	 *******************************************************/
	public PixelMask and(PixelMask and)
	{
		boolean[] maskB = this.mMask.get(FluxCap.ARM.BOTTOM);
		boolean[] maskL = this.mMask.get(FluxCap.ARM.LEFT);
		boolean[] maskR = this.mMask.get(FluxCap.ARM.RIGHT);

		for (int index = 0; index < maskB.length; index++)
		{
			maskB[index] &= maskB[index];
			maskL[index] &= maskL[index];
			maskR[index] &= maskR[index];
		}

		return this;
	}

}
