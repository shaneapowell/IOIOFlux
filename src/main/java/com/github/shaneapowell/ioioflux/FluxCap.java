package com.github.shaneapowell.ioioflux;

/**********************************************************
 * Represents a Flux Capacitor. It contains an array of
 * pixels that can affect either an Android View, or a physical
 * Flux Capacitor box made with RGB LEDs.
 ***********************************************************/
public class FluxCap
{

	public enum ARM { BOTTOM, LEFT, RIGHT, ALL };

	protected PixelMatrix mPixelIndexMatrix;
	private Pixel[] mPixels = null;

	/******************************************************
	 *
	 *******************************************************/
	public FluxCap(PixelMatrix matrix)
	{

		this.mPixelIndexMatrix = matrix;

		this.mPixelIndexMatrix.validate();

		this.mPixels = new Pixel[3 * matrix.getArmLength()];

		/* Init our matrix of pixels */
		for (int index = 0; index < this.mPixels.length; index++)
		{
			this.mPixels[index] = new Pixel();
		}
	}

	/******************************************************
	 * Return a copy of the pixel matrix.  Not the original.
	 * @return
	 *******************************************************/
	public PixelMatrix getPixelMatrix()
	{
		return new PixelMatrix(this.mPixelIndexMatrix);
	}


	/******************************************************
	 * Get the array of all pixels. This order of the pixels
	 * is intended to match the order of the RGB LEDs in the
	 * serial string of lights. If you wish to index a particular
	 * pixel by it's arm, or ring, use one o the other getPixel()
	 * methods.
	 *
	 * * @return
	 *******************************************************/
	public Pixel[] getAllPixels()
	{
		return this.mPixels;
	}

	/******************************************************
	 * Get the pixel in order at that index is in the string of LEDs.
	 * @param index
	 * @return
	 *******************************************************/
	public Pixel getPixel(int index)
	{
		return this.mPixels[index];
	}


	/******************************************************
	 * Get a pixel on a particular arm, at a given ring index.
	 *
	 * @param arm The arm to get the pixel from.
	 * @param index the index, up the arm to get.  0 is the inner most pixel.
	 * @return
	 *******************************************************/
	public Pixel getPixel(ARM arm, int index)
	{
		if (index >= getArmLength())
		{
			throw new RuntimeException("Index [" + index + "] out of range [" + mPixelIndexMatrix.getArmLength() + "]");
		}

		if (arm == ARM.BOTTOM)
		{
			return this.mPixels[this.mPixelIndexMatrix.bottom[index]];
		}
		else if (arm == ARM.LEFT)
		{
			return this.mPixels[this.mPixelIndexMatrix.left[index]];
		}
		else if (arm == ARM.RIGHT)
		{
			return this.mPixels[this.mPixelIndexMatrix.right[index]];
		}
		else
		{
			throw new RuntimeException("Invalid ARM " + arm);
		}

	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int getArmLength()
	{
		return this.mPixelIndexMatrix.getArmLength();
	}


}

