package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.dataobject.UpcomingDeliveryDays;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Girish Velivela on 8/8/2016.
 */
public class CustomerDA {

    private Context context;
    public CustomerDA(Context context){
        this.context = context;
    }

    public boolean insertCustomer(ArrayList<CustomerDO> arrProductDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertCustomer() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblCustomer(Site,SiteName,SiteNameInArabic,CustomerId,CustomerStatus," +
                    "CustAcctCreationDate,PartyName,ChannelCode,SubChannelCode,RegionCode,CountryCode,Category,Address1," +
                    "Address2,Address3,Address4,PoNumber,City,PaymentType,PaymentTermCode,CreditLimit,GeoCodeX," +
                    "GeoCodeY,PASSCODE,Email,ContactPersonName,PhoneNumber,AppCustomerId,MobileNumber1,MobileNumber2," +
                    "Website,CustomerType,CreatedBy,ModifiedBy,Source,CustomerCategory,CustomerSubCategory,CustomerGroupCode," +
                    "ModifiedDate,ModifiedTime,Description,CustomerName,Collector,SalesPerson,FaxCountryCode,FaxAreaCode," +
                    "FaxPhoneNumber,PaymentTerms,PaymentMethod,CurrencyCode,BankName,BankBranchName,BankAccountNumber," +
                    "CreditCheckRequired,CreditRating,Creditreviewcycle,OrderType,Warehouse,PriceList,Company,Location," +
                    "Division,Department,Account,Intercompany,Future,OperatingUnit,EDPCODE,Region,Country,Identifyingaddress," +
                    "CityCode,StoreGrowth,PasswordHash,ServerKey,ProfileImage,FBToken,GmailToken,TwitterToken,Birthday," +
                    "Anniversary,PreferedLanguage,Day,Route,Comments,IsExistingCustomer,PreferedTime,Notification,FlatNumber,VillaName,Street,Address1_Arabic,Address2_Arabic,Address4_Arabic,Id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblCustomer set Site=?,SiteName=?,SiteNameInArabic=?,CustomerId=?,CustomerStatus=?," +
                    "CustAcctCreationDate=?,PartyName=?,ChannelCode=?,SubChannelCode=?,RegionCode=?,CountryCode=?,Category=?,Address1=?," +
                    "Address2=?,Address3=?,Address4=?,PoNumber=?,City=?,PaymentType=?,PaymentTermCode=?,CreditLimit=?,GeoCodeX=?," +
                    "GeoCodeY=?,PASSCODE=?,Email=?,ContactPersonName=?,PhoneNumber=?,AppCustomerId=?,MobileNumber1=?,MobileNumber2=?," +
                    "Website=?,CustomerType=?,CreatedBy=?,ModifiedBy=?,Source=?,CustomerCategory=?,CustomerSubCategory=?,CustomerGroupCode=?," +
                    "ModifiedDate=?,ModifiedTime=?,Description=?,CustomerName=?,Collector=?,SalesPerson=?,FaxCountryCode=?,FaxAreaCode=?," +
                    "FaxPhoneNumber=?,PaymentTerms=?,PaymentMethod=?,CurrencyCode=?,BankName=?,BankBranchName=?,BankAccountNumber=?," +
                    "CreditCheckRequired=?,CreditRating=?,Creditreviewcycle=?,OrderType=?,Warehouse=?,PriceList=?,Company=?,Location=?," +
                    "Division=?,Department=?,Account=?,Intercompany=?,Future=?,OperatingUnit=?,EDPCODE=?,Region=?,Country=?,Identifyingaddress=?," +
                    "CityCode=?,StoreGrowth=?,PasswordHash=?,ServerKey=?,ProfileImage=?,FBToken=?,GmailToken=?,TwitterToken=?,Birthday=?," +
                    "Anniversary=?,PreferedLanguage=?, Day=?,Route=?,Comments=?,IsExistingCustomer=?,PreferedTime=?," +
                    "Notification=?,FlatNumber=?,VillaName=?,Street=?,Address1_Arabic=?,Address2_Arabic=?,Address4_Arabic=? where Id =?");

            for(CustomerDO customerDO : arrProductDOs) {
                updateSqLiteStatement.bindString(1, customerDO.customerId);
                updateSqLiteStatement.bindString(2, customerDO.siteName);
                updateSqLiteStatement.bindString(3, customerDO.siteNameInArabic);
                updateSqLiteStatement.bindString(4, customerDO.customerId);
                updateSqLiteStatement.bindString(5, customerDO.customerStatus);
                updateSqLiteStatement.bindString(6, customerDO.custAcctCreationDate);
                updateSqLiteStatement.bindString(7, customerDO.PartyName);
                updateSqLiteStatement.bindString(8, customerDO.channelCode);
                updateSqLiteStatement.bindString(9, customerDO.subChannelCode);
                updateSqLiteStatement.bindString(10, customerDO.regionCode);
                updateSqLiteStatement.bindString(11, customerDO.countryCode);
                updateSqLiteStatement.bindString(12, customerDO.category);
                updateSqLiteStatement.bindString(13, customerDO.address1);
                updateSqLiteStatement.bindString(14, customerDO.address2);
                updateSqLiteStatement.bindString(15, customerDO.address3);
                updateSqLiteStatement.bindString(16, customerDO.address4);
                updateSqLiteStatement.bindString(17, customerDO.poNumber);
                updateSqLiteStatement.bindString(18, customerDO.city);
                updateSqLiteStatement.bindString(19, customerDO.paymentType);
                updateSqLiteStatement.bindString(20, customerDO.paymentTermCode);
                updateSqLiteStatement.bindDouble(21, customerDO.creditLimit);
                updateSqLiteStatement.bindString(22, customerDO.GeoCodeX);
                updateSqLiteStatement.bindString(23, customerDO.GeoCodeY);
                updateSqLiteStatement.bindString(24, customerDO.passcode);
                updateSqLiteStatement.bindString(25, customerDO.cutomerEmailId);
                updateSqLiteStatement.bindString(26, customerDO.contactPersonName);
                updateSqLiteStatement.bindString(27, customerDO.phoneNumber);
                updateSqLiteStatement.bindString(28, customerDO.appCustomerId);
                updateSqLiteStatement.bindString(29, customerDO.mobileNumber);
                updateSqLiteStatement.bindString(30, customerDO.phoneNumber);
                updateSqLiteStatement.bindString(31, customerDO.website);
                updateSqLiteStatement.bindString(32, customerDO.customerType);
                updateSqLiteStatement.bindString(33, customerDO.createdBy);
                updateSqLiteStatement.bindString(34, customerDO.modifiedBy);
                updateSqLiteStatement.bindString(35, customerDO.Source);
                updateSqLiteStatement.bindString(36, customerDO.CustomerCategory);
                updateSqLiteStatement.bindString(37, customerDO.CustomerSubCategory);
                updateSqLiteStatement.bindString(38, customerDO.CustomerGroupCode);
                updateSqLiteStatement.bindLong(39, customerDO.ModifiedDate);
                updateSqLiteStatement.bindLong(40, customerDO.ModifiedTime);
                updateSqLiteStatement.bindString(41, customerDO.Description);
                updateSqLiteStatement.bindString(42, customerDO.siteName);
                updateSqLiteStatement.bindString(43, customerDO.collector);
                updateSqLiteStatement.bindString(44, customerDO.SalesPerson);
                updateSqLiteStatement.bindString(45, customerDO.FaxCountryCode);
                updateSqLiteStatement.bindString(46, customerDO.FaxAreaCode);
                updateSqLiteStatement.bindString(47, customerDO.FaxPhoneNumber);
                updateSqLiteStatement.bindString(48, customerDO.PaymentTerms);
                updateSqLiteStatement.bindString(49, customerDO.PaymentMethod);
                updateSqLiteStatement.bindString(50, customerDO.CurrencyCode);
                updateSqLiteStatement.bindString(51, customerDO.BankName);
                updateSqLiteStatement.bindString(52, customerDO.BankBranchName);
                updateSqLiteStatement.bindString(53, customerDO.BankAccountNumber);
                updateSqLiteStatement.bindString(54, customerDO.CreditCheckRequired);
                updateSqLiteStatement.bindString(55, customerDO.CreditRating);
                updateSqLiteStatement.bindString(56, customerDO.Creditreviewcycle);
                updateSqLiteStatement.bindString(57, customerDO.OrderType);
                updateSqLiteStatement.bindString(58, customerDO.Warehouse);
                updateSqLiteStatement.bindString(59, customerDO.PriceList);
                updateSqLiteStatement.bindString(60, customerDO.Company);
                updateSqLiteStatement.bindString(61, customerDO.Location);
                updateSqLiteStatement.bindString(62, customerDO.Division);
                updateSqLiteStatement.bindString(63, customerDO.Department);
                updateSqLiteStatement.bindString(64, customerDO.Account);
                updateSqLiteStatement.bindString(65, customerDO.Intercompany);
                updateSqLiteStatement.bindString(66, customerDO.Future);
                updateSqLiteStatement.bindString(67, customerDO.OperatingUnit);
                updateSqLiteStatement.bindString(68, customerDO.EDPCODE);
                updateSqLiteStatement.bindString(69, customerDO.Region);
                updateSqLiteStatement.bindString(70, customerDO.country);
                updateSqLiteStatement.bindLong(71, customerDO.Identifyingaddress);
                updateSqLiteStatement.bindString(72, customerDO.CityCode);
                updateSqLiteStatement.bindDouble(73, customerDO.StoreGrowth);
                updateSqLiteStatement.bindString(74, customerDO.PasswordHash);
                updateSqLiteStatement.bindString(75, customerDO.ServerKey);
                updateSqLiteStatement.bindString(76, customerDO.ProfileImage);
                updateSqLiteStatement.bindString(77, customerDO.fbToken);
                updateSqLiteStatement.bindString(78, customerDO.gmailToken);
                updateSqLiteStatement.bindString(79, customerDO.twitterToken);
                updateSqLiteStatement.bindString(80, customerDO.dateOfBirth);
                updateSqLiteStatement.bindString(81, customerDO.anniversary);
                updateSqLiteStatement.bindString(82, customerDO.PreferedLanguage);
                updateSqLiteStatement.bindString(83, customerDO.Day);
                updateSqLiteStatement.bindString(84, customerDO.Route);
                updateSqLiteStatement.bindString(85, customerDO.Comments);
                updateSqLiteStatement.bindString(86, customerDO.IsExistingCustomer+"");
                updateSqLiteStatement.bindString(87, customerDO.PreferedTime);
                updateSqLiteStatement.bindString(88, customerDO.Notification+"");
                updateSqLiteStatement.bindString(89, customerDO.FlatNumber+"");
                updateSqLiteStatement.bindString(90, customerDO.VillaName+"");
                updateSqLiteStatement.bindString(91, customerDO.Street+"");
                updateSqLiteStatement.bindString(92, customerDO.address1_Arabic+"");
                updateSqLiteStatement.bindString(93, customerDO.address2_Arabic+"");
                updateSqLiteStatement.bindString(94, customerDO.address4_Arabic+"");
                updateSqLiteStatement.bindLong(95, customerDO.Id);

                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindString(1, customerDO.customerId);
                    insertSqLiteStatement.bindString(2, customerDO.siteName);
                    insertSqLiteStatement.bindString(3, customerDO.siteNameInArabic);
                    insertSqLiteStatement.bindString(4, customerDO.customerId);
                    insertSqLiteStatement.bindString(5, customerDO.customerStatus);
                    insertSqLiteStatement.bindString(6, customerDO.custAcctCreationDate);
                    insertSqLiteStatement.bindString(7, customerDO.PartyName);
                    insertSqLiteStatement.bindString(8, customerDO.channelCode);
                    insertSqLiteStatement.bindString(9, customerDO.subChannelCode);
                    insertSqLiteStatement.bindString(10, customerDO.regionCode);
                    insertSqLiteStatement.bindString(11, customerDO.countryCode);
                    insertSqLiteStatement.bindString(12, customerDO.category);
                    insertSqLiteStatement.bindString(13, customerDO.address1);
                    insertSqLiteStatement.bindString(14, customerDO.address2);
                    insertSqLiteStatement.bindString(15, customerDO.address3);
                    insertSqLiteStatement.bindString(16, customerDO.address4);
                    insertSqLiteStatement.bindString(17, customerDO.poNumber);
                    insertSqLiteStatement.bindString(18, customerDO.city);
                    insertSqLiteStatement.bindString(19, customerDO.paymentType);
                    insertSqLiteStatement.bindString(20, customerDO.paymentTermCode);
                    insertSqLiteStatement.bindDouble(21, customerDO.creditLimit);
                    insertSqLiteStatement.bindString(22, customerDO.GeoCodeX);
                    insertSqLiteStatement.bindString(23, customerDO.GeoCodeY);
                    insertSqLiteStatement.bindString(24, customerDO.passcode);
                    insertSqLiteStatement.bindString(25, customerDO.cutomerEmailId);
                    insertSqLiteStatement.bindString(26, customerDO.contactPersonName);
                    insertSqLiteStatement.bindString(27, customerDO.appCustomerId);
                    insertSqLiteStatement.bindString(28, customerDO.mobileNumber);
                    insertSqLiteStatement.bindString(29, customerDO.phoneNumber);
                    insertSqLiteStatement.bindString(30, customerDO.website);
                    insertSqLiteStatement.bindString(31, customerDO.customerType);
                    insertSqLiteStatement.bindString(32, customerDO.paymentType);
                    insertSqLiteStatement.bindString(33, customerDO.createdBy);
                    insertSqLiteStatement.bindString(34, customerDO.modifiedBy);
                    insertSqLiteStatement.bindString(35, customerDO.Source);
                    insertSqLiteStatement.bindString(36, customerDO.CustomerCategory);
                    insertSqLiteStatement.bindString(37, customerDO.CustomerSubCategory);
                    insertSqLiteStatement.bindString(38, customerDO.CustomerGroupCode);
                    insertSqLiteStatement.bindLong(39, customerDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(40, customerDO.ModifiedTime);
                    insertSqLiteStatement.bindString(41, customerDO.Description);
                    insertSqLiteStatement.bindString(42, customerDO.siteName);
                    insertSqLiteStatement.bindString(43, customerDO.collector);
                    insertSqLiteStatement.bindString(44, customerDO.SalesPerson);
                    insertSqLiteStatement.bindString(45, customerDO.FaxCountryCode);
                    insertSqLiteStatement.bindString(46, customerDO.FaxAreaCode);
                    insertSqLiteStatement.bindString(47, customerDO.FaxPhoneNumber);
                    insertSqLiteStatement.bindString(48, customerDO.PaymentTerms);
                    insertSqLiteStatement.bindString(49, customerDO.PaymentMethod);
                    insertSqLiteStatement.bindString(50, customerDO.CurrencyCode);
                    insertSqLiteStatement.bindString(51, customerDO.BankName);
                    insertSqLiteStatement.bindString(52, customerDO.BankBranchName);
                    insertSqLiteStatement.bindString(53, customerDO.BankAccountNumber);
                    insertSqLiteStatement.bindString(54, customerDO.CreditCheckRequired);
                    insertSqLiteStatement.bindString(55, customerDO.CreditRating);
                    insertSqLiteStatement.bindString(56, customerDO.Creditreviewcycle);
                    insertSqLiteStatement.bindString(57, customerDO.OrderType);
                    insertSqLiteStatement.bindString(58, customerDO.Warehouse);
                    insertSqLiteStatement.bindString(59, customerDO.PriceList);
                    insertSqLiteStatement.bindString(60, customerDO.Company);
                    insertSqLiteStatement.bindString(61, customerDO.Location);
                    insertSqLiteStatement.bindString(62, customerDO.Division);
                    insertSqLiteStatement.bindString(63, customerDO.Department);
                    insertSqLiteStatement.bindString(64, customerDO.Account);
                    insertSqLiteStatement.bindString(65, customerDO.Intercompany);
                    insertSqLiteStatement.bindString(66, customerDO.Future);
                    insertSqLiteStatement.bindString(67, customerDO.OperatingUnit);
                    insertSqLiteStatement.bindString(68, customerDO.EDPCODE);
                    insertSqLiteStatement.bindString(69, customerDO.Region);
                    insertSqLiteStatement.bindString(70, customerDO.country);
                    insertSqLiteStatement.bindLong(71, customerDO.Identifyingaddress);
                    insertSqLiteStatement.bindString(72, customerDO.CityCode);
                    insertSqLiteStatement.bindDouble(73, customerDO.StoreGrowth);
                    insertSqLiteStatement.bindString(74, customerDO.PasswordHash);
                    insertSqLiteStatement.bindString(75, customerDO.ServerKey);
                    insertSqLiteStatement.bindString(76, customerDO.ProfileImage);
                    insertSqLiteStatement.bindString(77, customerDO.fbToken);
                    insertSqLiteStatement.bindString(78, customerDO.gmailToken);
                    insertSqLiteStatement.bindString(79, customerDO.twitterToken);
                    insertSqLiteStatement.bindString(80, customerDO.dateOfBirth);
                    insertSqLiteStatement.bindString(81, customerDO.anniversary);
                    insertSqLiteStatement.bindString(82, customerDO.PreferedLanguage);
                    insertSqLiteStatement.bindString(83, customerDO.Day);
                    insertSqLiteStatement.bindString(84, customerDO.Route);
                    insertSqLiteStatement.bindString(85, customerDO.Comments);
                    insertSqLiteStatement.bindString(86, customerDO.IsExistingCustomer+"");
                    insertSqLiteStatement.bindString(87, customerDO.PreferedTime);
                    insertSqLiteStatement.bindString(88, customerDO.Notification+"");
                    insertSqLiteStatement.bindString(89, customerDO.FlatNumber+"");
                    insertSqLiteStatement.bindString(90, customerDO.VillaName+"");
                    insertSqLiteStatement.bindString(91, customerDO.Street+"");
                    insertSqLiteStatement.bindString(92, customerDO.address1_Arabic+"");
                    insertSqLiteStatement.bindString(93, customerDO.address2_Arabic+"");
                    insertSqLiteStatement.bindString(94, customerDO.address4_Arabic+"");
                    insertSqLiteStatement.bindLong(95, customerDO.Id);
                    insertSqLiteStatement.executeInsert();
                }

            }
        }catch (Exception e){
    /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertCustomer() - ended");
        }
        return false;
    }

    public CustomerDO getCustomer(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {

            LogUtils.debug(LogUtils.LOG_TAG, "CustomerDA - getCustomer() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            try {
                //21 columns retriveing
                String query = "SELECT Id,Site,SiteName,Email,MobileNumber1,Birthday,Anniversary,Address1,Address2,Address3,Address4,City,GeoCodeX,GeoCodeY,AuthToken,PreferedLanguage,PreferedTime,Notification,ProfileImage,FlatNumber,VillaName,Street,Address1_Arabic,Address2_Arabic,Address4_Arabic FROM tblCustomer";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    CustomerDO customerDO = new CustomerDO();
                    AddressBookDO addressBookDO = null;
                    customerDO.Id = cursor.getInt(0);
                    customerDO.customerId = cursor.getString(1);
                    customerDO.siteName = cursor.getString(2);
                    customerDO.cutomerEmailId = cursor.getString(3);
                    customerDO.mobileNumber = cursor.getString(4);
                    customerDO.dateOfBirth = cursor.getString(5);
                    customerDO.anniversary = cursor.getString(6);

                    if(!StringUtils.isEmpty(cursor.getString(7))) {
                        addressBookDO = new AddressBookDO();
                        addressBookDO.firstName = customerDO.siteName;
                        addressBookDO.addressLine1 = cursor.getString(7);
                        addressBookDO.addressLine2 = cursor.getString(8);
                        addressBookDO.addressLine3 = cursor.getString(9);
                        addressBookDO.addressLine4 = cursor.getString(10);
                        addressBookDO.city = cursor.getString(11);
                        addressBookDO.FlatNumber = cursor.getString(19);
                        addressBookDO.VillaName = cursor.getString(20);
                        addressBookDO.Street = cursor.getString(21);
                        addressBookDO.addressLine1_Arabic = cursor.getString(22);
                        addressBookDO.addressLine2_Arabic = cursor.getString(23);
                        addressBookDO.addressLine4_Arabic = cursor.getString(24);
                        addressBookDO.phoneNo = customerDO.mobileNumber;
                        addressBookDO.email = customerDO.cutomerEmailId;
                        addressBookDO.mobileNo = customerDO.mobileNumber;
                        addressBookDO.isActive = false;
                        customerDO.address1 = addressBookDO.addressLine1;
                        customerDO.address1_Arabic = addressBookDO.addressLine1_Arabic == null?"":addressBookDO.addressLine1_Arabic;
                        customerDO.address2 = addressBookDO.addressLine2 == null?"":addressBookDO.addressLine2;;
                        customerDO.address2_Arabic = addressBookDO.addressLine2_Arabic == null?"":addressBookDO.addressLine2_Arabic;;
                        customerDO.address3 = addressBookDO.addressLine3 == null?"":addressBookDO.addressLine3;
                        customerDO.address4 = addressBookDO.addressLine4 == null?"":addressBookDO.addressLine4;
                        customerDO.address4_Arabic = addressBookDO.addressLine4_Arabic == null?"":addressBookDO.addressLine4_Arabic;
                        customerDO.VillaName = addressBookDO.VillaName == null?"":addressBookDO.VillaName;
                        customerDO.Street = addressBookDO.Street == null?"":addressBookDO.Street;
                        customerDO.city = addressBookDO.city;
                        customerDO.FlatNumber = addressBookDO.FlatNumber;
                    }
                    customerDO.GeoCodeX = cursor.getString(12);
                    customerDO.GeoCodeX = cursor.getString(13);
                    customerDO.AuthToken = cursor.getString(14);
                    customerDO.PreferedLanguage = cursor.getString(15);
                    customerDO.PreferedTime = cursor.getString(16);
                    customerDO.Notification = StringUtils.getBoolean(cursor.getString(17));
                    customerDO.ProfileImage = cursor.getString(18);
                    customerDO.addressBookDO = addressBookDO;
                    return customerDO;
                }
            }catch (Exception e) {
/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CustomerDA - getCustomer() - ended");
            }
            return null;
        }
    }

    public String getLastSyncTime() {

        SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
        Cursor cursor = null;
        String time=null;
        try {
            String query = "SELECT LastSyncDateTime FROM tblLastSyncDateTime";
            cursor = sqLiteDatabase.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                time = cursor.getString(0);
                    /*String tt[] = time.split(" ");
                    return tt[0];*/
                return  time;
            }
        }catch (Exception e) {
     /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return time;
    }


    public String getDeliveryDays(){
        SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
        Cursor cursor = null;
        String deliveryDays=null;
        try {
            String query = "Select distinct DeliveryDay From tblAreaDeliveryDetail ARD  Inner Join tblAreaandSubArea A ON A.AreaId= ARD.AreaId Where A.Name = (select Address1 from  tblCustomer)";
            cursor = sqLiteDatabase.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                do{
                    if(StringUtils.isEmpty(deliveryDays))
                        deliveryDays = cursor.getString(0);
                    else
                        deliveryDays += ", "+cursor.getString(0);
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
         /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return deliveryDays;
    }


    public ArrayList<String> getDeliveryFullDays(){
        SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
        Cursor cursor = null;
        ArrayList<String> deliveryDays=null;
        try {
            String query = "Select distinct DeliveryDay From tblAreaDeliveryDetail ARD  Inner Join tblAreaandSubArea A ON A.AreaId= ARD.AreaId Where A.Name = (select Address1 from  tblCustomer)";
            cursor = sqLiteDatabase.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                deliveryDays = new ArrayList<>();
                do{
                    deliveryDays.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return deliveryDays;
    }

    public ArrayList<String> getUpcommingDeliveryDays(){
        SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
        Cursor cursor = null;
        ArrayList<String> deliveryDays=null;
        try {
            String query = "select DeliveryDate from tblUpcomingDeliveryDates";
            cursor = sqLiteDatabase.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                deliveryDays = new ArrayList<>();
                do{
                    deliveryDays.add(CalendarUtil.getDate(cursor.getString(0),CalendarUtil.DATE_PATTERN_dd_MMM_YYYY,CalendarUtil.DD_MMM_YYYY_PATTERN, Locale.ENGLISH));
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
   /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return deliveryDays;
    }

    public ArrayList<DeliveryDay> getDeliveryDetails(){
        SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
        Cursor cursor = null;
        ArrayList<DeliveryDay> arrDeliveryDays=null;
        try {
            String query = "SELECT * FROM tblAreaDeliveryDetail AD inner join tblAreaandSubArea ASA on AD.AreaId = ASA.AreaId where ASA.Name = (select Address1 from  tblCustomer)";
            cursor = sqLiteDatabase.rawQuery(query,null);
            if (cursor.moveToFirst()) {
                do{
                    DeliveryDay deliveryDay = new DeliveryDay();
                    deliveryDay.AreaDeliveryDetailId = cursor.getInt(0);
                    deliveryDay.AreaId = cursor.getInt(1);
                    deliveryDay.DeliveryDay = cursor.getString(2);
                    deliveryDay.Route = cursor.getString(3);
                    if(arrDeliveryDays==null)
                        arrDeliveryDays=new ArrayList<>();
                    arrDeliveryDays.add(deliveryDay);
                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return arrDeliveryDays;
    }

    public ArrayList<Emirates> getEmirates(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<Emirates> arrEmirates = null;
            try {
                String query ="select * from tblEmirates";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    arrEmirates = new ArrayList<>();
                    do {
                        Emirates emirates = new Emirates();
                        emirates.EmiratesId = cursor.getInt(0);
                        emirates.Name = cursor.getString(1);
                        emirates.ArabicName = cursor.getString(2);
                        emirates.hmAreaDOs = getAreaSubarea(emirates.EmiratesId, sqLiteDatabase);
                        emirates.arrAreaDOs = emirates.hmAreaDOs.get(0);
                        emirates.hmAreaDOs.remove(0);
                        arrEmirates.add(emirates);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
        /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                return arrEmirates;
            }
        }
    }

    public HashMap<Integer,ArrayList<AreaDO>> getAreaSubarea(int emiratesId,SQLiteDatabase sqLiteDatabase){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            Cursor cursor = null;
            HashMap<Integer,ArrayList<AreaDO>> hmAreaDOs = null;
            try {
                String query ="select * from tblAreaandSubArea where EmiratesId ="+emiratesId;
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    hmAreaDOs = new HashMap<>();
                    do {
                        AreaDO areaDO = new AreaDO();
                        areaDO.AreaId = cursor.getInt(0);
                        areaDO.EmiratesId = cursor.getInt(1);
                        areaDO.ParentId = cursor.getInt(2);
                        areaDO.Name = cursor.getString(3);
                        areaDO.ArabicName = cursor.getString(4);
                        ArrayList<AreaDO> arrAreaDOs = hmAreaDOs.get(areaDO.ParentId);
                        if(arrAreaDOs==null)
                            arrAreaDOs = new ArrayList<>();
                        arrAreaDOs.add(areaDO);
                        hmAreaDOs.put(areaDO.ParentId,arrAreaDOs);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return hmAreaDOs;
        }
    }

    public boolean insertUpcomingDeliveryDays(ArrayList<UpcomingDeliveryDays> upcomingDeliveryDayses){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            if(upcomingDeliveryDayses != null){
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - insertUpcomingDeliveryDays() - strated");
                SQLiteStatement insertSqLiteStatement = null;
                SQLiteStatement updateSqLiteStatement = null;
                SQLiteDatabase sqLiteDatabase = null;
                try {
                    sqLiteDatabase = new DatabaseHelper(context).openDataBase();

                    insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblUpcomingDeliveryDates(Id,DeliveryDate) " +
                            "values(?,?)");
                    updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblUpcomingDeliveryDates set Id=? where DeliveryDate = ?");

                    for(UpcomingDeliveryDays upcomingDeliveryDays : upcomingDeliveryDayses) {
                        updateSqLiteStatement.bindLong(1, upcomingDeliveryDays.Id);
                        updateSqLiteStatement.bindString(2, upcomingDeliveryDays.DeliveryDate);
                        if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                            insertSqLiteStatement.bindLong(1, upcomingDeliveryDays.Id);
                            insertSqLiteStatement.bindString(2, upcomingDeliveryDays.DeliveryDate);
                            insertSqLiteStatement.executeInsert();
                        }
                    }
                    return true;
                } catch (Exception e) {
                    /*e.printStackTrace(); */               Log.d("This can never happen", e.getMessage());
                } finally {
                    if (insertSqLiteStatement != null)
                        insertSqLiteStatement.close();
                    if (updateSqLiteStatement != null)
                        updateSqLiteStatement.close();
                    if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                        sqLiteDatabase.close();
                    }
                    LogUtils.debug(LogUtils.LOG_TAG, "CartDA - insertUpcomingDeliveryDays() - ended");
                }
                return false;
            }
        }
        return false;
    }

}
