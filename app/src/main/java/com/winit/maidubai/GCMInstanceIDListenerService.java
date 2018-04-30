package com.winit.maidubai;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.winit.maidubai.utilities.LogUtils;

/**
 * Created by Girish Velivela on 21-09-2016.
 */
public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "GCMInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        LogUtils.debug(TAG,"onTokenRefresh - Strated");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        LogUtils.debug(TAG, "onTokenRefresh - Ended");
    }
}
