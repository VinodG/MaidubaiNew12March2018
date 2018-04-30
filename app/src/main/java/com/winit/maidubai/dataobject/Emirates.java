package com.winit.maidubai.dataobject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 26-09-2016.
 */
public class Emirates extends BaseDO{
    public int EmiratesId;
    public String Name;
    public String ArabicName;
    public ArrayList<AreaDO> arrAreaDOs;
    public HashMap<Integer,ArrayList<AreaDO>> hmAreaDOs;
    public HashMap<Integer,ArrayList<DeliveryDay>> hmDeliveryDay;
}
