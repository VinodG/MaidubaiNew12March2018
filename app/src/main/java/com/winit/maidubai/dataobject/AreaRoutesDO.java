package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 14-10-2016.
 */
public class AreaRoutesDO extends BaseDO{
    public int AreaRouteId = 0;
    public int AreaId = 0;
    public String Route="";
    public ArrayList<AreaRouteSupervisorsDO> arrAreaRouteSupervisorsDOs;
}
