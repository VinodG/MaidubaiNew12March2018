package com.winit.maidubai.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.utilities.LogUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Girish Velivela on 8/8/2016.
 */
public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static AddressBookDO getAddressFromLocation(final double latitude, final double longitude, final Context context) {

        LogUtils.debug(TAG,"getAddressFromLocation - Strated");
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                AddressBookDO addressBookDO = new AddressBookDO();
                Address address = addressList.get(0);
                if(LogUtils.isLogEnable)
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        LogUtils.errorLog(TAG,address.getAddressLine(i));
                    }
                addressBookDO.addressLine1 = address.getAddressLine(0)+" "+address.getAddressLine(1); // address
                addressBookDO.addressLine2 = address.getSubLocality(); // Area
                addressBookDO.city = address.getLocality();
                addressBookDO.addressLine3 = address.getAdminArea(); // state or emirates
                addressBookDO.country = address.getCountryName();
                addressBookDO.zipCode = address.getPostalCode();
                return addressBookDO;
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        } finally {
            LogUtils.debug(TAG,"getAddressFromLocation - Ended");
        }
        return null;
    }
}
