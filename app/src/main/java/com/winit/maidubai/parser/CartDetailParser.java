package com.winit.maidubai.parser;

import android.util.Log;

import com.winit.maidubai.dataobject.CartDetailsDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by Girish Velivela on 24-08-2016.
 */
public class CartDetailParser extends BaseJsonHandler{

    CartDetailsDO cartDetailsDO;
    public Object getData()
    {
        return status==0?message:cartDetailsDO;
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if(status != 0 && jsonObject.has("CartDetail")){
                jsonObject = jsonObject.getJSONObject("CartDetail");
                cartDetailsDO = new CartDetailsDO();
                cartDetailsDO.sessionId = jsonObject.getString("IsGift");
                cartDetailsDO.isGift = jsonObject.getBoolean("IsGift");
                cartDetailsDO.isGiftWrap = jsonObject.getBoolean("IsGiftWrap");
                cartDetailsDO.message = jsonObject.getString("Message");
                cartDetailsDO.giftCard = jsonObject.getString("GiftCard");
                cartDetailsDO.giftCardAmount = jsonObject.getDouble("GiftCardAmount");
                cartDetailsDO.couponCode = jsonObject.getString("CouponCode");
                cartDetailsDO.couponAmount = jsonObject.getDouble("CouponAmount");
                cartDetailsDO.userMessage = jsonObject.getString("UserMessage");
            }
        }catch (Exception e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
