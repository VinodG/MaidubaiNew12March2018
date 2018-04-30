package com.winit.maidubai.parser;

import android.content.Context;

import com.winit.maidubai.webaccessLayer.ServiceMethods;

import org.xml.sax.helpers.DefaultHandler;

public abstract class BaseJsonHandler extends DefaultHandler{

    protected int status;
    protected String message = "Some problem occured. Please contact Admin.";
    public boolean isError(){
        return status==0?true:false;
    }
    public abstract Object getData();
    public abstract void parse(String response);

    public static BaseJsonHandler getParser(ServiceMethods method, String respondsContent) {

        switch (method){
            case WS_LOGIN:
            case WS_SIGNUP:
            case WS_SN_SIGNUP:
                return new CustomerParser();
            case WS_OTP_MAIL:
            case WS_OTP_MAIL_LOGIN:
                return new OTPVerificationParser();
            case WS_FORGET_PASSWORD:
                return new ForgotPasswordParser();
            case WS_MASTER_TABLE:
                return new MasterTableParser();
            case WS_ADD_TO_CART:
            case WS_CART_LIST:
                return new CartListParser();
            case WS_UPDATE_CART_DETAILS:
                return new CartDetailParser();
            case WS_ADD_ORDER_ADDRESS:
                return new AddOrderAddressParser();
            case WS_EMIRATES:
                return new EmiratesParser();
            case WS_PLACE_ORDER:
            case WS_CANCEL_ORDER:
            case WS_GET_ORDERS:
            case WS_REPEAT_ORDER:
            case WS_RECURR_ORDER:
                return new OrdersParser();
            case WS_FEEDBACK:
                return new FeedbackParser();
            case WS_MANAGE_ADDRESS_BOOK:
                return new ManageAddressBookParser();
            case WS_ADDRESS_BOOK_LIST:
                return new AddressBookListParser();
            case WS_AREAS:
                return new AreaParser();
            case WS_DELIVERY_DAYS:
                return new DeliveryDaysParser();
            case WS_ADD_DRINK:
                return new AddDrinkParser();
            case WS_VALIDATE_MIN_ORDERS:
                return new ValidateMinimumOrderQuantityParser();
            case WS_UPCOMMING_DELIVERY_DAYS:
                return  new UpcomingDeliveryDayParser();
            case WS_RESEND_OTP:
            case WS_REMOVE_FROM_CART:
            case WS_UPDATE_SETTING:
            case WS_SAVE_HYDRATION_SETTINGS:
            case WS_REMOVE_ADDRESS:
            case WS_CANCEL_RECURRING:
            case WS_EDIT_ORDER_URL:
                return new CommonStatusParser();

        }
        return null;
    }

    public static BaseJsonHandler getDataSync(Context con){
        return new DataSyncParser(con);
    }

}
