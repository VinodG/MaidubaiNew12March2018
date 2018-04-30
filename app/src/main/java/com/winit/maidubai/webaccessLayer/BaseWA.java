package com.winit.maidubai.webaccessLayer;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.NetworkUtil;

import java.io.Serializable;


public class BaseWA implements HttpListener, Serializable {

	private WebAccessListener webAccesslistener;
	private Context mContext;

	public BaseWA(Context mContext, WebAccessListener webAccessListener)
	{
		this.mContext = mContext;
		this.webAccesslistener = webAccessListener;
	}

	public boolean startDataDownload(ServiceMethods method, String xmlRequest)
	{
		if(NetworkUtil.isNetworkConnectionAvailable(mContext))
		{
			HttpService httpService = new HttpService(method,this,xmlRequest,mContext);
			httpService.start();
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void onResponseReceived(ResponseDO response)
	{
		this.respondWithData(response);
	}

	protected void respondWithData(ResponseDO data)
	{
	    webAccesslistener.dataDownloaded(data);
	}
}
