package com.winit.maidubai.webaccessLayer;

import java.io.InputStream;
import java.util.TreeMap;

/**
 * Created by Girish Velivela on 8/18/2016.
 *
 * Class used
 *  1) Decides the Request is post or Get service
 *  2) Form an Url that need to request
 *  3) Prepares an headers of
 *
 */
public class RestCilent {

    public InputStream processRequest(ServiceMethods method, String data){

        HttpHelper httpHelper = new HttpHelper();
        String baseUrl = ServiceUrls.MAIN_URL + ServiceUrls.getRequestUrl(method);
        String methodType = ServiceUrls.getMethodType(method);
        String jsonRequest = null;
        TreeMap<String,String> headers = null;

        switch (method) {
            case WS_MASTER_TABLE:
            case WS_RESEND_OTP:
            case WS_CART_LIST:
            case WS_DATASYNC:
            case WS_GET_ORDERS:
            case WS_EMIRATES:
            case WS_REMOVE_ADDRESS:
            case WS_ADDRESS_BOOK_LIST:
            case WS_VALIDATE_MIN_ORDERS:
            case WS_UPCOMMING_DELIVERY_DAYS:
            case WS_EDIT_ORDER_URL:
                headers = new TreeMap<>();
                baseUrl += data;
                headers.put("Accept", "application/json");
                break;
            case WS_DOWNLOAD:
            case WS_DOWNLOAD_MASTER_TABLE:
                baseUrl = data;
                break;
            default:
                headers = new TreeMap<>();
                jsonRequest = data;
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                break;
        }

        return httpHelper.sendRequest(baseUrl, methodType, headers, jsonRequest);
    }

}
