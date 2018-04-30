package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by Girish Velivela on 24-08-2016.
 */
public class AddOrderAddressParser extends BaseJsonHandler{

    private AddressBookDO addressBookDO;

    public Object getData()
    {
        return status==0?message:addressBookDO;
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonAddress = new JSONObject(response);
            status = jsonAddress.optInt("Status");
            message = jsonAddress.optString("Message");
            if(status != 0 && jsonAddress.has("AddToAddress")){
                JSONObject jsonAddressBook = jsonAddress.getJSONObject("AddToAddress");
                addressBookDO  = new AddressBookDO();
                addressBookDO.shoppingCartAddressId   = jsonAddressBook.optInt("ShoppingCartAddressId");
                addressBookDO.addressType             = jsonAddressBook.optString("AddressType");
                addressBookDO.addressBookId           = jsonAddressBook.optInt("AddressBookId");
                addressBookDO.userId                  = jsonAddressBook.optInt("UserId");
                addressBookDO.firstName               = jsonAddressBook.optString("FirstName");
                addressBookDO.middleName              = jsonAddressBook.optString("MiddleName");
                addressBookDO.middleName              = jsonAddressBook.optString("MiddleName");
                addressBookDO.lastName                = jsonAddressBook.optString("LastName");
                addressBookDO.addressLine1            = jsonAddressBook.optString("AddressLine1");
                addressBookDO.addressLine2            = jsonAddressBook.optString("AddressLine2");
                addressBookDO.addressLine3            = jsonAddressBook.optString("AddressLine3");
                addressBookDO.city                    = jsonAddressBook.optString("City");
                addressBookDO.addressLine4            = jsonAddressBook.optString("AddressLine4");
                addressBookDO.VillaName               = jsonAddressBook.optString("VillaName");
                addressBookDO.Street                  = jsonAddressBook.optString("Street");
                addressBookDO.state                   = jsonAddressBook.optString("State");
                addressBookDO.country                 = jsonAddressBook.optString("Country");
                addressBookDO.zipCode                 = jsonAddressBook.optString("ZipCode");
                addressBookDO.phoneNo                 = jsonAddressBook.optString("Phone");
                addressBookDO.email                   = jsonAddressBook.optString("Email");
                addressBookDO.mobileNo                = jsonAddressBook.optString("MobileNumber");
                addressBookDO.FlatNumber              = jsonAddressBook.optString("FlatNumber");
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
