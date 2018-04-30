package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Kishore.G on 5/30/2016.
 */
public class PlaceOrderDO extends BaseDO {

    public String sessionId;
    public String trxDate;
    public boolean isCOD;
    public boolean useWallet;
    public String deviceId;
    public String PreferredLanguage;
    public String SpecialInstructions;
    public String OrderName;
    public boolean existingAddress;
    public boolean isRecurring;
    public String frequency = "";
    public int numberOfRepeatations = 3; //not using but for default by digen

    public String ServerTime;
    public ArrayList<TrxHeaderDO> arrTrxHeaderDOs;
    public String deliverydate ="";
}
