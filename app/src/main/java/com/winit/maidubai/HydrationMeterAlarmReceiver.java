package com.winit.maidubai;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.facebook.internal.Validate;
import com.winit.maidubai.HydrationMeterActivity;
import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataaccesslayer.HydrationMeterDA;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.utilities.CalendarUtil;

import java.util.Date;

/**
 * Created by Girish Velivela on 26-09-2016.
 */
public class HydrationMeterAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String currDate = CalendarUtil.getDate(new Date(), CalendarUtil.DD_MM_YYYY_PATTERN);
        HydrationMeterDA hydrationMeterDA =new HydrationMeterDA(context);
        HydrationMeterReadingDO hydrationMeterReadingDO = hydrationMeterDA.getHydrationMeterReading(currDate);
        HydrationMeterDO hydrationMeterDO = hydrationMeterDA.getHydrationMeterSettings();
        generateNotification(context,hydrationMeterDO,hydrationMeterReadingDO);
    }

    public void generateNotification(Context context, HydrationMeterDO hydrationMeterDO, HydrationMeterReadingDO hydrationMeterReadingDO)
    {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int count = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, HydrationMeterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,count,intent, 0);
        String message = "";
        if(hydrationMeterReadingDO == null || hydrationMeterReadingDO.WaterConsumed == 0){
            message = context.getString(R.string.add_first_glass);
        }else{
            message = String.format(context.getString(R.string.add_drink_meter),hydrationMeterReadingDO.WaterConsumed+"",hydrationMeterDO.DailyWaterConsumptionTarget+"");
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Mai Dubai")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(AppConstants.HYDRATION_NOTIFICATION, mBuilder.build());
    }
}
