package com.winit.maidubai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.DataSyncDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.os.SystemClock.sleep;

public class SplashActivity extends BaseActivity {


    private LinearLayout llMain;
    private boolean isFirst;
    private  GpsLocationReceiver gpsReciever;
    @Override
    public void initialise() {
        LogUtils.debug(LogUtils.LOG_TAG,"initialise");
        llMain = (LinearLayout) inflater.inflate(R.layout.activity_splash, null);
        toolbar.setVisibility(View.GONE);
        llBody.addView(llMain, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        isLoggedIn = preference.getbooleanFromPreference(Preference.IS_LOGGED_IN,false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        gpsReciever = new GpsLocationReceiver();
        registerReceiver(gpsReciever,intentFilter);
        Random r;

        try {
            r = SecureRandom.getInstance("SHA1PRNG");
            int i1 = r.nextInt();
            if(i1<0)
                i1=i1*-1;
            int i2 = r.nextInt();
        } catch(NoSuchAlgorithmException nsae) {
            // Process the exception in some way or the other
            throw new RuntimeException("This can never happen", nsae);
        }
        long installation_time = (Long) preference.getLongInPreference(preference.APP_INSTALLATION_DATE);
        if (installation_time == 0) {
            long value = System.currentTimeMillis();
            preference.saveLongInPreference(preference.APP_INSTALLATION_DATE,value);
            addShortcut(getApplicationContext());
        }

//        test();
        if(checkPermission())
            initializeSplash();
    }

    public class GpsLocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                if (!NetworkUtil.isGpsOn(SplashActivity.this)) {
                    Toast.makeText(SplashActivity.this, "Please...! Turn on GPS", Toast.LENGTH_LONG);
                }else{
                    loadData();
                }
            }
        }
    }


    private void test()
    {
        String time = "1473705000";
        long timestampLong = Long.parseLong(time)*1000;
        Date d = new Date(timestampLong);
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);

        LogUtils.errorLog("date",year +"-"+month+"-"+date);
    }
    public void initializeSplash(){
        LogUtils.debug(LogUtils.LOG_TAG,"initializeSplash");
        lockDrawer("SplashActivity");
        initialiseControls();
        setTypeFaceNormal(llMain);
        loadData();
    }

    private final int REQUEST_CODE_ASK_PERMISSIONS = 1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        LogUtils.debug(LogUtils.LOG_TAG,"onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0) {
                    for(int grantResult :grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(SplashActivity.this, "All permissions are required.", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    initializeSplash();
                } else {
                    Toast.makeText(SplashActivity.this, "All permissions are required.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            final ArrayList<String> permissions = new ArrayList<>();
/*
*
*  CALL_PHONE
*  CALL_PHONE
*  CAMERA
*  ACCESS_FINE_LOCATION
*
* */
//            if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.CALL_PHONE);
//            }
//            if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
            if (ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.CAMERA);
            }

            if (permissions.size()>0) {

                int permissionLength = permissions.size();
                final String[] permissionArray = new String[permissionLength];
                for(int i=0; i<permissionLength; i++){
                    permissionArray[i] = permissions.get(i);
                }
                ActivityCompat.requestPermissions(SplashActivity.this,permissionArray, REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void initialiseControls() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        preference.saveIntInPreference(Preference.DEVICE_DISPLAY_WIDTH, displaymetrics.widthPixels);
        preference.saveIntInPreference(Preference.DEVICE_DISPLAY_HEIGHT, displaymetrics.heightPixels);

        AppConstants.DinproBold        = Typeface.createFromAsset(getApplicationContext().getAssets(), "DINPro_Bold.ttf");
        AppConstants.DinproLight        = Typeface.createFromAsset(getApplicationContext().getAssets(), "DINPro_Light.otf");
        AppConstants.DinproMedium        = Typeface.createFromAsset(getApplicationContext().getAssets(), "DINPro_Medium.otf");
        AppConstants.DinproRegular        = Typeface.createFromAsset(getApplicationContext().getAssets(), "DINPro_Regular.otf");
        isFirst = preference.getbooleanFromPreference(Preference.FIRST_TIME_DISPLAY, false);

    }

    @Override
    public void loadData() {
        LogUtils.debug(LogUtils.LOG_TAG, "loadData");
        setLocale(preference.getStringFromPreference(Preference.LANGUAGE,""));

        /* To start Sync for every one hour */

       /* Intent alarmIntent = new Intent(SplashActivity.this, SyncReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, AppConstants.SYNC_SERVICE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, ((new Date()).getTime())*(1 * (3600 * 1000)), 1 * (3600 * 1000), pendingIntent);
        */
        /* To start Sync for every one hour */

        if (!NetworkUtil.isGpsOn(SplashActivity.this)) {
            showCustomDialog(SplashActivity.this, getString(R.string.alert), getString(R.string.No_GPS_turn_on), getString(R.string.turn_on), getString(R.string.cancel), AppConstants.GPS_ERROR,false);
        }else {
            String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
            if(StringUtils.isEmpty(token)) {
                Intent intent = new Intent(SplashActivity.this, RegistrationIntentService.class);
                startService(intent);
            }
            Thread timerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                       // FileUtils.exractZip(getAssets().open("Data.zip"),AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR);
//                        FileUtils.exractZip(getAssets().open("Data.zip"),getFilesDir().toString()+AppConstants.IMAGE_DIR);
                        if (isLoggedIn) {
                           /* final String lastSyncTime = new CustomerDA(SplashActivity.this).getLastSyncTime();
                            final String syncTime = CalendarUtil.getDate(lastSyncTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, 5 * 60 * 1000,Locale.ENGLISH);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (syncTime != null && NetworkUtil.isNetworkConnectionAvailable(SplashActivity.this)) {
                                        new CommonBL(SplashActivity.this, SplashActivity.this).dataSync(preference.getStringFromPreference(Preference.CUSTOMER_CODE, ""), syncTime);
                                        LogUtils.errorLog("lastSyncTime", syncTime);
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });*/
                            Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            sleep(2000);
                        }
                    } catch (Exception e) {
                       /*e.printStackTrace(); */   Log.d("This can never happen", e.getMessage());
                    } finally {
                        if (!isFirst) {
                            Intent intent = new Intent(SplashActivity.this, HelpActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (!isLoggedIn) {
                                Intent intent = new Intent(SplashActivity.this, LanguageSelectionActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }
                }
            });
            timerThread.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase(AppConstants.GPS_ERROR)){
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        super.onButtonYesClick(from);
    }

    @Override
    public void onButtonNoClick(String from) {
        super.onButtonNoClick(from);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(gpsReciever);
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && response.method == ServiceMethods.WS_DATASYNC) {
            if (response.data != null && response.data instanceof DataSyncDO) {
                DataSyncDO dataSyncDO = (DataSyncDO)response.data;
                String syncTime = CalendarUtil.getDate(dataSyncDO.serverTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.SYNCH_DATE_TIME_PATTERN,10*60*1000,Locale.ENGLISH);
                preference.saveStringInPreference(Preference.LAST_ORDERS_SYNC, syncTime);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String deliveryDays = new CustomerDA(SplashActivity.this).getDeliveryDays();
                    preference.saveStringInPreference(Preference.DELIVERY_DAYS,deliveryDays);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                            intent.putExtra("ShowDeliveryPopup",true);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }).start();
        }
    }

    public static void addShortcut(Context context) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName("com.winit.maidubai",
                "com.winit.maidubai.SplashActivity");
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
                        R.drawable.app_icon));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }
}
