package com.winit.maidubai.parser;

import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by Girish Velivela on 8/20/2016.
 */
public class RegistrationParser extends BaseJsonHandler{

    public Object getData()
    {
        return status==0?message:"";
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if(status != 0){

            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG,e.getMessage());
        }
    }

}
