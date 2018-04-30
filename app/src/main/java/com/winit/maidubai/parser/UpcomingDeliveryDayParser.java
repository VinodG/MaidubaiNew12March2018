package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.UpcomingDeliveryDays;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Girish Velivela on 24-08-2016.
 */
public class UpcomingDeliveryDayParser extends BaseJsonHandler{

    private ArrayList<UpcomingDeliveryDays> arrUpcomingDeliveryDays;

    public Object getData()
    {
        return status==0?message: arrUpcomingDeliveryDays;
    }
    public void parse(String response){
        try {
            JSONObject jsonAddress = new JSONObject(response);
            status = jsonAddress.optInt("Status");
            message = jsonAddress.optString("Message");
            if(status != 0 && jsonAddress.has("UpcomingDeliveryDates")){
                JSONArray jsonArray = jsonAddress.getJSONArray("UpcomingDeliveryDates");
                arrUpcomingDeliveryDays = new ArrayList<>();
                for(int i=0 ; i<jsonArray.length();i++) {
                    JSONObject jsonAddressBook = jsonArray.getJSONObject(i);
                    UpcomingDeliveryDays upcomingDeliveryDays = new UpcomingDeliveryDays();
                    upcomingDeliveryDays.Id = jsonAddressBook.optLong("Id");
                    upcomingDeliveryDays.DeliveryDate = CalendarUtil.getDate(jsonAddressBook.optString("DeliveryDate"),CalendarUtil.DD_MMM_YYYY_PATTERN,CalendarUtil.DATE_PATTERN_dd_MMM_YYYY, Locale.ENGLISH);
                    arrUpcomingDeliveryDays.add(upcomingDeliveryDays);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }

}
