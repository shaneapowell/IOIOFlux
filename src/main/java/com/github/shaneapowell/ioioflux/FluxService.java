package com.github.shaneapowell.ioioflux;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


import com.github.shaneapowell.ioioflux.animations.Animation;
import com.github.shaneapowell.ioioflux.animations.library.AnimationFactory;

import java.util.ArrayList;

import ioio.lib.api.IOIO;
import ioio.lib.api.SpiMaster;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**********************************************************
 * Currently, only use IOIO LIbrary 5.03.  the 5.04 library has some ADB connection issues,
 * as well as some sort of major issue with the SocketConnection implementation.
 ***********************************************************/
public class FluxService extends IOIOService implements IOIOLooper, Animator.AnimatorFrameCallback
{
	public static final int DEFAULT_ANIMATOR_FPS = 40;

	private static final int PIN_SLAVE_SELECT = 45;
	private static final int PIN_SPI_MISO = 46;
	private static final int PIN_SPI_CLOCK = 47;
	private static final int PIN_SPI_MOSI = 48;

	private FluxServiceBinder mBinder = new FluxServiceBinder();

	private IOIO mIOIO;
	private SpiMaster mSpiMaster;

	//	private FluxIO mFluxIO;
	private Animation mAnimation;
	private FluxCap mFluxCap;
	private Animator mAnimator;
//	private FluxCapView mFluxView;

	private RGB mStatusColor = RGB.RED;
	private Pixel mStatusPixel = new Pixel(mStatusColor);

	private ArrayList<Animation> mAnimationList = new ArrayList<Animation>();

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void onCreate()
	{
		super.onCreate();


	    /* Setup up our FluxCaps(s) with our IO API */
	    this.mFluxCap = new FluxCap(/*this,*/ new PixelMatrix());

		/* Create an animator to loop on all of our FluxCaps */
		this.mAnimator = new Animator(DEFAULT_ANIMATOR_FPS, this /*.mFluxCap */);

		/* Each flux cap, has a matching View to use for display purposes */
//		this.mFluxView = new FluxCapView(this, null);
//		this.mFluxView.setFluxCap(this.mFluxCap);


		initAnimations();
		this.mAnimation = this.mAnimationList.get(0);

		fireStatusMessage();
	}


	/******************************************************
	 *
	 *******************************************************/
	private void initAnimations()
	{

		AnimationFactory factory = new AnimationFactory(this, mFluxCap);

		/* 88 MPH */
		this.mAnimationList.add(factory.eightyEightMPH());

		/* Spiral Out */
		this.mAnimationList.add(factory.spiral());

		/* Fade in/out Red */
		this.mAnimationList.add(factory.fadeInOut(RGB.RED, R.string.animation_fade_in_out_red));

		/* Rotate Blue */
		this.mAnimationList.add(factory.rotate(RGB.BLUE, R.string.animation_blue_rotate));

		/* Expanding Pulse in Purple */
		this.mAnimationList.add(factory.pulseBeam(RGB.PURPLE, R.string.animation_pulse_purple));

		/* Rotating Rainbow */
		this.mAnimationList.add(factory.rotatingRainbow());

		/* Cylon */
		this.mAnimationList.add(factory.cylon());


		/* Debugging */
		if (BuildConfig.DEBUG)
		{
			this.mAnimationList.add(factory.test01());
		}



	}


	/******************************************************
	 * Respond to animation events, and hand out the processing
	 * as needed.
	 ******************************************************/
	@Override
	public void onAnimatorFrame(int fps) throws Exception
	{

		if (this.mAnimation != null)
		{
			if (this.mAnimation.isFinished() == false)
			{
				this.mAnimation.step(this.mFluxCap, fps);
			}

			this.mAnimation.write(this.mFluxCap);
		}

		/* We'll write this flux caps pixels every frame, if they change or not... but.. TODO: lets' watch the CPU load just in case. */
//		this.mFluxView.postInvalidate();
		this.write(this.mFluxCap);



	}


	/******************************************************
	 *
	 *******************************************************/
	public void setPowerOn(boolean power)
	{
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		if (power)
		{
			this.mAnimator.start();
			mStatusColor = RGB.GREEN;

			Notification not = new Notification(R.drawable.ic_launcher, "IOIOFlux started", System.currentTimeMillis());
//			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, FluxActivity.class), Notification.FLAG_ONGOING_EVENT);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, FluxActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			not.flags = Notification.FLAG_ONGOING_EVENT;
			not.setLatestEventInfo(this, this.getResources().getString(R.string.app_name), this.getResources().getString(R.string.app_desc), contentIntent);
			nm.notify(1, not);

		}
		else
		{
			this.mAnimator.stop();
			mStatusColor = RGB.RED;
			nm.cancel(1);
		}

		fireStatusMessage();
	}


	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public boolean isPoweredOn()
	{
		return this.mAnimator != null && this.mAnimator.isRunning();
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	public void onDestroy()
	{
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(0);
		setPowerOn(false);
		fireStatusMessage();
		super.onDestroy();
	}

	/******************************************************
	 *
	 * @param intent
	 * @return
	 *******************************************************/
	@Override
	public IBinder onBind(Intent intent)
	{
		fireStatusMessage();
		return this.mBinder;
	}

	/******************************************************
	 *
	 * @param intent
	 *******************************************************/
	@Override
	public void onRebind(Intent intent)
	{
		onBind(intent);
	}

	/******************************************************
	 *
	 * @param intent
	 * @return
	 *******************************************************/
	@Override
	public boolean onUnbind(Intent intent)
	{
		if (isPoweredOn() == false)
		{
			this.stopSelf();
		}
		fireStatusMessage();
		return true;
	}

	/******************************************************
	 *
	 * @param rootIntent
	 *******************************************************/
	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		if (isPoweredOn() == false)
		{
			this.stopSelf();
		}
		super.onTaskRemoved(rootIntent);
		fireStatusMessage();
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	@Override
	protected IOIOLooper createIOIOLooper()
	{
		fireStatusMessage();
		return this;
	}

	/******************************************************
	 *
	 * @param ioio
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 *******************************************************/
	@Override
	public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException
	{
		this.mIOIO = ioio;
		if (this.mSpiMaster != null)
		{
			this.mSpiMaster.close();
		}

		this.mSpiMaster = this.mIOIO.openSpiMaster(PIN_SPI_MISO, PIN_SPI_MOSI, PIN_SPI_CLOCK, PIN_SLAVE_SELECT, SpiMaster.Rate.RATE_1M);

		fireStatusMessage();
	}

	/******************************************************
	 *
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 *******************************************************/
	@Override
	public void loop() throws ConnectionLostException, InterruptedException
	{
		fireStatusMessage();
		Thread.sleep(500);

		if (this.mStatusPixel.getColor() == RGB.OFF)
		{
			this.mStatusPixel.setColor(mStatusColor);
		}
		else
		{
			this.mStatusPixel.setColor(RGB.OFF);
		}

		/* Writing the full flux cap every 500ms will flash the status LED */
		write(this.mFluxCap);
	}

	/*******************************************************
	 *
	 ********************************************************/
	@Override
	public void disconnected()
	{
		if (this.mSpiMaster != null)
		{
			this.mSpiMaster.close();
			this.mSpiMaster = null;
		}
		fireStatusMessage();
	}

	/*******************************************************
	 *
	 ********************************************************/
	@Override
	public void incompatible()
	{
		fireStatusMessage();
	}

//	/*******************************************************
//	 *
//	 ********************************************************/
//	@Override
//	public void incompatible(IOIO ioio)
//	{
//		fireStatusMessage();
//	}

	/******************************************************
	 *
	 * @param animation
	 *******************************************************/
	public void setAnimation(Animation animation)
	{
		animation.reset();
		this.mAnimation = animation;
	}


	/******************************************************
	 *
	 *******************************************************/
	public Animation getAnimation()
	{
		return this.mAnimation;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public ArrayList<Animation> getAnimationsList()
	{
		return this.mAnimationList;
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public FluxCap getFluxCap()
	{
		return this.mFluxCap;
	}

//	/******************************************************
//	 *
//	 * @return
//	 *******************************************************/
//	public FluxCapView getFluxView()
//	{
//		fireStatusMessage();
//		return this.mFluxView;
//	}

	/******************************************************
	 *
	 *******************************************************/
	private void fireStatusMessage()
	{
		FluxBroadcastReceiver.StatusMessage msg = new FluxBroadcastReceiver.StatusMessage();

		msg.connectionState = getString(R.string.ioio_state_disconnected);
		msg.isPoweredOn = isPoweredOn();

		if (this.mIOIO != null)
		{
			try
			{
				msg.connectionState = getString(R.string.ioio_state_disconnected);
				if (this.mIOIO.getState() == IOIO.State.INIT)
				{
					msg.connectionState = getString(R.string.ioio_state_connecting);
				}
				else if (this.mIOIO.getState() == IOIO.State.CONNECTED)
				{
					msg.connectionState = getString(R.string.ioio_state_connected);
				}
				else if (this.mIOIO.getState() == IOIO.State.INCOMPATIBLE)
				{
					msg.connectionState = getString(R.string.ioio_state_error);
				}
				else
				{
					msg.connectionState = getString(R.string.ioio_state_disconnected);
				}
				msg.hardwareVersion = this.mIOIO.getImplVersion(IOIO.VersionType.HARDWARE_VER);
				msg.bootloaderVersion = this.mIOIO.getImplVersion(IOIO.VersionType.BOOTLOADER_VER);
				msg.firmwareVersion = this.mIOIO.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER);
				msg.ioioLibVersion = this.mIOIO.getImplVersion(IOIO.VersionType.IOIOLIB_VER);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				msg.connectionState = getString(R.string.ioio_state_error);
			}
		}



		Intent intent = new Intent();
		intent.setAction(FluxBroadcastReceiver.INTENT_FILTER_STATUS_ACTION);
		intent.putExtra(FluxBroadcastReceiver.EXTRA_STATUS_MESSAGE, msg);

		sendBroadcast(intent);
	}

	/******************************************************
	 *
	 *******************************************************/
	public synchronized void write(FluxCap flux)
	{
		Pixel[] pixels = flux.getAllPixels();

		/* 3 bytes per LED, plus 1 for the status LED  */
		final byte[] out = new byte[(pixels.length + 1) * Pixel.BYTES_PER_PIXEL];

		for (int index = 0; index < pixels.length; index++)
		{
			pixels[index].write(out, index * Pixel.BYTES_PER_PIXEL);
		}

		/*, plus 3 more for the connection status LED */
		this.mStatusPixel.write(out, out.length - Pixel.BYTES_PER_PIXEL);

		/* Send it */
		this.write(out);


//		/*, plus 3 more for the connection status LED */
//		final byte[] statusOut = new byte[Pixel.BYTES_PER_PIXEL];
//		this.mStatusPixel.write(statusOut, 0);
//		this.write(statusOut);


	}

	/******************************************************
	 *
	 * @param bytes
	 *******************************************************/
	private void write(byte[] bytes)
	{
		if (this.mSpiMaster != null)
		{

			try
			{
				this.mSpiMaster.writeRead(bytes, bytes.length, bytes.length, null, 0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			//TODO
//			System.out.println(">>> SPI Bus not open");
		}

	}


	/******************************************************
	 * Our in-process binder to access the service.
	 *******************************************************/
	public class FluxServiceBinder extends Binder
	{
		FluxService getService()
		{
			return FluxService.this;
		}
	}
}
