package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONObject;

/**
 * Created by Girish Velivela on 02-09-2016.
 */
public class ManageAddressBookParser extends BaseJsonHandler{

    private AddressBookDO addressBookDO;

    public Object getData()
    {
        return status==0?message:addressBookDO;
    }
    public void parse(String response)
    {
        try {
            JSONObject jsonAddress = new JSONObject(response);
            status = jsonAddress.getInt("Status");
            message = jsonAddress.getString("Message");
            if(status != 0 && jsonAddress.has("AddToAddressBook")){
                JSONObject jsonAddressBook = jsonAddress.getJSONObject("AddToAddressBook");
                addressBookDO                         = new AddressBookDO();
                addressBookDO.addressBookId           = jsonAddressBook.getInt("AddressBookId");
                addressBookDO.userId                  = jsonAddressBook.getInt("UserId");
                addressBookDO.firstName               = jsonAddressBook.getString("FirstName");
                addressBookDO.middleName              = jsonAddressBook.getString("MiddleName");
                addressBookDO.middleName              = jsonAddressBook.optString("MiddleName");
                addressBookDO.lastName                = jsonAddressBook.optString("LastName");
                addressBookDO.addressLine1            = jsonAddressBook.optString("AddressLine1");
                addressBookDO.addressLine1_Arabic     = jsonAddressBook.optString("AddressLine1_Arabic");
                addressBookDO.addressLine2            = jsonAddressBook.optString("AddressLine2");
                addressBookDO.addressLine2_Arabic     = jsonAddressBook.optString("AddressLine2_Arabic");
                addressBookDO.addressLine3            = jsonAddressBook.optString("AddressLine3");
                addressBookDO.addressLine4            = jsonAddressBook.optString("AddressLine4");
                addressBookDO.addressLine4_Arabic     = jsonAddressBook.optString("AddressLine4_Arabic");
                addressBookDO.Street                  = jsonAddressBook.optString("Street");
                addressBookDO.VillaName               = jsonAddressBook.optString("VillaName");
                addressBookDO.city                    = jsonAddressBook.optString("City");
                addressBookDO.state                   = jsonAddressBook.optString("State");
                addressBookDO.country                 = jsonAddressBook.optString("Country");
                addressBookDO.zipCode                 = jsonAddressBook.optString("ZipCode");
                addressBookDO.phoneNo                 = jsonAddressBook.optString("Phone");
                addressBookDO.email                   = jsonAddressBook.optString("Email");
                addressBookDO.email                   = jsonAddressBook.optString("AuthToken");
                addressBookDO.mobileNo                = jsonAddressBook.optString("MobileNumber");
                addressBookDO.FlatNumber              = jsonAddressBook.optString("FlatNumber");
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }


}
