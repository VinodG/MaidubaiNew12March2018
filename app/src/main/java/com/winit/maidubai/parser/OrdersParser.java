package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.OrderNumberDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.RecurringOrderDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kishore.G on 4/14/2016.
 */
public class OrdersParser extends BaseJsonHandler {

    private PlaceOrderDO placeOrderDO;
    private int  errorcode = 0;

    @Override
    public Object getData() {
        return status==0?(errorcode == -16?message+errorcode:message):placeOrderDO;
    }

    @Override
    public void parse(String strResponse) {
        try {
            JSONObject jsonObject = new JSONObject(strResponse);
            status = jsonObject.optInt("Status");
            message = jsonObject.optString("Message");

            if (status == 0) {
                JSONObject jsonTrxCodesObject                                = jsonObject.optJSONObject("TrxCodes");
                if(jsonTrxCodesObject!=null)
                {
                    errorcode                            = jsonTrxCodesObject.optInt("ErrorCode");
                }

            }else {
                placeOrderDO = new PlaceOrderDO();
                placeOrderDO.ServerTime = jsonObject.optString("ServerTime");
                placeOrderDO.arrTrxHeaderDOs = new ArrayList<>();
                if(jsonObject.has("Order")) {
                    JSONObject jsonOrderObject = jsonObject.optJSONObject("Order");
                    if(jsonOrderObject != null){
                        placeOrderDO.arrTrxHeaderDOs.add(parseTrxHeader(jsonOrderObject,"Order"));
                    }
                }
                if(jsonObject.has("OrderAndPayments")) {
                    JSONArray jsonOrdersArray = jsonObject.optJSONArray("OrderAndPayments");
                    int jsonArrayLen = jsonOrdersArray.length();
                    for (int i = 0; i < jsonArrayLen; i++) {
                        JSONObject jsonOrderObject = jsonOrdersArray.optJSONObject(i).optJSONObject("TrxHeader");
                        if (jsonOrderObject != null) {
                            placeOrderDO.arrTrxHeaderDOs.add(parseTrxHeader(jsonOrderObject,"OrderSync"));
                        }
                    }
                }
           /* JSONObject jsonTrxCodesObject                                = jsonObject.optJSONObject("TrxCodes");
            if(jsonTrxCodesObject!=null)
            {
                placeOrderDO.trxCodesDO                                      = new TrxCodesDO();
                placeOrderDO.trxCodesDO.trxId                                = jsonTrxCodesObject.optInt("TrxId");
                placeOrderDO.trxCodesDO.trxCode                              = jsonTrxCodesObject.optString("TrxCode");
                placeOrderDO.trxCodesDO.paymentCode                          = jsonTrxCodesObject.optString("PaymentCode");
                placeOrderDO.trxCodesDO.errorCode                            = jsonTrxCodesObject.optInt("ErrorCode");
                placeOrderDO.trxCodesDO.message                              = jsonTrxCodesObject.optString("Message");
                placeOrderDO.trxCodesDO.verificationCode                     = jsonTrxCodesObject.optString("VerificationCode");
            }*/
            }
        }
        catch(Exception e){
//            e.printStackTrace();
            LogUtils.errorLog("PlaceOrder Parser: ", e.getMessage());
        }
    }

    private TrxHeaderDO parseTrxHeader(JSONObject jsonOrderObject,String type) {
        TrxHeaderDO trxHeaderDO = new TrxHeaderDO();
        trxHeaderDO.trxId = jsonOrderObject.optInt("TrxId");
        trxHeaderDO.trxCode = jsonOrderObject.optString("TrxCode");
        trxHeaderDO.trxDate = jsonOrderObject.optString("TrxDate");
        trxHeaderDO.trxType = jsonOrderObject.optString("TrxType");
        trxHeaderDO.userId = jsonOrderObject.optInt("UserId");
        trxHeaderDO.trxStatus = jsonOrderObject.optInt("TrxStatus");
        trxHeaderDO.isCOD = jsonOrderObject.optBoolean("IsCOD");
        trxHeaderDO.productSubTotalWDiscount = jsonOrderObject.optDouble("ProductSubTotalWDiscount");
        trxHeaderDO.totalTrxAmount = jsonOrderObject.optDouble("TotalTrxAmount");
        trxHeaderDO.billingEmail = jsonOrderObject.optString("BillingEmail");
        trxHeaderDO.giftCardUsed = jsonOrderObject.optString("GiftCardUsed");
        trxHeaderDO.giftCardAmount = jsonOrderObject.optDouble("GiftCardAmount");
        trxHeaderDO.promotionId = jsonOrderObject.optInt("PromotionId");
        trxHeaderDO.promotionDiscount = jsonOrderObject.optDouble("PromotionDiscount");
        trxHeaderDO.isCouponCodeApplicable = jsonOrderObject.optBoolean("IsCouponCodeApplicable");
        trxHeaderDO.couponCode = jsonOrderObject.optString("CouponCode");
        trxHeaderDO.couponAmount = jsonOrderObject.optDouble("CouponAmount");
        trxHeaderDO.docketNumber = jsonOrderObject.optString("DocketNumber");
        trxHeaderDO.userMessage = jsonOrderObject.optString("UserMessage");
        trxHeaderDO.walletAmount = jsonOrderObject.optDouble("WalletAmount");
        trxHeaderDO.isCancelled = jsonOrderObject.optBoolean("Is_Canceled");
        trxHeaderDO.trxDisplayCode = jsonOrderObject.optString("TrxCodeToDisplay");
        trxHeaderDO.specialInstructions = jsonOrderObject.optString("SpecialInstructions");
        trxHeaderDO.orderName = jsonOrderObject.optString("OrderName");
        trxHeaderDO.deliveryDate = CalendarUtil.convertToDeliDateToInsert(jsonOrderObject.optString("DeliveryDate"));
        trxHeaderDO.isRecurring = jsonOrderObject.optBoolean("IsRecurring");

        trxHeaderDO.TotalVAT = jsonOrderObject.optDouble("TotalVAT");
        trxHeaderDO.deliveryDate = jsonOrderObject.optString("DeliveryDate");
        trxHeaderDO.CancelledOn = jsonOrderObject.optString("CancelledOn");
        JSONArray jsonDetailsArray = jsonOrderObject.optJSONArray("TrxDetails");
        if (jsonDetailsArray != null) {
            trxHeaderDO.trxDetailsDOs = new ArrayList<>();
            for (int i = 0; i < jsonDetailsArray.length(); i++) {
                JSONObject jsonDetailsObject = jsonDetailsArray.optJSONObject(i);
                trxHeaderDO.trxDetailsDOs.add(parseTrxDetails(jsonDetailsObject,trxHeaderDO.trxCode));
            }
        }
        if(jsonOrderObject.has("OrderNumbers")) {
            JSONArray jsonArrOrderNumber = jsonOrderObject.optJSONArray("OrderNumbers");
            if (jsonArrOrderNumber != null && jsonArrOrderNumber.length()>0) {
                JSONObject jsonOrderNumber = jsonArrOrderNumber.optJSONObject(0);
                if (jsonArrOrderNumber != null) {
                    trxHeaderDO.orderNumberDO = new OrderNumberDO();
                    trxHeaderDO.orderNumberDO.Id = jsonOrderNumber.optInt("Id");
                    trxHeaderDO.orderNumberDO.OrderNumber = jsonOrderNumber.optString("OrderNumber");
                    trxHeaderDO.orderNumberDO.TrxId = jsonOrderNumber.optInt("TrxId");
                    trxHeaderDO.orderNumberDO.CreatedDate = jsonOrderNumber.optString("CreatedDate");
                }
            }
        }

        if(jsonOrderObject.has("RecurringOrders")) {
            JSONArray recurringOrders = jsonOrderObject.optJSONArray("RecurringOrders");
            if (recurringOrders != null && recurringOrders.length()>0) {
                trxHeaderDO.recurringOrderDOs = new ArrayList<>();
                for(int i=0;i<recurringOrders.length();i++) {
                    JSONObject jsonRecurringOrder = recurringOrders.optJSONObject(i);
                    RecurringOrderDO recurringOrderDO = new RecurringOrderDO();
                    recurringOrderDO.RecurringOrderId = jsonRecurringOrder.optInt("RecurringOrderId");
                    recurringOrderDO.TrxCode = jsonRecurringOrder.optString("TrxCode");
                    recurringOrderDO.Frequency = jsonRecurringOrder.optString("Frequency");
                    recurringOrderDO.NumberOfRepeatations = jsonRecurringOrder.optInt("NumberOfRepeatations");
                    recurringOrderDO.RemovedFromRecurring = jsonRecurringOrder.optBoolean("RemovedFromRecurring");
                    recurringOrderDO.RemovedFromRecurringOn = jsonRecurringOrder.optString("RemovedFromRecurringOn");
                    trxHeaderDO.recurringOrderDOs.add(recurringOrderDO);
                }
            }
        }

        JSONArray jsonTxChargeArray = jsonOrderObject.optJSONArray("TrxTaxAndCharges");
                /*if(jsonTxChargeArray!=null)
                {
                    placeOrderDO.trxHeaderDO.trxTaxAndChargeDOs           = new ArrayList<>();
                    for(int i=0; i<jsonTxChargeArray.length(); i++)
                    {
                        JSONObject jsonTxChargeObject                 = jsonTxChargeArray.optJSONObject(i);
                        TrxTaxAndChargeDO taxAndChargeDO              = new TrxTaxAndChargeDO();
                        taxAndChargeDO.trxTaxAndChargeId              = jsonTxChargeObject.optInt("TrxTaxAndChargeId");
                        taxAndChargeDO.TrxCode                        = jsonTxChargeObject.optString("TrxCode");
                        taxAndChargeDO.name                           = jsonTxChargeObject.optString("Name");
                        taxAndChargeDO.value                          = jsonTxChargeObject.optDouble("TrxTaxAndChargeId");
                        placeOrderDO.trxHeaderDO.trxTaxAndChargeDOs.add(taxAndChargeDO);
                    }
                }*/
        JSONArray jsonTrxAddressArray = jsonOrderObject.optJSONArray("TrxAddress");
        if (jsonTrxAddressArray != null) {
            trxHeaderDO.trxAddressBookDOs = new ArrayList<>();
            for (int i = 0; i < jsonTrxAddressArray.length(); i++) {
                JSONObject jsonTrxAddressObject = jsonTrxAddressArray.optJSONObject(i);
                trxHeaderDO.trxAddressBookDOs.add(parseAddress(jsonTrxAddressObject,trxHeaderDO.trxCode));
            }
        }
        JSONArray jsonCanceledOrdersArray = jsonOrderObject.optJSONArray("CanceledOrders");
        if (jsonCanceledOrdersArray != null) {
            trxHeaderDO.trxAddressBookDOs = new ArrayList<>();
            for (int i = 0; i < jsonCanceledOrdersArray.length(); i++) {
//                   JSONObject jsonTrxAddressObject                     = jsonCanceledOrdersArray.optJSONObject(i);
            }
        }
        // placeOrderDO.arrTrxHeaderDOs.add(trxHeaderDO);
        return trxHeaderDO;
    }

    private AddressBookDO parseAddress(JSONObject jsonObj,String trxCode)
    {
        AddressBookDO addressBookDO           = new AddressBookDO();
        addressBookDO.trxCode                 = trxCode;
        addressBookDO.shoppingCartAddressId   = jsonObj.optInt("ShoppingCartAddressId");
        addressBookDO.addressType             = jsonObj.optString("AddressType");
        addressBookDO.addressBookId           = jsonObj.optInt("AddressBookId");
        addressBookDO.trxAddressId            = jsonObj.optInt("TrxAddressId");
        addressBookDO.sessionId               = jsonObj.optString("SessionId");
        addressBookDO.firstName               = jsonObj.optString("FirstName");
        addressBookDO.middleName              = jsonObj.optString("MiddleName");
        addressBookDO.lastName                = jsonObj.optString("LastName");
        addressBookDO.addressLine1            = jsonObj.optString("AddressLine1");
        addressBookDO.addressLine1_Arabic     = jsonObj.optString("AddressLine1_Arabic");
        addressBookDO.addressLine2            = jsonObj.optString("AddressLine2");
        addressBookDO.addressLine2_Arabic     = jsonObj.optString("AddressLine2_Arabic");
        addressBookDO.addressLine3            = jsonObj.optString("AddressLine3");
        addressBookDO.addressLine4            = jsonObj.optString("AddressLine4");
        addressBookDO.addressLine4_Arabic     = jsonObj.optString("AddressLine4_Arabic");
        addressBookDO.VillaName               = jsonObj.optString("VillaName");
        addressBookDO.Street                  = jsonObj.optString("Street");
        addressBookDO.city                    = jsonObj.optString("City");
        addressBookDO.state                   = jsonObj.optString("State");
        addressBookDO.country                 = jsonObj.optString("Country");
        addressBookDO.zipCode                 = jsonObj.optString("ZipCode");
        addressBookDO.phoneNo                 = jsonObj.optString("Phone");
        addressBookDO.email                   = jsonObj.optString("Email");
        addressBookDO.mobileNo                = jsonObj.optString("MobileNumber");
        addressBookDO.FlatNumber              = jsonObj.optString("FlatNumber");

        return  addressBookDO;
    }

    private TrxDetailsDO parseTrxDetails(JSONObject jsonDetailsObject,String trxCode)
    {
        TrxDetailsDO trxDetailsDO                           = new TrxDetailsDO();
        trxDetailsDO.trxCode                                = trxCode;
        trxDetailsDO.trxDetailId                            = jsonDetailsObject.optInt("TrxDetailId");
        trxDetailsDO.productId                              = jsonDetailsObject.optInt("ProductId");
        trxDetailsDO.sequence                               = jsonDetailsObject.optInt("Sequence");
        trxDetailsDO.quantity                               = jsonDetailsObject.optDouble("Quantity");
        trxDetailsDO.unitPrice                              = jsonDetailsObject.optDouble("UnitPrice");
        trxDetailsDO.totalAmountWODiscount                  = jsonDetailsObject.optDouble("TotalAmountWODiscount");
        trxDetailsDO.discountAmount                         = jsonDetailsObject.optDouble("DiscountAmount");
        trxDetailsDO.discountRefId                          = jsonDetailsObject.optInt("DiscountRefId");
        trxDetailsDO.reference                              = jsonDetailsObject.optString("Reference");
        trxDetailsDO.orderAmount                            = jsonDetailsObject.optDouble("OrderAmount");
        trxDetailsDO.smallImage                             = jsonDetailsObject.optString("SmallImage");
        trxDetailsDO.mediumImage                            = jsonDetailsObject.optString("MediumImage");
        trxDetailsDO.largeImage                             = jsonDetailsObject.optString("LargeImage");
        trxDetailsDO.originalImage                          = StringUtils.replaceAll("(%20)|[~]",jsonDetailsObject.optString("OriginalImage"),"");
        trxDetailsDO.productCode                            = jsonDetailsObject.optString("ProductCode");
        trxDetailsDO.productName                            = jsonDetailsObject.optString("ProductName");
        trxDetailsDO.productDescription                     = jsonDetailsObject.optString("ProductDescription");
        trxDetailsDO.productALTDescription                  = jsonDetailsObject.optString("AltDescription");
        trxDetailsDO.VAT                                    = jsonDetailsObject.optInt("VAT");
        trxDetailsDO.TotalVAT                               = jsonDetailsObject.optDouble("TotalVAT");

        trxDetailsDO.initialQuantity                        = (int)trxDetailsDO.quantity;
        return  trxDetailsDO;
    }
}
