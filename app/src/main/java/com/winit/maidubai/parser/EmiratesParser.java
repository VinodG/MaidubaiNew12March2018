package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 26-09-2016.
 */
public class EmiratesParser extends BaseJsonHandler{

    private ArrayList<Emirates> arrEmirates;

    Object[] data;

    @Override
    public Object getData() {
        return status==0?message:data;
    }

    @Override
    public void parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonStatus = jsonObject.optJSONObject("objResponse");
            status = jsonStatus.optInt("Status");
            message = jsonStatus.optString("Message");
            if(status != 0){
                data = new Object[2];
                JSONArray jsonArray = jsonObject.optJSONArray("Emirates");
                int jsonEmiratesLength = jsonArray.length();
                arrEmirates = new ArrayList<>();
                for(int i=0;i<jsonEmiratesLength;i++){
                    JSONObject jsonEmirate = jsonArray.optJSONObject(i);
                    Emirates emirates = new Emirates();
                    emirates.EmiratesId = jsonEmirate.optInt("EmiratesId");
                    emirates.Name = jsonEmirate.optString("Name");
                    emirates.ArabicName = jsonEmirate.optString("Name_Arabic");
                    emirates.arrAreaDOs = parseAreas(jsonEmirate.optJSONArray("Areas"));
                    arrEmirates.add(emirates);
                }
                data[0] = arrEmirates;
                data[1] = parseDeliveryDays(jsonObject.optJSONArray("AreaDeliveryDetails"));
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }

    public ArrayList<AreaDO> parseAreas(JSONArray jsonArray) throws JSONException{
        if(jsonArray !=null){
            ArrayList<AreaDO> areaDOs = new ArrayList<>();
            int jsonArealenght = jsonArray.length();
            for(int i=0;i<jsonArealenght;i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                AreaDO areaDO = new AreaDO();
                areaDO.AreaId = jsonObject.optInt("AreaId");
                areaDO.Name = jsonObject.optString("Name");
                areaDO.ArabicName = jsonObject.optString("Name_Arabic");
                areaDO.arrAreaDOs = parseSubAreaDays(jsonObject.optJSONArray("SubAreas"));
                areaDOs.add(areaDO);
            }
            return areaDOs;
        }
        return null;
    }
    public ArrayList<AreaDO> parseSubAreaDays(JSONArray jsonArray) throws JSONException{
        if(jsonArray !=null){
            ArrayList<AreaDO> areaDOs = new ArrayList<>();
            int jsonArealenght = jsonArray.length();
            for(int i=0;i<jsonArealenght;i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                AreaDO areaDO = new AreaDO();
                areaDO.AreaId = jsonObject.optInt("AreaId");
                areaDO.Name = jsonObject.optString("Name");
                areaDO.ArabicName = jsonObject.optString("Name_Arabic");
                areaDOs.add(areaDO);
            }
            return areaDOs;
        }
        return null;
    }

    public HashMap<Integer,ArrayList<DeliveryDay>> parseDeliveryDays(JSONArray jsonArray) throws JSONException{
        HashMap<Integer,ArrayList<DeliveryDay>> hmDeliveryDays = null;
        if(jsonArray !=null){
            hmDeliveryDays = new HashMap<>();
            int jsonArealenght = jsonArray.length();
            for(int i=0;i<jsonArealenght;i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                DeliveryDay deliveryDay = new DeliveryDay();
                deliveryDay.AreaDeliveryDetailId = jsonObject.optInt("AreaDeliveryDetailId");
                deliveryDay.AreaId = jsonObject.optInt("AreaId");
                deliveryDay.AreaRouteSupervisorId = jsonObject.optInt("SupervisorId");
                deliveryDay.DeliveryDay = jsonObject.optString("DeliveryDay");
                deliveryDay.Route = jsonObject.optString("RouteId");
                ArrayList<DeliveryDay> arrDeliveryDays = hmDeliveryDays.get(deliveryDay.AreaId);
                if(arrDeliveryDays == null)
                    arrDeliveryDays = new ArrayList<>();
                arrDeliveryDays.add(deliveryDay);
                hmDeliveryDays.put(deliveryDay.AreaId,arrDeliveryDays);
            }
        }
        return hmDeliveryDays;
    }
}
