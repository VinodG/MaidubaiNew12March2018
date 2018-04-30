package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by Kishore.G on 4/14/2016.
 */
public class CommonStatusParser extends BaseJsonHandler {

    private CommonResponseDO responseDO;
    @Override
    public Object getData() {
            return responseDO;
    }

    @Override
    public void parse(String strResponse) {
        try{
            JSONObject jsonObject       = new JSONObject(strResponse);
            responseDO                  = new CommonResponseDO();
            responseDO.status           = jsonObject.optInt("Status");
            status = jsonObject.getInt("Status");

            responseDO.message          = jsonObject.optString("Message");
            responseDO.serverTime       = jsonObject.optString("ServerTime");
        }
        catch(Exception e){
//            e.printStackTrace();
            LogUtils.errorLog("CommonStatusParser Parser : ", e.getMessage());
        }
    }
}
