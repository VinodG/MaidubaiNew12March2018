package com.winit.maidubai;

import android.app.IntentService;
import android.content.Intent;

import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.DataSyncDO;
import com.winit.maidubai.parser.BaseJsonHandler;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.BuildJsonRequest;
import com.winit.maidubai.webaccessLayer.HttpHelper;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Girish Velivela on 01-10-2016.
 */
public class SyncIntentService extends IntentService
{
    private static SyncListener syncListener;

    public static SyncListener getSyncListener() {
        return syncListener;
    }

    public static void setSyncListener(SyncListener syncListener) {
        SyncIntentService.syncListener = syncListener;
    }

    public interface SyncListener extends Serializable{
        public void onStart();
        public void onError(String message);
        public void onEnd();
    }

    public SyncIntentService(String name) {
        super("SyncIntentService");
    }

    public  SyncIntentService()
    {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        syncListener = (SyncListener)intent.getSerializableExtra("Listener");

        Preference preference = new Preference(this);
        preference.saveBooleanInPreference(Preference.IS_SYNCING,true);
        preference.saveLongInPreference(Preference.LAST_UPLOAD_TIME,System.currentTimeMillis());

        if(syncListener != null)
            syncListener.onStart();
        CustomerDA customerDA =  new CustomerDA(this);
        String lastSyncTime = customerDA.getLastSyncTime();
        String syncTime = CalendarUtil.getDate(lastSyncTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, 5 * 60 * 1000, Locale.ENGLISH);
        String customerCode = preference.getStringFromPreference(Preference.CUSTOMER_CODE, "");
        BaseJsonHandler baseHandler         = BaseJsonHandler.getDataSync(this);
        String url = ServiceUrls.MAIN_URL +ServiceUrls.DATASYNC_URL+ BuildJsonRequest.dataSyncQueryParams(customerCode,syncTime);
//        String url = "http://maidubai.winitsoftware.com/api/MasterDataAPI/DeltaSync?CustomerId=DXO2389&LastSyncDateTime=2015-12-17+17%3A38%3A42";
        try {
            InputStream inputStream = new HttpHelper().sendRequest(url,ServiceUrls.METHOD_GET,null,null);
            if(inputStream != null) {
                String responseStr = StringUtils.convertStreamToString(inputStream);
                baseHandler.parse(responseStr);
                Object data = baseHandler.getData();
                String deliveryDays = customerDA.getDeliveryDays();
                preference.saveStringInPreference(Preference.DELIVERY_DAYS,deliveryDays);
                if(data != null && data instanceof DataSyncDO && ((DataSyncDO)data).status !=0){
                    if(syncListener != null)
                        syncListener.onEnd();
                }else if(syncListener != null)
                    syncListener.onError("");
            }
        }catch (Exception e){
//            e.printStackTrace();
            if(syncListener != null) {
                syncListener.onError("");
            }
            throw new RuntimeException("This can never happen", e);
        }finally {
            preference.saveBooleanInPreference(Preference.IS_SYNCING,false);
            if(syncListener != null)
                syncListener.onError("");
            syncListener=null;
        }

    }
}
