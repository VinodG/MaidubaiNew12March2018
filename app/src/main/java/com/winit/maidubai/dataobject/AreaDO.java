package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 02-09-2016.
 */
public class AreaDO extends BaseDO {
    public int AreaId = 0;
    public int ParentId = 0;
    public int EmiratesId = 0;
    public String Name = "";
    public String ArabicName = "";
    public String CreatedDate = "";
    public String CreatedBy = "";
    public String ModifiedDate = "";
    public String ModifiedBy = "";
    public ArrayList<String> arrDays;
    public ArrayList<DeliveryDay> arrDeliveryDays;
    public ArrayList<AreaDO> arrAreaDOs;
    public ArrayList<AreaRoutesDO> arrAreaRoutes;
}
