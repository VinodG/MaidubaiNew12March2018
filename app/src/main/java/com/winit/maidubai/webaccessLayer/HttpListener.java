package com.winit.maidubai.webaccessLayer;

import com.winit.maidubai.dataobject.ResponseDO;

public interface HttpListener {
	void onResponseReceived(ResponseDO response);
}
