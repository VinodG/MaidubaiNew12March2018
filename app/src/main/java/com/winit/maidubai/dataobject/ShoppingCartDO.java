package com.winit.maidubai.dataobject;

/**
 * Created by sudheer.jampana on 8/29/2016.
 */
public class ShoppingCartDO extends BaseDO {
    public int ShoppingCartId;
    public String SessionId;
    public int UserId;
    public String ProductCode;
    public int Quantity;
    public float UnitPrice;
    public float TotalPriceWODiscount;
    public int DiscountRefId;
    public float DiscountAmount;
    public float TotalPrice;
    public String ModifiedOn;
    public String UOM;

    public int VAT=0;
    public double  TotalVAT=0;
}
