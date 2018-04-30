package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.MasterTableDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by namashivaya.gangishe on 8/20/2016.
 */
public class MasterTableParser extends BaseJsonHandler{

    private MasterTableDO masterTableDO;
    public Object getData()
    {
        return status==0?message:masterTableDO;
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if(status != 0){
                masterTableDO = new MasterTableDO();
                masterTableDO.masterTablePath = jsonObject.getString("sqliteFileName");
                masterTableDO.serverTime       = jsonObject.optString("ServerTime");
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG,e.getMessage());
        }
    }

}
