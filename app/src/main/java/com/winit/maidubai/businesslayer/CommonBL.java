package com.winit.maidubai.businesslayer;

import android.content.Context;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CartDetailsDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.webaccessLayer.BaseWA;
import com.winit.maidubai.webaccessLayer.BuildJsonRequest;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;

public class CommonBL extends BaseBL
{
	private Preference preference;

	public CommonBL(Context mContext, DataListener listener)
	{
		super(mContext, listener);
		preference   = new Preference(mContext);
	}

	public void login(String emailId, String mobile, String password, String loginType,String androidId,String language){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_LOGIN, BuildJsonRequest.loginRequest(emailId,mobile, password,loginType,androidId,language));
	}
	public void otpVerifyRequest(String mobile, String otp, String loginType){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_OTP_VERYFY, BuildJsonRequest.otpVerify(mobile, otp, AppConstants.EXISTING));
	}
	public void registerCustomer(CustomerDO customerDO){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_SIGNUP, BuildJsonRequest.getRegistrationReguestString(customerDO));
	}
	public void snRegisterCustomer(CustomerDO customerDO){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_SN_SIGNUP, BuildJsonRequest.getRegistrationReguestString(customerDO));
	}
	public void verifyOTP(String email, String otpstring){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_OTP_MAIL, BuildJsonRequest.verifyOTPRequest(email, otpstring));
	}
	public void verifyOTPLogin(String email, String otpstring){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_OTP_MAIL_LOGIN, BuildJsonRequest.verifyOTPRequest(email, otpstring));
	}
	public void forgorPassword(String emailId){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_FORGET_PASSWORD,BuildJsonRequest.getForgetPasswordRequest(emailId));
	}
	public void downloadMasterTable(String customerId, String lastSyncDateTime){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_MASTER_TABLE,BuildJsonRequest.masterTableQueryParams(customerId, lastSyncDateTime));
	}
	public void resndOTP(String mailId, String mobile, String type){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_RESEND_OTP,BuildJsonRequest.resendOTPQueryParams(mailId, mobile, type));
	}
	public void addToCart(ArrayList<ProductDO> listProductDetails, int shoppingCartId, String sessionId, int userId){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_ADD_TO_CART, BuildJsonRequest.addToCart(listProductDetails, shoppingCartId, sessionId, userId));
	}
	public void addToCartNew(ArrayList<ProductDO> listProductDetails, int shoppingCartId, String sessionId, int userId){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_ADD_TO_CART, BuildJsonRequest.addToCart(listProductDetails, shoppingCartId, sessionId, userId));
	}
	public void getCartList(String sessionId){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CART_LIST, BuildJsonRequest.cartListRequest(sessionId));
	}
	public void updateCartDetails(CartDetailsDO cartDetailsDO){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_UPDATE_CART_DETAILS, BuildJsonRequest.updateCartDetail(cartDetailsDO));
	}
	public void addOrderAddress(AddressBookDO addressBookDO){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_ADD_ORDER_ADDRESS, BuildJsonRequest.addOrderAddress(addressBookDO));
	}
	public  void placeCODOrder(PlaceOrderDO placeOrderDO){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_PLACE_ORDER, BuildJsonRequest.placeCODOrder(placeOrderDO));
	}
	public  void cancelOrder(TrxHeaderDO trxHeaderDO, String userCode,String category, String additionalInfo,String Language){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CANCEL_ORDER, BuildJsonRequest.cancelOrder(trxHeaderDO, userCode, category, additionalInfo,Language));
	}
	public  void repeatOrder(String trxCode,String userId){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_REPEAT_ORDER, BuildJsonRequest.repeatOrder(trxCode, userId));
	}
	public  void recurrOrder(String trxCode,String frequency){
		new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_RECURR_ORDER, BuildJsonRequest.recurrOrder(trxCode, frequency));
	}
	public  void cancelRecuuring(String trxCode,String date){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_CANCEL_RECURRING, BuildJsonRequest.cancelRecurring(trxCode, date));
	}
	public void downloadMasterTable(String url){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_DOWNLOAD_MASTER_TABLE,url);
	}
	public void download(String url){
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_DOWNLOAD,url);
	}
	public void removeFromCart(String sessionId,int shoppingCartId,int userId)
	{
		 new BaseWA(mContext,this).startDataDownload(ServiceMethods.WS_REMOVE_FROM_CART,BuildJsonRequest.removeFromCart(sessionId,shoppingCartId,userId));
	}
	public void feedBack(int userId, String category, String rating, String comments,String email, String deviceId) {
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_FEEDBACK, BuildJsonRequest.feedbackRequest(userId, category, rating, comments, email, deviceId));
	}
	public void dataSync(String CustomerId,String lastSync){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_DATASYNC, BuildJsonRequest.dataSyncQueryParams(CustomerId, lastSync));
	}
	public void manageAddressBookList(AddressBookDO addressBookDO){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_MANAGE_ADDRESS_BOOK, BuildJsonRequest.manageAddressRequest(addressBookDO));
	}
	public void removeAddress(int CustomerId,int addressId, String authToken){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_REMOVE_ADDRESS, BuildJsonRequest.removeAddressQueryParams(CustomerId,addressId, authToken));
	}
	public void getAddressBookList(int CustomerId,String authToken){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_ADDRESS_BOOK_LIST, BuildJsonRequest.addressBookListQueryParams(CustomerId, authToken));
	}
	public void getAreas(){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_AREAS, "");
	}
	public void getEmirates(){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_EMIRATES, "");
	}
	public void getDeliveryDays(){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_DELIVERY_DAYS, "");
	}
	public void updateProfileSettings(String customerId,String PreferedLanguage,String PreferedTime, boolean Notification){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_UPDATE_SETTING, BuildJsonRequest.updateSettings(customerId,PreferedLanguage, PreferedTime, Notification));
	}
	public void saveHydrationMeterSettings(HydrationMeterDO hydrationMeterDO){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_SAVE_HYDRATION_SETTINGS, BuildJsonRequest.saveHydrationMeterSettings(hydrationMeterDO));
	}
	public void addDrink(HydrationMeterReadingDO hydrationMeterReadingDO){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_ADD_DRINK, BuildJsonRequest.addDrink(hydrationMeterReadingDO));
	}

	public void getOrders(String UserId,String AuthToken,String OrderType,String Timeline,String SearchText,
						  String OrderStatus,String LastSyncDateTime){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_GET_ORDERS, BuildJsonRequest.getOrderQueryParams(UserId,AuthToken,OrderType,Timeline,SearchText
		 ,OrderStatus,LastSyncDateTime));
	}
	public void getRollBackTransactionForEditOrder(String trxcode,String sessionId){
		 new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_EDIT_ORDER_URL, BuildJsonRequest.getRollBackTransactionForEditOrder(trxcode ,sessionId));
	}

	public void validateMinOrderQty(String sessionId) {
		new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_VALIDATE_MIN_ORDERS, BuildJsonRequest.getValidateMinQueryParams(sessionId));
	}

	public void upcommingDeliveryDays(String customerId) {
		new BaseWA(mContext, this).startDataDownload(ServiceMethods.WS_UPCOMMING_DELIVERY_DAYS, BuildJsonRequest.getUpcommingDelieryDaysQueryParams(customerId));
	}

}
