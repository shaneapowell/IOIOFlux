package com.github.shaneapowell.ioioflux.animations.library;

import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.RGB;
import com.github.shaneapowell.ioioflux.animations.AdditionGroup;
import com.github.shaneapowell.ioioflux.animations.One;
import com.github.shaneapowell.ioioflux.animations.SequentialGroup;
import com.github.shaneapowell.ioioflux.animations.Solid;

/**********************************************************
 * A concreate animation class that mimics the red eye of a Cylon
 **********************************************************/
public class Cylon extends SequentialGroup
{

	private static final float SEC_PER_CYCLE = 0.13f;

	/******************************************************
	 *
	 *******************************************************/
	public Cylon()
	{

		super(CYCLE_FOREVER);

		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.LEFT, 2)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.LEFT, 1)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.LEFT, 0)));

		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.RIGHT, 0)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.RIGHT, 1)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.RIGHT, 2)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.RIGHT, 1)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.RIGHT, 0)));

		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.LEFT, 0)));
		this.add(new AdditionGroup(1).add(new Solid(RGB.OFF)).add(new One(RGB.RED, SEC_PER_CYCLE, 1, FluxCap.ARM.LEFT, 1)));


	}
}
