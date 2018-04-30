package com.winit.maidubai.parser;


import android.util.Log;

import com.winit.maidubai.dataobject.CartDetailsDO;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.CartResponseDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kishore.G on 4/15/2016.
 */
public class CartListParser extends BaseJsonHandler {

    private CartResponseDO cartResponseDO = new CartResponseDO();
    @Override
    public Object getData() {
        return cartResponseDO;
    }

    @Override
    public void parse(String strResponse) {
        try {
            JSONObject jsonObject = new JSONObject(strResponse);

            cartResponseDO.status = jsonObject.optInt("Status");
            status = jsonObject.getInt("Status");
            cartResponseDO.message= jsonObject.optString("Message");
            if(cartResponseDO.cartListDOs==null)
                cartResponseDO.cartListDOs = new ArrayList<>();
            JSONArray jsonArray      = jsonObject.optJSONArray("CartLists");
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObj              = jsonArray.optJSONObject(i);

                CartListDO cartListDO           = new CartListDO();
                cartListDO.shoppingCartId       = jsonObj.optInt("ShoppingCartId");
                cartListDO.sessionId            = jsonObj.optString("SessionId");
                cartListDO.userId               = jsonObj.optInt("UserId");
                cartListDO.productCode          = jsonObj.optString("ProductCode");
                cartListDO.productName          = jsonObj.optString("ProductName");
                cartListDO.productId            = jsonObj.optInt("ProductId");
                cartListDO.name                 = jsonObj.optString("Name");
                cartListDO.quantity             = jsonObj.optInt("Quantity");
                cartListDO.unitPrice            = jsonObj.optDouble("UnitPrice");
                cartListDO.discountedUnitPrice  = jsonObj.optDouble("DiscountedUnitPrice");
                cartListDO.totalPriceWODiscount = jsonObj.optDouble("TotalPriceWODiscount");
                cartListDO.discountAmount       = jsonObj.optDouble("DiscountAmount");
                cartListDO.totalPrice           = jsonObj.optDouble("TotalPrice");
                cartListDO.productColor         = jsonObj.optString("ProductColor");
                cartListDO.productImage         = jsonObj.optString("ProductImage");
                cartListDO.smallImage           = jsonObj.optString("SmallImage");
                cartListDO.mediumImage          = jsonObj.optString("MediumImage");
                cartListDO.largeImage           = jsonObj.optString("LargeImage");
                cartListDO.originalImage        = StringUtils.replaceAll("(%20)|[~]",jsonObj.optString("OriginalImage"),"");
                cartListDO.UOM                  = jsonObj.optString("UOM");

                cartListDO.vatPerc                  =  jsonObj.optInt("VAT") ;
                cartListDO.vatAmount                  = jsonObj.optDouble("TotalVAT");
                cartResponseDO.cartListDOs.add(cartListDO);
            }
            JSONObject cartDetailsJSON = jsonObject.optJSONObject("CartDetail");
            if(cartResponseDO.cartDetailsDO == null)
                cartResponseDO.cartDetailsDO = new CartDetailsDO();
            if(cartDetailsJSON!=null )
            {
                cartResponseDO.cartDetailsDO.sessionId     = cartDetailsJSON.optString("SessionId");
                cartResponseDO.cartDetailsDO.isGift        = cartDetailsJSON.optBoolean("IsGift");
                cartResponseDO.cartDetailsDO.isGiftWrap    = cartDetailsJSON.optBoolean("IsGiftWrap");
                cartResponseDO.cartDetailsDO.message       = cartDetailsJSON.optString("Message");
                cartResponseDO.cartDetailsDO.giftCard      = cartDetailsJSON.optString("GiftCard");if(cartResponseDO.cartDetailsDO.giftCard!=null && cartResponseDO.cartDetailsDO.giftCard.equalsIgnoreCase("null"))
                cartResponseDO.cartDetailsDO.giftCard = "";
            else
                cartResponseDO.cartDetailsDO.giftCard = "";

                cartResponseDO.cartDetailsDO.giftCardAmount= cartDetailsJSON.optDouble("GiftCardAmount");
                cartResponseDO.cartDetailsDO.couponCode    = cartDetailsJSON.optString("CouponCode");
                if(cartResponseDO.cartDetailsDO.couponCode!=null && cartResponseDO.cartDetailsDO.couponCode.equalsIgnoreCase("null"))
                    cartResponseDO.cartDetailsDO.couponCode = "";
                cartResponseDO.cartDetailsDO.couponAmount  = cartDetailsJSON.optDouble("CouponAmount");
                cartResponseDO.cartDetailsDO.userMessage   =  cartDetailsJSON.optString("UserMessage");

            }
            //----------PARSE COUPON OBJECT----------------------------------------------//
            /*JSONObject couponJSON   = cartDetailsJSON.optJSONObject("CouponObject");
            if(cartResponseDO.cartDetailsDO.couponDO == null)
                cartResponseDO.cartDetailsDO.couponDO = new CouponDO();
            if(couponJSON!=null)
            {
                cartResponseDO.cartDetailsDO.couponDO.couponCode    =   couponJSON.optString("CouponCode");
                cartResponseDO.cartDetailsDO.couponDO.couponId      =   couponJSON.optInt("CouponId");
                cartResponseDO.cartDetailsDO.couponDO.discountType  =   couponJSON.optString("DiscountType");
                cartResponseDO.cartDetailsDO.couponDO.discountValue =   couponJSON.optDouble("DiscountValue");
                cartResponseDO.cartDetailsDO.couponDO.maxDiscount   =   couponJSON.optDouble("MaxDiscount");
            }*/
            //----------PARSE GIFT CARD OBJECT----------------------------------------------//
            /*JSONObject giftCardJSON   = cartDetailsJSON.optJSONObject("GiftCardObject");
            if(cartResponseDO.cartDetailsDO.giftCardDO == null)
                cartResponseDO.cartDetailsDO.giftCardDO = new GiftCardDO();
            if(giftCardJSON!=null)
            {
                cartResponseDO.cartDetailsDO.giftCardDO.giftCardId  =   giftCardJSON.optInt("GiftCardId");
                cartResponseDO.cartDetailsDO.giftCardDO.userId      =   giftCardJSON.optInt("UserId");
                cartResponseDO.cartDetailsDO.giftCardDO.code        =     giftCardJSON.optString("Code");
                cartResponseDO.cartDetailsDO.giftCardDO.amount      =   giftCardJSON.optDouble("Amount");
                cartResponseDO.cartDetailsDO.giftCardDO.usedAmount  =   giftCardJSON.optDouble("UsedAmount");
                cartResponseDO.cartDetailsDO.giftCardDO.balance     =   giftCardJSON.optDouble("Balance");
                cartResponseDO.cartDetailsDO.giftCardDO.validFrom   =   giftCardJSON.optString("ValidFrom");
                cartResponseDO.cartDetailsDO.giftCardDO.validTo     =   giftCardJSON.optString("ValidTo");
                cartResponseDO.cartDetailsDO.giftCardDO.trxCode     =   giftCardJSON.optString("TrxCode");
                cartResponseDO.cartDetailsDO.giftCardDO.recipientFirstName  =   giftCardJSON.optString("RecipientFirstName");
                cartResponseDO.cartDetailsDO.giftCardDO.recipientLastName   =   giftCardJSON.optString("RecipientLastName");
                cartResponseDO.cartDetailsDO.giftCardDO.recipientEmail      =   giftCardJSON.optString("RecipientEmail");
                cartResponseDO.cartDetailsDO.giftCardDO.recipientMobile     =   giftCardJSON.optString("RecipientMobile");
                cartResponseDO.cartDetailsDO.giftCardDO.message             =   giftCardJSON.optString("Message");
                cartResponseDO.cartDetailsDO.giftCardDO.templateId          =   giftCardJSON.optInt("TemplateId");
                cartResponseDO.cartDetailsDO.giftCardDO.recipientMobile     =   giftCardJSON.optString("RecipientPhone");
            }

            JSONObject stateTaxJSON =   cartDetailsJSON.optJSONObject("StateTaxObject");
            if(cartResponseDO.cartDetailsDO.stateTaxDO == null)
                cartResponseDO.cartDetailsDO.stateTaxDO =   new StateTaxDO();
            if(stateTaxJSON!=null)
            {
                cartResponseDO.cartDetailsDO.stateTaxDO.stateTaxId  =   stateTaxJSON.optInt("StateTaxId");
                cartResponseDO.cartDetailsDO.stateTaxDO.stateCode   =   stateTaxJSON.optString("StateCode");
                cartResponseDO.cartDetailsDO.stateTaxDO.type        =   stateTaxJSON.optString("Type");
                cartResponseDO.cartDetailsDO.stateTaxDO.value       =   stateTaxJSON.optDouble("Value");
                cartResponseDO.cartDetailsDO.stateTaxDO.startDate   =   stateTaxJSON.optString("StartDate");
                cartResponseDO.cartDetailsDO.stateTaxDO.endDate     =   stateTaxJSON.optString("EndDate");
            }*/

        }
        catch (Exception e){
            /*e.printStackTrace(); */
            Log.d("This can never happen", e.getMessage());
            LogUtils.errorLog("CartListParser Parser ", e.getMessage());
        }
    }

}
