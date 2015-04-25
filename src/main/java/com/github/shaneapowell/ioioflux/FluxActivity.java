package com.github.shaneapowell.ioioflux;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.shaneapowell.ioioflux.animations.Animation;



/**********************************************************
 *
 *********************************************************/
public class FluxActivity extends Activity implements FluxBroadcastReceiver.StatusMessageReceiver//, AdapterView.OnItemSelectedListener
{


	private FluxCapGLView mFluxView;

	private FluxService.FluxServiceBinder mFluxServiceBinder;
	private FluxService mFluxService;

	private ServiceConnection fluxServiceConnection;

	private FluxBroadcastReceiver mFluxReceiver = null;

	private TextView mStatusTextView;

	/**************************************************
	 *
	 * @param savedInstanceState
	 ***************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

	    /* Fire up the service */
	    startService(new Intent(this, FluxService.class));

	    /* Setup our status message receiver */
	    this.mFluxReceiver = new FluxBroadcastReceiver();
	    this.mFluxReceiver.addMessageReceiver(this);

	    setContentView(R.layout.activity_flux);

	    this.mStatusTextView = (TextView)findViewById(R.id.tv_status);
	    this.mFluxView = (FluxCapGLView)findViewById(R.id.fluxcap);


	    /* Create our service connection */
	    this.fluxServiceConnection = new ServiceConnection()
	    {
		    @Override
		    public void onServiceConnected(ComponentName name, IBinder service)
		    {
			    mFluxServiceBinder = (FluxService.FluxServiceBinder)service;
			    mFluxService = mFluxServiceBinder.getService();
			    mFluxView.setFluxCap(mFluxService.getFluxCap());
			    invalidateOptionsMenu();
			    populateAnimationSpinner();
		    }

		    @Override
		    public void onServiceDisconnected(ComponentName name)
		    {
			    mFluxView.setFluxCap(null);
			    mFluxServiceBinder = null;
			    mFluxService = null;
			    invalidateOptionsMenu();
				populateAnimationSpinner();
		    }

	    };

    }



	/******************************************************
	 *
	 *******************************************************/
	@Override
	protected void onStart()
	{
		super.onStart();
		bindService(new Intent(this, FluxService.class), this.fluxServiceConnection, Context.BIND_AUTO_CREATE);
		this.registerReceiver(this.mFluxReceiver, FluxBroadcastReceiver.createIntentFilter());
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	protected void onResume()
	{
		super.onResume();
		this.mFluxView.onResume();
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	protected void onPause()
	{
		super.onPause();
		this.mFluxView.onPause();
	}

	/******************************************************
	 *
	 *******************************************************/
	@Override
	protected void onStop()
	{
//		mFluxContainer.removeAllViews();
		unbindService(fluxServiceConnection);
		this.unregisterReceiver(this.mFluxReceiver);
		super.onStop();

	}

	/**************************************************
	 *
	 * @param menu
	 * @return
	 ***************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/**************************************************
	 *
	 * @param item
	 * @return
	 ***************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            startActivityForResult(new Intent(this, FluxPrefsActivity.class), 1);
        }
	    else if (id == R.id.action_power)
        {
	        this.mFluxService.setPowerOn(!this.mFluxService.isPoweredOn());
        }

	    invalidateOptionsMenu();

        return super.onOptionsItemSelected(item);
    }

	/******************************************************
	 *
	 * @param menu
	 * @return
	 *******************************************************/
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		{
			boolean power = this.mFluxService != null && this.mFluxService.isPoweredOn();
			MenuItem item = menu.findItem(R.id.action_power);
			item.setIcon(power ? R.drawable.ic_action_power_on : R.drawable.ic_action_power_off);
		}

		return super.onPrepareOptionsMenu(menu);
	}


	/******************************************************
	 * Setup the navigation bar as an animation selector.
	 *******************************************************/
	private void populateAnimationSpinner()
	{
		ActionBar ab = getActionBar();
		if (this.mFluxService != null)
		{

			ab.removeAllTabs();
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			ab.setTitle(null);

			ArrayAdapter<Animation> adapter = new ArrayAdapter<Animation>(
					getBaseContext(),
					android.R.layout.simple_spinner_dropdown_item, mFluxService.getAnimationsList());

			ab.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener()
			{
				@Override
				public boolean onNavigationItemSelected(int itemPosition, long itemId)
				{
					mFluxService.setAnimation(mFluxService.getAnimationsList().get(itemPosition));
					return true;
				}
			});


			ab.setSelectedNavigationItem(adapter.getPosition(mFluxService.getAnimation()));

		}
		else
		{
			ab.setTitle(R.string.app_name);
		}
	}


	/******************************************************
	 *
	 * @param msg
	 *******************************************************/
	@Override
	public void onStatusMessage(FluxBroadcastReceiver.StatusMessage msg)
	{
		this.mStatusTextView.setText(msg.connectionState);
	}



}
