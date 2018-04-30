package com.winit.maidubai.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


@SuppressLint("CommitPrefEdits") public class Preference
{

	private SharedPreferences preferences;
	private SharedPreferences.Editor edit;
	public static String LANGUAGE 						=	"LANGUAGE";
	public static String EMAIL_ID 						=	"EMAILID";
	public static String CUSTOMER_ID 					=	"CUSTOMERID";
	public static String CUSTOMER_CODE 					=	"CUSTOMER_CODE";
	public static String LAST_ORDERS_SYNC 				=	"LastSyncDateTime";
	public static String SESSION_ID 					=	"SESSION_ID";
	public static String CUSTOMER_NAME 					=	"NAME";
	public static String CUSTOMER_MOBILE 				=	"CUSTOMER_MOBILE";
	public static String DEVICE_DISPLAY_WIDTH 			=	"DEVICE_DISPLAY_WIDTH";
	public static String DEVICE_DISPLAY_HEIGHT			=	"DEVICE_DISPLAY_HEIGHT";
	public static String FIRST_TIME_DISPLAY  			=	"IS_FIRST_TIME";
	public static String IS_LOGGED_IN  					=	"Is Logged in";
	public static String NOTIFICATION_FLAG  			=	"notificationFlag";
	public static String CityName  				    	=	"CityName";
	public static String TEMPERATURE  					=	"TEMPERATURE";
	public static String TEMPERATURE_ICON  				=	"TEMP ICON ID";
	public static String Latitude  						=	"Latitude";
	public static String Longitude  					=	"Longitude";
	public static String USER_IMAGE  				    =	"USER IMAGE";
	public static String OTP  				    		=	"OTP";
	public static String ISSINGUP  				    	=	"ISSINGUP";
	public static String SHOWDELIVERY  				    =	"DeliveryPopup";
	public static String DELIVERY_DAYS  				=	"DELIVERYDAYS";
	public static String IS_DISCLAIMER  				=	"DISCLAIMER";
	public static String GCM_TOKEN  					=	"GCM_TOKEN";
	public static String IS_SYNCING  					=	"IS_SYNCING";
	public static String LAST_UPLOAD_TIME  				=	"LAST_UPLOAD_TIME";
	public static String LOCALE_UPDATED  				=	"LocaleUpdated";
	public static String HYDRATION_TIME_PERIOD  		=	"HYDRATION_TIME_PERIOD";

	public static String APP_INSTALLATION_DATE = "installation_date";



	public Preference(Context context)
	{
		preferences		=	PreferenceManager.getDefaultSharedPreferences(context);
		edit			=	preferences.edit();
	}

	public void saveStringInPreference(String strKey,String strValue)
	{
		edit.putString(strKey, strValue);
		commitPreference();
	}

	public void saveIntInPreference(String strKey,int value)
	{
		edit.putInt(strKey, value);
		commitPreference();
	}
	public void saveBooleanInPreference(String strKey,boolean value)
	{
		edit.putBoolean(strKey, value);
		commitPreference();
	}
	public void saveLongInPreference(String strKey,Long value)
	{
		edit.putLong(strKey, value);
		commitPreference();
	}
	public void saveDoubleInPreference(String strKey,String value)
	{
		edit.putString(strKey, value);
		commitPreference();
	}

	public void removeFromPreference(String strKey)
	{
		edit.remove(strKey);
		commitPreference();
	}
	private void commitPreference()
	{
		edit.commit();
	}
	public String getStringFromPreference(String strKey,String defaultValue )
	{
		return preferences.getString(strKey, defaultValue);
	}
	public boolean getbooleanFromPreference(String strKey,boolean defaultValue)
	{
		return preferences.getBoolean(strKey, defaultValue);
	}
	public int getIntFromPreference(String strKey,int defaultValue)
	{
		return preferences.getInt(strKey, defaultValue);
	}
	public double getDoubleFromPreference(String strKey,double defaultValue)
	{
		return	Double.parseDouble(preferences.getString(strKey, ""+defaultValue));
	}

	public long getLongInPreference(String strKey)
	{
		return preferences.getLong(strKey, 0);
	}

	public void clearPreferences() {
		edit.clear();
		edit.commit();
	}
}
