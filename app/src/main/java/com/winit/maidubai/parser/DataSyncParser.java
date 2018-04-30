package com.winit.maidubai.parser;

import android.content.Context;
import android.util.Log;

import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataaccesslayer.SyncDA;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.AreaRouteSupervisorsDO;
import com.winit.maidubai.dataobject.AreaRoutesDO;
import com.winit.maidubai.dataobject.CategoryDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.DataSyncDO;
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
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.FileUtils;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by sudheer.jampana on 8/29/2016.
 */
public class DataSyncParser extends BaseJsonHandler {

    private DataSyncDO dataSyncDO = new DataSyncDO();
    private Context mContext;
    private SyncDA mSyncDa;

    public  DataSyncParser (Context context){
        this.mContext = context;
        this.mSyncDa = new SyncDA(mContext);
    }

    @Override
    public Object getData() {
        return dataSyncDO;
    }

    @Override
    public void parse(String strResponse) {
        try {
            JSONObject jsonObject = new JSONObject(strResponse);

            JSONArray jsonArray = jsonObject.optJSONArray("ProductList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ProductDO productDO = new ProductDO();
                productDO.ProductId = jsonObj.optInt("ProductId");
                productDO.ParentId = jsonObj.optInt("ParentId");
                productDO.code = jsonObj.optString("Code");
                productDO.name = jsonObj.optString("Name");
                productDO.description = jsonObj.optString("Description");
                productDO.AltDescription = jsonObj.optString("AltDescription");
                productDO.SalesOrgCode = jsonObj.optString("SalesOrgCode");
                productDO.BaseUOM = jsonObj.optString("BaseUOM");
                productDO.ColorId = jsonObj.optInt("ColorId");
                productDO.IsSellable = jsonObj.optBoolean("IsSellable");
                productDO.IsActive = jsonObj.optBoolean("IsActive");
                productDO.IsOutOfStock = jsonObj.optBoolean("IsOutOfStock");
                productDO.URLName = jsonObj.optString("URLName");
                productDO.CreatedBy = jsonObj.optInt("CreatedBy");
                productDO.CreatedOn = jsonObj.optString("CreatedOn");
                productDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                productDO.ModifiedOn = jsonObj.optString("ModifiedOn");
                productDO.Timestamp = jsonObj.optInt("Timestamp");
                productDO.IsInventoryControlled = jsonObj.optBoolean("IsInventoryControlled");
                productDO.Weight = (float) jsonObj.optDouble("Weight");

                productDO.GroupLevel1 = jsonObj.optString("GroupLevel1");
                productDO.GroupLevel2 = jsonObj.optString("GroupLevel2");
                productDO.GroupLevel3 = jsonObj.optString("GroupLevel3");
                productDO.GroupLevel4 = jsonObj.optString("GroupLevel4");
                productDO.GroupLevel5 = jsonObj.optString("GroupLevel5");
                productDO.GroupLevel6 = jsonObj.optString("GroupLevel6");
                productDO.EAUOM = jsonObj.optString("EAUOM");
                productDO.Sequence = jsonObj.optString("Sequence");

                dataSyncDO.productDOs.add(productDO);
                jsonObj = null;
            }

            //*********************************************************************************************
            JSONArray productImgJson = jsonObject.optJSONArray("ProductImages");
            for (int i = 0; i < productImgJson.length(); i++) {
                JSONObject jsonObj = productImgJson.optJSONObject(i);
                ProductImagesDO productImagesDO = new ProductImagesDO();
                productImagesDO.ProductImageId = jsonObj.optInt("ProductImageId");
                productImagesDO.ProductId = jsonObj.optInt("ProductId");
                productImagesDO.productCode = jsonObj.optString("ImageProductCode");
                productImagesDO.OriginalImage = jsonObj.optString("OriginalImage");
                productImagesDO.SmallImage = jsonObj.optString("SmallImage");
                productImagesDO.MediumImage = jsonObj.optString("MediumImage");
                productImagesDO.LargeImage = jsonObj.optString("LargeImage");
                productImagesDO.IsActive = jsonObj.optBoolean("IsActive");
                productImagesDO.IsDefault = jsonObj.optBoolean("IsDefault");
                dataSyncDO.productImagesDOs.add(productImagesDO);
                jsonObj=null;
            }

            JSONArray customerListJson = jsonObject.optJSONArray("CustomerList");
            for (int i = 0; i < customerListJson.length(); i++) {
                JSONObject jsonobj = customerListJson.optJSONObject(i);
                CustomerDO customerDOs = new CustomerDO();
                customerDOs.sessionId = jsonobj.optString("SessionId");
                customerDOs.Id = jsonobj.optInt("Id");
                customerDOs.site = jsonobj.optString("Site");
                customerDOs.siteName = jsonobj.optString("SiteName");
                customerDOs.siteNameInArabic = jsonobj.optString("SiteNameInArabic");
                customerDOs.customerId = jsonobj.optString("CustomerId");
                customerDOs.customerStatus = jsonobj.optString("CustomerStatus");
                customerDOs.custAcctCreationDate = jsonobj.optString("CustAcctCreationDate");
                customerDOs.PartyName=jsonobj.optString("PartyName");
                customerDOs.channelCode = jsonobj.optString("ChannelCode");
                customerDOs.subChannelCode = jsonobj.optString("SubChannelCode");
                customerDOs.regionCode = jsonobj.optString("RegionCode");
                customerDOs.countryCode = jsonobj.optString("CountryCode");
                customerDOs.category = jsonobj.optString("Category");
                customerDOs.address1 = jsonobj.optString("Address1");
                customerDOs.address1_Arabic = jsonobj.optString("Address1_Arabic");
                customerDOs.address2 = jsonobj.optString("Address2");
                customerDOs.address2_Arabic = jsonobj.optString("Address2_Arabic");
                customerDOs.address3 = jsonobj.optString("Address3");
                customerDOs.address4 = jsonobj.optString("Address4");
                customerDOs.address4_Arabic = jsonobj.optString("Address4_Arabic");
                customerDOs.poNumber = jsonobj.optString("PoNumber");
                customerDOs.city = jsonobj.optString("City");
                customerDOs.paymentType = jsonobj.optString("PaymentType");
                customerDOs.paymentTermCode = jsonobj.optString("PaymentTermCode");
                customerDOs.creditLimit = jsonobj.optDouble("CreditLimit");
                customerDOs.GeoCodeX = jsonobj.optString("GeoCodeX");
                customerDOs.GeoCodeY = jsonobj.optString("GeoCodeY");
                customerDOs.passcode = jsonobj.optString("PASSCODE");
                customerDOs.cutomerEmailId = jsonobj.optString("Email");
                customerDOs.contactPersonName = jsonobj.optString("ContactPersonName");
                customerDOs.landlineNumber = jsonobj.optString("PhoneNumber");
                customerDOs.appCustomerId = jsonobj.optString("AppCustomerId");
                customerDOs.mobileNumber = jsonobj.optString("MobileNumber1");
                customerDOs.phoneNumber = jsonobj.optString("MobileNumber2");
                customerDOs.customerType = jsonobj.optString("CustomerType");
                customerDOs.createdBy = jsonobj.optString("CreatedBy");
                customerDOs.modifiedBy = jsonobj.optString("ModifiedBy");
                customerDOs.Source = jsonobj.optString("Source");
                customerDOs.CustomerCategory = jsonobj.optString("CustomerCategory");
                customerDOs.CustomerSubCategory = jsonobj.optString("CustomerSubCategory");
                customerDOs.CustomerGroupCode = jsonobj.optString("CustomerGroupCode");
                customerDOs.ModifiedDate = jsonobj.optInt("ModifiedDate");
                customerDOs.ModifiedTime = jsonobj.optInt("ModifiedTime");
                customerDOs.Description = jsonobj.optString("Description");
                customerDOs.collector = jsonobj.optString("Collector");
                customerDOs.SalesPerson = jsonobj.optString("SalesPerson");
                customerDOs.FaxCountryCode = jsonobj.optString("FaxCountryCode");
                customerDOs.FaxAreaCode = jsonobj.optString("FaxAreaCode");
                customerDOs.FaxPhoneNumber = jsonobj.optString("FaxPhoneNumber");
                customerDOs.PaymentTerms = jsonobj.optString("PaymentTerms");
                customerDOs.PaymentMethod = jsonobj.optString("PaymentMethod");
                customerDOs.CurrencyCode = jsonobj.optString("CurrencyCode");
                customerDOs.BankName = jsonobj.optString("BankName");
                customerDOs.BankBranchName = jsonobj.optString("BankBranchName");
                customerDOs.BankAccountNumber = jsonobj.optString("BankAccountNumber");
                customerDOs.CreditCheckRequired = jsonobj.optString("CreditCheckRequired");
                customerDOs.CreditRating = jsonobj.optString("CreditRating");
                customerDOs.Creditreviewcycle = jsonobj.optString("Creditreviewcycle");
                customerDOs.OrderType = jsonobj.optString("OrderType");
                customerDOs.Warehouse = jsonobj.optString("Warehouse");
                customerDOs.PriceList = jsonobj.optString("PriceList");
                customerDOs.Company = jsonobj.optString("Company");
                customerDOs.Location = jsonobj.optString("Location");
                customerDOs.Division = jsonobj.optString("Division");
                customerDOs.Department = jsonobj.optString("Department");
                customerDOs.Account = jsonobj.optString("Account");
                customerDOs.Intercompany = jsonobj.optString("Intercompany");
                customerDOs.Future = jsonobj.optString("Future");
                customerDOs.OperatingUnit = jsonobj.optString("OperatingUnit");
                customerDOs.EDPCODE = jsonobj.optString("EDPCODE");
                customerDOs.Region = jsonobj.optString("Region");
                customerDOs.country = jsonobj.optString("Country");
                customerDOs.Identifyingaddress = jsonobj.optInt("Identifyingaddress");
                customerDOs.StoreGrowth = jsonobj.optDouble("StoreGrowth");
                customerDOs.CityCode = jsonobj.optString("CityCode");
                customerDOs.PasswordHash = jsonobj.optString("PasswordHash");
                customerDOs.ServerKey = jsonobj.optString("ServerKey");
                customerDOs.ProfileImage = jsonobj.optString("ProfileImage");
                customerDOs.fbToken = jsonobj.optString("FBToken");
                customerDOs.gmailToken = jsonobj.optString("GmailToken");
                customerDOs.twitterToken = jsonobj.optString("TwitterToken");
                customerDOs.dateOfBirth = jsonobj.optString("Birthday");
                customerDOs.anniversary = jsonobj.optString("Anniversary");
                customerDOs.PreferedLanguage = jsonobj.optString("PreferedLanguage");
                customerDOs.Day = jsonobj.optString("Day");
                customerDOs.Route = jsonobj.optString("Route");
                customerDOs.Comments = jsonobj.optString("Comments");
                customerDOs.IsExistingCustomer = jsonobj.optBoolean("IsExistingCustomer");
                customerDOs.PreferedTime = jsonobj.optString("PreferedTime");
                customerDOs.Notification = jsonobj.optBoolean("Notification");
                customerDOs.Source = jsonobj.optString("Source");
                customerDOs.FlatNumber = jsonobj.optString("FlatNumber");
                customerDOs.VillaName = jsonobj.optString("VillaName");
                customerDOs.Street = jsonobj.optString("Street");
                dataSyncDO.customerDOs.add(customerDOs);
                jsonobj=null;
            }

            JSONArray galleryListJson = jsonObject.optJSONArray("GalleryList");
            for (int i = 0; i < galleryListJson.length(); i++) {
                JSONObject jsonobj = galleryListJson.optJSONObject(i);
                GalleryDO galleryDO = new GalleryDO();
                galleryDO.GalleryId = jsonobj.optInt("GalleryId");
                galleryDO.Type = jsonobj.optString("Type");
                galleryDO.Path = jsonobj.optString("Path");
                dataSyncDO.galleryDOs.add(galleryDO);
                jsonobj=null;
            }

            JSONArray help = jsonObject.optJSONArray("HelpCategoryList");
            for (int i = 0; i < help.length(); i++) {
                JSONObject jsonobj = help.optJSONObject(i);
                HelpCategoryDO helpDo = new HelpCategoryDO();
                helpDo.HelpCategoryId = jsonobj.optInt("HelpCategoryId");
                helpDo.Code = jsonobj.optString("Code");
                helpDo.Title = jsonobj.optString("Title");
                helpDo.ParentId = jsonobj.optInt("ParentId");
                helpDo.CreatedDate = jsonobj.optString("CreatedDate");
                helpDo.CreatedBy = jsonobj.optInt("CreatedBy");
                helpDo.ModifiedDate = jsonobj.optString("ModifiedDate");
                helpDo.ModifiedBy = jsonobj.optInt("ModifiedBy");
                dataSyncDO.helpCategoryDOs.add(helpDo);
                jsonobj=null;
            }

            JSONArray helpQ = jsonObject.optJSONArray("HelpQuestionList");
            for (int i = 0; i < helpQ.length(); i++) {
                JSONObject jsonobj = helpQ.optJSONObject(i);
                HelpQuestionDO helpDo = new HelpQuestionDO();
                helpDo.HelpQuestionId = jsonobj.optInt("HelpQuestionId");
                helpDo.CategoryCode = jsonobj.optString("CategoryCode");
                helpDo.Question = jsonobj.optString("Question");
                helpDo.CreatedDate = jsonobj.optString("CreatedDate");
                helpDo.CreatedBy = jsonobj.optInt("CreatedBy");
                helpDo.ModifiedDate = jsonobj.optString("ModifiedDate");
                helpDo.ModifiedBy = jsonobj.optInt("ModifiedBy");
                dataSyncDO.helpQuestionDOs.add(helpDo);
                jsonobj=null;
            }

            JSONArray helpA = jsonObject.optJSONArray("HelpAnswerList");
            for (int i = 0; i < helpA.length(); i++) {
                JSONObject jsonobj = helpA.optJSONObject(i);
                HelpAnswerDO helpDo = new HelpAnswerDO();
                helpDo.HelpAnswerId = jsonobj.optInt("HelpAnswerId");
                helpDo.HelpQuestionId = jsonobj.optInt("HelpQuestionId");
                helpDo.Answer = jsonobj.optString("Answer");
                helpDo.CreatedDate = jsonobj.optString("CreatedDate");
                helpDo.CreatedBy = jsonobj.optInt("CreatedBy");
                helpDo.ModifiedDate = jsonobj.optString("ModifiedDate");
                helpDo.ModifiedBy = jsonobj.optInt("ModifiedBy");
                dataSyncDO.helpAnswerDOs.add(helpDo);
                jsonobj=null;
            }

            JSONObject cartDetailsJSON = jsonObject.optJSONObject("CartDetail");

            jsonArray = jsonObject.optJSONArray("PriceLists");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                PriceListDO priceListDO = new PriceListDO();
                priceListDO.PriceListId = jsonObj.optInt("PriceListId");
                priceListDO.Name = jsonObj.optString("Name");
                dataSyncDO.priceListDOs.add(priceListDO);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("Prices");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                PriceDO priceDO = new PriceDO();
                priceDO.PriceId = jsonObj.optInt("PriceId");
                priceDO.ProductId = jsonObj.optInt("ProductId");
                priceDO.UnitPrice = (float) jsonObj.optDouble("UnitPrice");
                priceDO.CurrencyCode = jsonObj.optString("CurrencyCode");
                priceDO.UOM = jsonObj.optString("UOM");
                priceDO.StartDate = jsonObj.optString("StartDate");
                priceDO.EndDate = jsonObj.optString("EndDate");
                priceDO.IsActive = jsonObj.optBoolean("IsActive");
                priceDO.CreatedBy = jsonObj.optInt("CreatedBy");
                priceDO.CreatedOn = jsonObj.optString("CreatedOn");
                priceDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                priceDO.ModifiedOn = jsonObj.optString("ModifiedOn");
                priceDO.Timestamp = jsonObj.optInt("Timestamp");
                priceDO.SalesOrgCode = jsonObj.optString("SalesOrgCode");
                priceDO.PriceListCode = jsonObj.optString("PriceListCode");

                dataSyncDO.priceDOs.add(priceDO);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("ItemUOMs");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ItemUOMDO itemUOMDO = new ItemUOMDO();
                itemUOMDO.ItemUOMId = jsonObj.optInt("ItemUOMId");
                itemUOMDO.ItemCode = jsonObj.optString("ItemCode");
                itemUOMDO.SalesOrgCode = jsonObj.optString("SalesOrgCode");
                itemUOMDO.UOM = jsonObj.optString("UOM");
                itemUOMDO.Numerator = (float) jsonObj.optDouble("Numerator");
                itemUOMDO.Denominator = (float) jsonObj.optDouble("Denominator");
                itemUOMDO.Conversion = (float) jsonObj.optDouble("Conversion");
                itemUOMDO.BarCode = jsonObj.optString("BarCode");
                itemUOMDO.Length = (float) jsonObj.optDouble("Length");
                itemUOMDO.Depth = (float) jsonObj.optDouble("Depth");
                itemUOMDO.Width = (float) jsonObj.optDouble("Width");
                itemUOMDO.Height = (float) jsonObj.optDouble("Height");
                itemUOMDO.Volume = (float) jsonObj.optDouble("Volume");
                itemUOMDO.VolumeUnit = jsonObj.optString("VolumeUnit");
                itemUOMDO.Weight = (float) jsonObj.optDouble("Weight");
                itemUOMDO.WeightUnit = jsonObj.optString("WeightUnit");
                itemUOMDO.CreatedDate = jsonObj.optString("CreatedDate");
                itemUOMDO.CreatedBy = jsonObj.optString("CreatedBy");
                itemUOMDO.ModifiedBy = jsonObj.optString("ModifiedBy");
                itemUOMDO.ModifiedDate = jsonObj.optInt("ModifiedDate");
                itemUOMDO.HeightUnit = jsonObj.optString("HeightUnit");
                itemUOMDO.LengthUnit = jsonObj.optString("LengthUnit");

//                itemUOMDO.ModifiedTime = jsonObj.optInt("ModifiedTime");
                dataSyncDO.itemUOMDOs.add(itemUOMDO);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("ShoppingCarts");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ShoppingCartDO shoppingCartDO = new ShoppingCartDO();
                shoppingCartDO.ShoppingCartId = jsonObj.optInt("ShoppingCartId");
                shoppingCartDO.SessionId = jsonObj.optString("SessionId");
                shoppingCartDO.UserId = jsonObj.optInt("UserId");
                shoppingCartDO.ProductCode = jsonObj.optString("ProductCode");
                shoppingCartDO.Quantity = jsonObj.optInt("Quantity");
                shoppingCartDO.UnitPrice = (float) jsonObj.optDouble("UnitPrice");
                shoppingCartDO.TotalPriceWODiscount = (float) jsonObj.optDouble("TotalPriceWODiscount");
                shoppingCartDO.DiscountRefId = jsonObj.optInt("DiscountRefId");
                shoppingCartDO.DiscountAmount = (float) jsonObj.optDouble("DiscountAmount");
                shoppingCartDO.TotalPrice = (float) jsonObj.optDouble("TotalPrice");
                shoppingCartDO.ModifiedOn = jsonObj.optString("ModifiedOn");
                shoppingCartDO.UOM = jsonObj.optString("UOM");
                shoppingCartDO.VAT = jsonObj.optInt("VAT");
                shoppingCartDO.TotalVAT = jsonObj.optDouble("TotalVAT");
                dataSyncDO.shoppingCartDOs.add(shoppingCartDO);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("ShoppingCartDetails");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ShoppingCartDetailsDO ss = new ShoppingCartDetailsDO();
                ss.ShoppingCartDetailId = jsonObj.optInt("ShoppingCartDetailId");
                ss.SessionId = jsonObj.optString("SessionId");
                ss.IsGift = jsonObj.optBoolean("IsGift");
                ss.IsGiftWrap = jsonObj.optBoolean("IsGiftWrap");
                ss.Message = jsonObj.optString("Message");
                ss.GiftCard = jsonObj.optString("GiftCard");

                ss.GiftCardAmount = (float) jsonObj.optDouble("GiftCardAmount");
                ss.IsGiftCardBeingUsed = jsonObj.optBoolean("IsGiftCardBeingUsed");
                ss.GiftCardUsageStartTime = jsonObj.optString("GiftCardUsageStartTime");
                ss.CouponCode = jsonObj.optString("CouponCode");
                ss.CouponAmount = (float) jsonObj.optDouble("CouponAmount");
                ss.IsCouponApplicable = jsonObj.optBoolean("IsCouponApplicable");
                ss.UserMessage = jsonObj.optString("UserMessage");
                dataSyncDO.shoppingCartDetailsDOs.add(ss);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("ShoppingCartAddresss");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ShoppingCartAddressDO shopCartAddDO = new ShoppingCartAddressDO();
                shopCartAddDO.ShoppingCartAddressId = jsonObj.optInt("ShoppingCartAddressId");
                shopCartAddDO.AddressBookId = jsonObj.optInt("AddressBookId");
                shopCartAddDO.AddressType = jsonObj.optString("AddressType");
                shopCartAddDO.FirstName = jsonObj.optString("FirstName");
                shopCartAddDO.MiddleName = jsonObj.optString("MiddleName");
                shopCartAddDO.LastName = jsonObj.optString("LastName");
                shopCartAddDO.AddressLine1 = jsonObj.optString("AddressLine1");
                shopCartAddDO.AddressLine2 = jsonObj.optString("AddressLine2");
                shopCartAddDO.AddressLine3 = jsonObj.optString("AddressLine3");
                shopCartAddDO.City = jsonObj.optString("City");
                shopCartAddDO.Country = jsonObj.optString("Country");
                shopCartAddDO.ZipCode = jsonObj.optString("ZipCode");
                shopCartAddDO.Phone = jsonObj.optString("Phone");
                shopCartAddDO.Email = jsonObj.optString("Email");
                shopCartAddDO.MobileNumber = jsonObj.optString("MobileNumber");
                dataSyncDO.shoppingCartAddressDOs.add(shopCartAddDO);
                jsonObj = null;
            }
            jsonArray = jsonObject.optJSONArray("Categories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                CategoryDO categoryDO = new CategoryDO();
                categoryDO.categoryId = jsonObj.optInt("CategoryId");
                categoryDO.code = jsonObj.optString("Code");
                categoryDO.name = jsonObj.optString("Name");
                categoryDO.URLName = jsonObj.optString("URLName");
                categoryDO.CreatedOn = jsonObj.optString("CreatedOn");
                categoryDO.ParentId = jsonObj.optInt("ParentId");
                categoryDO.Level = jsonObj.optInt("Level");
                categoryDO.Sequence = jsonObj.optInt("Sequence");
                categoryDO.CreatedBy = jsonObj.optInt("CreatedBy");
                categoryDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                categoryDO.Timestamp = jsonObj.optInt("Timestamp");
                categoryDO.ModifiedOn = jsonObj.optString("ModifiedOn");
                categoryDO.IsActive = jsonObj.optBoolean("IsActive");
                categoryDO.ShowInMenu = jsonObj.optBoolean("ShowInMenu");
                dataSyncDO.categoryDOs.add(categoryDO);
                jsonObj = null;
            }
            jsonArray = jsonObject.optJSONArray("ProductCategories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ProductCategoryDO prodCatDO = new ProductCategoryDO();
                prodCatDO.ProductCategoryId = jsonObj.optInt("ProductCategoryId");
                prodCatDO.ProductId = jsonObj.optInt("ProductId");
                prodCatDO.CategoryId = jsonObj.optInt("CategoryId");
                prodCatDO.CreatedBy = jsonObj.optInt("CreatedBy");
                prodCatDO.CreatedOn = jsonObj.optString("CreatedOn");
                prodCatDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                prodCatDO.ModifiedOn = jsonObj.optString("ModifiedOn");
                prodCatDO.Timestamp = jsonObj.optInt("Timestamp");
                dataSyncDO.productCategoryDOs.add(prodCatDO);
                jsonObj = null;
            }
            jsonArray = jsonObject.optJSONArray("TrxAddresss");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                TrxAddressDO trxAdd = new TrxAddressDO();
                trxAdd.TrxAddressId = jsonObj.optInt("TrxAddressId");
                trxAdd.TrxCode = jsonObj.optString("TrxCode");
                trxAdd.AddressType = jsonObj.optString("AddressType");
                trxAdd.AddressBookId = jsonObj.optInt("AddressBookId");
                trxAdd.SessionId = jsonObj.optString("SessionId");
                trxAdd.FirstName = jsonObj.optString("FirstName");
                trxAdd.MiddleName = jsonObj.optString("MiddleName");
                trxAdd.LastName = jsonObj.optString("LastName");
                trxAdd.AddressLine1 = jsonObj.optString("AddressLine1");
                trxAdd.AddressLine1_Arabic = jsonObj.optString("AddressLine1_Arabic");
                trxAdd.AddressLine2 = jsonObj.optString("AddressLine2");
                trxAdd.AddressLine2_Arabic = jsonObj.optString("AddressLine2_Arabic");
                trxAdd.AddressLine3 = jsonObj.optString("AddressLine3");
                trxAdd.AddressLine4 = jsonObj.optString("AddressLine4");
                trxAdd.AddressLine4_Arabic = jsonObj.optString("AddressLine4_Arabic");
                trxAdd.FlatNumber = jsonObj.optString("FlatNumber");
                trxAdd.City = jsonObj.optString("City");
                trxAdd.State = jsonObj.optString("State");
                trxAdd.Country = jsonObj.optString("Country");
                trxAdd.ZipCode = jsonObj.optString("ZipCode");
                trxAdd.Phone = jsonObj.optString("Phone");
                trxAdd.Email = jsonObj.optString("Email");
                trxAdd.MobileNumber = jsonObj.optString("MobileNumber");
                trxAdd.Street = jsonObj.optString("Street");
                dataSyncDO.trxAddressDOs.add(trxAdd);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("TrxHeaders");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                TrxHeaderDO trxHeaderDO = new TrxHeaderDO();
                trxHeaderDO.trxId = jsonObj.optInt("TrxId");
                trxHeaderDO.trxCode = jsonObj.optString("TrxCode");
                trxHeaderDO.trxRefCode = jsonObj.optString("RefCode");
                trxHeaderDO.trxDate = CalendarUtil.getDate(jsonObj.optString("TrxDate"),CalendarUtil.DATE_PATTERN,CalendarUtil.DATE_PATTERN_dd_MM_YYYY, Locale.ENGLISH);
                trxHeaderDO.trxType = jsonObj.optString("TrxType");
                trxHeaderDO.userId = jsonObj.optInt("UserId");
                trxHeaderDO.trxStatus = jsonObj.optInt("TrxStatus");
                trxHeaderDO.productSubTotalWDiscount = (float) jsonObj.optDouble("ProductSubTotalWDiscount");
                trxHeaderDO.totalAmountWODiscount = (float) jsonObj.optDouble("TotalAmountWODiscount");
                trxHeaderDO.discountAmt = (float) jsonObj.optDouble("DiscountAmount");
                trxHeaderDO.shippingCharges = (float) jsonObj.optDouble("ShippingCharges");
                trxHeaderDO.totalTrxAmount = (float) jsonObj.optDouble("TotalAmount");
                trxHeaderDO.isCOD = jsonObj.optBoolean("IsCOD");
                trxHeaderDO.isGiftCard = jsonObj.optBoolean("IsGift");
                trxHeaderDO.isGiftWrap = jsonObj.optBoolean("IsGiftWrap");
                trxHeaderDO.message = jsonObj.optString("Message");
                trxHeaderDO.giftCardUsed = jsonObj.optString("GiftCardUsed");
                trxHeaderDO.giftCardAmount = jsonObj.optDouble("GiftCardDiscount");
                trxHeaderDO.promotionId = jsonObj.optInt("PromotionId");
                trxHeaderDO.promotionDiscount = jsonObj.optDouble("PromotionDiscount");
                trxHeaderDO.isCouponCodeApplicable = jsonObj.optBoolean("IsCouponCodeApplicable");
                trxHeaderDO.couponCode = jsonObj.optString("CouponCode");
                trxHeaderDO.couponAmount = jsonObj.optDouble("CouponDiscount");
                trxHeaderDO.isCancelled = jsonObj.optBoolean("Is_Canceled");
                trxHeaderDO.userMessage = jsonObj.optString("UserMessage");
                trxHeaderDO.shippingStatus = jsonObj.optString("ShippingStatus");
                trxHeaderDO.docketNumber = jsonObj.optString("DocketNo");
                trxHeaderDO.walletAmount = jsonObj.optDouble("WalletAmount");
                trxHeaderDO.sessionId = jsonObj.optString("SessionId");
                trxHeaderDO.ModifiedDate = jsonObj.optString("ModifiedOn");
                trxHeaderDO.CancelledBy = jsonObj.optString("CancelledBy");
                trxHeaderDO.DeviceType = jsonObj.optString("DeviceType");
                trxHeaderDO.DeviceId = jsonObj.optString("DeviceId");
                trxHeaderDO.ReasonForCancellation = jsonObj.optString("ReasonForCancellation");
                trxHeaderDO.CancellationReasonCategory = jsonObj.optString("CancellationReasonCategory");
                trxHeaderDO.SpecialInstructions = jsonObj.optString("SpecialInstructions");
                trxHeaderDO.OrderLimitApplied = jsonObj.optBoolean("OrderLimitApplied");
                trxHeaderDO.deliveryDate = CalendarUtil.convertToDeliDateToInsert(jsonObj.optString("DeliveryDate"));
                trxHeaderDO.isRecurring = jsonObj.optBoolean("IsRecurring");
                trxHeaderDO.NotificationForNextDayDeliverySent = jsonObj.optInt("NotificationForNextDayDeliverySent");
                trxHeaderDO.NotificationForNextDayDeliverySentOn = jsonObj.optString("NotificationForNextDayDeliverySentOn");
                trxHeaderDO.CancelledOn = jsonObj.optString("CancelledOn");
                trxHeaderDO.TotalVAT = jsonObj.optInt("TotalVAT");
                dataSyncDO.trxHeaderDOs.add(trxHeaderDO);
                jsonObj = null;
            }

            jsonArray = jsonObject.optJSONArray("TrxDetails");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                TrxDetailsDO trxDetailsDO = new TrxDetailsDO();
                trxDetailsDO.trxDetailId = jsonObj.optInt("TrxDetailId");
                trxDetailsDO.trxCode = jsonObj.optString("TrxCode");
                trxDetailsDO.productId = jsonObj.optInt("ProductId");
                trxDetailsDO.sequence = jsonObj.optInt("Sequence");
                trxDetailsDO.quantity = jsonObj.optDouble("Quantity");
                trxDetailsDO.unitPrice = jsonObj.optDouble("UnitPrice");
                trxDetailsDO.totalAmountWODiscount = jsonObj.optDouble("TotalAmountWODiscount");
                trxDetailsDO.totalAmountWODiscount = jsonObj.optDouble("DiscountAmount");
                trxDetailsDO.discountRefId = jsonObj.optInt("DiscountRefId");
                trxDetailsDO.reference = jsonObj.optString("Reference");
                trxDetailsDO.orderAmount = jsonObj.optDouble("TotalAmount");
                trxDetailsDO.initialQuantity = jsonObj.optInt("InitialQuantity");
                trxDetailsDO.UOM = jsonObj.optString("UOM");
                trxDetailsDO.VAT = jsonObj.optInt("VAT");
                trxDetailsDO.TotalVAT = jsonObj.optDouble("TotalVAT");
                dataSyncDO.trxDetailsDOs.add(trxDetailsDO);
                jsonObj = null;

            }

            // ProductAttributes
            jsonArray = jsonObject.optJSONArray("ProductAttributes");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ProductAttribute productAttribute = new ProductAttribute();
                productAttribute.ProductAttributeId = jsonObj.optInt("ProductAttributeId");
                productAttribute.ProductId = jsonObj.optInt("ProductId");
                productAttribute.Name = jsonObj.optString("Name");
                productAttribute.Icon = jsonObj.optString("Icon");
                productAttribute.Description = jsonObj.optString("Description");
                productAttribute.CreatedBy = jsonObj.optString("CreatedBy");
                productAttribute.CreatedOn = jsonObj.optString("CreatedOn");
                productAttribute.ModifiedBy = jsonObj.optString("ModifiedBy");
                productAttribute.ModifiedOn = jsonObj.optString("ModifiedOn");
                productAttribute.Timestamp = jsonObj.optString("Timestamp");
                dataSyncDO.productAttributes.add(productAttribute);
                jsonObj = null;
            }

            // ProductAttributeDetails
            jsonArray = jsonObject.optJSONArray("ProductAttributeDetails");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                ProductAttributeDetailsDO productAttribute = new ProductAttributeDetailsDO();
                productAttribute.ProductAttributeDetailId = jsonObj.optInt("ProductAttributeDetailId");
                productAttribute.ProductAttributeId = jsonObj.optInt("ProductAttributeId");
                productAttribute.Description = jsonObj.optString("Description");
                productAttribute.CreatedBy = jsonObj.optInt("CreatedBy");
                productAttribute.CreatedOn = jsonObj.optString("CreatedOn");
                productAttribute.ModifiedBy = jsonObj.optInt("ModifiedBy");
                productAttribute.ModifiedOn = jsonObj.optString("ModifiedOn");
                productAttribute.Timestamp = jsonObj.optString("Timestamp");
                dataSyncDO.productAttributeDetailsDOs.add(productAttribute);
                jsonObj = null;
            }

            // OrderNumbers
            jsonArray = jsonObject.optJSONArray("OrderNumbers");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                OrderNumberDO orderNumberDO = new OrderNumberDO();
                orderNumberDO.Id = jsonObj.optInt("Id");
                orderNumberDO.TrxId = jsonObj.optInt("TrxId");
                orderNumberDO.OrderNumber = jsonObj.optString("OrderNumber");
                orderNumberDO.CreatedDate = jsonObj.optString("CreatedDate");
                dataSyncDO.orderNumberDOs.add(orderNumberDO);
                jsonObj = null;
            }

            // Emirates
            jsonArray = jsonObject.optJSONArray("Emirates");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    EmirateDO orderNumberDO = new EmirateDO();
                    orderNumberDO.EmiratesId = jsonObj.optInt("EmiratesId");
                    orderNumberDO.Code = jsonObj.optString("Code");
                    orderNumberDO.Name = jsonObj.optString("Name");
                    orderNumberDO.ArabicName = jsonObj.optString("Arabic_Name");
                    orderNumberDO.CreatedDate = jsonObj.optString("CreatedDate");
                    orderNumberDO.CreatedBy = jsonObj.optInt("CreatedBy");
                    orderNumberDO.ModifiedDate = jsonObj.optString("ModifiedDate");
                    orderNumberDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                    dataSyncDO.emirateDOs.add(orderNumberDO);
                    jsonObj = null;
                }

            // AreaandSubAreas...
            jsonArray = jsonObject.optJSONArray("AreaandSubAreas");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                AreaDO areaDO = new AreaDO();
                areaDO.AreaId = jsonObj.optInt("AreaId");
                areaDO.EmiratesId = jsonObj.optInt("EmiratesId");
                areaDO.ParentId = jsonObj.optInt("ParentId");
                areaDO.Name = jsonObj.optString("Name");
                areaDO.ArabicName = jsonObj.optString("Arabic_Name");
                areaDO.CreatedDate = jsonObj.optString("CreatedDate");
                areaDO.CreatedBy = jsonObj.optString("CreatedBy");
                areaDO.ModifiedDate = jsonObj.optString("ModifiedDate");
                areaDO.ModifiedBy = jsonObj.optString("ModifiedBy");
                dataSyncDO.areaDOs.add(areaDO);
                jsonObj = null;
            }

            // AreaRoutes
            jsonArray = jsonObject.optJSONArray("AreaRoutes");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    AreaRoutesDO areaRoutesDO = new AreaRoutesDO();
                    areaRoutesDO.AreaRouteId = jsonObj.optInt("AreaRouteId");
                    areaRoutesDO.AreaId = jsonObj.optInt("AreaId");
                    areaRoutesDO.Route = jsonObj.optString("Route");
                    areaRoutesDO.CreatedDate = jsonObj.optString("CreatedDate");
                    areaRoutesDO.CreatedBy = jsonObj.optInt("CreatedBy");
                    areaRoutesDO.ModifiedDate = jsonObj.optString("ModifiedDate");
                    areaRoutesDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                    dataSyncDO.areaRoutesDOs.add(areaRoutesDO);
                    jsonObj = null;
                }

            // AreaRouteSupervisors
            jsonArray = jsonObject.optJSONArray("AreaRouteSupervisors");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    AreaRouteSupervisorsDO areaRoutesDO = new AreaRouteSupervisorsDO();
                    areaRoutesDO.AreaRouteSupervisorId = jsonObj.optInt("AreaRouteSupervisorId");
                    areaRoutesDO.AreaRouteId = jsonObj.optInt("AreaRouteId");
                    areaRoutesDO.Supervisor = jsonObj.optString("Supervisor");
                    areaRoutesDO.CreatedDate = jsonObj.optString("CreatedDate");
                    areaRoutesDO.CreatedBy = jsonObj.optInt("CreatedBy");
                    areaRoutesDO.ModifiedDate = jsonObj.optString("ModifiedDate");
                    areaRoutesDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                    dataSyncDO.areaRouteSupervisorsDOs.add(areaRoutesDO);
                    jsonObj = null;
                }

            // AreaDeliveryDetails
            jsonArray = jsonObject.optJSONArray("AreaDeliveryDetails");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                DeliveryDay areaDO = new DeliveryDay();
                areaDO.AreaDeliveryDetailId = jsonObj.optInt("AreaDeliveryDetailId");
                areaDO.AreaId = jsonObj.optInt("AreaId");
                areaDO.Route = jsonObj.optString("RouteId");
                areaDO.AreaRouteSupervisorId = jsonObj.optInt("AreaRouteSupervisorId");
                areaDO.DeliveryDay = jsonObj.optString("DeliveryDay");
                dataSyncDO.deliveryDays.add(areaDO);
                jsonObj = null;
            }

            // HydrationMeterSetting
            jsonArray = jsonObject.optJSONArray("HydrationMeterSetting");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                HydrationMeterSettingDO areaDO = new HydrationMeterSettingDO();
                areaDO.HydrationMeterSettingId = jsonObj.optInt("HydrationMeterSettingId");
                areaDO.CustomerId = jsonObj.optInt("CustomerId");
                areaDO.Weight = jsonObj.optDouble("Weight");
                areaDO.Height= jsonObj.optDouble("Height");
                areaDO.Notification= jsonObj.optBoolean("Notification");
                areaDO.DailyWaterConsumptionTarget = jsonObj.optDouble("DailyWaterConsumptionTarget");
                areaDO.CreatedDate = jsonObj.optString("CreatedDate");
                areaDO.CreatedBy = jsonObj.optInt("CreatedBy");
                areaDO.ModifiedDate = jsonObj.optString("ModifiedDate");
                areaDO.ModifiedBy = jsonObj.optInt("ModifiedBy");
                dataSyncDO.hydrationMeterSettingDOs.add(areaDO);
                jsonObj = null;
            }

            // HydrationMeterReading
            jsonArray = jsonObject.optJSONArray("HydrationMeterReadings");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                HydrationMeterReadingDO areaDO = new HydrationMeterReadingDO();
                areaDO.HydrationMeterReadingId = jsonObj.optInt("HydrationMeterReadingId");
                areaDO.CustomerId = jsonObj.optInt("CustomerId");
                areaDO.WaterConsumed = jsonObj.optDouble("WaterConsumed");
                areaDO.ConsumptionDate = jsonObj.optString("ConsumptionDate");
                areaDO.WaterConsumedPercent= jsonObj.optDouble("Percentage");
                dataSyncDO.hydrationMeterReadingDOs.add(areaDO);
                jsonObj = null;
            }

            //Order Cancel Reasons
            jsonArray = jsonObject.optJSONArray("OrderCancellationReasonCategories");
            if(jsonArray != null && jsonArray.length()>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.optJSONObject(i);
                OrderCancelDO cancelDO = new OrderCancelDO();
                cancelDO.Id = jsonObj.optInt("OrderCancellationReasonCategoryId");
                cancelDO.Category = jsonObj.optString("Category");
                cancelDO.AltDescription = jsonObj.optString("AltDescription");
                cancelDO.ParentId = jsonObj.optInt("ParentId");
                cancelDO.CreatedDate = jsonObj.optString("CreatedDate");
                cancelDO.CreatedBy= jsonObj.optInt("CreatedBy");
                cancelDO.ModifiedDate= jsonObj.optString("ModifiedDate");
                cancelDO.ModifiedBy= jsonObj.optInt("ModifiedBy");
                dataSyncDO.orderCancelDOs.add(cancelDO);
                jsonObj = null;
            }


            // RecurringOrders...
            jsonArray = jsonObject.optJSONArray("RecurringOrders");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    RecurringOrderDO recurringOrderDO = new RecurringOrderDO();
                    recurringOrderDO.RecurringOrderId = jsonObj.optInt("RecurringOrderId");
                    recurringOrderDO.TrxCode = jsonObj.optString("TrxCode");
                    recurringOrderDO.Frequency = jsonObj.optString("Frequency");
                    recurringOrderDO.NumberOfRepeatations = jsonObj.optInt("NumberOfRepeatations");
                    recurringOrderDO.CreatedDate = jsonObj.optString("CreatedDate");
                    recurringOrderDO.CreatedBy= jsonObj.optInt("CreatedBy");
                    recurringOrderDO.ModifiedDate= jsonObj.optString("ModifiedDate");
                    recurringOrderDO.ModifiedBy= jsonObj.optInt("ModifiedBy");
                    recurringOrderDO.RemovedFromRecurring = jsonObj.optBoolean("RemovedFromRecurring");
                    recurringOrderDO.RemovedFromRecurringOn= jsonObj.optString("RemovedFromRecurringOn");
                    dataSyncDO.recurringOrderDOs.add(recurringOrderDO);
                    jsonObj = null;
                }

            // OrderLimits...
            jsonArray = jsonObject.optJSONArray("OrderLimits");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    ProductOrderLimitDO productOrderLimitDO = new ProductOrderLimitDO();
                    productOrderLimitDO .OrderLimitId = jsonObj.optInt("OrderLimitId");
                    productOrderLimitDO .ProductId = jsonObj.optInt("ProductId");
                    productOrderLimitDO .Quantity = jsonObj.optInt("Quantity");
                    productOrderLimitDO .StartDate = jsonObj.optString("StartDate").contains("T")?jsonObj.optString("StartDate").replace("T", " "):jsonObj.optString("StartDate");
                    productOrderLimitDO .EndDate = jsonObj.optString("EndDate").contains("T")?jsonObj.optString("EndDate").replace("T", " "):jsonObj.optString("EndDate");
                    productOrderLimitDO .CreatedDate = jsonObj.optString("CreatedDate");
                    productOrderLimitDO .CreatedBy= jsonObj.optInt("CreatedBy");
                    productOrderLimitDO .ModifiedDate= jsonObj.optString("ModifiedDate");
                    productOrderLimitDO .ModifiedBy= jsonObj.optInt("ModifiedBy");
                    dataSyncDO.productOrderLimitDOs.add(productOrderLimitDO );
                    jsonObj = null;
                }
            // OrderMinLimits...
            jsonArray = jsonObject.optJSONArray("OrderMinLimits");
            if(jsonArray != null && jsonArray.length()>0)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    ProductOrderLimitDO productOrderLimitDO = new ProductOrderLimitDO();
                    productOrderLimitDO .OrderLimitId = jsonObj.optInt("OrderMinLimitId");
                    productOrderLimitDO .ProductId = jsonObj.optInt("ProductId");
                    productOrderLimitDO .Quantity = jsonObj.optInt("Quantity");
                    productOrderLimitDO .StartDate = jsonObj.optString("StartDate").contains("T")?jsonObj.optString("StartDate").replace("T", " "):jsonObj.optString("StartDate");
                    productOrderLimitDO .EndDate = jsonObj.optString("EndDate").contains("T")?jsonObj.optString("EndDate").replace("T", " "):jsonObj.optString("EndDate");
                    productOrderLimitDO .CreatedDate = jsonObj.optString("CreatedDate");
                    productOrderLimitDO .CreatedBy= jsonObj.optInt("CreatedBy");
                    productOrderLimitDO .ModifiedDate= jsonObj.optString("ModifiedDate");
                    productOrderLimitDO .ModifiedBy= jsonObj.optInt("ModifiedBy");
                    dataSyncDO.productMinOrderLimitDOs.add(productOrderLimitDO );
                    jsonObj = null;
                }

            JSONObject jb = jsonObject.getJSONObject("objResponse");
            dataSyncDO.status = jb.getInt("Status");
            dataSyncDO.message=jb.getString("Message");
            dataSyncDO.serverTime=jb.getString("ServerTime");
            //dataSyncDO.objStatus = jsonObject.getInt("objStatus");

            if(jsonObject.has("DeletedRecords")) {
                jsonArray = jsonObject.getJSONArray("DeletedRecords");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                    DeletedRecordDO deletedRecordDO = new DeletedRecordDO();
                    deletedRecordDO.Id = jsonObj.optInt("Id");
                    deletedRecordDO.TableName = jsonObj.optString("TableName");
                    deletedRecordDO.PrimaryKey = jsonObj.optString("PrimaryKey");
                    deletedRecordDO.PrimaryKeyValue = jsonObj.optInt("PrimaryKeyValue");
                    deletedRecordDO.CreatedDate = jsonObj.optString("CreatedDate");

                    if (deletedRecordDO.TableName != null)
                        dataSyncDO.deletedRecordDOs.add(deletedRecordDO);
                    jsonObj = null;
                }
            }
            mSyncDa.updateLastSyncDateTime(dataSyncDO.serverTime);
        } catch (Exception e) {
/*e.printStackTrace(); */
            Log.d("This can never happen", e.getMessage());
            LogUtils.errorLog("CartListParser Parser ", e.getMessage());
        }finally {

            if(dataSyncDO.status != 0) {

            /*
            *
            * Girish 24-09-2016
            *
            * All the insertion need to happen in the finally because if any
            * jsonexception of any module it will not effect previous one
            *
            * but update lastsynctime need to be in try
            *
            * */
                mSyncDa.insertProducts(dataSyncDO.productDOs);
                mSyncDa.insertProductImages(dataSyncDO.productImagesDOs);
                new CustomerDA(mContext).insertCustomer(dataSyncDO.customerDOs);
                mSyncDa.insertGalleryImages(dataSyncDO.galleryDOs);
                mSyncDa.insertHelpCategory(dataSyncDO.helpCategoryDOs);
                mSyncDa.insertHelpQuestion(dataSyncDO.helpQuestionDOs);
                mSyncDa.insertHelpAnswer(dataSyncDO.helpAnswerDOs);
                mSyncDa.insertPriceLists(dataSyncDO.priceListDOs);
                mSyncDa.insertPrices(dataSyncDO.priceDOs);
                mSyncDa.insertItemUOMs(dataSyncDO.itemUOMDOs);
                mSyncDa.insertShoppingCarts(dataSyncDO.shoppingCartDOs);
                mSyncDa.inserttblShoppingCartDetails(dataSyncDO.shoppingCartDetailsDOs);
                mSyncDa.insertShoppingCartAddress(dataSyncDO.shoppingCartAddressDOs);
                mSyncDa.insertTrxHeaders(dataSyncDO.trxHeaderDOs);
                mSyncDa.insertTrxDetails(dataSyncDO.trxDetailsDOs);
                mSyncDa.insertTrxAddress(dataSyncDO.trxAddressDOs);
                mSyncDa.insertCategory(dataSyncDO.categoryDOs);
                mSyncDa.insertProductCategory(dataSyncDO.productCategoryDOs);
                mSyncDa.insertProductAttributes(dataSyncDO.productAttributes);
                mSyncDa.insertProductAttributeDetails(dataSyncDO.productAttributeDetailsDOs);
                mSyncDa.insertOrderNumbers(dataSyncDO.orderNumberDOs);
                mSyncDa.insertAreaandSubAreas(dataSyncDO.areaDOs);
                mSyncDa.insertAreaDeliveryDetails(dataSyncDO.deliveryDays);
                mSyncDa.insertHydrationMeterSettings(dataSyncDO.hydrationMeterSettingDOs);
                mSyncDa.insertHydrationMeterReading(dataSyncDO.hydrationMeterReadingDOs);
                mSyncDa.insertOrderLimit(dataSyncDO.productOrderLimitDOs);
                mSyncDa.insertMinOrderLimit(dataSyncDO.productMinOrderLimitDOs);
                mSyncDa.insertEmirate(dataSyncDO.emirateDOs);
                mSyncDa.insertAreaRoute(dataSyncDO.areaRoutesDOs);
                mSyncDa.insertAreaRouteSupervisors(dataSyncDO.areaRouteSupervisorsDOs);
                mSyncDa.insertRecurringOrder(dataSyncDO.recurringOrderDOs);
                mSyncDa.insertOrderCancelReasons(dataSyncDO.orderCancelDOs);
                mSyncDa.deleteRecords(dataSyncDO.deletedRecordDOs);
                FileUtils.updateProductImages(dataSyncDO.productImagesDOs);
            }
        }
    }

}
