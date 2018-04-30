package com.winit.maidubai.webaccessLayer;

import android.content.Context;

import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.parser.BaseJsonHandler;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.io.IOException;
import java.io.InputStream;


public class HttpService extends Thread
{
    private ServiceMethods method;
    private HttpListener httpListener;
    private String jsonRequest;
    private Context mContext;

    public HttpService(ServiceMethods method,HttpListener httpListener,String jsonRequest,Context mContext){
        this.method = method;
        this.httpListener = httpListener;
        this.jsonRequest = jsonRequest;
        this.mContext = mContext;
    }


    @Override
    public void run() {
        String respondsContent          = "";
        ResponseDO response             = new ResponseDO(method, true, "Unable to connect server, please try again.");
        InputStream inputStream         = null;
        RestCilent restCilent           = new RestCilent();
        try {

            LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, "************************************");
            LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, "WebService : " + String.valueOf(method));
            LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, "************************************");
            LogUtils.infoLog("Request Format : ", "" + jsonRequest);
            LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, "************************************");

            inputStream = restCilent.processRequest(method,jsonRequest);

            if (inputStream != null){
                BaseJsonHandler baseHandler = null;

                if(method == ServiceMethods.WS_DATASYNC)
                    baseHandler = BaseJsonHandler.getDataSync(mContext);
                else
                    baseHandler = BaseJsonHandler.getParser(method, respondsContent);

                if((method == ServiceMethods.WS_DOWNLOAD) || (method == ServiceMethods.WS_DOWNLOAD_MASTER_TABLE)) {
                    response.method = method;
                    response.isError = false;
                    response.data = inputStream;
                }else if (baseHandler != null) {
                    LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, String.valueOf(method) + " Parsing started");
                    String responseStr = StringUtils.convertStreamToString(inputStream);
//                    LogUtils.convertRequestToFile(responseStr);
                    baseHandler.parse(responseStr);
                    LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, String.valueOf(method) + " Parsing completed");
//                    if (baseHandler.isError()){
                    response.method = method;
                    response.isError = baseHandler.isError();
                    response.data = baseHandler.getData();
                    /*}else{
                        response.isError = true;
                    }*/
                }else
                    LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG, "JsonBaseParser  null");
            }else
                LogUtils.infoLog(LogUtils.SERVICE_LOG_TAG , " InputStream is NULL ");
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.errorLog(LogUtils.SERVICE_LOG_TAG, e.getMessage());
        }finally {
            httpListener.onResponseReceived(response);

            if(!((method == ServiceMethods.WS_DOWNLOAD) || (method == ServiceMethods.WS_DOWNLOAD_MASTER_TABLE))) {
                   if(inputStream!=null)
                    safeClose(inputStream);
            }


        }
    }
    public static void safeClose(InputStream rd) {
        if (rd != null) {
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}