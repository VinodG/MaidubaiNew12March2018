package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 24-08-2016.
 */
public class ValidateMinimumOrderQuantityParser extends BaseJsonHandler{

    private ArrayList<ProductDO> arrProductDOs;

    public Object getData()
    {
        return status==0?message:arrProductDOs;
    }
    public void parse(String response){
        try {
            JSONObject jsonAddress = new JSONObject(response);
            status = jsonAddress.optInt("Status");
            message = jsonAddress.optString("Message");
            if(status != 0 && jsonAddress.has("MinimumOrderProducts")){
                JSONArray jsonArray = jsonAddress.getJSONArray("MinimumOrderProducts");
                arrProductDOs = new ArrayList<>();
                for(int i=0 ; i<jsonArray.length();i++) {
                    JSONObject jsonAddressBook = jsonArray.getJSONObject(i);
                    ProductDO productDO = new ProductDO();
                    productDO.name = jsonAddressBook.optString("Name");
                    productDO.AltDescription = jsonAddressBook.optString("AltDescription");
                    productDO.btlCount = jsonAddressBook.optInt("Quantity");
                    arrProductDOs.add(productDO);
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
