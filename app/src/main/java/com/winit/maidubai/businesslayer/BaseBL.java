package com.winit.maidubai.businesslayer;

import android.content.Context;

import com.winit.maidubai.BaseActivity;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.webaccessLayer.WebAccessListener;

public class BaseBL implements WebAccessListener {
    DataListener listener;
    public Context mContext;

    public BaseBL(Context mContext, DataListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public void dataDownloaded(ResponseDO data) {
        if (listener != null) {
            ((BaseActivity) mContext).runOnUiThread(new DataRetreivedRunnable(listener, data));
        }
    }

    class DataRetreivedRunnable implements Runnable {
        DataListener listener;
        ResponseDO data;

        DataRetreivedRunnable(DataListener listener, ResponseDO data) {
            this.listener = listener;
            this.data = data;
        }

        @Override
        public void run() {
            listener.dataRetrieved(data);
        }
    }
}

