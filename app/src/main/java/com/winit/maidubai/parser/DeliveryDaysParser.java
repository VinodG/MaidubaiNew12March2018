package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 02-09-2016.
 */
public class DeliveryDaysParser extends BaseJsonHandler {

    private HashMap<Integer,ArrayList<DeliveryDay>> hmDeliveryDays;

    public Object getData()
    {
        return status==0?message: hmDeliveryDays;
    }
    public void parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if (status != 0 && jsonObject.has("AreaDeliveryDetails")) {
                JSONArray jsonAreaArray = jsonObject.getJSONArray("AreaDeliveryDetails");
                int jsonAreaArrayLength = jsonAreaArray.length();
                hmDeliveryDays = new HashMap<>();
                for(int i=0;i<jsonAreaArrayLength;i++){
                    JSONObject jsonArea = jsonAreaArray.getJSONObject(i);
                    DeliveryDay deliveryDay = new DeliveryDay();
                    deliveryDay.AreaDeliveryDetailId = jsonArea.getInt("AreaDeliveryDetailId");
                    deliveryDay.AreaId = jsonArea.getInt("AreaId");
                    deliveryDay.DeliveryDay = jsonArea.getString("DeliveryDay");
                    deliveryDay.Route = jsonArea.getString("Route");
                    ArrayList<DeliveryDay> arrDeliveryDays = hmDeliveryDays.get(deliveryDay.AreaId);
                    if(arrDeliveryDays==null)
                        arrDeliveryDays = new ArrayList<>();
                    arrDeliveryDays.add(deliveryDay);
                    hmDeliveryDays.put(deliveryDay.AreaId, arrDeliveryDays);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
