package com.winit.maidubai.parser;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 02-09-2016.
 */
public class AddressBookListParser extends BaseJsonHandler{

    private ArrayList<AddressBookDO> arrAddressBookDOs;
    public Object getData()
    {
        return status==0?message:arrAddressBookDOs;
    }

    @Override
    public void parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("Status");
            message = jsonObject.getString("Message");
            if(status != 0){
                JSONArray jsonAddressListArray = jsonObject.getJSONArray("AddressBookLists");
                int addressListLength = jsonAddressListArray.length();
                arrAddressBookDOs = new ArrayList<>();
                for(int i=0; i< addressListLength; i++){
                    JSONObject jsonAddressBook = jsonAddressListArray.getJSONObject(i);
                    AddressBookDO addressBookDO           = new AddressBookDO();
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
                    addressBookDO.VillaName               = jsonAddressBook.optString("VillaName");
                    addressBookDO.Street                  = jsonAddressBook.optString("Street");
                    addressBookDO.city                    = jsonAddressBook.optString("City");
                    addressBookDO.state                   = jsonAddressBook.optString("State");
                    addressBookDO.country                 = jsonAddressBook.optString("Country");
                    addressBookDO.zipCode                 = jsonAddressBook.optString("ZipCode");
                    addressBookDO.phoneNo                 = jsonAddressBook.optString("Phone");
                    addressBookDO.email                   = jsonAddressBook.optString("Email");
                    addressBookDO.mobileNo                = jsonAddressBook.optString("MobileNumber");
                    addressBookDO.FlatNumber              = jsonAddressBook.optString("FlatNumber");
                    addressBookDO.isActive                = jsonAddressBook.optBoolean("IsActive");

                    arrAddressBookDOs.add(addressBookDO);
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            LogUtils.debug(LogUtils.LOG_TAG, e.getMessage());
        }
    }
}
