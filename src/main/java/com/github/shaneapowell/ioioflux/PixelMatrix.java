package com.github.shaneapowell.ioioflux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**********************************************************
 *
 * A pixel matrix is setup and passed to a FluxCap to define the location
 * of the pixels within the flux cap as it relates to the string of
 * RGB LEDs.  Each of the 3 arrays MUST be of the same
 * length.  The same index must NOT be re-used.  There must NOT be
 * any gaps between indexes.  If you hae a 3 and a 5, you MUST have a 4 somewhere.
 * There can also NOT be any duplicate indexes.
 * The default matrix looks like this
 *

 9	         5
   7       4
     6   3
       0
       1
       2

 *
 ***********************************************************/
public class PixelMatrix
{
	public int[] bottom = {0,1,2};
	public int[] right  = {3,4,5};
	public int[] left   = {6,7,8};


	/**************************************************
	 *
	 ***************************************************/
	public PixelMatrix()
	{
		super();
	}


	/**************************************************
	 *
	 * @param src
	 ***************************************************/
	public PixelMatrix(PixelMatrix src)
	{
		this();
		this.bottom = Arrays.copyOf(src.bottom, src.bottom.length);
		this.left = Arrays.copyOf(src.left, src.left.length);
		this.right = Arrays.copyOf(src.right, src.right.length);
	}


	/**************************************************
	 *
	 * @return
	 ***************************************************/
	public int getArmLength()
	{
		return bottom.length;
	}


	/**************************************************
	 * Validate/Verify the matrix meets requirements.
	 * @throws RuntimeException
	 ***************************************************/
	public void validate() throws RuntimeException
	{
		if (bottom.length != left.length && bottom.length != right.length)
		{
			throw new RuntimeException("Each Matrix Arm MUST have the same number of pixels");
		}

		int totalIndexes = bottom.length + left.length + right.length;

		ArrayList<Integer> indexList = new ArrayList<Integer>();
		HashSet<Integer> indexSet = new HashSet<Integer>();

			/* Find the lowest and highest indexes */
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int index = 0; index < bottom.length; index++)
		{
			min = Math.min(min, bottom[index]);
			min = Math.min(min, left[index]);
			min = Math.min(min, right[index]);
			max = Math.min(max, bottom[index]);
			max = Math.min(max, left[index]);
			max = Math.min(max, right[index]);

			indexList.add(new Integer(bottom[index]));
			indexList.add(new Integer(left[index]));
			indexList.add(new Integer(right[index]));

			indexSet.add(new Integer(bottom[index]));
			indexSet.add(new Integer(left[index]));
			indexSet.add(new Integer(right[index]));
		}


		if (indexSet.size() < totalIndexes)
		{
			throw new RuntimeException("Matrix has duplicate index somewhere");
		}

		for (int index = min; index <= max; index++)
		{
			if (indexList.contains(new Integer(index)) == false)
			{
				throw new RuntimeException("Matrix is missing an sequencial index of " + index);
			}
		}

	}

}