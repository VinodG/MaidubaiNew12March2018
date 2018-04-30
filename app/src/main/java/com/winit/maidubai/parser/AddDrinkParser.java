package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by srikrishna.nunna on 09-09-2016.
 */
public class AddDrinkParser extends BaseJsonHandler{

    private HydrationMeterReadingDO hydrationMeterReadingDO;
    public Object getData()
    {
        return status==0?message:hydrationMeterReadingDO;
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if(status != 0){
                JSONArray jsonArray = jsonObject.getJSONArray("HydrationMeterReading");
                if(jsonArray!=null && jsonArray.length()>0) {
                    JSONObject jsonObjectHydration = jsonArray.getJSONObject(0);
                    hydrationMeterReadingDO = new HydrationMeterReadingDO();
                    hydrationMeterReadingDO.HydrationMeterReadingId = jsonObjectHydration.getInt("HydrationMeterReadingId");
                    hydrationMeterReadingDO.CustomerId = jsonObjectHydration.getInt("CustomerId");
                    hydrationMeterReadingDO.WaterConsumed = jsonObjectHydration.getInt("WaterConsumed");
                    hydrationMeterReadingDO.WaterConsumedPercent = jsonObjectHydration.getInt("Percentage");
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }

}
