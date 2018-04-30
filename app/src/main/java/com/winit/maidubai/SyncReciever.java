package com.winit.maidubai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;

import java.util.Locale;

/**
 * Created by Girish Velivela on 01-10-2016.
 */
public class SyncReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Preference preference = new Preference(context);
        boolean isLoggedIn = preference.getbooleanFromPreference(Preference.IS_LOGGED_IN, false);
        if (isLoggedIn && NetworkUtil.isNetworkConnectionAvailable(context)) {
            Intent syncIntent = new Intent(context,SyncIntentService.class);
            context.startService(syncIntent);
        }
    }
}
