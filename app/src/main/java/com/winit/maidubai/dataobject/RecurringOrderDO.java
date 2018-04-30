package com.winit.maidubai.dataobject;

/**
 * Created by Girish Velivela on 15-10-2016.
 */
public class RecurringOrderDO extends BaseDO{
    public int RecurringOrderId;
    public String TrxCode;
    public String Frequency;
    public int NumberOfRepeatations;
    public boolean RemovedFromRecurring;
    public String RemovedFromRecurringOn;
    public String ReasonForRemovalFromRecurring;
    public String CreatedDate;
    public int CreatedBy;
    public String ModifiedDate;
    public int ModifiedBy;
}
