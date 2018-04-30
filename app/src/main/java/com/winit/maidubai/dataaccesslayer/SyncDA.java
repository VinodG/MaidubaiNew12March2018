package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.AreaRouteSupervisorsDO;
import com.winit.maidubai.dataobject.AreaRoutesDO;
import com.winit.maidubai.dataobject.CategoryDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.DeletedRecordDO;
import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.dataobject.EmirateDO;
import com.winit.maidubai.dataobject.GalleryDO;
import com.winit.maidubai.dataobject.HelpAnswerDO;
import com.winit.maidubai.dataobject.HelpCategoryDO;
import com.winit.maidubai.dataobject.HelpQuestionDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.dataobject.HydrationMeterSettingDO;
import com.winit.maidubai.dataobject.ItemUOMDO;
import com.winit.maidubai.dataobject.OrderCancelDO;
import com.winit.maidubai.dataobject.OrderNumberDO;
import com.winit.maidubai.dataobject.PriceDO;
import com.winit.maidubai.dataobject.PriceListDO;
import com.winit.maidubai.dataobject.ProductAttribute;
import com.winit.maidubai.dataobject.ProductAttributeDetailsDO;
import com.winit.maidubai.dataobject.ProductCategoryDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ProductImagesDO;
import com.winit.maidubai.dataobject.ProductOrderLimitDO;
import com.winit.maidubai.dataobject.RecurringOrderDO;
import com.winit.maidubai.dataobject.ShoppingCartAddressDO;
import com.winit.maidubai.dataobject.ShoppingCartDO;
import com.winit.maidubai.dataobject.ShoppingCartDetailsDO;
import com.winit.maidubai.dataobject.TrxAddressDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 23-08-2016.
 */
public class SyncDA {

    private Context context;
    public SyncDA(Context context){
        this.context = context;
    }

    public boolean insertProducts(ArrayList<ProductDO> arrProductDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertProducts() - started");

        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblProduct(ProductId,ParentId,Code,Name," +
                    " Description,AltDescription,SalesOrgCode,BaseUOM,ColorId,IsSellable,IsActive,IsOutOfStock," +
                    " URLName,CreatedBy,CreatedOn,ModifiedBy,ModifiedOn,Timestamp,IsInventoryControlled,Weight," +
                    " GroupLevel1,GroupLevel2,GroupLevel3,GroupLevel4,GroupLevel5,GroupLevel6,EAUOM,Sequence) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblProduct set ParentId=?,Code=?,Name=?," +
                    "Description=?,AltDescription=?,SalesOrgCode=?,BaseUOM=?,ColorId=?,IsSellable=?,IsActive=?,IsOutOfStock=?," +
                    "URLName=?,CreatedBy=?,CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=?,IsInventoryControlled=?,Weight=?," +
                    "GroupLevel1=?,GroupLevel2=?,GroupLevel3=?,GroupLevel4=?,GroupLevel5=?,GroupLevel6=?,EAUOM=?,Sequence=? where ProductId = ?");

            for(ProductDO productDO: arrProductDOs) {
                updateSqLiteStatement.bindLong(1, productDO.ParentId);
                updateSqLiteStatement.bindString(2, productDO.code);
                updateSqLiteStatement.bindString(3, productDO.name);
                updateSqLiteStatement.bindString(4, productDO.description);
                updateSqLiteStatement.bindString(5, productDO.AltDescription);
                updateSqLiteStatement.bindString(6, productDO.SalesOrgCode);
                updateSqLiteStatement.bindString(7, productDO.BaseUOM);
                updateSqLiteStatement.bindLong(8, productDO.ColorId);
                updateSqLiteStatement.bindString(9, productDO.IsSellable + "");
                updateSqLiteStatement.bindString(10, productDO.IsActive+"");
                updateSqLiteStatement.bindString(11, productDO.IsOutOfStock + "");
                updateSqLiteStatement.bindString(12, productDO.URLName);
                updateSqLiteStatement.bindLong(13, productDO.CreatedBy);
                updateSqLiteStatement.bindString(14, productDO.CreatedOn);
                updateSqLiteStatement.bindLong(15, productDO.ModifiedBy);
                updateSqLiteStatement.bindString(16, productDO.ModifiedOn);
                updateSqLiteStatement.bindLong(17, productDO.Timestamp);
                updateSqLiteStatement.bindString(18, productDO.IsInventoryControlled + "");
                updateSqLiteStatement.bindDouble(19, productDO.Weight);
                updateSqLiteStatement.bindString(20, productDO.GroupLevel1);
                updateSqLiteStatement.bindString(21, productDO.GroupLevel2);
                updateSqLiteStatement.bindString(22, productDO.GroupLevel3);
                updateSqLiteStatement.bindString(23, productDO.GroupLevel4);
                updateSqLiteStatement.bindString(24, productDO.GroupLevel5);
                updateSqLiteStatement.bindString(25, productDO.GroupLevel6);
                updateSqLiteStatement.bindString(26, productDO.EAUOM);
                updateSqLiteStatement.bindString(27, productDO.Sequence);
                updateSqLiteStatement.bindLong(28, productDO.ProductId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, productDO.ProductId);
                    insertSqLiteStatement.bindLong(2, productDO.ParentId);
                    insertSqLiteStatement.bindString(3, productDO.code);
                    insertSqLiteStatement.bindString(4, productDO.name);
                    insertSqLiteStatement.bindString(5, productDO.description);
                    insertSqLiteStatement.bindString(6, productDO.AltDescription);
                    insertSqLiteStatement.bindString(7, productDO.SalesOrgCode);
                    insertSqLiteStatement.bindString(8, productDO.BaseUOM);
                    insertSqLiteStatement.bindLong(9, productDO.ColorId);
                    insertSqLiteStatement.bindString(10, productDO.IsSellable + "");
                    insertSqLiteStatement.bindString(11, productDO.IsActive+"");
                    insertSqLiteStatement.bindString(12, productDO.IsOutOfStock + "");
                    insertSqLiteStatement.bindString(13, productDO.URLName);
                    insertSqLiteStatement.bindLong(14, productDO.CreatedBy);
                    insertSqLiteStatement.bindString(15, productDO.CreatedOn);
                    insertSqLiteStatement.bindLong(16, productDO.ModifiedBy);
                    insertSqLiteStatement.bindString(17, productDO.ModifiedOn);
                    insertSqLiteStatement.bindLong(18, productDO.Timestamp);
                    insertSqLiteStatement.bindString(19, productDO.IsInventoryControlled + "");
                    insertSqLiteStatement.bindDouble(20, productDO.Weight);
                    insertSqLiteStatement.bindString(21, productDO.GroupLevel1);
                    insertSqLiteStatement.bindString(22, productDO.GroupLevel2);
                    insertSqLiteStatement.bindString(23, productDO.GroupLevel3);
                    insertSqLiteStatement.bindString(24, productDO.GroupLevel4);
                    insertSqLiteStatement.bindString(25, productDO.GroupLevel5);
                    insertSqLiteStatement.bindString(26, productDO.GroupLevel6);
                    insertSqLiteStatement.bindString(27, productDO.EAUOM);
                    insertSqLiteStatement.bindString(28, productDO.Sequence);
                    insertSqLiteStatement.executeInsert();
                }
            }

        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertProducts() - ended");
            return false;
        }
    }


    public boolean insertProductImages(ArrayList<ProductImagesDO> arrProductDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertProductImages() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblProductImages(ProductId,OriginalImage,SmallImage," +
                    "MediumImage,LargeImage,IsActive,IsDefault,ProductImageId) values(?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblProductImages set ProductId=?,OriginalImage=?,SmallImage=?," +
                    "MediumImage=?,LargeImage=?,IsActive=?,IsDefault=? where ProductImageId = ?");

            for(ProductImagesDO productImagesDO : arrProductDOs) {
                updateSqLiteStatement.bindLong(1, productImagesDO.ProductId);
                updateSqLiteStatement.bindString(2, productImagesDO.OriginalImage);
                updateSqLiteStatement.bindString(3, productImagesDO.SmallImage);
                updateSqLiteStatement.bindString(4, productImagesDO.MediumImage);
                updateSqLiteStatement.bindString(5, productImagesDO.LargeImage);
                updateSqLiteStatement.bindString(6, productImagesDO.IsActive+"");
                updateSqLiteStatement.bindString(7, productImagesDO.IsDefault+"");
                updateSqLiteStatement.bindLong(8, productImagesDO.ProductImageId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, productImagesDO.ProductId);
                    insertSqLiteStatement.bindString(2, productImagesDO.OriginalImage);
                    insertSqLiteStatement.bindString(3, productImagesDO.SmallImage);
                    insertSqLiteStatement.bindString(4, productImagesDO.MediumImage);
                    insertSqLiteStatement.bindString(5, productImagesDO.LargeImage);
                    insertSqLiteStatement.bindString(6, productImagesDO.IsActive+"");
                    insertSqLiteStatement.bindString(7, productImagesDO.IsDefault+"");
                    insertSqLiteStatement.bindLong(8, productImagesDO.ProductImageId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertProducts() - ended");
            return false;
        }
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
                    "Anniversary,PreferedLanguage,Day,Route,Comments,IsExistingCustomer,PreferedTime,Notification,Id,FlatNumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
                    "Anniversary=?,PreferedLanguage=?, Day=?,Route=?,Comments=?,IsExistingCustomer=?,PreferedTime=?,Notification=?,FlatNumber=? where Id =?");

            for(CustomerDO customerDO : arrProductDOs) {
                updateSqLiteStatement.bindString(1, customerDO.site);
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
                updateSqLiteStatement.bindString(89, customerDO.FlatNumber);
                updateSqLiteStatement.bindLong(90, customerDO.Id);

                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindString(1, customerDO.site);
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
                    insertSqLiteStatement.bindLong(89, customerDO.Id);
                    insertSqLiteStatement.bindString(90, customerDO.FlatNumber);
                    insertSqLiteStatement.executeInsert();
                }

            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertCustomer() - ended");
            return false;
        }
    }

    // Gallery Syncing

    public boolean insertGalleryImages(ArrayList<GalleryDO> arrGalleryDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertGalleryImages() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblGallery(Type,Path,GalleryId) values(?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblGallery set Type=?,Path=? where GalleryId = ?");

            for(GalleryDO galleryDO : arrGalleryDOs) {
                updateSqLiteStatement.bindString(1, galleryDO.Type);
                updateSqLiteStatement.bindString(2, galleryDO.Path);
                updateSqLiteStatement.bindLong(3, galleryDO.GalleryId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, galleryDO.Type);
                    insertSqLiteStatement.bindString(2, galleryDO.Path);
                    insertSqLiteStatement.bindLong(3, galleryDO.GalleryId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
            /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertGalleryImages() - ended");
            return false;
        }
    }

    // tblHelpQuestion Syncing
    public boolean insertHelpQuestion(ArrayList<HelpQuestionDO> arrHelpQuestions){
        LogUtils.debug(LogUtils.LOG_TAG,"insertHelpQuestion() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHelpQuestion(CategoryCode,Question,HelpQuestionId) values(?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHelpQuestion set CategoryCode=?,Question=? where HelpQuestionId = ?");
            for(HelpQuestionDO galleryDO : arrHelpQuestions) {
                updateSqLiteStatement.bindString(1, galleryDO.CategoryCode);
                updateSqLiteStatement.bindString(2, galleryDO.Question);
                updateSqLiteStatement.bindLong(3, galleryDO.HelpQuestionId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, galleryDO.CategoryCode);
                    insertSqLiteStatement.bindString(2, galleryDO.Question);
                    insertSqLiteStatement.bindLong(3, galleryDO.HelpQuestionId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertHelpQuestion() - ended");
            return false;
        }
    }

    // tblHelpAnswer Syncing
    public boolean insertHelpAnswer(ArrayList<HelpAnswerDO> arrHelpAnswers){
        LogUtils.debug(LogUtils.LOG_TAG,"insertHelpAnswer() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHelpAnswer(HelpQuestionId,Answer,HelpAnswerId) values(?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHelpAnswer set HelpQuestionId=?,Answer=? where HelpAnswerId = ?");
            for(HelpAnswerDO galleryDO : arrHelpAnswers) {
                updateSqLiteStatement.bindLong(1, galleryDO.HelpQuestionId);
                updateSqLiteStatement.bindString(2, galleryDO.Answer);
                updateSqLiteStatement.bindLong(3, galleryDO.HelpAnswerId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, galleryDO.HelpQuestionId);
                    insertSqLiteStatement.bindString(2, galleryDO.Answer);
                    insertSqLiteStatement.bindLong(3, galleryDO.HelpAnswerId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
/*            e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertHelpAnswer() - ended");
            return false;
        }
    }

    // tblHelpCategory Syncing
    public boolean insertHelpCategory(ArrayList<HelpCategoryDO> arrHelpCategorys){
        LogUtils.debug(LogUtils.LOG_TAG,"insertHelpCategory() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHelpCategory(Code,Title,ParentId,HelpCategoryId) values(?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHelpCategory set Code=?,Title=?,ParentId=? where HelpCategoryId = ?");
            for(HelpCategoryDO galleryDO : arrHelpCategorys) {
                updateSqLiteStatement.bindString(1, galleryDO.Code);
                updateSqLiteStatement.bindString(2, galleryDO.Title);
                updateSqLiteStatement.bindLong(3, galleryDO.ParentId);
                updateSqLiteStatement.bindLong(4, galleryDO.HelpCategoryId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, galleryDO.Code);
                    insertSqLiteStatement.bindString(2, galleryDO.Title);
                    insertSqLiteStatement.bindLong(3, galleryDO.ParentId);
                    insertSqLiteStatement.bindLong(4, galleryDO.HelpCategoryId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertHelpCategory() - ended");
            return false;
        }
    }

    // Price List Syncing..
    public boolean insertPriceLists(ArrayList<PriceListDO> arrPriceListDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertPriceLists() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblPriceList(Name,PriceListId) values(?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblPriceList set Name=? where PriceListId = ?");

            for(PriceListDO priceListDO : arrPriceListDOs) {
                updateSqLiteStatement.bindString(1, priceListDO.Name);
                updateSqLiteStatement.bindLong(2, priceListDO.PriceListId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, priceListDO.Name);
                    insertSqLiteStatement.bindLong(2, priceListDO.PriceListId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertPriceLists() - ended");
            return false;
        }
    }

    // Prices Syncing..

    public boolean insertPrices(ArrayList<PriceDO> arrPriceDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertPrices() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblPrice(ProductId,UnitPrice,CurrencyCode,UOM,StartDate,EndDate,IsActive," +
                    "CreatedBy,CreatedOn,ModifiedBy,ModifiedOn,Timestamp,SalesOrgCode,PriceListId,PriceId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblPrice set ProductId=?,UnitPrice=?,CurrencyCode=?,UOM=?,StartDate=?,EndDate=?,IsActive=?," +
                    "CreatedBy=?,CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=?,SalesOrgCode=?,PriceListId=? where PriceId = ?");

            for(PriceDO priceDO : arrPriceDOs) {
                updateSqLiteStatement.bindLong(1, priceDO.ProductId);
                updateSqLiteStatement.bindDouble(2, priceDO.UnitPrice);
                updateSqLiteStatement.bindString(3, priceDO.CurrencyCode);
                updateSqLiteStatement.bindString(4, priceDO.UOM);
                updateSqLiteStatement.bindString(5, priceDO.StartDate + "");
                updateSqLiteStatement.bindString(6, priceDO.EndDate + "");
                updateSqLiteStatement.bindString(7, priceDO.IsActive + "");
                updateSqLiteStatement.bindLong(8, priceDO.CreatedBy);
                updateSqLiteStatement.bindString(9, priceDO.CreatedOn + "");
                updateSqLiteStatement.bindLong(10, priceDO.ModifiedBy);
                updateSqLiteStatement.bindString(11, priceDO.ModifiedOn + "");
                updateSqLiteStatement.bindLong(12, priceDO.Timestamp);
                updateSqLiteStatement.bindString(13, priceDO.SalesOrgCode + "");
                updateSqLiteStatement.bindString(14, priceDO.PriceListCode + "");
                updateSqLiteStatement.bindLong(15, priceDO.PriceId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, priceDO.ProductId);
                    insertSqLiteStatement.bindDouble(2, priceDO.UnitPrice);
                    insertSqLiteStatement.bindString(3, priceDO.CurrencyCode);
                    insertSqLiteStatement.bindString(4, priceDO.UOM);
                    insertSqLiteStatement.bindString(5, priceDO.StartDate + "");
                    insertSqLiteStatement.bindString(6, priceDO.EndDate + "");
                    insertSqLiteStatement.bindString(7, priceDO.IsActive + "");
                    insertSqLiteStatement.bindLong(8, priceDO.CreatedBy);
                    insertSqLiteStatement.bindString(9, priceDO.CreatedOn + "");
                    insertSqLiteStatement.bindLong(10, priceDO.ModifiedBy);
                    insertSqLiteStatement.bindString(11, priceDO.ModifiedOn + "");
                    insertSqLiteStatement.bindLong(12, priceDO.Timestamp);
                    insertSqLiteStatement.bindString(13, priceDO.SalesOrgCode + "");
                    insertSqLiteStatement.bindString(14, priceDO.PriceListCode + "");
                    insertSqLiteStatement.bindLong(15, priceDO.PriceId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertPriceLists() - ended");
            return false;
        }
    }

    // ItemUOM Syncing..

    public boolean insertItemUOMs(ArrayList<ItemUOMDO> arrItemUOMDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertItemUOMs() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblItemUOM(ItemCode,SalesOrgCode,UOM,Numerator,Denominator,Conversion,BarCode," +
                    "Length,Depth,Width,Height,Volume,VolumeUnit,Weight,WeightUnit,CreatedBy,ModifiedBy,ModifiedDate,ModifiedTime,HeightUnit,LengthUnit,ItemUOMId) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblItemUOM set ItemCode=?,SalesOrgCode=?,UOM=?,Numerator=?,Denominator=?,Conversion=?,BarCode=?," +
                    "Length=?,Depth=?,Width=?,Height=?,Volume=?,VolumeUnit=?,Weight=?,WeightUnit=?,CreatedBy=?," +
                    "ModifiedBy=?,ModifiedDate=?,ModifiedTime=?,HeightUnit=?,LengthUnit=? where ItemUOMId = ?");

            for(ItemUOMDO itemUOMDO : arrItemUOMDOs) {
                updateSqLiteStatement.bindString(1, itemUOMDO.ItemCode);
                updateSqLiteStatement.bindString(2, itemUOMDO.SalesOrgCode);
                updateSqLiteStatement.bindString(3, itemUOMDO.UOM);
                updateSqLiteStatement.bindDouble(4, itemUOMDO.Numerator);
                updateSqLiteStatement.bindDouble(5, itemUOMDO.Denominator);
                updateSqLiteStatement.bindDouble(6, itemUOMDO.Conversion);
                updateSqLiteStatement.bindString(7, itemUOMDO.BarCode);
                updateSqLiteStatement.bindDouble(8, itemUOMDO.Length);
                updateSqLiteStatement.bindDouble(9, itemUOMDO.Depth);
                updateSqLiteStatement.bindDouble(10, itemUOMDO.Width);
                updateSqLiteStatement.bindDouble(11, itemUOMDO.Height);
                updateSqLiteStatement.bindDouble(12, itemUOMDO.Volume);
                updateSqLiteStatement.bindString(13, itemUOMDO.VolumeUnit);
                updateSqLiteStatement.bindDouble(14, itemUOMDO.Weight);
                updateSqLiteStatement.bindString(15, itemUOMDO.WeightUnit);
                updateSqLiteStatement.bindString(16, itemUOMDO.CreatedBy);
                updateSqLiteStatement.bindString(17, itemUOMDO.ModifiedBy);
                updateSqLiteStatement.bindLong(18, itemUOMDO.ModifiedDate);
                updateSqLiteStatement.bindLong(19, itemUOMDO.ModifiedTime);
                updateSqLiteStatement.bindString(20, itemUOMDO.HeightUnit);
                updateSqLiteStatement.bindString(21, itemUOMDO.LengthUnit);
                updateSqLiteStatement.bindLong(22, itemUOMDO.ItemUOMId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, itemUOMDO.ItemCode);
                    insertSqLiteStatement.bindString(2, itemUOMDO.SalesOrgCode);
                    insertSqLiteStatement.bindString(3, itemUOMDO.UOM);
                    insertSqLiteStatement.bindDouble(4, itemUOMDO.Numerator);
                    insertSqLiteStatement.bindDouble(5, itemUOMDO.Denominator);
                    insertSqLiteStatement.bindDouble(6, itemUOMDO.Conversion);
                    insertSqLiteStatement.bindString(7, itemUOMDO.BarCode);
                    insertSqLiteStatement.bindDouble(8, itemUOMDO.Length);
                    insertSqLiteStatement.bindDouble(9, itemUOMDO.Depth);
                    insertSqLiteStatement.bindDouble(10, itemUOMDO.Width);
                    insertSqLiteStatement.bindDouble(11, itemUOMDO.Height);
                    insertSqLiteStatement.bindDouble(12, itemUOMDO.Volume);
                    insertSqLiteStatement.bindString(13, itemUOMDO.VolumeUnit);
                    insertSqLiteStatement.bindDouble(14, itemUOMDO.Weight);
                    insertSqLiteStatement.bindString(15, itemUOMDO.WeightUnit);
                    insertSqLiteStatement.bindString(16, itemUOMDO.CreatedBy);
                    insertSqLiteStatement.bindString(17, itemUOMDO.ModifiedBy);
                    insertSqLiteStatement.bindLong(18, itemUOMDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(19, itemUOMDO.ModifiedTime);
                    insertSqLiteStatement.bindString(20, itemUOMDO.HeightUnit);
                    insertSqLiteStatement.bindString(21, itemUOMDO.LengthUnit);
                    insertSqLiteStatement.bindLong(22, itemUOMDO.ItemUOMId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertItemUOMs() - ended");
            return false;
        }
    }


    // ShoppingCarts Syncing..

    public boolean insertShoppingCarts(ArrayList<ShoppingCartDO> arrShoppingCartDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertShoppingCarts() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblShoppingCart(SessionId,UserId,ProductCode,Quantity,UnitPrice,TotalPriceWODiscount,DiscountRefId," +
                    "DiscountAmount,TotalPrice,ModifiedOn,UOM,ShoppingCartId, VAT,TotalVAT) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,? ,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblShoppingCart set SessionId=?,UserId=?,ProductCode=?,Quantity=?,UnitPrice=?," +
                    "TotalPriceWODiscount=?,DiscountRefId=?," +
                    "DiscountAmount=?,TotalPrice=?,ModifiedOn+=?,UOM=? , VAT=?,TotalVAT=? where ShoppingCartId = ?");

            for(ShoppingCartDO shoppingCartDO : arrShoppingCartDOs) {
                updateSqLiteStatement.bindString(1, shoppingCartDO.SessionId);
                updateSqLiteStatement.bindLong(2, shoppingCartDO.UserId);
                updateSqLiteStatement.bindString(3, shoppingCartDO.ProductCode);
                updateSqLiteStatement.bindLong(4, shoppingCartDO.Quantity);
                updateSqLiteStatement.bindDouble(5, shoppingCartDO.UnitPrice);
                updateSqLiteStatement.bindDouble(6, shoppingCartDO.TotalPriceWODiscount);
                updateSqLiteStatement.bindLong(7, shoppingCartDO.DiscountRefId);
                updateSqLiteStatement.bindDouble(8, shoppingCartDO.DiscountAmount);
                updateSqLiteStatement.bindDouble(9, shoppingCartDO.TotalPrice);
                updateSqLiteStatement.bindString(10, shoppingCartDO.ModifiedOn + "");
                updateSqLiteStatement.bindString(11, shoppingCartDO.UOM + "");
                updateSqLiteStatement.bindLong(12, shoppingCartDO.VAT);
                updateSqLiteStatement.bindDouble(13, shoppingCartDO.TotalVAT);
                updateSqLiteStatement.bindLong(14, shoppingCartDO.ShoppingCartId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, shoppingCartDO.SessionId);
                    insertSqLiteStatement.bindLong(2, shoppingCartDO.UserId);
                    insertSqLiteStatement.bindString(3, shoppingCartDO.ProductCode);
                    insertSqLiteStatement.bindLong(4, shoppingCartDO.Quantity);
                    insertSqLiteStatement.bindDouble(5, shoppingCartDO.UnitPrice);
                    insertSqLiteStatement.bindDouble(6, shoppingCartDO.TotalPriceWODiscount);
                    insertSqLiteStatement.bindLong(7, shoppingCartDO.DiscountRefId);
                    insertSqLiteStatement.bindDouble(8, shoppingCartDO.DiscountAmount);
                    insertSqLiteStatement.bindDouble(9, shoppingCartDO.TotalPrice);
                    insertSqLiteStatement.bindString(10, shoppingCartDO.ModifiedOn + "");
                    insertSqLiteStatement.bindString(11, shoppingCartDO.UOM+"");
                    insertSqLiteStatement.bindLong(12, shoppingCartDO.ShoppingCartId);
                    insertSqLiteStatement.bindLong(13, shoppingCartDO.VAT);
                    insertSqLiteStatement.bindDouble(14, shoppingCartDO.TotalVAT);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertShoppingCarts() - ended");
            return false;
        }
    }

// ShoppingCartDetails Syncing..
    public boolean inserttblShoppingCartDetails(ArrayList<ShoppingCartDetailsDO> arrShoppingCartDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-inserttblShoppingCartDetails() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblShoppingCartDetail(SessionId,IsGift,IsGiftWrap,Message,GiftCard,GiftCardAmount," +
                    "IsGiftCardBeingUsed,GiftCardUsageStartTime,CouponCode,CouponAmount,IsCouponApplicable,UserMessage,ShoppingCartDetailId) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblShoppingCartDetail set SessionId=?,IsGift=?,IsGiftWrap=?,Message=?,GiftCard=?,GiftCardAmount=?," +
                    "IsGiftCardBeingUsed=?,GiftCardUsageStartTime=?,CouponCode=?,CouponAmount=?,IsCouponApplicable=?,UserMessage =? where ShoppingCartDetailId = ?");

            for(ShoppingCartDetailsDO shoppingCartDO : arrShoppingCartDOs) {
                updateSqLiteStatement.bindString(1, shoppingCartDO.SessionId);
                updateSqLiteStatement.bindString(2, shoppingCartDO.IsGift + "");
                updateSqLiteStatement.bindString(3, shoppingCartDO.IsGiftWrap + "");
                updateSqLiteStatement.bindString(4, shoppingCartDO.Message);
                updateSqLiteStatement.bindString(5, shoppingCartDO.GiftCard);
                updateSqLiteStatement.bindDouble(6, shoppingCartDO.GiftCardAmount);
                updateSqLiteStatement.bindString(7, shoppingCartDO.IsGiftCardBeingUsed + "");
                updateSqLiteStatement.bindString(8, shoppingCartDO.GiftCardUsageStartTime + "");
                updateSqLiteStatement.bindString(9, shoppingCartDO.CouponCode);
                updateSqLiteStatement.bindDouble(10, shoppingCartDO.CouponAmount);
                updateSqLiteStatement.bindString(11, shoppingCartDO.IsCouponApplicable + "");
                updateSqLiteStatement.bindString(12, shoppingCartDO.UserMessage);
                updateSqLiteStatement.bindLong(13, shoppingCartDO.ShoppingCartDetailId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, shoppingCartDO.SessionId);
                    insertSqLiteStatement.bindString(2, shoppingCartDO.IsGift + "");
                    insertSqLiteStatement.bindString(3, shoppingCartDO.IsGiftWrap + "");
                    insertSqLiteStatement.bindString(4, shoppingCartDO.Message);
                    insertSqLiteStatement.bindString(5, shoppingCartDO.GiftCard);
                    insertSqLiteStatement.bindDouble(6, shoppingCartDO.GiftCardAmount);
                    insertSqLiteStatement.bindString(7, shoppingCartDO.IsGiftCardBeingUsed + "");
                    insertSqLiteStatement.bindString(8, shoppingCartDO.GiftCardUsageStartTime + "");
                    insertSqLiteStatement.bindString(9, shoppingCartDO.CouponCode);
                    insertSqLiteStatement.bindDouble(10, shoppingCartDO.CouponAmount);
                    insertSqLiteStatement.bindString(11, shoppingCartDO.IsCouponApplicable + "");
                    insertSqLiteStatement.bindString(12, shoppingCartDO.UserMessage);
                    insertSqLiteStatement.bindLong(13, shoppingCartDO.ShoppingCartDetailId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-inserttblShoppingCartDetails() - ended");
            return false;
        }
    }

    // ShoppingCartAddress Syncing..
    public boolean insertShoppingCartAddress(ArrayList<ShoppingCartAddressDO> arrShoppingCartDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-inserttblShoppingCartDetails() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblShoppingCartAddress(AddressType,AddressBookId,SessionId,FirstName,MiddleName,LastName," +
                    "AddressLine1,AddressLine2,AddressLine3,City,State,Country,ZipCode,Phone,Email,MobileNumber,ShoppingCartAddressId) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblShoppingCartAddress set AddressType=?,AddressBookId=?,SessionId=?,FirstName=?,MiddleName=?,LastName=?," +
                    "AddressLine1=?,AddressLine2=?,AddressLine3=?,City=?,State=?,Country=?,ZipCode=?,Phone=?,Email=?,MobileNumber =? where ShoppingCartAddressId = ?");

            for(ShoppingCartAddressDO shoppingCartDO : arrShoppingCartDOs) {
                updateSqLiteStatement.bindString(1, shoppingCartDO.AddressType);
                updateSqLiteStatement.bindLong(2, shoppingCartDO.AddressBookId);
                updateSqLiteStatement.bindString(3, shoppingCartDO.SessionId + "");
                updateSqLiteStatement.bindString(4, shoppingCartDO.FirstName);
                updateSqLiteStatement.bindString(5, shoppingCartDO.MiddleName);
                updateSqLiteStatement.bindString(6, shoppingCartDO.LastName);
                updateSqLiteStatement.bindString(7, shoppingCartDO.AddressLine1 );
                updateSqLiteStatement.bindString(8, shoppingCartDO.AddressLine2 );
                updateSqLiteStatement.bindString(9, shoppingCartDO.AddressLine3);
                updateSqLiteStatement.bindString(10, shoppingCartDO.City);
                updateSqLiteStatement.bindString(11, shoppingCartDO.State);
                updateSqLiteStatement.bindString(12, shoppingCartDO.Country);
                updateSqLiteStatement.bindString(13, shoppingCartDO.ZipCode);
                updateSqLiteStatement.bindString(14, shoppingCartDO.Phone);
                updateSqLiteStatement.bindString(15, shoppingCartDO.Email);
                updateSqLiteStatement.bindString(16, shoppingCartDO.MobileNumber);
                updateSqLiteStatement.bindLong(17, shoppingCartDO.ShoppingCartAddressId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, shoppingCartDO.AddressType);
                    insertSqLiteStatement.bindLong(2, shoppingCartDO.AddressBookId);
                    insertSqLiteStatement.bindString(3, shoppingCartDO.SessionId + "");
                    insertSqLiteStatement.bindString(4, shoppingCartDO.FirstName);
                    insertSqLiteStatement.bindString(5, shoppingCartDO.MiddleName);
                    insertSqLiteStatement.bindString(6, shoppingCartDO.LastName);
                    insertSqLiteStatement.bindString(7, shoppingCartDO.AddressLine1 );
                    insertSqLiteStatement.bindString(8, shoppingCartDO.AddressLine2 );
                    insertSqLiteStatement.bindString(9, shoppingCartDO.AddressLine3);
                    insertSqLiteStatement.bindString(10, shoppingCartDO.City);
                    insertSqLiteStatement.bindString(11, shoppingCartDO.State);
                    insertSqLiteStatement.bindString(12, shoppingCartDO.Country);
                    insertSqLiteStatement.bindString(13, shoppingCartDO.ZipCode);
                    insertSqLiteStatement.bindString(14, shoppingCartDO.Phone);
                    insertSqLiteStatement.bindString(15, shoppingCartDO.Email);
                    insertSqLiteStatement.bindString(16, shoppingCartDO.MobileNumber);
                    insertSqLiteStatement.bindLong(17, shoppingCartDO.ShoppingCartAddressId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-inserttblShoppingCartDetails() - ended");
            return false;
        }
    }

    // TrxHeader Syncing..
    public boolean insertTrxHeaders(ArrayList<TrxHeaderDO> arrTrxHeaderDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertTrxHeaders() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxHeader(TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus," +
                    "ProductSubTotalWDiscount,TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message,GiftCardUsed,GiftCardDiscount" +
                    ",PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode,CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId," +
                    "ModifiedOn,CancelledBy,DeviceType,DeviceId,ReasonForCancellation,CancellationReasonCategory,SpecialInstructions,OrderName,IsRecurring,DeliveryDate,TrxId,TotalVAT) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxHeader set TrxCode=?,RefCode=?,TrxDate=?,TrxType=?,UserId=?,TrxStatus=?," +
                    "ProductSubTotalWDiscount=?,TotalAmountWODiscount=?,DiscountAmount=?,ShippingCharges=?,TotalAmount=?,IsCOD=?,IsGift=?,IsGiftWrap=?,Message=?," +
                    "GiftCardUsed=?,GiftCardDiscount=?" +
                    ",PromotionId=?,PromotionDiscount=?,IsCouponCodeApplicable=?,CouponCode=?,CouponDiscount=?,Is_Canceled=?,UserMessage=?,ShippingStatus=?,DocketNo=?,WalletAmount=?," +
                    "SessionId=?,ModifiedOn=?,CancelledBy=?,DeviceType=?,DeviceId=?,ReasonForCancellation=?,CancellationReasonCategory=?,SpecialInstructions=?," +
                    "OrderName=?,IsRecurring=?,DeliveryDate =? ,  TotalVAT=? where TrxId = ?");

            for(TrxHeaderDO trxHeaderDO : arrTrxHeaderDOs) {
                updateSqLiteStatement.bindString(1, trxHeaderDO.trxCode);
                updateSqLiteStatement.bindString(2, trxHeaderDO.trxRefCode);
                updateSqLiteStatement.bindString(3, trxHeaderDO.trxDate + "");
                updateSqLiteStatement.bindString(4, trxHeaderDO.trxType+ "");
                updateSqLiteStatement.bindLong(5, trxHeaderDO.userId);
                updateSqLiteStatement.bindLong(6, trxHeaderDO.trxStatus);
                updateSqLiteStatement.bindDouble(7, trxHeaderDO.productSubTotalWDiscount);
                updateSqLiteStatement.bindDouble(8, trxHeaderDO.totalAmountWODiscount);
                updateSqLiteStatement.bindDouble(9, trxHeaderDO.discountAmt);
                updateSqLiteStatement.bindDouble(10, trxHeaderDO.shippingCharges);
                updateSqLiteStatement.bindDouble(11, trxHeaderDO.totalTrxAmount);
                updateSqLiteStatement.bindString(12, trxHeaderDO.isCOD + "");
                updateSqLiteStatement.bindString(13, trxHeaderDO.isGiftCard + "");
                updateSqLiteStatement.bindString(14, trxHeaderDO.isGiftWrap + "");
                updateSqLiteStatement.bindString(15, trxHeaderDO.message + "");
                updateSqLiteStatement.bindString(16, trxHeaderDO.giftCardUsed + "");
                updateSqLiteStatement.bindDouble(17, trxHeaderDO.giftCardAmount);
                updateSqLiteStatement.bindLong(18, trxHeaderDO.promotionId);
                updateSqLiteStatement.bindDouble(19, trxHeaderDO.promotionDiscount);
                updateSqLiteStatement.bindString(20, trxHeaderDO.isCouponCodeApplicable + "");
                updateSqLiteStatement.bindString(21, trxHeaderDO.couponCode + "");
                updateSqLiteStatement.bindDouble(22, trxHeaderDO.couponAmount);
                updateSqLiteStatement.bindString(23, trxHeaderDO.isCancelled + "");
                updateSqLiteStatement.bindString(24, trxHeaderDO.userMessage + "");
                updateSqLiteStatement.bindString(25, trxHeaderDO.shippingStatus);
                updateSqLiteStatement.bindString(26, trxHeaderDO.docketNumber);
                updateSqLiteStatement.bindDouble(27, trxHeaderDO.walletAmount);
                updateSqLiteStatement.bindString(28, trxHeaderDO.sessionId);
                updateSqLiteStatement.bindString(29, trxHeaderDO.ModifiedDate);
                updateSqLiteStatement.bindString(30, trxHeaderDO.CancelledBy);
                updateSqLiteStatement.bindString(31, trxHeaderDO.DeviceType);
                updateSqLiteStatement.bindString(32, trxHeaderDO.DeviceId);
                updateSqLiteStatement.bindString(33, trxHeaderDO.ReasonForCancellation);
                updateSqLiteStatement.bindString(34, trxHeaderDO.CancellationReasonCategory);
                updateSqLiteStatement.bindString(35, trxHeaderDO.SpecialInstructions);
                updateSqLiteStatement.bindString(36, trxHeaderDO.orderName);
                updateSqLiteStatement.bindString(37, trxHeaderDO.isRecurring+"");
                updateSqLiteStatement.bindString(38, trxHeaderDO.deliveryDate);
                updateSqLiteStatement.bindDouble(39, trxHeaderDO.TotalVAT);
                updateSqLiteStatement.bindLong(40, trxHeaderDO.trxId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, trxHeaderDO.trxCode);
                    insertSqLiteStatement.bindString(2, trxHeaderDO.trxRefCode);
                    insertSqLiteStatement.bindString(3, trxHeaderDO.trxDate + "");
                    insertSqLiteStatement.bindString(4, trxHeaderDO.trxType+ "");
                    insertSqLiteStatement.bindLong(5, trxHeaderDO.userId);
                    insertSqLiteStatement.bindLong(6, trxHeaderDO.trxStatus);
                    insertSqLiteStatement.bindDouble(7, trxHeaderDO.productSubTotalWDiscount);
                    insertSqLiteStatement.bindDouble(8, trxHeaderDO.totalAmountWODiscount);
                    insertSqLiteStatement.bindDouble(9, trxHeaderDO.discountAmt);
                    insertSqLiteStatement.bindDouble(10, trxHeaderDO.shippingCharges);
                    insertSqLiteStatement.bindDouble(11, trxHeaderDO.totalTrxAmount);
                    insertSqLiteStatement.bindString(12, trxHeaderDO.isCOD+"");
                    insertSqLiteStatement.bindString(13, trxHeaderDO.isGiftCard+"");
                    insertSqLiteStatement.bindString(14, trxHeaderDO.isGiftWrap+"");
                    insertSqLiteStatement.bindString(15, trxHeaderDO.message + "");
                    insertSqLiteStatement.bindString(16, trxHeaderDO.giftCardUsed + "");
                    insertSqLiteStatement.bindDouble(17, trxHeaderDO.giftCardAmount);
                    insertSqLiteStatement.bindLong(18, trxHeaderDO.promotionId);
                    insertSqLiteStatement.bindDouble(19, trxHeaderDO.promotionDiscount);
                    insertSqLiteStatement.bindString(20, trxHeaderDO.isCouponCodeApplicable + "");
                    insertSqLiteStatement.bindString(21, trxHeaderDO.couponCode + "");
                    insertSqLiteStatement.bindDouble(22, trxHeaderDO.couponAmount);
                    insertSqLiteStatement.bindString(23, trxHeaderDO.isCancelled+"");
                    insertSqLiteStatement.bindString(24, trxHeaderDO.userMessage+"");
                    insertSqLiteStatement.bindString(25, trxHeaderDO.shippingStatus);
                    insertSqLiteStatement.bindString(26, trxHeaderDO.docketNumber);
                    insertSqLiteStatement.bindDouble(27, trxHeaderDO.walletAmount);
                    insertSqLiteStatement.bindString(28, trxHeaderDO.sessionId);
                    insertSqLiteStatement.bindString(29, trxHeaderDO.ModifiedDate);
                    insertSqLiteStatement.bindString(30, trxHeaderDO.CancelledBy);
                    insertSqLiteStatement.bindString(31, trxHeaderDO.DeviceType);
                    insertSqLiteStatement.bindString(32, trxHeaderDO.DeviceId);
                    insertSqLiteStatement.bindString(33, trxHeaderDO.ReasonForCancellation);
                    insertSqLiteStatement.bindString(34, trxHeaderDO.CancellationReasonCategory);
                    insertSqLiteStatement.bindString(35, trxHeaderDO.SpecialInstructions);
                    insertSqLiteStatement.bindString(36, trxHeaderDO.orderName);
                    insertSqLiteStatement.bindString(37, trxHeaderDO.isRecurring + "");
                    insertSqLiteStatement.bindString(38, trxHeaderDO.deliveryDate);
                    insertSqLiteStatement.bindLong(39, trxHeaderDO.trxId);
                    insertSqLiteStatement.bindDouble(40, trxHeaderDO.TotalVAT);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertTrxHeaders() - ended");
            return false;
        }
    }

    // TrxDetail Syncing..
    public boolean insertTrxDetails(ArrayList<TrxDetailsDO> arrTrxHeaderDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertTrxHeaders() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxDetail(TrxCode,ProductId,Sequence,Quantity,UnitPrice,TotalAmountWODiscount," +
                    "DiscountAmount,DiscountRefId,Reference,TotalAmount,InitialQuantity,UOM,TrxDetailId, VAT,TotalVAT) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxDetail set TrxCode=?,ProductId=?,Sequence=?,Quantity=?,UnitPrice=?,TotalAmountWODiscount=?," +
                    "DiscountAmount=?,DiscountRefId=?,Reference=?,TotalAmount=?,InitialQuantity=?,UOM=? ,  VAT=?, TotalVAT=? where TrxDetailId = ?");

            for(TrxDetailsDO trxHeaderDO : arrTrxHeaderDOs) {
                updateSqLiteStatement.bindString(1, trxHeaderDO.trxCode);
                updateSqLiteStatement.bindLong(2, trxHeaderDO.productId);
                updateSqLiteStatement.bindLong(3, trxHeaderDO.sequence);
                updateSqLiteStatement.bindDouble(4, trxHeaderDO.quantity);
                updateSqLiteStatement.bindDouble(5, trxHeaderDO.unitPrice);
                updateSqLiteStatement.bindDouble(6, trxHeaderDO.totalAmountWODiscount);
                updateSqLiteStatement.bindDouble(7, trxHeaderDO.discountAmount);
                updateSqLiteStatement.bindLong(8, trxHeaderDO.discountRefId);
                updateSqLiteStatement.bindString(9, trxHeaderDO.reference + "");
                updateSqLiteStatement.bindDouble(10, trxHeaderDO.orderAmount);
                updateSqLiteStatement.bindLong(11, trxHeaderDO.initialQuantity);
                updateSqLiteStatement.bindString(12, trxHeaderDO.UOM + "");
                updateSqLiteStatement.bindString(13, trxHeaderDO.VAT + "");
                updateSqLiteStatement.bindString(14, trxHeaderDO.TotalVAT + "");
                updateSqLiteStatement.bindLong(15, trxHeaderDO.trxDetailId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, trxHeaderDO.trxCode);
                    insertSqLiteStatement.bindLong(2, trxHeaderDO.productId);
                    insertSqLiteStatement.bindLong(3, trxHeaderDO.sequence);
                    insertSqLiteStatement.bindDouble(4, trxHeaderDO.quantity);
                    insertSqLiteStatement.bindDouble(5, trxHeaderDO.unitPrice);
                    insertSqLiteStatement.bindDouble(6, trxHeaderDO.totalAmountWODiscount);
                    insertSqLiteStatement.bindDouble(7, trxHeaderDO.discountAmount);
                    insertSqLiteStatement.bindLong(8, trxHeaderDO.discountRefId);
                    insertSqLiteStatement.bindString(9, trxHeaderDO.reference + "");
                    insertSqLiteStatement.bindDouble(10, trxHeaderDO.orderAmount);
                    insertSqLiteStatement.bindLong(11, trxHeaderDO.initialQuantity);
                    insertSqLiteStatement.bindString(12, trxHeaderDO.UOM + "");
                    insertSqLiteStatement.bindLong(13, trxHeaderDO.trxDetailId);
                    insertSqLiteStatement.bindLong(14, trxHeaderDO.VAT);
                    insertSqLiteStatement.bindDouble(15, trxHeaderDO.TotalVAT);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertTrxHeaders() - ended");
            return false;
        }
    }

    // TrxAddress Syncing..
    public boolean insertTrxAddress(ArrayList<TrxAddressDO> arrTrxHeaderDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertTrxDetails() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxAddress(TrxCode,AddressType,AddressBookId,SessionId,FirstName,MiddleName," +
                    "LastName,AddressLine1,AddressLine2,AddressLine3,City,State,Country,ZipCode,Phone,Email,MobileNumber,TrxAddressId,AddressLine4,FlatNumber,Street,AddressLine1_Arabic,AddressLine2_Arabic,AddressLine4_Arabic) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxAddress set TrxCode=?,AddressType=?,AddressBookId=?,SessionId=?,FirstName=?,MiddleName=?," +
                    "LastName=?,AddressLine1=?,AddressLine2=?,AddressLine3=?,City=?,State=?,Country=?,ZipCode=?,Phone=?,Email=?,MobileNumber =?,AddressLine4=?,FlatNumber=?,Street=?,AddressLine1_Arabic=?,AddressLine2_Arabic=?,AddressLine4_Arabic=? where TrxAddressId = ?");

            for(TrxAddressDO trxHeaderDO : arrTrxHeaderDOs) {
                updateSqLiteStatement.bindString(1, trxHeaderDO.TrxCode);
                updateSqLiteStatement.bindString(2, trxHeaderDO.AddressType);
                updateSqLiteStatement.bindLong(3, trxHeaderDO.AddressBookId);
                updateSqLiteStatement.bindString(4, trxHeaderDO.SessionId);
                updateSqLiteStatement.bindString(5, trxHeaderDO.FirstName);
                updateSqLiteStatement.bindString(6, trxHeaderDO.MiddleName);
                updateSqLiteStatement.bindString(7, trxHeaderDO.LastName);
                updateSqLiteStatement.bindString(8, trxHeaderDO.AddressLine1);
                updateSqLiteStatement.bindString(9, trxHeaderDO.AddressLine2);
                updateSqLiteStatement.bindString(10, trxHeaderDO.AddressLine3);
                updateSqLiteStatement.bindString(11, trxHeaderDO.City);
                updateSqLiteStatement.bindString(12, trxHeaderDO.State);
                updateSqLiteStatement.bindString(13, trxHeaderDO.Country);
                updateSqLiteStatement.bindString(14, trxHeaderDO.ZipCode);
                updateSqLiteStatement.bindString(15, trxHeaderDO.Phone);
                updateSqLiteStatement.bindString(16, trxHeaderDO.Email);
                updateSqLiteStatement.bindString(17, trxHeaderDO.MobileNumber);
                updateSqLiteStatement.bindString(18, trxHeaderDO.AddressLine4);
                updateSqLiteStatement.bindString(19, trxHeaderDO.FlatNumber);
                updateSqLiteStatement.bindString(20, trxHeaderDO.Street);
                updateSqLiteStatement.bindString(21, trxHeaderDO.AddressLine1_Arabic);
                updateSqLiteStatement.bindString(22, trxHeaderDO.AddressLine2_Arabic);
                updateSqLiteStatement.bindString(23, trxHeaderDO.AddressLine4_Arabic);
                updateSqLiteStatement.bindLong(24, trxHeaderDO.TrxAddressId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, trxHeaderDO.TrxCode);
                    insertSqLiteStatement.bindString(2, trxHeaderDO.AddressType);
                    insertSqLiteStatement.bindLong(3, trxHeaderDO.AddressBookId);
                    insertSqLiteStatement.bindString(4, trxHeaderDO.SessionId);
                    insertSqLiteStatement.bindString(5, trxHeaderDO.FirstName);
                    insertSqLiteStatement.bindString(6, trxHeaderDO.MiddleName);
                    insertSqLiteStatement.bindString(7, trxHeaderDO.LastName);
                    insertSqLiteStatement.bindString(8, trxHeaderDO.AddressLine1);
                    insertSqLiteStatement.bindString(9, trxHeaderDO.AddressLine2);
                    insertSqLiteStatement.bindString(10, trxHeaderDO.AddressLine3);
                    insertSqLiteStatement.bindString(11, trxHeaderDO.City);
                    insertSqLiteStatement.bindString(12, trxHeaderDO.State);
                    insertSqLiteStatement.bindString(13, trxHeaderDO.Country);
                    insertSqLiteStatement.bindString(14, trxHeaderDO.ZipCode);
                    insertSqLiteStatement.bindString(15, trxHeaderDO.Phone);
                    insertSqLiteStatement.bindString(16, trxHeaderDO.Email);
                    insertSqLiteStatement.bindString(17, trxHeaderDO.MobileNumber);
                    insertSqLiteStatement.bindLong(18, trxHeaderDO.TrxAddressId);
                    insertSqLiteStatement.bindString(19, trxHeaderDO.AddressLine4);
                    insertSqLiteStatement.bindString(20, trxHeaderDO.FlatNumber);
                    insertSqLiteStatement.bindString(21, trxHeaderDO.Street);
                    insertSqLiteStatement.bindString(22, trxHeaderDO.AddressLine1_Arabic);
                    insertSqLiteStatement.bindString(23, trxHeaderDO.AddressLine2_Arabic);
                    insertSqLiteStatement.bindString(24, trxHeaderDO.AddressLine4_Arabic);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertTrxDetails() - ended");
            return false;
        }
    }

    // tblCategory Syncing..
    public boolean insertCategory(ArrayList<CategoryDO> arrCategoryDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertTrxDetails() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblCategory(Code,Name,ParentId,Level,URLName,Sequence," +
                    "IsActive,CreatedBy,CreatedOn,ModifiedBy,ModifiedOn,Timestamp,ShowInMenu,CategoryId) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblCategory set Code=?,Name=?,ParentId=?,Level=?,URLName=?,Sequence=?," +
                    "IsActive=?,CreatedBy=?,CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=?,ShowInMenu=? where CategoryId = ?");

            for(CategoryDO categoryDO : arrCategoryDOs) {
                updateSqLiteStatement.bindString(1, categoryDO.code);
                updateSqLiteStatement.bindString(2, categoryDO.name);
                updateSqLiteStatement.bindLong(3, categoryDO.ParentId);
                updateSqLiteStatement.bindLong(4, categoryDO.Level);
                updateSqLiteStatement.bindString(5, categoryDO.URLName);
                updateSqLiteStatement.bindLong(6, categoryDO.Sequence);
                updateSqLiteStatement.bindString(7, categoryDO.IsActive+"");
                updateSqLiteStatement.bindLong(8, categoryDO.CreatedBy);
                updateSqLiteStatement.bindString(9, categoryDO.CreatedOn);
                updateSqLiteStatement.bindLong(10, categoryDO.ModifiedBy);
                updateSqLiteStatement.bindString(11, categoryDO.ModifiedOn);
                updateSqLiteStatement.bindLong(12, categoryDO.Timestamp);
                updateSqLiteStatement.bindString(13, categoryDO.ShowInMenu + "");
                updateSqLiteStatement.bindLong(14, categoryDO.categoryId);

                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, categoryDO.code);
                    insertSqLiteStatement.bindString(2, categoryDO.name);
                    insertSqLiteStatement.bindLong(3, categoryDO.ParentId);
                    insertSqLiteStatement.bindLong(4, categoryDO.Level);
                    insertSqLiteStatement.bindString(5, categoryDO.URLName);
                    insertSqLiteStatement.bindLong(6, categoryDO.Sequence);
                    insertSqLiteStatement.bindString(7, categoryDO.IsActive+"");
                    insertSqLiteStatement.bindLong(8, categoryDO.CreatedBy);
                    insertSqLiteStatement.bindString(9, categoryDO.CreatedOn);
                    insertSqLiteStatement.bindLong(10, categoryDO.ModifiedBy);
                    insertSqLiteStatement.bindString(11, categoryDO.ModifiedOn);
                    insertSqLiteStatement.bindLong(12, categoryDO.Timestamp);
                    insertSqLiteStatement.bindString(13, categoryDO.ShowInMenu+"");
                    insertSqLiteStatement.bindLong(14, categoryDO.categoryId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertTrxDetails() - ended");
            return false;
        }
    }

    // ProductCategory Syncing..
    public boolean insertProductCategory(ArrayList<ProductCategoryDO> arrProductDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"ProductDOs-insertProductCategory() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{

            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblProductCategory(ProductId,CategoryId,CreatedBy," +
                    "CreatedOn,ModifiedBy,ModifiedOn,Timestamp,ProductCategoryId) values(?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblProductCategory set ProductId=?,CategoryId=?,CreatedBy=?," +
                    "CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=? where ProductCategoryId = ?");

            for(ProductCategoryDO prodCatDO : arrProductDOs) {
                updateSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                updateSqLiteStatement.bindLong(2, prodCatDO.CategoryId);
                updateSqLiteStatement.bindLong(3, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(4, prodCatDO.CreatedOn);
                updateSqLiteStatement.bindLong(5, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindString(6, prodCatDO.ModifiedOn);
                updateSqLiteStatement.bindLong(7, prodCatDO.Timestamp);
                updateSqLiteStatement.bindLong(8, prodCatDO.ProductCategoryId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                    insertSqLiteStatement.bindLong(2, prodCatDO.CategoryId);
                    insertSqLiteStatement.bindLong(3, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(4, prodCatDO.CreatedOn);
                    insertSqLiteStatement.bindLong(5, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindString(6, prodCatDO.ModifiedOn);
                    insertSqLiteStatement.bindLong(7, prodCatDO.Timestamp);
                    insertSqLiteStatement.bindLong(8, prodCatDO.ProductCategoryId);
                    insertSqLiteStatement.executeInsert();
                }

            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertProducts() - ended");
            return false;
        }
    }

    // ProductAttribute Syncing..
    public boolean insertProductAttributes(ArrayList<ProductAttribute> arrProductAttributes){
        LogUtils.debug(LogUtils.LOG_TAG,"insertProductAttributes() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblProductAttribute(ProductId,Name," +
                    "Icon,Description,CreatedBy,CreatedOn,ModifiedBy,ModifiedOn,Timestamp,ProductAttributeId) values(?,?,?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblProductAttribute set ProductId=?,Name=?, Icon=?,Description=?,CreatedBy=?," +
                    "CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=? where ProductAttributeId = ?");
            for(ProductAttribute prodCatDO : arrProductAttributes) {
                updateSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                updateSqLiteStatement.bindString(2, prodCatDO.Name);
                updateSqLiteStatement.bindString(3, prodCatDO.Icon);
                updateSqLiteStatement.bindString(4, prodCatDO.Description);
                updateSqLiteStatement.bindString(5, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(6, prodCatDO.CreatedOn);
                updateSqLiteStatement.bindString(7, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindString(8, prodCatDO.ModifiedOn);
                updateSqLiteStatement.bindString(9, prodCatDO.Timestamp);
                updateSqLiteStatement.bindLong(10, prodCatDO.ProductAttributeId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                    insertSqLiteStatement.bindString(2, prodCatDO.Name);
                    insertSqLiteStatement.bindString(3, prodCatDO.Icon);
                    insertSqLiteStatement.bindString(4, prodCatDO.Description);
                    insertSqLiteStatement.bindString(5, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(6, prodCatDO.CreatedOn);
                    insertSqLiteStatement.bindString(7, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindString(8, prodCatDO.ModifiedOn);
                    insertSqLiteStatement.bindString(9, prodCatDO.Timestamp);
                    insertSqLiteStatement.bindLong(10, prodCatDO.ProductAttributeId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertProducts() - ended");
            return false;
        }
    }

    // ProductAttributeDetails Syncing..
    public boolean insertProductAttributeDetails(ArrayList<ProductAttributeDetailsDO> arrProductAttributes){
        LogUtils.debug(LogUtils.LOG_TAG,"insertProductAttributes() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblProductAttributeDetail(ProductAttributeId,Description," +
                    "CreatedBy,CreatedOn,ModifiedBy,ModifiedOn,Timestamp,ProductAttributeDetailId) values(?,?,?,?,?,?,?,?)");

            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblProductAttributeDetail set ProductAttributeId=?,Description=?,CreatedBy=?,CreatedOn=?,ModifiedBy=?,ModifiedOn=?,Timestamp=? where ProductAttributeDetailId = ?");
            for(ProductAttributeDetailsDO prodCatDO : arrProductAttributes) {
                updateSqLiteStatement.bindLong(1, prodCatDO.ProductAttributeId);
                updateSqLiteStatement.bindString(2, prodCatDO.Description);
                updateSqLiteStatement.bindLong(3, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(4, prodCatDO.CreatedOn);
                updateSqLiteStatement.bindLong(5, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindString(6, prodCatDO.ModifiedOn);
                updateSqLiteStatement.bindString(7, prodCatDO.Timestamp);
                updateSqLiteStatement.bindLong(8, prodCatDO.ProductAttributeDetailId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.ProductAttributeId);
                    insertSqLiteStatement.bindString(2, prodCatDO.Description);
                    insertSqLiteStatement.bindLong(3, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(4, prodCatDO.CreatedOn);
                    insertSqLiteStatement.bindLong(5, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindString(6, prodCatDO.ModifiedOn);
                    insertSqLiteStatement.bindString(7, prodCatDO.Timestamp);
                    insertSqLiteStatement.bindLong(8, prodCatDO.ProductAttributeDetailId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"Products-insertProducts() - ended");
            return false;
        }
    }

    // OrderNumber Syncing..
    public boolean insertOrderNumbers(ArrayList<OrderNumberDO> arrOrderNumbers){
        LogUtils.debug(LogUtils.LOG_TAG,"insertOrderNumbers() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblOrderNumber(OrderNumber,TrxId," +
                    "CreatedDate,Id) values(?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblOrderNumber set OrderNumber=?,TrxId=?,CreatedDate=? where Id = ?");
            for(OrderNumberDO prodCatDO : arrOrderNumbers) {
                updateSqLiteStatement.bindString(1, prodCatDO.OrderNumber);
                updateSqLiteStatement.bindLong(2, prodCatDO.TrxId);
                updateSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                updateSqLiteStatement.bindLong(4, prodCatDO.Id);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindString(1, prodCatDO.OrderNumber);
                    insertSqLiteStatement.bindLong(2, prodCatDO.TrxId);
                    insertSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                    insertSqLiteStatement.bindLong(4, prodCatDO.Id);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
            /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertOrderNumbers() - ended");
        }
        return false;
    }

    // AreaandSubArea Syncing..
    public boolean insertAreaandSubAreas(ArrayList<AreaDO> arrAreas){
        LogUtils.debug(LogUtils.LOG_TAG,"insertAreaandSubAreas() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblAreaandSubArea(EmiratesId,ParentId,Name," +
                    "Arabic_Name,AreaId) values(?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblAreaandSubArea set EmiratesId=?,ParentId=?,Name=?," +
                    "Arabic_Name=? where AreaId = ?");
            for(AreaDO prodCatDO : arrAreas) {
                updateSqLiteStatement.bindLong(1, prodCatDO.EmiratesId);
                updateSqLiteStatement.bindLong(2, prodCatDO.ParentId);
                updateSqLiteStatement.bindString(3, prodCatDO.Name);
                updateSqLiteStatement.bindString(4, prodCatDO.ArabicName);
                updateSqLiteStatement.bindLong(5, prodCatDO.AreaId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.EmiratesId);
                    insertSqLiteStatement.bindLong(2, prodCatDO.ParentId);
                    insertSqLiteStatement.bindString(3, prodCatDO.Name);
                    insertSqLiteStatement.bindString(4, prodCatDO.ArabicName);
                    insertSqLiteStatement.bindLong(5, prodCatDO.AreaId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertAreaandSubAreas() - ended");
            return false;
        }
    }

    // AreaDeliveryDetail Syncing..
    public boolean insertAreaDeliveryDetails(ArrayList<DeliveryDay> arrDeliveryDays){
        LogUtils.debug(LogUtils.LOG_TAG,"insertAreaDeliveryDetails() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblAreaDeliveryDetail(AreaId,DeliveryDay," +
                    "RouteId,SupervisorId,AreaDeliveryDetailId) values(?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblAreaDeliveryDetail set AreaId=?, DeliveryDay=?,RouteId=?,SupervisorId=? " +
                    "where AreaDeliveryDetailId = ?");
            for(DeliveryDay prodCatDO : arrDeliveryDays) {
                updateSqLiteStatement.bindLong(1, prodCatDO.AreaId);
                updateSqLiteStatement.bindString(2, prodCatDO.DeliveryDay);
                updateSqLiteStatement.bindString(3, prodCatDO.Route);
                updateSqLiteStatement.bindLong(4, prodCatDO.AreaRouteSupervisorId);
                updateSqLiteStatement.bindLong(5, prodCatDO.AreaDeliveryDetailId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.AreaId);
                    insertSqLiteStatement.bindString(2, prodCatDO.DeliveryDay);
                    insertSqLiteStatement.bindString(3, prodCatDO.Route);
                    insertSqLiteStatement.bindLong(4, prodCatDO.AreaRouteSupervisorId);
                    insertSqLiteStatement.bindLong(5, prodCatDO.AreaDeliveryDetailId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
            /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertAreaDeliveryDetails() - ended");
            return false;
        }
    }


    // tblHydrationMeterSetting..
    public boolean insertHydrationMeterSettings(ArrayList<HydrationMeterSettingDO> hydrationMeterSettingDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertHydrationMeterSettings() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHydrationMeterSetting(CustomerId,Weight,Height,Notification," +
                    "DailyWaterConsumptionTarget,CreatedDate,CreatedBy,ModifiedDate,ModifiedBy,HydrationMeterSettingId) values(?,?,?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHydrationMeterSetting set CustomerId=?,Weight=?,Height=?,Notification=?," +
                    "DailyWaterConsumptionTarget=?,CreatedDate=?,CreatedBy=?,ModifiedDate=?,ModifiedBy=? where HydrationMeterSettingId = ?");
            for(HydrationMeterSettingDO prodCatDO : hydrationMeterSettingDOs) {
                updateSqLiteStatement.bindLong(1, prodCatDO.CustomerId);
                updateSqLiteStatement.bindDouble(2, prodCatDO.Weight);
                updateSqLiteStatement.bindDouble(3, prodCatDO.Height);
                updateSqLiteStatement.bindString(4, prodCatDO.Notification + "");
                updateSqLiteStatement.bindDouble(5, prodCatDO.DailyWaterConsumptionTarget);
                updateSqLiteStatement.bindString(6, prodCatDO.CreatedDate);
                updateSqLiteStatement.bindLong(7, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(8, prodCatDO.ModifiedDate);
                updateSqLiteStatement.bindLong(9, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindLong(10, prodCatDO.HydrationMeterSettingId);

                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.CustomerId);
                    insertSqLiteStatement.bindDouble(2, prodCatDO.Weight);
                    insertSqLiteStatement.bindDouble(3, prodCatDO.Height);
                    insertSqLiteStatement.bindString(4, prodCatDO.Notification + "");
                    insertSqLiteStatement.bindDouble(5, prodCatDO.DailyWaterConsumptionTarget);
                    insertSqLiteStatement.bindString(6, prodCatDO.CreatedDate);
                    insertSqLiteStatement.bindLong(7, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(8, prodCatDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(9, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindLong(10, prodCatDO.HydrationMeterSettingId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertHydrationMeterSettings() - ended");
        }
        return false;
    }

    // tblHydrationMeterReading..
    public boolean insertHydrationMeterReading(ArrayList<HydrationMeterReadingDO> hydrationMeterReadingDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertHydrationMeterReading() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHydrationMeterReading(CustomerId,WaterConsumed," +
                    "ConsumptionDate,Percentage,HydrationMeterReadingId) values(?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHydrationMeterReading set CustomerId=?,WaterConsumed=?," +
                    "ConsumptionDate=?,Percentage=? where HydrationMeterReadingId = ?");
            for(HydrationMeterReadingDO prodCatDO : hydrationMeterReadingDOs) {
                updateSqLiteStatement.bindLong(1, prodCatDO.CustomerId);
                updateSqLiteStatement.bindDouble(2, prodCatDO.WaterConsumed);
                updateSqLiteStatement.bindString(3, prodCatDO.ConsumptionDate);
                updateSqLiteStatement.bindDouble(4, prodCatDO.WaterConsumedPercent);
                updateSqLiteStatement.bindLong(5, prodCatDO.HydrationMeterReadingId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.CustomerId);
                    insertSqLiteStatement.bindDouble(2, prodCatDO.WaterConsumed);
                    insertSqLiteStatement.bindString(3, prodCatDO.ConsumptionDate);
                    insertSqLiteStatement.bindDouble(4, prodCatDO.WaterConsumedPercent);
                    insertSqLiteStatement.bindLong(5, prodCatDO.HydrationMeterReadingId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertHydrationMeterReading() - ended");
            return false;
        }
    }


    // tblRecurringOrder..
    public boolean insertRecurringOrder(ArrayList<RecurringOrderDO> recurringOrderDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertRecurringOrder() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblRecurringOrder(TrxCode,Frequency," +
                    "NumberOfRepeatations,RemovedFromRecurring,RemovedFromRecurringOn,ReasonForRemovalFromRecurring,RecurringOrderId) values(?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblRecurringOrder set TrxCode=?,Frequency=?," +
                    "NumberOfRepeatations=?,RemovedFromRecurring=?, RemovedFromRecurringOn=?,ReasonForRemovalFromRecurring = ? where RecurringOrderId = ?");
            for(RecurringOrderDO prodCatDO : recurringOrderDOs) {
                updateSqLiteStatement.bindString(1, prodCatDO.TrxCode);
                updateSqLiteStatement.bindString(2, prodCatDO.Frequency);
                updateSqLiteStatement.bindLong(3, prodCatDO.NumberOfRepeatations);
                updateSqLiteStatement.bindString(4, prodCatDO.RemovedFromRecurring + "");
                updateSqLiteStatement.bindString(5, prodCatDO.RemovedFromRecurringOn);
                updateSqLiteStatement.bindString(6, StringUtils.isEmpty(prodCatDO.ReasonForRemovalFromRecurring) ? "" : prodCatDO.ReasonForRemovalFromRecurring);
                updateSqLiteStatement.bindLong(7, prodCatDO.RecurringOrderId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindString(1, prodCatDO.TrxCode);
                    insertSqLiteStatement.bindString(2, prodCatDO.Frequency);
                    insertSqLiteStatement.bindLong(3, prodCatDO.NumberOfRepeatations);
                    insertSqLiteStatement.bindString(4, prodCatDO.RemovedFromRecurring + "");
                    insertSqLiteStatement.bindString(5, prodCatDO.RemovedFromRecurringOn);
                    insertSqLiteStatement.bindString(6, prodCatDO.ReasonForRemovalFromRecurring);
                    insertSqLiteStatement.bindLong(7, prodCatDO.RecurringOrderId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertRecurringOrder() - ended");
            return false;
        }
    }


    // tblOrderLimit..
    public boolean insertOrderLimit(ArrayList<ProductOrderLimitDO> productOrderLimitDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertOrderLimit() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblOrderLimit(ProductId,Quantity," +
                    "StartDate,EndDate,OrderLimitId) values(?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblOrderLimit set ProductId=?,Quantity=?," +
                    "StartDate=?,EndDate=? where OrderLimitId = ?");
            for(ProductOrderLimitDO prodCatDO : productOrderLimitDOs) {
                updateSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                updateSqLiteStatement.bindLong(2, prodCatDO.Quantity);
                updateSqLiteStatement.bindString(3, prodCatDO.StartDate);
                updateSqLiteStatement.bindString(4, prodCatDO.EndDate);
                updateSqLiteStatement.bindLong(5, prodCatDO.OrderLimitId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                    insertSqLiteStatement.bindLong(2, prodCatDO.Quantity);
                    insertSqLiteStatement.bindString(3, prodCatDO.StartDate);
                    insertSqLiteStatement.bindString(4, prodCatDO.EndDate);
                    insertSqLiteStatement.bindLong(5, prodCatDO.OrderLimitId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertOrderLimit() - ended");
            return false;
        }
    }
    // tblOrderMinLimit..
    public boolean insertMinOrderLimit(ArrayList<ProductOrderLimitDO> productOrderLimitDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertOrderMinLimit() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblOrderMinLimit(ProductId,Quantity," +
                    "StartDate,EndDate,OrderMinLimitId) values(?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblOrderMinLimit set ProductId=?,Quantity=?," +
                    "StartDate=?,EndDate=? where OrderMinLimitId = ?");
            for(ProductOrderLimitDO prodCatDO : productOrderLimitDOs) {
                updateSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                updateSqLiteStatement.bindLong(2, prodCatDO.Quantity);
                updateSqLiteStatement.bindString(3, prodCatDO.StartDate);
                updateSqLiteStatement.bindString(4, prodCatDO.EndDate);
                updateSqLiteStatement.bindLong(5, prodCatDO.OrderLimitId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.ProductId);
                    insertSqLiteStatement.bindLong(2, prodCatDO.Quantity);
                    insertSqLiteStatement.bindString(3, prodCatDO.StartDate);
                    insertSqLiteStatement.bindString(4, prodCatDO.EndDate);
                    insertSqLiteStatement.bindLong(5, prodCatDO.OrderLimitId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertOrderMinLimit() - ended");
            return false;
        }
    }


    // tblEmirate..
    public boolean insertEmirate(ArrayList<EmirateDO> emirateDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertEmirate() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblEmirates(Name,Arabic_Name," +
                    "Code,EmiratesId) values(?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblEmirates set Name=?,Arabic_Name=?," +
                    "Code=? where EmiratesId = ?");
            for(EmirateDO prodCatDO : emirateDOs) {
                updateSqLiteStatement.bindString(1, prodCatDO.Name);
                updateSqLiteStatement.bindString(2, prodCatDO.ArabicName);
                updateSqLiteStatement.bindString(3, prodCatDO.Code);
                updateSqLiteStatement.bindLong(4, prodCatDO.EmiratesId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindString(1, prodCatDO.Name);
                    insertSqLiteStatement.bindString(2, prodCatDO.ArabicName);
                    insertSqLiteStatement.bindString(3, prodCatDO.Code);
                    insertSqLiteStatement.bindLong(4, prodCatDO.EmiratesId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertEmirate() - ended");
            return false;
        }
    }

    // tblAreaRoute..
    public boolean insertAreaRoute(ArrayList<AreaRoutesDO> AreaRoutesDO){
        LogUtils.debug(LogUtils.LOG_TAG,"inserttblAreaRoute() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblAreaRoute(AreaId,Route," +
                    "CreatedDate,CreatedBy,ModifiedDate,ModifiedBy,AreaRouteId) values(?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblAreaRoute set AreaId=?,Route=?," +
                    "CreatedDate=?,CreatedBy=?,ModifiedDate=?,ModifiedBy=? where AreaRouteId = ?");
            for(AreaRoutesDO prodCatDO : AreaRoutesDO) {
                updateSqLiteStatement.bindLong(1, prodCatDO.AreaId);
                updateSqLiteStatement.bindString(2, prodCatDO.Route);
                updateSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                updateSqLiteStatement.bindLong(4, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(5, prodCatDO.ModifiedDate);
                updateSqLiteStatement.bindLong(6, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindLong(7, prodCatDO.AreaRouteId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.AreaId);
                    insertSqLiteStatement.bindString(2, prodCatDO.Route);
                    insertSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                    insertSqLiteStatement.bindLong(4, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(5, prodCatDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(6, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindLong(7, prodCatDO.AreaRouteId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"inserttblAreaRoute() - ended");
            return false;
        }
    }

    // AreaRouteSupervisors..
    public boolean insertAreaRouteSupervisors(ArrayList<AreaRouteSupervisorsDO> AreaRoutesDO){
        LogUtils.debug(LogUtils.LOG_TAG,"insertAreaRouteSupervisors() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblAreaRouteSupervisor(AreaRouteId,Supervisor," +
                    "CreatedDate,CreatedBy,ModifiedDate,ModifiedBy,AreaRouteSupervisorId) values(?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblAreaRouteSupervisor set AreaRouteId=?,Supervisor=?," +
                    "CreatedDate=?,CreatedBy=?,ModifiedDate=?,ModifiedBy=? where AreaRouteSupervisorId = ?");
            for(AreaRouteSupervisorsDO prodCatDO : AreaRoutesDO) {
                updateSqLiteStatement.bindLong(1, prodCatDO.AreaRouteId);
                updateSqLiteStatement.bindString(2, prodCatDO.Supervisor);
                updateSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                updateSqLiteStatement.bindLong(4, prodCatDO.CreatedBy);
                updateSqLiteStatement.bindString(5, prodCatDO.ModifiedDate);
                updateSqLiteStatement.bindLong(6, prodCatDO.ModifiedBy);
                updateSqLiteStatement.bindLong(7, prodCatDO.AreaRouteSupervisorId);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, prodCatDO.AreaRouteId);
                    insertSqLiteStatement.bindString(2, prodCatDO.Supervisor);
                    insertSqLiteStatement.bindString(3, prodCatDO.CreatedDate);
                    insertSqLiteStatement.bindLong(4, prodCatDO.CreatedBy);
                    insertSqLiteStatement.bindString(5, prodCatDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(6, prodCatDO.ModifiedBy);
                    insertSqLiteStatement.bindLong(7, prodCatDO.AreaRouteSupervisorId);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertAreaRouteSupervisors() - ended");
            return false;
        }
    }


    // LastSyncDateTime Syncing..
    public boolean updateLastSyncDateTime(String synctime){
        LogUtils.debug(LogUtils.LOG_TAG,"updateLastSyncDateTime() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblLastSyncDateTime set LastSyncDateTime=?");
            updateSqLiteStatement.bindString(1, synctime);
            updateSqLiteStatement.execute();
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"updateLastSyncDateTime() - ended");
            return false;
        }
    }


    // Deleting the Records..
    public boolean deleteRecords(ArrayList<DeletedRecordDO> deletedRecordDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"deleteRecords() - started");


            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            try {
                for (int i = 0;i < deletedRecordDOs.size();i++) {
                    DeletedRecordDO dd = deletedRecordDOs.get(i);
                    if(dd.TableName != null && dd.PrimaryKey!=null && dd.PrimaryKeyValue != 0)
                    sqLiteDatabase.execSQL("delete from "+dd.TableName+" where "+dd.PrimaryKey+" = " + dd.PrimaryKeyValue + "");
                }
            }catch (Exception e) {
                 /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
            } finally {
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "deleteRecords() - ended");
                return false;
            }

    }


    // tblOrderCancellationReasonCategory..
    public boolean insertOrderCancelReasons(ArrayList<OrderCancelDO> orderCancelDOs){
        LogUtils.debug(LogUtils.LOG_TAG,"insertEmirate() - started");
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblOrderCancellationReasonCategory(OrderCancellationReasonCategoryId,Category," +
                    "ParentId,CreatedDate,CreatedBy,ModifiedDate,ModifiedBy,AltDescription) values(?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblOrderCancellationReasonCategory set Category=?,ParentId=?," +
                    "CreatedDate=?,CreatedBy=?,ModifiedDate=?,ModifiedBy=?,AltDescription=? where OrderCancellationReasonCategoryId = ?");
            for(OrderCancelDO orderCancelDO : orderCancelDOs) {
                updateSqLiteStatement.bindString(1, orderCancelDO.Category);
                updateSqLiteStatement.bindLong(2, orderCancelDO.ParentId);
                updateSqLiteStatement.bindString(3, orderCancelDO.CreatedDate);
                updateSqLiteStatement.bindLong(4, orderCancelDO.CreatedBy);
                updateSqLiteStatement.bindString(5, orderCancelDO.ModifiedDate);
                updateSqLiteStatement.bindLong(6, orderCancelDO.ModifiedBy);
                updateSqLiteStatement.bindString(7, orderCancelDO.AltDescription);
                updateSqLiteStatement.bindLong(8, orderCancelDO.Id);
                if(updateSqLiteStatement.executeUpdateDelete() <= 0){
                    insertSqLiteStatement.bindLong(1, orderCancelDO.Id);
                    insertSqLiteStatement.bindString(2, orderCancelDO.Category);
                    insertSqLiteStatement.bindLong(3, orderCancelDO.ParentId);
                    insertSqLiteStatement.bindString(4, orderCancelDO.CreatedDate);
                    insertSqLiteStatement.bindLong(5, orderCancelDO.CreatedBy);
                    insertSqLiteStatement.bindString(6, orderCancelDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(7, orderCancelDO.ModifiedBy);
                    insertSqLiteStatement.bindString(8, orderCancelDO.AltDescription);
                    insertSqLiteStatement.executeInsert();
                }
            }
        }catch (Exception e){
             /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"insertEmirate() - ended");
            return false;
        }
    }






    public HashMap<Integer,CategoryDO> getProductsByCategory(String categoryId){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProducts() - started");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            HashMap<Integer,CategoryDO> hmProducts = null;
            try {
                String query = "SELECT Distinct P.ProductId, P.Code, P.Name, P.Description, P.AltDescription, " +
                        "PI.OriginalImage,PI.SmallImage,PI.MediumImage, PI.LargeImage,PR.UnitPrice,U.UOM,C.CategoryId, C.Name " +
                        "FROM tblCategory C Inner Join tblProductCategory PC on C.CategoryId = PC.CategoryId " +
                        "Inner Join tblProduct P on PC.ProductId = P.ProductId " +
                        "Inner Join tblItemUOM U ON P.Code = U.ItemCode " +
                        "Inner Join tblPrice PR ON P.ProductId = PR.ProductId AND U.UOM = PR.UOM " +
                        "Inner Join tblProductImages PI ON P.ProductId = PI.ProductId AND PI.ISDefault = 'True'";

                if(!StringUtils.isEmpty(categoryId))
                query += " and C.CategoryId = '"+categoryId+"'";

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    hmProducts = new HashMap<>();
                    do {
                        ProductDO productDO = new ProductDO();
                        productDO.id = cursor.getString(0);
                        productDO.code = cursor.getString(1);
                        productDO.name = cursor.getString(2);
                        productDO.description = cursor.getString(3);
                        productDO.AltDescription = cursor.getString(4);
                        productDO.gridImage = cursor.getString(6); // 5 original image skipped
                        productDO.listImage = cursor.getString(6);
                        productDO.pagerImage = cursor.getString(6);// medium (7) large(8) image skipped
                        productDO.price = cursor.getFloat(9);
                        productDO.UOM = cursor.getString(10);
                        productDO.categoryId = cursor.getInt(11);
                        CategoryDO categoryDO = hmProducts.get(productDO.categoryId);
                        if(categoryDO == null){
                            categoryDO = new CategoryDO();
                            categoryDO.categoryId = cursor.getInt(11);
                            categoryDO.name = cursor.getString(12);
                            categoryDO.arrProductDOs = new ArrayList<>();
                        }
                        categoryDO.arrProductDOs.add(productDO);
                        hmProducts.put(categoryDO.categoryId, categoryDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                 /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getMedia() - ended");
                return hmProducts;
            }
        }
    }

}
