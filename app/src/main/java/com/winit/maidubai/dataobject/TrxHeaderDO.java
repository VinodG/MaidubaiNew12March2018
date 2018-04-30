package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Kishore.G on 5/26/2016.
 */
public class TrxHeaderDO extends BaseDO {

    public int trxId                    = 0;
    public int userId                   = 0;
    public int trxStatus                = 0;
    public int promotionId              = 0;
    public boolean isCOD                    = false;
    public boolean isGiftCard                    = false;
    public boolean isGiftWrap                    = false;
    public boolean isCancelled                    = false;
    public boolean isCouponCodeApplicable   = false;
    public double productSubTotalWDiscount  = 0.0d;
    public double totalAmountWODiscount  = 0.0d;
    public double totalTrxAmount            = 0.0d;
    public double giftCardAmount            = 0.0d;
    public double promotionDiscount         = 0.0d;
    public double couponAmount              = 0.0d;
    public double walletAmount	            = 0.0d;
    public double discountAmt	            = 0.0d;
    public double shippingCharges	            = 0.0d;
    public String specialInstructions   = "";
    public boolean isRecurring;
    public String deliveryDate          = "";
    public String orderName             = "";
    public String trxCode               = "";
    public String trxDisplayCode        = "";
    public String sessionId             = "";
    public String trxRefCode            = "";
    public String trxDate               = "";
    public String trxType               = "";
    public String trxCodeToDisplay      = "";
    public String billingEmail          = "";
    public String giftCardUsed          = "";
    public String couponCode	        = "";
    public String docketNumber	        = "";
    public String message	        = "";
    public String userMessage	        = "";
    public String shippingStatus	        = "";
    public ArrayList<TrxDetailsDO> trxDetailsDOs;
    public OrderNumberDO orderNumberDO;
    public RecurringOrderDO recurringOrderDO;
    public ArrayList<RecurringOrderDO> recurringOrderDOs;
//    public ArrayList<TrxTaxAndChargeDO> trxTaxAndChargeDOs;
    public ArrayList<AddressBookDO> trxAddressBookDOs;

    public String CancelledBy;
    public String DeviceType;
    public String DeviceId;
    public String ReasonForCancellation;
    public String CancellationReasonCategory;
    public String SpecialInstructions;
    public boolean OrderLimitApplied;
    public int NotificationForNextDayDeliverySent;
    public String NotificationForNextDayDeliverySentOn;
    public String CancelledOn;
    //Total VAT amount
    public double TotalVAT=0;
}
