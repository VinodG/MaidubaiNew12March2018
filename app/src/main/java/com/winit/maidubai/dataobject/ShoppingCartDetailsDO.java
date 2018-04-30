package com.winit.maidubai.dataobject;

/**
 * Created by sudheer.jampana on 8/29/2016.
 */
public class ShoppingCartDetailsDO extends BaseDO {
    public int ShoppingCartDetailId;
    public String SessionId;
    public boolean IsGift;
    public boolean IsGiftWrap;
    public String Message;
    public String GiftCard;
    public float GiftCardAmount;
    public boolean IsGiftCardBeingUsed;
    public String GiftCardUsageStartTime;
    public  String CouponCode;
    public float CouponAmount;
    public boolean IsCouponApplicable;
    public String UserMessage;
}
