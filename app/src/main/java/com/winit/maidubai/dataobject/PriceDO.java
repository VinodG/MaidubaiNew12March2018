package com.winit.maidubai.dataobject;

/**
 * Created by sudheer.jampana on 8/29/2016.
 */
public class PriceDO extends  BaseDO {
    public int PriceId;
    public int ProductId;
    public float UnitPrice;
    public String CurrencyCode;
    public String UOM;
    public String StartDate;
    public String EndDate;
    public boolean IsActive;
    public int CreatedBy;
    public String CreatedOn;
    public int ModifiedBy;
    public String ModifiedOn;
    public int Timestamp;
    public String SalesOrgCode;
    public String PriceListCode;
}
