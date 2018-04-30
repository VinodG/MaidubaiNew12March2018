package com.winit.maidubai.utilities;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtil
{
	/**
	 * Method to check Network Connections 
	 * @param context
	 * @return boolean value
	 */
	public static boolean isNetworkConnectionAvailable(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		if(activeNetworkInfo != null) 
		    isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;

		return isNetworkConnectionAvailable;
	}

	public static boolean isGpsOn(Context context)
	{
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
		if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
			else
		return false;
	}

	public static boolean isWifiConnected(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		ConnectivityManager connManager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			isNetworkConnectionAvailable = true;
		}
		return isNetworkConnectionAvailable;
	}
	
	
	
}
