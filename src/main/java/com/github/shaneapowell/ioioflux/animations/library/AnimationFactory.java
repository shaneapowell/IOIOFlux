package com.github.shaneapowell.ioioflux.animations.library;

import android.content.Context;

import com.github.shaneapowell.ioioflux.FluxCap;
import com.github.shaneapowell.ioioflux.PixelMask;
import com.github.shaneapowell.ioioflux.R;
import com.github.shaneapowell.ioioflux.RGB;
import com.github.shaneapowell.ioioflux.animations.AdditionGroup;
import com.github.shaneapowell.ioioflux.animations.Animation;
import com.github.shaneapowell.ioioflux.animations.Contract;
import com.github.shaneapowell.ioioflux.animations.Expand;
import com.github.shaneapowell.ioioflux.animations.Fade;
import com.github.shaneapowell.ioioflux.animations.Group;
import com.github.shaneapowell.ioioflux.animations.Rotate;
import com.github.shaneapowell.ioioflux.animations.SequentialGroup;
import com.github.shaneapowell.ioioflux.animations.Solid;

import static com.github.shaneapowell.ioioflux.animations.Group.CYCLE_FOREVER;

/**********************************************************
 * Just a simple utility class to help create some of the better known animations.
 ***********************************************************/
public class AnimationFactory
{
	private Context mCtx = null;
	private FluxCap mFluxCap = null;


	/******************************************************
	 *
	 * @param ctx
	 *******************************************************/
	public AnimationFactory(Context ctx, FluxCap fluxCap)
	{
		this.mCtx = ctx;
		this.mFluxCap = fluxCap;
	}


	/******************************************************/
	public Animation eightyEightMPH()
	{
		RGB on = new RGB(0x00, 0xaa, 0xaa);
		RGB trail1 = new RGB(0x00, 0x11, 0x11);
		RGB trail2 = new RGB(0x00, 0x0f, 0x0f);

		float normalSpeed = 0.3f;
		float fastSpeed = 0.2f;

		normalSpeed = 0.4f;
		fastSpeed = 0.15f;

		Contract normal = new Contract(-1, 0, normalSpeed, 1, RGB.OFF, on, trail1, trail2, RGB.OFF);
		Contract fast = new Contract(-1, 0, fastSpeed, 1, RGB.OFF, on, trail1, trail2, trail2, RGB.OFF);

		fast.setSecondsPerCycle(fastSpeed);
		Solid pause = new Solid(RGB.OFF, normalSpeed, 1);

		return new SequentialGroup(CYCLE_FOREVER)
				.add(new SequentialGroup(10)
						.add(normal)
						.add(pause)
					)
				.add(new SequentialGroup(5)
						.add(fast)
						.add(pause)
					)
				.setName(mCtx.getString(R.string.animation_88mph));

	}


	/******************************************************/
	public Animation rotate(RGB color, int name)
	{
		return new Rotate(color, RGB.OFF, RGB.OFF, 0.6, -1).setName(mCtx.getString(name));
	}

	/******************************************************/
	public Animation fadeInOut(RGB color, int name)
	{
		/* Fade in/out Red */
		return new SequentialGroup(CYCLE_FOREVER)
						.add(new Fade(RGB.OFF, color, 2, 1))
						.add(new Fade(color, RGB.OFF, 2, 1))
						.add(new Solid(RGB.OFF)
						).setName(mCtx.getString(name));
	}

	/******************************************************/
	public Animation pulseBeam(RGB color, int name)
	{
		return new Expand(0, -1, 0.5, CYCLE_FOREVER, RGB.OFF, color, RGB.OFF ).setName(mCtx.getString(name));
	}


	/******************************************************/
	public Animation rotatingRainbow()
	{
		return new Rotate(RGB.RED, RGB.BLUE, RGB.GREEN, 0.8, -1).setName(mCtx.getString(R.string.animation_rainbow_rotate));
	}


	/******************************************************/
	public Animation cylon()
	{
		return new Cylon().setName(mCtx.getString(R.string.animation_cylon));
	}

	/******************************************************/
	public Animation spiral()
	{
		/* Spiral Out */
		float secondsPerCycle = 0.4f;
		PixelMask ring0on = new PixelMask(this.mFluxCap, false).setRing(0, true);
		PixelMask ring1on = new PixelMask(this.mFluxCap, false).setRing(1, true);
		PixelMask ring2on = new PixelMask(this.mFluxCap, false).setRing(2, true);

		Group ring0Group = new AdditionGroup(1);
		Group ring1Group = new AdditionGroup(1);
		Group ring2Group = new AdditionGroup(1);

		ring0Group.add(new Solid(RGB.OFF));
		ring0Group.add(new Rotate(RGB.RED, RGB.OFF, RGB.OFF, secondsPerCycle, 1).setMask(ring0on));

		ring1Group.add(new Solid(RGB.OFF));
		ring1Group.add(new Rotate(RGB.GREEN, RGB.OFF, RGB.OFF, secondsPerCycle, 1).setMask(ring1on));

		ring2Group.add(new Solid(RGB.OFF));
		ring2Group.add(new Rotate(RGB.BLUE, RGB.OFF, RGB.OFF, secondsPerCycle, 1).setMask(ring2on));

		return new SequentialGroup(CYCLE_FOREVER)
				.add(ring0Group)
				.add(ring1Group)
				.add(ring2Group)
				.add(ring1Group.copy())
				.setName(mCtx.getString(R.string.animation_spiral));

	}


	/******************************************************/
	public Animation test01()
	{
		return new AdditionGroup(CYCLE_FOREVER)
						.add(new Solid(RGB.RED))
						.add(new Contract(-1,  0, 0.5, 5, RGB.OFF, RGB.BLUE, RGB.OFF))
						.setName("Debug Test 1");
	}
}
