package com.winit.maidubai;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.io.IOException;

/**
 * Created by Girish Velivela on 21-09-2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = "";
        int i=0;
        while (true){
            try {
                InstanceID instanceID = InstanceID.getInstance(this);
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                LogUtils.debug("GCM Token SplashActivity : ", token+"");
            } catch (IOException e) {
              /*e.printStackTrace(); */   Log.d("This can never happen", e.getMessage());

            }finally {
                if(i == 3){
                    break;
                }
                if(!StringUtils.isEmpty(token)){
                    Preference preference = new Preference(this);
                    preference.saveStringInPreference(Preference.GCM_TOKEN,token);
                    sendRegistrationToServer(token);
                    break;
                }
            }
            i++;
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

}
