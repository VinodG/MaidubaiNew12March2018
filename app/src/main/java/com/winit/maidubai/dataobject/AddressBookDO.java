package com.winit.maidubai.dataobject;

/**
 * Created by Kishore.G on 4/18/2016.
 */
public class AddressBookDO extends BaseDO {

    public int trxAddressId                 = 0;
    public String trxCode                   = "";
    public int addressBookId                = 0;
    public int shoppingCartAddressId        = 0;
    public int userId                       = 0;
    public String addressType               = "";
    public String sessionId                 = "";
    public String firstName                 = "";
    public String middleName                = "";
    public String lastName                  = "";
    public String addressLine1              = ""; // Area
    public String addressLine1_Arabic       = ""; // Area Arabic
    public String addressLine2_Arabic       = ""; // Sub-Area Arabic
    public String addressLine2              = ""; // Sub-Area
    public String addressLine3              = ""; // landmark
    public String addressLine4              = ""; // emirates
    public String addressLine4_Arabic       = ""; // emirates Arabic
    public String Street                    = "";
    public String VillaName                 = "";
    public String city                      = "";
    public String state                     = "";
    public String country                   = "";
    public String zipCode                   = "";
    public String phoneNo                   = "";
    public String email                     = "";
    public String mobileNo                  = "";
    public String FlatNumber                = "";
    public String AuthToken                 = "";
    public boolean isActive; // isActive means is the address in the pending state or not simply "is active in pending state"
    public boolean isExisting;

    public String status = "Active";

    public static String BILLING_ADDRESS_TYPE = "B";
    public static String SIPPINGING_ADDRESS_TYPE = "S";
}
