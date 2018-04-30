package com.winit.maidubai.webaccessLayer;

import android.util.Log;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CartDetailsDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


public class BuildJsonRequest {

	private final static String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<soap:Body>";

	private final static String SOAP_FOOTER = "</soap:Body>" +
			"</soap:Envelope>";

	public static String loginRequest(String emailId, String mobile, String password, String loginType,String deviceId,String language) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Email", emailId);
			jsonObject.put("Password",password);
			jsonObject.put("MobileNumber", mobile);
			jsonObject.put("LoginType", loginType);
			jsonObject.put("Hash1", "");
			jsonObject.put("Hash2", "");
			jsonObject.put("DeviceId", deviceId);
			jsonObject.put("PreferedLanguage", language);
			jsonObject.put("DeviceType", "Android");
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String otpVerify(String mobile, String otp, String loginType) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("OTP",otp);
			jsonObject.put("MobileNumber", mobile);
			jsonObject.put("LoginType", loginType);

		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();

	}
	public static String otpSendMail(String email, String otpstring) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?Email=").append(email).append("&Name=").append(otpstring);

		}
		catch (Exception e) {
		/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String masterTableQueryParams(String customerCode, String lastSyncDateTime) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?CustomerId=").append(customerCode).append("&LastSyncDateTime=").append(lastSyncDateTime);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}
	public static String resendOTPQueryParams(String mailId,String mobile,String type) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?Email=").append(mailId).append("&MobileNumber=").append(mobile).append("&Type=").append(type);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String getRollBackTransactionForEditOrder(String trxcode ,String sessionId) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?TrxCode=").append(trxcode)
					.append("&SessionId=").append(sessionId);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}
	public static String getOrderQueryParams(String UserId,String AuthToken,String OrderType,String Timeline,String SearchText,
											 String OrderStatus,String LastSyncDateTime) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?UserId=").append(UserId)
					.append("&AuthToken=").append(AuthToken)
					.append("&OrderType=").append(OrderType)
					.append("&Timeline=").append(Timeline)
					.append("&SearchText=").append(SearchText)
					.append("&OrderStatus=").append(OrderStatus)
					.append("&LastSyncDateTime=").append(URLEncoder.encode(LastSyncDateTime,"UTF-8"));
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String getValidateMinQueryParams(String sessionId) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?SessionId=").append(sessionId);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String getUpcommingDelieryDaysQueryParams(String customerId) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?CustomerId=").append(customerId);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String verifyOTPRequest(String email, String otpstring) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("MobileNumber", email);
			jsonObject.put("VerificationCode", otpstring);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String cartListRequest(String sessionId) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?SessionId=").append(sessionId);
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();
	}
	public static String removeFromCart(String sessionId,int shoppingCartId,int userId)
	{
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("ShoppingCartIds",shoppingCartId+"");
			jsonObject.put("SessionId",sessionId);
			jsonObject.put("UserId", userId);

		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		Log.i("Request",jsonObject.toString());
		return jsonObject.toString();
	}


	public static String getRegistrationReguestString(CustomerDO customerDO) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Id", customerDO.Code);
			jsonObject.put("SiteName",customerDO.siteName);
			jsonObject.put("Email", customerDO.cutomerEmailId);
			jsonObject.put("PASSCODE", customerDO.password);
			jsonObject.put("Address1", customerDO.area);
			jsonObject.put("Address2", customerDO.subArea);
			jsonObject.put("Address3", customerDO.landmark);
			jsonObject.put("Address4", customerDO.emirates);
			jsonObject.put("Street", customerDO.Street);
			jsonObject.put("City", customerDO.city);
			jsonObject.put("MobileNumber1", customerDO.mobileNumber);
			jsonObject.put("MobileNumber2", customerDO.landlineNumber);
			jsonObject.put("SocialMediaType", customerDO.socialMediaType);
			jsonObject.put("FBToken", customerDO.fbToken);
			jsonObject.put("GmailToken", customerDO.gmailToken);
			jsonObject.put("TwitterToken", customerDO.twitterToken);
			jsonObject.put("GeoCodeX", customerDO.GeoCodeX);
			jsonObject.put("GeoCodeY", customerDO.GeoCodeY);
			jsonObject.put("GeoCodeY", customerDO.GeoCodeY);
			jsonObject.put("GeoCodeY", customerDO.GeoCodeY);
			jsonObject.put("DeviceType", "ANDROID");
			jsonObject.put("DeviceId", customerDO.DeviceId);
			jsonObject.put("PreferedLanguage", customerDO.PreferedLanguage);
			jsonObject.put("FlatNumber", customerDO.FlatNumber);
			jsonObject.put("VillaName", customerDO.VillaName);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}
	public static String getForgetPasswordRequest(String emailId)
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("Email",emailId);
		}catch (JSONException e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());

		}
		return jsonObject.toString();
	}

	public static String addToCart(ArrayList<ProductDO> listProductDetails, int shoppingCartId, String sessionId, int userId) {
		JSONArray jsonArray = new JSONArray();
		try {
			for(int i=0;i<listProductDetails.size();i++)
			{

				JSONObject jsonObject = new JSONObject();
				ProductDO productDetailDO = listProductDetails.get(i);
				jsonObject.put("SessionId", sessionId);
				jsonObject.put("ShoppingCartId", shoppingCartId);
				jsonObject.put("UserId", userId+"");
				jsonObject.put("ProductCode", productDetailDO.code);
				jsonObject.put("Quantity", productDetailDO.btlCount);
				jsonObject.put("UOM", productDetailDO.UOM);
//				jsonObject.put("VAT", productDetailDO.VAT);
//				jsonObject.put("VATAmount", productDetailDO.VATAmount);
//				jsonObject.put("TotalVAT", productDetailDO.VATAmount);
				//jsonObject.put("UnitPrice", productDetailDO.btlRate);
				//jsonObject.put("ProductId", productDetailDO.id);
				//jsonObject.put("TotalPriceWODiscount", productDetailDO.totalPriceWODiscount);
				//jsonObject.put("TotalPrice", productDetailDO.totalPrice);
				//jsonObject.put("ProductColor", productDetailDO.colorId);
				//jsonObject.put("ProductName", productDetailDO.productName);
				//jsonObject.put("ProductImage", productDetailDO.originalImage);
				//jsonObject.put("DiscountAmount", productDetailDO.discountValue);
				//jsonObject.put("DiscountPercent", productDetailDO.discountValue);
//            jsonObject.put("ProductId", productDetailDO.productId);
//            jsonObject.put("SmallImage", productDetailDO.smallImage);
//            jsonObject.put("MediumImage", productDetailDO.mediumImage);
//            jsonObject.put("LargeImage", productDetailDO.largeImage);
//            jsonObject.put("OriginalImage", productDetailDO.originalImage);
				jsonArray.put(jsonObject);
			}


		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonArray.toString();
	}

	public static String updateCartDetail(CartDetailsDO cartDetailsDO){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("ShoppingCartDetailId",cartDetailsDO.shoppingCartDetailId);
			jsonObject.put("SessionId",cartDetailsDO.sessionId);
			jsonObject.put("IsGift",cartDetailsDO.isGift);
			jsonObject.put("IsGiftWrap",cartDetailsDO.isGiftWrap);
			jsonObject.put("Message",cartDetailsDO.message);
			jsonObject.put("GiftCard",cartDetailsDO.giftCard);
			jsonObject.put("GiftCardAmount",cartDetailsDO.giftCardAmount);
			jsonObject.put("CouponCode",cartDetailsDO.couponCode);
			jsonObject.put("CouponAmount",cartDetailsDO.couponAmount);
			jsonObject.put("UserMessage",cartDetailsDO.userMessage);
			jsonObject.put("DeviceType","Android");
			jsonObject.put("DeviceId",cartDetailsDO.deviceId);

	} catch (JSONException e) {
		/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
	}
		return jsonObject.toString();
	}

	public static String addOrderAddress(AddressBookDO addressBookDO){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("ShoppingCartAddressId",addressBookDO.shoppingCartAddressId);
			jsonObject.put("AddressType",addressBookDO.addressType);
			jsonObject.put("AddressBookId",addressBookDO.addressBookId);
			jsonObject.put("SessionId",addressBookDO.sessionId);
			jsonObject.put("FirstName",addressBookDO.firstName);
			jsonObject.put("MiddleName",addressBookDO.middleName);
			jsonObject.put("LastName",addressBookDO.lastName);
			jsonObject.put("AddressLine1",addressBookDO.addressLine1);
			jsonObject.put("AddressLine2",addressBookDO.addressLine2);
			jsonObject.put("AddressLine3",addressBookDO.addressLine3);
			jsonObject.put("AddressLine4",addressBookDO.addressLine4);
			jsonObject.put("VillaName",addressBookDO.VillaName);
			jsonObject.put("Street",addressBookDO.Street);
			jsonObject.put("City",addressBookDO.city);
			jsonObject.put("State",addressBookDO.state);
			jsonObject.put("Country",addressBookDO.country);
			jsonObject.put("ZipCode",addressBookDO.zipCode);
			jsonObject.put("Phone",addressBookDO.phoneNo);
			jsonObject.put("Email",addressBookDO.email);
			jsonObject.put("MobileNumber",addressBookDO.mobileNo);
			jsonObject.put("FlatNumber",addressBookDO.FlatNumber);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}
	public static String placeCODOrder(PlaceOrderDO placeOrderDO)
	{
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("SessionId",placeOrderDO.sessionId+"");
			jsonObject.put("TrxDate", placeOrderDO.trxDate);
			jsonObject.put("IsCOD", placeOrderDO.isCOD);
			jsonObject.put("UseWallet", placeOrderDO.useWallet);
			jsonObject.put("PreferredLanguage", placeOrderDO.PreferredLanguage);
			jsonObject.put("DeviceType", "Android");
			jsonObject.put("DeviceId", placeOrderDO.deviceId);
			jsonObject.put("SpecialInstructions", placeOrderDO.SpecialInstructions);
			jsonObject.put("OrderName", placeOrderDO.OrderName);
			jsonObject.put("ExistingAddress", placeOrderDO.existingAddress);
			jsonObject.put("IsRecurring", placeOrderDO.isRecurring);
			jsonObject.put("Frequency", placeOrderDO.frequency);
			jsonObject.put("NumberOfRepeatations", placeOrderDO.numberOfRepeatations);
			jsonObject.put("DeliveryDate", placeOrderDO.deliverydate);
		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String cancelOrder(TrxHeaderDO trxHeaderDO, String userCode, String category, String additionalInfo,String Language)
	{
		JSONObject mainjsonObject = new JSONObject();
		try {

			mainjsonObject.put("TrxCode", trxHeaderDO.trxCode);
			mainjsonObject.put("UserId", trxHeaderDO.userId);
			mainjsonObject.put("PreferredLanguage", Language);
			mainjsonObject.put("CancelledBy", userCode);
			mainjsonObject.put("CancellationReasonCategory", category);
			mainjsonObject.put("ReasonForCancellation", additionalInfo);
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<trxHeaderDO.trxDetailsDOs.size();i++)
			{

				JSONObject jsonObject = new JSONObject();
				TrxDetailsDO productDetailDO = trxHeaderDO.trxDetailsDOs.get(i);
				jsonObject.put("ProductId", productDetailDO.productId);
				jsonObject.put("Quantity", 0);
				jsonArray.put(jsonObject);
			}
			mainjsonObject.put("CancelOrderProducts", jsonArray);
		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return mainjsonObject.toString();
	}
	public static String repeatOrder(String trxCode,String userId)
	{
		JSONObject mainjsonObject = new JSONObject();
		try {
			mainjsonObject.put("TrxCode", trxCode);
			mainjsonObject.put("CustomerId", userId);
		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return mainjsonObject.toString();
	}
	public static String recurrOrder(String trxCode,String frequency)
	{
		JSONObject mainjsonObject = new JSONObject();
		try {
			mainjsonObject.put("TrxCode", trxCode);
			mainjsonObject.put("Frequency", frequency);
			mainjsonObject.put("NumberOfRepeatations", 3);
		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return mainjsonObject.toString();
	}
	public static String cancelRecurring(String trxCode,String date)
	{
		JSONObject mainjsonObject = new JSONObject();
		try {
			mainjsonObject.put("TrxCode", trxCode);
			mainjsonObject.put("RemovedFromRecurringOn", date);
		}
		catch(Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return mainjsonObject.toString();
	}
	public static String feedbackRequest(int userId,String category, String rating, String comments, String email, String deviceId) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("category", category);
			jsonObject.put("rating",rating);
			jsonObject.put("comments", comments);
			jsonObject.put("email", email);
			jsonObject.put("DeviceType", "Android");
			jsonObject.put("DeviceId", deviceId);
			jsonObject.put("CustomerId", userId);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String dataSyncQueryParams(String CustomerId,String LastSyncDateTime) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?CustomerId=").append(CustomerId).append("&LastSyncDateTime=").append(URLEncoder.encode(LastSyncDateTime, "UTF-8"));
		}
		catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String manageAddressRequest(AddressBookDO addressBookDO){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("AddressBookId",addressBookDO.addressBookId);
			jsonObject.put("UserId",addressBookDO.userId);
			jsonObject.put("FirstName",addressBookDO.firstName);
			jsonObject.put("MiddleName",addressBookDO.middleName);
			jsonObject.put("LastName",addressBookDO.lastName);
			jsonObject.put("AddressLine1",addressBookDO.addressLine1);
			jsonObject.put("AddressLine2",addressBookDO.addressLine2);
			jsonObject.put("AddressLine3",addressBookDO.addressLine3);
			jsonObject.put("AddressLine4",addressBookDO.addressLine4);
			jsonObject.put("Street",addressBookDO.Street);
			jsonObject.put("VillaName",addressBookDO.VillaName);
			jsonObject.put("City",addressBookDO.city);
			jsonObject.put("State",addressBookDO.state);
			jsonObject.put("Country",addressBookDO.country);
			jsonObject.put("ZipCode",addressBookDO.zipCode);
			jsonObject.put("Phone",addressBookDO.phoneNo);
			jsonObject.put("Email",addressBookDO.email);
			jsonObject.put("AuthToken",addressBookDO.AuthToken);
			jsonObject.put("MobileNumber",addressBookDO.mobileNo);
			jsonObject.put("FlatNumber",addressBookDO.FlatNumber);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String addressBookListQueryParams(int CustomerId,String authToken) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?UserId=").append(CustomerId).append("&AuthToken=").append(authToken);
		}catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}
	public static String removeAddressQueryParams(int CustomerId,int addressId, String authToken) {

		StringBuilder builder = new StringBuilder();
		try{
			builder.append("?UserId=").append(CustomerId)
					.append("&AddressBookIds=").append(addressId)
					.append("&AuthToken=").append(authToken);
		}catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return builder.toString();

	}

	public static String updateSettings(String customerId,String PreferedLanguage,String PreferedTime, boolean Notification){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("CustomerId",customerId);
			jsonObject.put("PreferredLanguage",PreferedLanguage);
			jsonObject.put("PreferredTime",PreferedTime);
			jsonObject.put("Notification", Notification);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String saveHydrationMeterSettings(HydrationMeterDO hydrationMeterDO){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("HydrationMeterSettingId",hydrationMeterDO.Id);
			jsonObject.put("CustomerId",hydrationMeterDO.CustomerId);
			jsonObject.put("Weight",hydrationMeterDO.Weight);
			jsonObject.put("Height",hydrationMeterDO.Height);
			jsonObject.put("Age",hydrationMeterDO.age);
			jsonObject.put("Gender",hydrationMeterDO.gender);
			jsonObject.put("DailyWaterConsumptionTarget",hydrationMeterDO.DailyWaterConsumptionTarget);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

	public static String addDrink(HydrationMeterReadingDO hydrationMeterReadingDO){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("HydrationMeterReadingId",hydrationMeterReadingDO.HydrationMeterReadingId);
			jsonObject.put("CustomerId",hydrationMeterReadingDO.CustomerId);
			jsonObject.put("WaterConsumed",hydrationMeterReadingDO.WaterConsumed);
			jsonObject.put("ConsumptionDate",hydrationMeterReadingDO.ConsumptionDate);
		} catch (JSONException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return jsonObject.toString();
	}

}