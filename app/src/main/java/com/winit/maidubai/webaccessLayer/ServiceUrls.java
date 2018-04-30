package com.winit.maidubai.webaccessLayer;

public class ServiceUrls {


    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

//    public static final String MAIN_URL                     = "http://dev4.winitsoftware.com/General/";
//    Live URL
    public static final String MAIN_URL                     = "http://maidubai.winitsoftware.com/";
//    public static final String MAIN_URL                     = "http://maidubai2.winitsoftware.com/";
//    public static final String MAIN_URL                     = "http://192.168.154.40/MaiDubai/";

    private static final String LOGIN_URL                   = "api/CustomerAPI/ValidateCustomer";
    private static final String REGISTRATION_URL            = "api/CustomerAPI/CustomerRegistration";
    private static final String SN_REGISTRATION_URL         = "api/CustomerAPI/SocialMediaValidate";
    private static final String FORGET_PASSWORS_URL         = "api/CustomerAPI/ForgotPassword";
    private static final String SIGNUP_VERIFICATION_URL     = "api/CustomerAPI/SignupVerification";
    private static final String LOGIN_VERIFICATION_URL      = "api/CustomerAPI/LoginVerification";
    private static final String MASTER_TABLE_URL            = "api/MasterDataAPI/GetMasterData";
    private static final String RESEND_OTP_URL              = "api/CustomerAPI/ResendVerificationCode";
    private static final String ADD_TO_CART_URL             = "/api/CartAPI/AddToCart";
    public final static String REMOVE_FROM_CART_URL         = "/api/CartAPI/RemoveFromCart";
    private static final String UPDATE_CART_DETAILS_URL     = "api/CartAPI/UpdateCartDetails";
    private static final String CART_LIST_URL               = "api/CartAPI/CartList";
    private static final String ADD_ORDER_ADDRESS_URL       = "api/AddressAPI/AddAddress";
    public final static String PLACE_ORDER_URL              = "/api/OrderAndPaymentAPI/PlaceOrder";
    public final static String CANCEL_ORDER_URL             = "api/OrderAndPaymentAPI/CancelOrder";
    public final static String FEEDBACK_URL                 = "api/CustomerAPI/Feedback";
    public final static String DATASYNC_URL                 = "api/MasterDataAPI/DeltaSync";
    public final static String MANAGE_ADDRESS_BOOK_URL      = "api/AddressBookAPI/AddToAddressBook";
    public final static String ADDRESS_BOOK_LIST_URL        = "api/AddressBookAPI/AddressBookList";
    public final static String AREA_URL                     = "api/CustomerAPI/getAreaandSubAreas";
    public final static String DELIVERY_DAYS_URL            = "api/CustomerAPI/getAreaDeliveryDetails";
    public final static String UPDATE_SETTINGS_URL          = "api/CustomerAPI/UpdateSettings";
    public final static String GET_ORDER_URL                = "api/OrderAndPaymentAPI/getOrderAndPayments";
    public final static String SAVE_HYDRATION_SETTINGS_URL  = "api/CustomerAPI/UpdateHydrationMeterSettings";
    public final static String ADD_DRINK_URL                = "api/CustomerAPI/InsertHydrationMeterReading";
    public final static String GET_EMIRATES_URL             = "api/CustomerAPI/EmiratesAreaSubAreaDeliveryDetail";
    public final static String REPEAT_ORDER_URL             = "api/OrderAndPaymentAPI/RepeatOrder";
    public final static String RECURR_ORDER_URL             = "api/OrderAndPaymentAPI/RecurringOrder";
    public final static String CANCEL_RECURRING_URL         = "api/OrderAndPaymentAPI/RemoveFromRecurringOrder";
    public final static String EDIT_ORDER_URL         =      "api/OrderAndPaymentAPI/RollBackTransaction";
    public final static String REMOVE_ADDRESS_URL           = "api/AddressBookAPI/RemoveFromAddressBook";
    public final static String VALIDATE_MIN_ORDERS          = "api/OrderAndPaymentAPI/ValidateMinimumOrderQuantity";
    public final static String IMAGE_ZIP_URL                = "api/ProductAPI/DownloadProductImages?email=";
    public final static String UPCOMMING_DELIVERY_DAYS_URL  = "api/CustomerAPI/getUpcomingDeliveryDates";
//    GET api/OrderAndPaymentAPI/RollBackTransaction?TrxCode={TrxCode}&SessionId={SessionId}

    public static String getRequestUrl(ServiceMethods method){
        switch (method) {
            case WS_LOGIN:
                return LOGIN_URL;
            case WS_SIGNUP:
                return REGISTRATION_URL;
            case WS_SN_SIGNUP:
                return SN_REGISTRATION_URL;
            case WS_FORGET_PASSWORD:
                return FORGET_PASSWORS_URL;
            case WS_OTP_MAIL:
                return SIGNUP_VERIFICATION_URL;
            case WS_OTP_MAIL_LOGIN:
                return LOGIN_VERIFICATION_URL;
            case WS_MASTER_TABLE:
                return MASTER_TABLE_URL;
            case WS_RESEND_OTP:
                return RESEND_OTP_URL;
            case WS_ADD_TO_CART:
                return ADD_TO_CART_URL;
            case WS_CART_LIST:
                return CART_LIST_URL;
            case WS_UPDATE_CART_DETAILS:
                return UPDATE_CART_DETAILS_URL;
            case WS_ADD_ORDER_ADDRESS:
                return ADD_ORDER_ADDRESS_URL;
            case WS_REMOVE_FROM_CART:
                return REMOVE_FROM_CART_URL;
            case WS_PLACE_ORDER:
                return PLACE_ORDER_URL;
            case WS_CANCEL_ORDER:
                return CANCEL_ORDER_URL;
            case WS_FEEDBACK:
                return FEEDBACK_URL;
            case WS_DATASYNC:
                return DATASYNC_URL;
            case WS_MANAGE_ADDRESS_BOOK:
                return MANAGE_ADDRESS_BOOK_URL;
            case WS_ADDRESS_BOOK_LIST:
                return ADDRESS_BOOK_LIST_URL;
            case WS_AREAS:
                return AREA_URL;
            case WS_DELIVERY_DAYS:
                return DELIVERY_DAYS_URL;
            case WS_UPDATE_SETTING:
                return UPDATE_SETTINGS_URL;
            case WS_GET_ORDERS:
                return GET_ORDER_URL;
            case WS_SAVE_HYDRATION_SETTINGS:
                return SAVE_HYDRATION_SETTINGS_URL;
            case WS_ADD_DRINK:
                return ADD_DRINK_URL;
            case WS_REPEAT_ORDER:
                return REPEAT_ORDER_URL;
            case WS_RECURR_ORDER:
                return RECURR_ORDER_URL;
            case WS_EMIRATES:
                return GET_EMIRATES_URL;
            case WS_CANCEL_RECURRING:
                return CANCEL_RECURRING_URL;
            case WS_REMOVE_ADDRESS:
                return REMOVE_ADDRESS_URL;
            case WS_UPCOMMING_DELIVERY_DAYS:
                return UPCOMMING_DELIVERY_DAYS_URL;
            case WS_VALIDATE_MIN_ORDERS:
                return VALIDATE_MIN_ORDERS;
            case WS_EDIT_ORDER_URL:
                return EDIT_ORDER_URL;
        }
        return "";
    }

    public static String getMethodType(ServiceMethods method) {
        switch (method) {
            // GET methods
            case WS_MASTER_TABLE:
            case WS_RESEND_OTP:
            case WS_CART_LIST:
            case WS_DOWNLOAD:
            case WS_DOWNLOAD_MASTER_TABLE:
            case WS_DATASYNC:
            case WS_ADDRESS_BOOK_LIST:
            case WS_GET_ORDERS:
            case WS_DELIVERY_DAYS:
            case WS_EMIRATES:
            case WS_VALIDATE_MIN_ORDERS:
            case WS_UPCOMMING_DELIVERY_DAYS:
            case WS_EDIT_ORDER_URL:
                return METHOD_GET;
            default:
                return METHOD_POST;
        }
    }
}
