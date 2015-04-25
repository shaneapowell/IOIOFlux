package com.github.shaneapowell.ioioflux;

/**********************************************************
 *x
 ***********************************************************/
public class Animator// extends TimerTask
{

	private int mFps = 0;
	private AnimatorFrameCallback mFrameCallback = null;

//	private Timer mTimer = null;
	private Thread mThread = null;

	private boolean mRunning = true;

	/*****************************************************
	 *
	 * @param fps
	 * @param frameCallback
	 ******************************************************/
	public Animator(int fps, AnimatorFrameCallback frameCallback)
	{
		this.mFps = fps;
		this.mFrameCallback = frameCallback;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public int getFPS()
	{
		return this.mFps;
	}

	/******************************************************
	 *
	 * @param fps
	 *******************************************************/
	public void setFPS(int fps)
	{
		this.mFps = Math.max(0, fps);
	}

	/******************************************************
	 * Convert time in Milliseconds to a frame count to match this
	 * animators FPS.
	 * @param ms
	 * @return
	 *******************************************************/
	public int msToFrames(int ms)
	{
		return secToFrames(((float) ms / 1000f));
	}


	/******************************************************
	 * Convert time in seconds to a frame count to match this
	 * animators FPS. Rounded to the nearest frame.
	 * @param sec
	 * @return
	 *******************************************************/
	public int secToFrames(float sec)
	{
		return Math.round(sec * ((float)this.mFps));
	}


//
//	/******************************************************
//	 *
//	 * @param pause
//	 *******************************************************/
//	public void setPause(boolean pause)
//	{
//		this.mPause = pause;
//	}

	/******************************************************
	 *
	 *******************************************************/
	public void start()
	{
		stop();

		this.mRunning = true;
		this.mThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(mRunning)
				{
					doFrame();
					try
					{
						/* Not an ideal delay mechanism, but for the accuracy needed to animate a
						Flux Capacitor, it's more than accurate enough */
						Thread.sleep(1000 / mFps);
					}
					catch(Exception e)
					{
						// Do Nothing
					}
				}
			}

		}, "IOIOFluxAnimator");
		this.mThread.start();


	}

	/******************************************************
	 *
	 *******************************************************/
	public void stop()
	{
		System.out.println(">> Stop Animation");
		this.mRunning = false;
		this.mThread = null;
	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public boolean isRunning()
	{
		return this.mThread != null;
	}

	/*****************************************************
	 *
	 ******************************************************/
	private void doFrame()
	{
		try
		{
			this.mFrameCallback.onAnimatorFrame(this.mFps);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}


	/******************************************************
	 * The above animator will call this onAnimatorFrame method every
	 * n Milliseconds to achieve the desired frame rate.
	 * The frame parameter is a circular number of the frame.
	 * If the animator is setup to run at 24fps, this frame will be 0-23
	 *******************************************************/
	public static interface AnimatorFrameCallback
	{
		public void onAnimatorFrame(int fps) throws Exception;
	}
}
