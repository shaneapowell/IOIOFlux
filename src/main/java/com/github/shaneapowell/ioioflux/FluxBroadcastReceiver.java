package com.github.shaneapowell.ioioflux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**********************************************************
 *
 **********************************************************/
public class FluxBroadcastReceiver extends BroadcastReceiver
{

	public static final String INTENT_FILTER_STATUS_ACTION = StatusMessage.class.getName();
	public static final String EXTRA_STATUS_MESSAGE = "_STATUS_MESSAGE_";

	private List<StatusMessageReceiver> messageReceiverList = new ArrayList<StatusMessageReceiver>();

	/******************************************************
	 *
	 * @param context
	 * @param intent
	 *******************************************************/
	@Override
	public void onReceive(Context context, Intent intent)
	{
		StatusMessage msg = (StatusMessage)intent.getSerializableExtra(EXTRA_STATUS_MESSAGE);
		if (msg != null)
		{
			for (StatusMessageReceiver receiver : this.messageReceiverList)
			{
				receiver.onStatusMessage(msg);
			}
		}
	}

	/******************************************************
	 *
	 * @param receiver
	 *******************************************************/
	public void addMessageReceiver(StatusMessageReceiver receiver)
	{
		this.messageReceiverList.add(receiver);
	}


	/******************************************************
	 *
	 * @param receiver
	 *******************************************************/
	public void removeMessageReceiver(StatusMessageReceiver receiver)
	{
		this.messageReceiverList.remove(receiver);
	}

	/******************************************************
	 *
	 * @return
	 *******************************************************/
	public static IntentFilter createIntentFilter()
	{
		return new IntentFilter(INTENT_FILTER_STATUS_ACTION);
	}

	/******************************************************
	 *
	 *******************************************************/
	public static interface StatusMessageReceiver
	{
		public void onStatusMessage(StatusMessage msg);
	}

	/******************************************************
	 *
	 *******************************************************/
	public static final class StatusMessage implements Serializable
	{
		public String connectionState;

		public String hardwareVersion;
		public String bootloaderVersion;
		public String firmwareVersion;
		public String ioioLibVersion;

		public boolean isPoweredOn;

	}
}
