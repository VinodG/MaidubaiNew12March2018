package com.winit.maidubai.dataobject;

/**
 * Created by Kishore.G on 5/2/2016.
 */
public class CartDetailsDO extends BaseDO {

    public int shoppingCartDetailId         = 0;
    public String sessionId                 = "";
    public boolean isGift                   = false;
    public boolean isGiftWrap               = false;
    public String message                   = "";
    public String giftCard                  = "";
    public double giftCardAmount            = 0.0;
    public String couponCode                = "";
    public double couponAmount              = 0.0;
    public String userMessage               = "";
    public double payableAmount             = 0.0;
    public String deviceId                  = "";


    // public GiftCardDO giftCardDO            = new GiftCardDO();
    // public CouponDO couponDO                = new CouponDO();
    // public StateTaxDO stateTaxDO            = new StateTaxDO();
}
