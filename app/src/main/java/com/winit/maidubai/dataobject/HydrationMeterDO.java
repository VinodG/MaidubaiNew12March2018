package com.winit.maidubai.dataobject;

/**
 * Created by srikrishna.nunna on 09-09-2016.
 */
public class HydrationMeterDO extends BaseDO {

    public int Id;
    public int CustomerId;
    public double Weight;
    public double Height;
    public int age;
    public char[] strWeight = {'0','0','0','.','0'};
    public char[] strHeight = {'0','0','.','0','0'};
    public char[] strAge = {'0','0'};
    public String gender = "";
    public double DailyWaterConsumptionTarget;
    public String CreatedDate = "";
    public int CreatedBy;
    public String ModifiedDate = "";
    public int ModifiedBy;
    public int timePeriod = 0;

    public boolean notifyMeTime = true;
    public static String MALE = "Male";
    public static String FEMALE = "Female";

}
