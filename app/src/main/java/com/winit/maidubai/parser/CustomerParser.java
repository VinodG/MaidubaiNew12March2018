package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

public class CustomerParser extends BaseJsonHandler
{
	CustomerDO customerDO;
	public Object getData()
	{
		return status==0?message:customerDO;
	}
	public void parse(String response)
	{
		try {
			JSONObject jsonObject = new JSONObject(response);
			status = jsonObject.getInt("Status");
			message = jsonObject.getString("Message");
			if(status != 0){
				JSONObject jsonCustomer = jsonObject.getJSONObject("Login");
				customerDO = new CustomerDO();
				customerDO.Id = jsonCustomer.getInt("Id");
				customerDO.sessionId = jsonCustomer.getString("SessionId");
				customerDO.customerId = jsonCustomer.getString("CustomerId");
				customerDO.siteName = jsonCustomer.getString("SiteName");
				customerDO.cutomerEmailId = jsonCustomer.getString("Email");
				customerDO.mobileNumber = jsonCustomer.getString("MobileNumber1");
				customerDO.AuthToken = jsonCustomer.getString("AuthToken");
				customerDO.emirates = jsonCustomer.getString("Address1");
				//Added street
				if(jsonCustomer.has("Street"))
					customerDO.Street   = jsonCustomer.getString("Street");
				customerDO.area = jsonCustomer.getString("Address2");
				customerDO.city = jsonCustomer.getString("City");
				customerDO.GeoCodeX = jsonCustomer.getString("GeoCodeX");
				customerDO.GeoCodeY = jsonCustomer.getString("GeoCodeY");
				if(jsonCustomer.has("LoginorRegister"))
					customerDO.loginorRegister = jsonCustomer.getString("LoginorRegister");
				if(jsonCustomer.has("PreferedLanguage"))
					customerDO.PreferedLanguage = jsonCustomer.getString("PreferedLanguage");
				customerDO.Address = customerDO.emirates+","+customerDO.area+","+customerDO.city;

			}
		}catch (Exception e){
//			e.printStackTrace();
			LogUtils.debug(LogUtils.LOG_TAG,e.getMessage());
		}
	}

}
