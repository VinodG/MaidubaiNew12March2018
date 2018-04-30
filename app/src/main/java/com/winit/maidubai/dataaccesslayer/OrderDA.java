package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.OrderCancelDO;
import com.winit.maidubai.dataobject.OrderNumberDO;
import com.winit.maidubai.dataobject.RecurringOrderDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 25-08-2016.
 */
public class OrderDA {

    private Context context;
    public OrderDA(Context context){
        this.context = context;
    }

    public boolean insertOrders(ArrayList<TrxHeaderDO> arrTrxHeaderDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrders() - strated");

        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxHeader(TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                    "TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message," +
                    "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                    "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId,SpecialInstructions,OrderName,IsRecurring,DeliveryDate,TotalVAT,CancelledOn) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxHeader set RefCode=?,TrxDate=?,TrxType=?," +
                    "UserId=?,TrxStatus=?,ProductSubTotalWDiscount=?,TotalAmountWODiscount=?,DiscountAmount=?,ShippingCharges=?,TotalAmount=?,IsCOD=?," +
                    "IsGift=?,IsGiftWrap=?,Message=?,GiftCardUsed=?,GiftCardDiscount=?,PromotionId=?,PromotionDiscount=?,IsCouponCodeApplicable=?," +
                    "CouponCode=?,CouponDiscount=?,Is_Canceled=?,UserMessage=?,ShippingStatus=?,DocketNo=?,WalletAmount=?,SessionId=?,TrxCode=?," +
                    "SpecialInstructions=?,OrderName=?,IsRecurring=?,DeliveryDate=?,TotalVAT =? ,CancelledOn=? where TrxId = ?");
            for(TrxHeaderDO trxHeaderDO : arrTrxHeaderDOs) {
                updateSqLiteStatement.bindString(1, trxHeaderDO.trxRefCode);
                updateSqLiteStatement.bindString(2, CalendarUtil.convertToTrxDate(trxHeaderDO.trxDate));
                updateSqLiteStatement.bindString(3, trxHeaderDO.trxType);
                updateSqLiteStatement.bindLong(4, trxHeaderDO.userId);
                updateSqLiteStatement.bindLong(5, trxHeaderDO.trxStatus);
                updateSqLiteStatement.bindDouble(6, trxHeaderDO.productSubTotalWDiscount);
                updateSqLiteStatement.bindDouble(7, trxHeaderDO.totalAmountWODiscount);
                updateSqLiteStatement.bindDouble(8, trxHeaderDO.discountAmt);
                updateSqLiteStatement.bindDouble(9, trxHeaderDO.shippingCharges);
                updateSqLiteStatement.bindDouble(10, trxHeaderDO.totalTrxAmount);
                updateSqLiteStatement.bindString(11, trxHeaderDO.isCOD + "");
                updateSqLiteStatement.bindString(12, trxHeaderDO.isGiftCard + "");
                updateSqLiteStatement.bindString(13, trxHeaderDO.isGiftWrap+"");
                updateSqLiteStatement.bindString(14, trxHeaderDO.message);
                updateSqLiteStatement.bindString(15, trxHeaderDO.giftCardUsed);
                updateSqLiteStatement.bindDouble(16, trxHeaderDO.giftCardAmount);
                updateSqLiteStatement.bindLong(17, trxHeaderDO.promotionId);
                updateSqLiteStatement.bindDouble(18, trxHeaderDO.promotionDiscount);
                updateSqLiteStatement.bindString(19, trxHeaderDO.isCouponCodeApplicable + "");
                updateSqLiteStatement.bindString(20, trxHeaderDO.couponCode);
                updateSqLiteStatement.bindDouble(21, trxHeaderDO.couponAmount);
                updateSqLiteStatement.bindString(22, trxHeaderDO.isCancelled + "");
                updateSqLiteStatement.bindString(23, trxHeaderDO.userMessage);
                updateSqLiteStatement.bindString(24, trxHeaderDO.shippingStatus);
                updateSqLiteStatement.bindString(25, trxHeaderDO.docketNumber);
                updateSqLiteStatement.bindDouble(26, trxHeaderDO.walletAmount);
                updateSqLiteStatement.bindString(27, trxHeaderDO.sessionId);
                updateSqLiteStatement.bindString(28, trxHeaderDO.trxCode);
                updateSqLiteStatement.bindString(29, trxHeaderDO.specialInstructions);
                updateSqLiteStatement.bindString(30, trxHeaderDO.orderName);
                updateSqLiteStatement.bindString(31, trxHeaderDO.isRecurring + "");
                updateSqLiteStatement.bindString(32, trxHeaderDO.deliveryDate);
                updateSqLiteStatement.bindDouble(33, trxHeaderDO.TotalVAT);
                updateSqLiteStatement.bindString(34, trxHeaderDO.CancelledOn);
                updateSqLiteStatement.bindLong(35, trxHeaderDO.trxId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, trxHeaderDO.trxId);
                    insertSqLiteStatement.bindString(2, trxHeaderDO.trxCode);
                    insertSqLiteStatement.bindString(3, trxHeaderDO.trxRefCode);
                    insertSqLiteStatement.bindString(4, CalendarUtil.convertToTrxDate(trxHeaderDO.trxDate));
                    insertSqLiteStatement.bindString(5, trxHeaderDO.trxType);
                    insertSqLiteStatement.bindLong(6, trxHeaderDO.userId);
                    insertSqLiteStatement.bindLong(7, trxHeaderDO.trxStatus);
                    insertSqLiteStatement.bindDouble(8, trxHeaderDO.productSubTotalWDiscount);
                    insertSqLiteStatement.bindDouble(9, trxHeaderDO.totalAmountWODiscount);
                    insertSqLiteStatement.bindDouble(10, trxHeaderDO.discountAmt);
                    insertSqLiteStatement.bindDouble(11, trxHeaderDO.shippingCharges);
                    insertSqLiteStatement.bindDouble(12, trxHeaderDO.totalTrxAmount);
                    insertSqLiteStatement.bindString(13, trxHeaderDO.isCOD + "");
                    insertSqLiteStatement.bindString(14, trxHeaderDO.isGiftCard + "");
                    insertSqLiteStatement.bindString(15, trxHeaderDO.isGiftWrap+"");
                    insertSqLiteStatement.bindString(16, trxHeaderDO.message);
                    insertSqLiteStatement.bindString(17, trxHeaderDO.giftCardUsed);
                    insertSqLiteStatement.bindDouble(18, trxHeaderDO.giftCardAmount);
                    insertSqLiteStatement.bindLong(19, trxHeaderDO.promotionId);
                    insertSqLiteStatement.bindDouble(20, trxHeaderDO.promotionDiscount);
                    insertSqLiteStatement.bindString(21, trxHeaderDO.isCouponCodeApplicable + "");
                    insertSqLiteStatement.bindString(22, trxHeaderDO.couponCode);
                    insertSqLiteStatement.bindDouble(23, trxHeaderDO.couponAmount);
                    insertSqLiteStatement.bindString(24, trxHeaderDO.isCancelled + "");
                    insertSqLiteStatement.bindString(25, trxHeaderDO.userMessage);
                    insertSqLiteStatement.bindString(26, trxHeaderDO.shippingStatus);
                    insertSqLiteStatement.bindString(27, trxHeaderDO.docketNumber);
                    insertSqLiteStatement.bindDouble(28, trxHeaderDO.walletAmount);
                    insertSqLiteStatement.bindString(29, trxHeaderDO.sessionId);
                    insertSqLiteStatement.bindString(30, trxHeaderDO.specialInstructions);
                    insertSqLiteStatement.bindString(31, trxHeaderDO.orderName);
                    insertSqLiteStatement.bindString(32, trxHeaderDO.isRecurring+"");
                    insertSqLiteStatement.bindString(33, trxHeaderDO.deliveryDate);
                    insertSqLiteStatement.bindDouble(34, trxHeaderDO.TotalVAT);
                    insertSqLiteStatement.bindString(35, trxHeaderDO.CancelledOn);
                    insertSqLiteStatement.executeInsert();
                }
                insertOrderNumber(trxHeaderDO.orderNumberDO); // should not check for boolean b'coz in order sync serivce order number will not come
                insertRecurringOrders(trxHeaderDO.recurringOrderDOs); // should not check for boolean b'coz for normal Orders recurring order will not come
                if(!insertOrdersDetails(trxHeaderDO.trxDetailsDOs))
                    return false;
                if(!insertOrderAddress(trxHeaderDO.trxAddressBookDOs))
                    return false;
            }
            return true;
        }catch (Exception e){
        /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            if(sqLiteDatabase!=null && sqLiteDatabase.isOpen()){
                sqLiteDatabase.close();
            }
            LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrders() - ended");
            return false;
        }
    }

    private boolean insertOrderNumber(OrderNumberDO orderNumberDo){
        if(orderNumberDo !=null) {
            LogUtils.debug(LogUtils.LOG_TAG, "insertOrderNumbers() - started");
            SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
            SQLiteStatement insertSqLiteStatement = null;
            SQLiteStatement updateSqLiteStatement = null;
            try {
                insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblOrderNumber(OrderNumber,TrxId," +
                        "CreatedDate,Id) values(?,?,?,?)");
                updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblOrderNumber set OrderNumber=?,TrxId=?,CreatedDate=? where Id = ?");
                updateSqLiteStatement.bindString(1, orderNumberDo.OrderNumber);
                updateSqLiteStatement.bindLong(2, orderNumberDo.TrxId);
                updateSqLiteStatement.bindString(3, orderNumberDo.CreatedDate);
                updateSqLiteStatement.bindLong(4, orderNumberDo.Id);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindString(1, orderNumberDo.OrderNumber);
                    insertSqLiteStatement.bindLong(2, orderNumberDo.TrxId);
                    insertSqLiteStatement.bindString(3, orderNumberDo.CreatedDate);
                    insertSqLiteStatement.bindLong(4, orderNumberDo.Id);
                    insertSqLiteStatement.executeInsert();
                }
                return true;
            } catch (Exception e) {
           /*e.printStackTrace(); */                Log.d("This can never happen", e.getMessage());
            } finally {
                if (insertSqLiteStatement != null)
                    insertSqLiteStatement.close();
                if (updateSqLiteStatement != null)
                    updateSqLiteStatement.close();
                LogUtils.debug(LogUtils.LOG_TAG, "insertOrderNumbers() - ended");
            }
        }
        return false;
    }

    private boolean insertRecurringOrders(ArrayList<RecurringOrderDO> recurringOrderDOs){
        if(recurringOrderDOs !=null) {
            LogUtils.debug(LogUtils.LOG_TAG, "insertOrderNumbers() - started");
            SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
            SQLiteStatement insertSqLiteStatement = null;
            SQLiteStatement updateSqLiteStatement = null;
            try {
                insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblRecurringOrder(RecurringOrderId,TrxCode," +
                        "Frequency,NumberOfRepeatations,RemovedFromRecurring,RemovedFromRecurringOn) values(?,?,?,?,?,?)");
                updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblRecurringOrder set TrxCode=?,Frequency=?,NumberOfRepeatations=?, " +
                        "RemovedFromRecurring=?,RemovedFromRecurringOn=?" +
                        "where RecurringOrderId = ?");
                for(RecurringOrderDO recurringOrderDO : recurringOrderDOs) {
                    updateSqLiteStatement.bindString(1, recurringOrderDO.TrxCode);
                    updateSqLiteStatement.bindString(2, recurringOrderDO.Frequency);
                    updateSqLiteStatement.bindLong(3, recurringOrderDO.NumberOfRepeatations);
                    updateSqLiteStatement.bindString(4, recurringOrderDO.RemovedFromRecurring + "");
                    updateSqLiteStatement.bindString(5, recurringOrderDO.RemovedFromRecurringOn);
                    updateSqLiteStatement.bindLong(6, recurringOrderDO.RecurringOrderId);
                    if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                        insertSqLiteStatement.bindLong(1, recurringOrderDO.RecurringOrderId);
                        insertSqLiteStatement.bindString(2, recurringOrderDO.TrxCode);
                        insertSqLiteStatement.bindString(3, recurringOrderDO.Frequency);
                        insertSqLiteStatement.bindLong(4, recurringOrderDO.NumberOfRepeatations);
                        insertSqLiteStatement.bindString(5, recurringOrderDO.RemovedFromRecurring + "");
                        insertSqLiteStatement.bindString(6, recurringOrderDO.RemovedFromRecurringOn);
                        insertSqLiteStatement.executeInsert();
                    }
                }
                return true;
            } catch (Exception e) {
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (insertSqLiteStatement != null)
                    insertSqLiteStatement.close();
                if (updateSqLiteStatement != null)
                    updateSqLiteStatement.close();
                LogUtils.debug(LogUtils.LOG_TAG, "insertOrderNumbers() - ended");
            }
        }
        return false;
    }

    private boolean insertOrdersDetails(ArrayList<TrxDetailsDO> arrTrxDetailsDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrdersDetails() - strated");

        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxDetail(TrxDetailId,TrxCode,ProductId," +
                    "Sequence,Quantity,UnitPrice,TotalAmountWODiscount,DiscountAmount,DiscountRefId,Reference," +
                    "TotalAmount,InitialQuantity,UOM,VAT,TotalVAT) values(?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxDetail set TrxCode=?,ProductId=?,Sequence=?," +
                    "Quantity=?,UnitPrice=?,TotalAmountWODiscount=?,DiscountAmount=?,DiscountRefId=?,Reference=?," +
                    "TotalAmount=?,InitialQuantity=?,UOM=?,VAT=? , TotalVAT=?  where TrxDetailId = ?");
            for(TrxDetailsDO trxDetailsDO: arrTrxDetailsDOs) {
                updateSqLiteStatement.bindString(1,trxDetailsDO.trxCode);
                updateSqLiteStatement.bindLong(2, trxDetailsDO.productId);
                updateSqLiteStatement.bindDouble(3, trxDetailsDO.sequence);
                updateSqLiteStatement.bindDouble(4, trxDetailsDO.quantity);
                updateSqLiteStatement.bindDouble(5, trxDetailsDO.unitPrice);
                updateSqLiteStatement.bindDouble(6, trxDetailsDO.totalAmountWODiscount);
                updateSqLiteStatement.bindDouble(7, trxDetailsDO.discountAmount);
                updateSqLiteStatement.bindLong(8, trxDetailsDO.discountRefId);
                updateSqLiteStatement.bindString(9,trxDetailsDO.reference);
                updateSqLiteStatement.bindDouble(10, trxDetailsDO.orderAmount);
                updateSqLiteStatement.bindDouble(11, trxDetailsDO.initialQuantity);
                updateSqLiteStatement.bindString(12,trxDetailsDO.UOM);
                updateSqLiteStatement.bindLong(13,trxDetailsDO.VAT);
                updateSqLiteStatement.bindDouble(14,trxDetailsDO.TotalVAT);
                updateSqLiteStatement.bindLong(15, trxDetailsDO.trxDetailId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, trxDetailsDO.trxDetailId);
                    insertSqLiteStatement.bindString(2,trxDetailsDO.trxCode);
                    insertSqLiteStatement.bindLong(3, trxDetailsDO.productId);
                    insertSqLiteStatement.bindDouble(4, trxDetailsDO.sequence);
                    insertSqLiteStatement.bindDouble(5, trxDetailsDO.quantity);
                    insertSqLiteStatement.bindDouble(6, trxDetailsDO.unitPrice);
                    insertSqLiteStatement.bindDouble(7, trxDetailsDO.totalAmountWODiscount);
                    insertSqLiteStatement.bindDouble(8, trxDetailsDO.discountAmount);
                    insertSqLiteStatement.bindLong(9, trxDetailsDO.discountRefId);
                    insertSqLiteStatement.bindString(10,trxDetailsDO.reference);
                    insertSqLiteStatement.bindDouble(11, trxDetailsDO.orderAmount);
                    insertSqLiteStatement.bindDouble(12, trxDetailsDO.initialQuantity);
                    insertSqLiteStatement.bindString(13,trxDetailsDO.UOM);
                    insertSqLiteStatement.bindLong(14,trxDetailsDO.VAT);
                    insertSqLiteStatement.bindDouble(15,trxDetailsDO.TotalVAT);
                    insertSqLiteStatement.executeInsert();
                }
            }
            return true;

        }catch (Exception e){
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrdersDetails() - ended");
        }
        return false;
    }

    private boolean insertOrderAddress(ArrayList<AddressBookDO> arrAddressBookDOs){

        LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrderAddress() - strated");

        SQLiteDatabase sqLiteDatabase = DatabaseHelper.openDataBase();
        SQLiteStatement insertSqLiteStatement = null;
        SQLiteStatement updateSqLiteStatement = null;
        try{
            insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblTrxAddress(TrxAddressId,TrxCode,AddressType,AddressBookId,SessionId,FirstName," +
                    "MiddleName,LastName,AddressLine1,AddressLine2,AddressLine3,City,AddressLine4,Country,ZipCode,Phone,Email,MobileNumber,FlatNumber,VillaName,Street,AddressLine1_Arabic,AddressLine2_Arabic,AddressLine4_Arabic) " +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblTrxAddress set TrxCode=?,AddressType=?," +
                    "AddressBookId=?,SessionId=?,FirstName=?,MiddleName=?,LastName=?,AddressLine1=?,AddressLine2=?,AddressLine3=?," +
                    "City=?,AddressLine4=?,Country=?,ZipCode=?,Phone=?,Email=?,MobileNumber=?,FlatNumber=?,VillaName=?,Street=?,AddressLine1_Arabic=?,AddressLine2_Arabic=?,AddressLine4_Arabic=? where TrxAddressId = ?");
            for(AddressBookDO addressBookDO : arrAddressBookDOs) {
                updateSqLiteStatement.bindString(1, addressBookDO.trxCode);
                updateSqLiteStatement.bindString(2,addressBookDO.addressType);
                updateSqLiteStatement.bindLong(3, addressBookDO.addressBookId);
                updateSqLiteStatement.bindString(4, addressBookDO.sessionId);
                updateSqLiteStatement.bindString(5,addressBookDO.firstName);
                updateSqLiteStatement.bindString(6, addressBookDO.middleName);
                updateSqLiteStatement.bindString(7, addressBookDO.lastName);
                updateSqLiteStatement.bindString(8, addressBookDO.addressLine1);
                updateSqLiteStatement.bindString(9, addressBookDO.addressLine2);
                updateSqLiteStatement.bindString(10, addressBookDO.addressLine3);
                updateSqLiteStatement.bindString(11, addressBookDO.city);
                updateSqLiteStatement.bindString(12, addressBookDO.addressLine4);
                updateSqLiteStatement.bindString(13, addressBookDO.country);
                updateSqLiteStatement.bindString(14, addressBookDO.zipCode);
                updateSqLiteStatement.bindString(15, addressBookDO.phoneNo);
                updateSqLiteStatement.bindString(16, addressBookDO.email);
                updateSqLiteStatement.bindString(17, addressBookDO.mobileNo);
                updateSqLiteStatement.bindString(18, addressBookDO.FlatNumber);
                updateSqLiteStatement.bindString(19, addressBookDO.VillaName);
                updateSqLiteStatement.bindString(20, addressBookDO.Street);
                updateSqLiteStatement.bindString(21, addressBookDO.addressLine1_Arabic);
                updateSqLiteStatement.bindString(22, addressBookDO.addressLine2_Arabic);
                updateSqLiteStatement.bindString(23, addressBookDO.addressLine4_Arabic);
                updateSqLiteStatement.bindLong(24, addressBookDO.trxAddressId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1,addressBookDO.trxAddressId);
                    insertSqLiteStatement.bindString(2, addressBookDO.trxCode);
                    insertSqLiteStatement.bindString(3,addressBookDO.addressType);
                    insertSqLiteStatement.bindLong(4, addressBookDO.addressBookId);
                    insertSqLiteStatement.bindString(5, addressBookDO.sessionId);
                    insertSqLiteStatement.bindString(6,addressBookDO.firstName);
                    insertSqLiteStatement.bindString(7, addressBookDO.middleName);
                    insertSqLiteStatement.bindString(8,addressBookDO.lastName);
                    insertSqLiteStatement.bindString(9, addressBookDO.addressLine1);
                    insertSqLiteStatement.bindString(10, addressBookDO.addressLine2);
                    insertSqLiteStatement.bindString(11, addressBookDO.addressLine3);
                    insertSqLiteStatement.bindString(12, addressBookDO.city);
                    insertSqLiteStatement.bindString(13, addressBookDO.addressLine4);
                    insertSqLiteStatement.bindString(14, addressBookDO.country);
                    insertSqLiteStatement.bindString(15, addressBookDO.zipCode);
                    insertSqLiteStatement.bindString(16, addressBookDO.phoneNo);
                    insertSqLiteStatement.bindString(17, addressBookDO.email);
                    insertSqLiteStatement.bindString(18, addressBookDO.mobileNo);
                    insertSqLiteStatement.bindString(19, addressBookDO.FlatNumber);
                    insertSqLiteStatement.bindString(20, addressBookDO.VillaName);
                    insertSqLiteStatement.bindString(21,addressBookDO.Street);
                    insertSqLiteStatement.bindString(22,addressBookDO.addressLine1_Arabic);
                    insertSqLiteStatement.bindString(23,addressBookDO.addressLine2_Arabic);
                    insertSqLiteStatement.bindString(24,addressBookDO.addressLine4_Arabic);
                    insertSqLiteStatement.executeInsert();
                }
            }
            return true;
        }catch (Exception e){
        /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            if(insertSqLiteStatement !=null)
                insertSqLiteStatement.close();
            if(updateSqLiteStatement !=null)
                updateSqLiteStatement.close();
            LogUtils.debug(LogUtils.LOG_TAG,"OrderDA-insertOrderAddress() - ended");
        }
        return false;
    }

    public ArrayList<TrxHeaderDO> getOrders(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<TrxHeaderDO> arrTrxHeaderDo = null;
            try {
                /*String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader where RefCode='' order by TrxDate desc";*/
               /* String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN TotalAmount ELSE (SELECT TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader TH where RefCode='' order by TrxId desc";
*/
//                String query = "SELECT TH.TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
//                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN TotalAmount ELSE (SELECT " +
//                        "TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt,IsCOD,IsGift,IsGiftWrap,Message," +
//                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode,CouponDiscount," +
//                        "Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId,OrderNumber,SpecialInstructions,OrderName," +
//                        "IsRecurring,DeliveryDate " +
//                        "from tblTrxHeader TH  left outer join tblOrderNumber DN on TH.TrxId = DN .TrxId " +
//                        "where RefCode='' and IsRecurring = 'false' collate nocase order by TH.TrxId desc";
                //vinod
                String query = "SELECT TH.TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN TotalAmount ELSE (SELECT " +
                        "TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode,CouponDiscount," +
                        "Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId,OrderNumber,SpecialInstructions,OrderName," +
                        "IsRecurring,DeliveryDate, TH.TotalVAT, TH.cancelledon " +
                        "from tblTrxHeader TH  left outer join tblOrderNumber DN on TH.TrxId = DN .TrxId " +
                        "where RefCode='' and IsRecurring = 'false' collate nocase AND TH.trxstatus !=1  order by TH.TrxId desc";
//trxstatus =1 if payment has not done for Rossom and those details are need not to show in MYORDERS..

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrTrxHeaderDo = new ArrayList<>();
                    do {
                        TrxHeaderDO trxHeaderDO                 = new TrxHeaderDO();
                        trxHeaderDO.trxId                       = cursor.getInt(0);
                        trxHeaderDO.trxCode                     = cursor.getString(1);
                        trxHeaderDO.trxRefCode                  = cursor.getString(2);
                        trxHeaderDO.trxDate                     = cursor.getString(3);
                        trxHeaderDO.trxType                     = cursor.getString(4);
                        trxHeaderDO.userId                      = cursor.getInt(5);
                        trxHeaderDO.trxStatus                   = cursor.getInt(6);
                        trxHeaderDO.productSubTotalWDiscount    = cursor.getDouble(7);
                        trxHeaderDO.totalAmountWODiscount       = cursor.getDouble(8);
                        trxHeaderDO.discountAmt                 = cursor.getDouble(9);
                        trxHeaderDO.shippingCharges             = cursor.getDouble(10);
                        trxHeaderDO.totalTrxAmount              = cursor.getDouble(11);
                        trxHeaderDO.isCOD                       = StringUtils.getBoolean(cursor.getString(12));
                        trxHeaderDO.isGiftCard                  = StringUtils.getBoolean(cursor.getString(13));
                        trxHeaderDO.isGiftWrap                  = StringUtils.getBoolean(cursor.getString(14));
                        trxHeaderDO.message                     = cursor.getString(15);
                        trxHeaderDO.giftCardUsed                = cursor.getString(16);
                        trxHeaderDO.giftCardAmount              = cursor.getDouble(17);
                        trxHeaderDO.promotionId                 = cursor.getInt(18);
                        trxHeaderDO.promotionDiscount           = cursor.getDouble(19);
                        trxHeaderDO.isCouponCodeApplicable      = StringUtils.getBoolean(cursor.getString(20));
                        trxHeaderDO.couponCode                  = cursor.getString(21);
                        trxHeaderDO.couponAmount                = cursor.getDouble(22);
                        trxHeaderDO.isCancelled                 = StringUtils.getBoolean(cursor.getString(23));
                        trxHeaderDO.userMessage                 = cursor.getString(24);
                        trxHeaderDO.shippingStatus              = cursor.getString(25);
                        trxHeaderDO.docketNumber                = cursor.getString(26);
                        trxHeaderDO.walletAmount                = cursor.getDouble(27);
                        trxHeaderDO.sessionId                   = cursor.getString(28);
                        trxHeaderDO.trxDisplayCode              = (cursor.getString(29) == null)?trxHeaderDO.trxCode :cursor.getString(29);
                        trxHeaderDO.specialInstructions         = cursor.getString(30);
                        trxHeaderDO.orderName                   = cursor.getString(31);
                        trxHeaderDO.isRecurring                 = StringUtils.getBoolean(cursor.getString(32));
                        trxHeaderDO.deliveryDate                = cursor.getString(33);
                        trxHeaderDO.TotalVAT                = cursor.getDouble(34);
                        trxHeaderDO.CancelledOn                = cursor.getString(35);
//                        trxHeaderDO.trxDisplayCode              = trxHeaderDO.trxCode;
                        trxHeaderDO.trxDetailsDOs               = getOrderDetails(sqLiteDatabase, trxHeaderDO.trxCode);
                        trxHeaderDO.trxAddressBookDOs           = getOrderAddress(sqLiteDatabase, trxHeaderDO.trxCode);
                        arrTrxHeaderDo.add(trxHeaderDO);
                    } while (cursor.moveToNext());
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
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - ended");
            }
            return arrTrxHeaderDo;
        }
    }

    public ArrayList<TrxHeaderDO> getRecurringOrders(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<TrxHeaderDO> arrTrxHeaderDo = null;
            try {
                /*String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader where RefCode='' order by TrxDate desc";*/
               /* String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN TotalAmount ELSE (SELECT TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader TH where RefCode='' order by TrxId desc";
*/
                String query = "SELECT TH.TrxId, TH.TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN" +
                        " TotalAmount ELSE (SELECT TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt," +
                        "IsCOD,IsGift,IsGiftWrap,Message,GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount," +
                        "IsCouponCodeApplicable,CouponCode,CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount," +
                        "SessionId,OrderNumber,SpecialInstructions,OrderName,IsRecurring,DeliveryDate" +
                        ",RO.RecurringOrderId,RO.Frequency,RO.NumberOfRepeatations,RO.RemovedFromRecurring,RO.RemovedFromRecurringOn," +
                        "RO.ReasonForRemovalFromRecurring, TH.TotalVAT " +
                        "from tblTrxHeader TH  inner join tblRecurringOrder RO on TH.TrxCode == RO.TrxCode  " +
                        "left outer join tblOrderNumber DN on TH.TrxId = DN .TrxId " +
                        "where RefCode='' order by TH.TrxId desc";

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrTrxHeaderDo = new ArrayList<>();
                    do {
                        TrxHeaderDO trxHeaderDO                 = new TrxHeaderDO();
                        trxHeaderDO.trxId                       = cursor.getInt(0);
                        trxHeaderDO.trxCode                     = cursor.getString(1);
                        trxHeaderDO.trxRefCode                  = cursor.getString(2);
                        trxHeaderDO.trxDate                     = cursor.getString(3);
                        trxHeaderDO.trxType                     = cursor.getString(4);
                        trxHeaderDO.userId                      = cursor.getInt(5);
                        trxHeaderDO.trxStatus                   = cursor.getInt(6);
                        trxHeaderDO.productSubTotalWDiscount    = cursor.getDouble(7);
                        trxHeaderDO.totalAmountWODiscount       = cursor.getDouble(8);
                        trxHeaderDO.discountAmt                 = cursor.getDouble(9);
                        trxHeaderDO.shippingCharges             = cursor.getDouble(10);
                        trxHeaderDO.totalTrxAmount              = cursor.getDouble(11);
                        trxHeaderDO.isCOD                       = StringUtils.getBoolean(cursor.getString(12));
                        trxHeaderDO.isGiftCard                  = StringUtils.getBoolean(cursor.getString(13));
                        trxHeaderDO.isGiftWrap                  = StringUtils.getBoolean(cursor.getString(14));
                        trxHeaderDO.message                     = cursor.getString(15);
                        trxHeaderDO.giftCardUsed                = cursor.getString(16);
                        trxHeaderDO.giftCardAmount              = cursor.getDouble(17);
                        trxHeaderDO.promotionId                 = cursor.getInt(18);
                        trxHeaderDO.promotionDiscount           = cursor.getDouble(19);
                        trxHeaderDO.isCouponCodeApplicable      = StringUtils.getBoolean(cursor.getString(20));
                        trxHeaderDO.couponCode                  = cursor.getString(21);
                        trxHeaderDO.couponAmount                = cursor.getDouble(22);
                        trxHeaderDO.isCancelled                 = StringUtils.getBoolean(cursor.getString(23));
                        trxHeaderDO.userMessage                 = cursor.getString(24);
                        trxHeaderDO.shippingStatus              = cursor.getString(25);
                        trxHeaderDO.docketNumber                = cursor.getString(26);
                        trxHeaderDO.walletAmount                = cursor.getDouble(27);
                        trxHeaderDO.sessionId                   = cursor.getString(28);
                        trxHeaderDO.trxDisplayCode              = (cursor.getString(29) == null)?trxHeaderDO.trxCode :cursor.getString(29);
                        trxHeaderDO.specialInstructions         = cursor.getString(30);
                        trxHeaderDO.orderName                   = cursor.getString(31);
                        trxHeaderDO.isRecurring                 = StringUtils.getBoolean(cursor.getString(32));
                        trxHeaderDO.deliveryDate                = cursor.getString(33);
                        trxHeaderDO.recurringOrderDO            = new RecurringOrderDO();
                        trxHeaderDO.recurringOrderDO.RecurringOrderId                = cursor.getInt(34);
                        trxHeaderDO.recurringOrderDO.TrxCode                         = trxHeaderDO.trxCode;
                        trxHeaderDO.recurringOrderDO.Frequency                       = cursor.getString(35);
                        trxHeaderDO.recurringOrderDO.NumberOfRepeatations            = cursor.getInt(36);
                        trxHeaderDO.recurringOrderDO.RemovedFromRecurring            = StringUtils.getBoolean(cursor.getString(37));
                        trxHeaderDO.recurringOrderDO.RemovedFromRecurringOn          = cursor.getString(38);
                        trxHeaderDO.recurringOrderDO. ReasonForRemovalFromRecurring         = cursor.getString(39);
                        trxHeaderDO.TotalVAT          = cursor.getDouble(40);
                        trxHeaderDO.trxDetailsDOs               = getOrderDetails(sqLiteDatabase, trxHeaderDO.trxCode);
                        trxHeaderDO.trxAddressBookDOs           = getOrderAddress(sqLiteDatabase, trxHeaderDO.trxCode);
                        arrTrxHeaderDo.add(trxHeaderDO);
                    } while (cursor.moveToNext());
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
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - ended");
            }
            return arrTrxHeaderDo;
        }
    }
    public ArrayList<TrxHeaderDO> getOrdersToBeDelivery(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<TrxHeaderDO> arrTrxHeaderDo = null;
            try {
                /*String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader where RefCode='' order by TrxDate desc";*/
               /* String query = "SELECT TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,(CASE WHEN TotalAmount > 0 THEN TotalAmount ELSE (SELECT TotalAmount from tblTrxHeader where RefCode = TH.TrxCode ) END )as totalAmt,IsCOD,IsGift,IsGiftWrap,Message," +
                        "GiftCardUsed,GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode," +
                        "CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo,WalletAmount,SessionId from tblTrxHeader TH where RefCode='' order by TrxId desc";
*/
                String query = "SELECT TH.TrxId, TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount,TotalAmountWODiscount," +
                        "DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message,GiftCardUsed,GiftCardDiscount,PromotionId," +
                        "PromotionDiscount,IsCouponCodeApplicable,CouponCode,CouponDiscount,Is_Canceled,UserMessage,ShippingStatus,DocketNo," +
                        "WalletAmount,SessionId,OrderNumber,SpecialInstructions,OrderName,IsRecurring,DeliveryDate from tblTrxHeader TH  " +
                        "left outer join tblOrderNumber DN on TH.TrxId = DN .TrxId where RefCode='' and Is_Canceled != 'True' collate nocase and TrxStatus != 100 " +
                        "and IsRecurring  != 'True' collate nocase order by TH.TrxId desc";

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrTrxHeaderDo = new ArrayList<>();
                    do {
                        TrxHeaderDO trxHeaderDO                 = new TrxHeaderDO();
                        trxHeaderDO.trxId                       = cursor.getInt(0);
                        trxHeaderDO.trxCode                     = cursor.getString(1);
                        trxHeaderDO.trxRefCode                  = cursor.getString(2);
                        trxHeaderDO.trxDate                     = cursor.getString(3);
                        trxHeaderDO.trxType                     = cursor.getString(4);
                        trxHeaderDO.userId                      = cursor.getInt(5);
                        trxHeaderDO.trxStatus                   = cursor.getInt(6);
                        trxHeaderDO.productSubTotalWDiscount    = cursor.getDouble(7);
                        trxHeaderDO.totalAmountWODiscount       = cursor.getDouble(8);
                        trxHeaderDO.discountAmt                 = cursor.getDouble(9);
                        trxHeaderDO.shippingCharges             = cursor.getDouble(10);
                        trxHeaderDO.totalTrxAmount              = cursor.getDouble(11);
                        trxHeaderDO.isCOD                       = StringUtils.getBoolean(cursor.getString(12));
                        trxHeaderDO.isGiftCard                  = StringUtils.getBoolean(cursor.getString(13));
                        trxHeaderDO.isGiftWrap                  = StringUtils.getBoolean(cursor.getString(14));
                        trxHeaderDO.message                     = cursor.getString(15);
                        trxHeaderDO.giftCardUsed                = cursor.getString(16);
                        trxHeaderDO.giftCardAmount              = cursor.getDouble(17);
                        trxHeaderDO.promotionId                 = cursor.getInt(18);
                        trxHeaderDO.promotionDiscount           = cursor.getDouble(19);
                        trxHeaderDO.isCouponCodeApplicable      = StringUtils.getBoolean(cursor.getString(20));
                        trxHeaderDO.couponCode                  = cursor.getString(21);
                        trxHeaderDO.couponAmount                = cursor.getDouble(22);
                        trxHeaderDO.isCancelled                 = StringUtils.getBoolean(cursor.getString(23));
                        trxHeaderDO.userMessage                 = cursor.getString(24);
                        trxHeaderDO.shippingStatus              = cursor.getString(25);
                        trxHeaderDO.docketNumber                = cursor.getString(26);
                        trxHeaderDO.walletAmount                = cursor.getDouble(27);
                        trxHeaderDO.sessionId                   = cursor.getString(28);
                        trxHeaderDO.trxDisplayCode              = (cursor.getString(29) == null)?trxHeaderDO.trxCode :cursor.getString(29);
                        trxHeaderDO.specialInstructions         = cursor.getString(30);
                        trxHeaderDO.orderName                   = cursor.getString(31);
                        trxHeaderDO.isRecurring                 = StringUtils.getBoolean(cursor.getString(32));
                        trxHeaderDO.deliveryDate                = cursor.getString(33);
//                        trxHeaderDO.trxDisplayCode              = trxHeaderDO.trxCode;
                        trxHeaderDO.trxDetailsDOs               = getOrderDetails(sqLiteDatabase, trxHeaderDO.trxCode);
                        trxHeaderDO.trxAddressBookDOs           = getOrderAddress(sqLiteDatabase, trxHeaderDO.trxCode);
                        arrTrxHeaderDo.add(trxHeaderDO);
                    } while (cursor.moveToNext());
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                String recurringQuery = "SELECT TH.TrxId, TH.TrxCode,RefCode,TrxDate,TrxType,UserId,TrxStatus,ProductSubTotalWDiscount," +
                        "TotalAmountWODiscount,DiscountAmount,ShippingCharges,TotalAmount,IsCOD,IsGift,IsGiftWrap,Message,GiftCardUsed," +
                        "GiftCardDiscount,PromotionId,PromotionDiscount,IsCouponCodeApplicable,CouponCode,CouponDiscount,Is_Canceled,UserMessage," +
                        "ShippingStatus,DocketNo,WalletAmount,SessionId,OrderNumber,SpecialInstructions,OrderName,IsRecurring,DeliveryDate," +
                        "RO.RecurringOrderId,RO.Frequency,RO.NumberOfRepeatations,RO.RemovedFromRecurring,RO.RemovedFromRecurringOn," +
                        "RO.ReasonForRemovalFromRecurring from tblTrxHeader TH inner join tblRecurringOrder RO on TH.TrxCode == RO.TrxCode  " +
                        "left outer join tblOrderNumber DN on TH.TrxId = DN .TrxId where RefCode='' and Is_Canceled != 'True' collate nocase and TrxStatus != 100 " +
                        "and IsRecurring  == 'True' collate nocase order by TH.TrxId desc";

                cursor = sqLiteDatabase.rawQuery(recurringQuery,null);
                if (cursor.moveToFirst()) {
                    if(arrTrxHeaderDo == null)
                        arrTrxHeaderDo = new ArrayList<>();
                    do {
                        TrxHeaderDO trxHeaderDO                 = new TrxHeaderDO();
                        trxHeaderDO.trxId                       = cursor.getInt(0);
                        trxHeaderDO.trxCode                     = cursor.getString(1);
                        trxHeaderDO.trxRefCode                  = cursor.getString(2);
                        trxHeaderDO.trxDate                     = cursor.getString(3);
                        trxHeaderDO.trxType                     = cursor.getString(4);
                        trxHeaderDO.userId                      = cursor.getInt(5);
                        trxHeaderDO.trxStatus                   = cursor.getInt(6);
                        trxHeaderDO.productSubTotalWDiscount    = cursor.getDouble(7);
                        trxHeaderDO.totalAmountWODiscount       = cursor.getDouble(8);
                        trxHeaderDO.discountAmt                 = cursor.getDouble(9);
                        trxHeaderDO.shippingCharges             = cursor.getDouble(10);
                        trxHeaderDO.totalTrxAmount              = cursor.getDouble(11);
                        trxHeaderDO.isCOD                       = StringUtils.getBoolean(cursor.getString(12));
                        trxHeaderDO.isGiftCard                  = StringUtils.getBoolean(cursor.getString(13));
                        trxHeaderDO.isGiftWrap                  = StringUtils.getBoolean(cursor.getString(14));
                        trxHeaderDO.message                     = cursor.getString(15);
                        trxHeaderDO.giftCardUsed                = cursor.getString(16);
                        trxHeaderDO.giftCardAmount              = cursor.getDouble(17);
                        trxHeaderDO.promotionId                 = cursor.getInt(18);
                        trxHeaderDO.promotionDiscount           = cursor.getDouble(19);
                        trxHeaderDO.isCouponCodeApplicable      = StringUtils.getBoolean(cursor.getString(20));
                        trxHeaderDO.couponCode                  = cursor.getString(21);
                        trxHeaderDO.couponAmount                = cursor.getDouble(22);
                        trxHeaderDO.isCancelled                 = StringUtils.getBoolean(cursor.getString(23));
                        trxHeaderDO.userMessage                 = cursor.getString(24);
                        trxHeaderDO.shippingStatus              = cursor.getString(25);
                        trxHeaderDO.docketNumber                = cursor.getString(26);
                        trxHeaderDO.walletAmount                = cursor.getDouble(27);
                        trxHeaderDO.sessionId                   = cursor.getString(28);
                        trxHeaderDO.trxDisplayCode              = (cursor.getString(29) == null)?trxHeaderDO.trxCode :cursor.getString(29);
                        trxHeaderDO.specialInstructions         = cursor.getString(30);
                        trxHeaderDO.orderName                   = cursor.getString(31);
                        trxHeaderDO.isRecurring                 = StringUtils.getBoolean(cursor.getString(32));
                        trxHeaderDO.deliveryDate                = cursor.getString(33);
                        trxHeaderDO.recurringOrderDO            = new RecurringOrderDO();
                        trxHeaderDO.recurringOrderDO.RecurringOrderId                = cursor.getInt(34);
                        trxHeaderDO.recurringOrderDO.TrxCode                         = trxHeaderDO.trxCode;
                        trxHeaderDO.recurringOrderDO.Frequency                       = cursor.getString(35);
                        trxHeaderDO.recurringOrderDO.NumberOfRepeatations            = cursor.getInt(36);
                        trxHeaderDO.recurringOrderDO.RemovedFromRecurring            = StringUtils.getBoolean(cursor.getString(37));
                        trxHeaderDO.recurringOrderDO.RemovedFromRecurringOn          = cursor.getString(38);
                        trxHeaderDO.recurringOrderDO.ReasonForRemovalFromRecurring   = cursor.getString(39);
//                        trxHeaderDO.trxDisplayCode              = trxHeaderDO.trxCode;
                        trxHeaderDO.trxDetailsDOs               = getOrderDetails(sqLiteDatabase, trxHeaderDO.trxCode);
                        trxHeaderDO.trxAddressBookDOs           = getOrderAddress(sqLiteDatabase, trxHeaderDO.trxCode);
                        arrTrxHeaderDo.add(trxHeaderDO);
                    } while (cursor.moveToNext());
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
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrders() - ended");
            }
            return arrTrxHeaderDo;
        }
    }

    private ArrayList<TrxDetailsDO> getOrderDetails(SQLiteDatabase sqLiteDatabase,String trxCode){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrderDetails() - strated");
            Cursor cursor = null;
            ArrayList<TrxDetailsDO> arrTrxDetailsDOs = null;
            try {
                String query = "SELECT TD.TrxDetailId,TD.TrxCode,P.ProductId,TD.Sequence,TD.Quantity,TD.UnitPrice," +
                        "TD.TotalAmountWODiscount,TD.DiscountAmount,TD.DiscountRefId,TD.Reference,TD.TotalAmount,TD.InitialQuantity," +
                        "TD.UOM,P.Code,P.Name,P.Description,P.AltDescription,PI.OriginalImage,PI.SmallImage,PI.MediumImage,PI.LargeImage,TD.VAT, TD.TotalVAT " +
                        "from tblTrxDetail TD Inner Join tblProduct P on TD.ProductId = P.ProductId " +
                        "Inner Join tblProductImages PI ON P.ProductId = PI.ProductId AND PI.ISDefault = 'True' collate nocase where TD.TrxCode='"+trxCode+"'";

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrTrxDetailsDOs = new ArrayList<>();
                    do {
                        TrxDetailsDO trxDetailsDO                           = new TrxDetailsDO();
                        trxDetailsDO.trxDetailId                            = cursor.getInt(0);
                        trxDetailsDO.trxCode                                = cursor.getString(1);
                        trxDetailsDO.productId                              = cursor.getInt(2);
                        trxDetailsDO.sequence                               = cursor.getInt(3);
                        trxDetailsDO.quantity                               = cursor.getDouble(4);
                        trxDetailsDO.unitPrice                              = cursor.getDouble(5);
                        trxDetailsDO.totalAmountWODiscount                  = cursor.getDouble(6);
                        trxDetailsDO.discountAmount                         = cursor.getDouble(7);
                        trxDetailsDO.discountRefId                          = cursor.getInt(8);
                        trxDetailsDO.reference                              = cursor.getString(9);
                        trxDetailsDO.orderAmount                            = cursor.getDouble(10);
                        trxDetailsDO.initialQuantity                        = cursor.getInt(11);
                        trxDetailsDO.UOM                                    = cursor.getString(12);
                        trxDetailsDO.productCode                            = cursor.getString(13);
                        trxDetailsDO.productName                            = cursor.getString(14);
                        trxDetailsDO.productDescription                     = cursor.getString(15);
                        trxDetailsDO.productALTDescription                  = cursor.getString(16);
                        trxDetailsDO.originalImage                          = StringUtils.replaceAll("(%20)|[~]",cursor.getString(17),"");
                        trxDetailsDO.smallImage                             = cursor.getString(18);
                        trxDetailsDO.mediumImage                            = cursor.getString(19);
                        trxDetailsDO.largeImage                             = cursor.getString(20);
                        trxDetailsDO.VAT                             = cursor.getInt(21);
                        trxDetailsDO.TotalVAT                             = cursor.getDouble(22);
                        arrTrxDetailsDOs.add(trxDetailsDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrderDetails() - ended");
            }
            return arrTrxDetailsDOs;
        }
    }

    private ArrayList<AddressBookDO> getOrderAddress(SQLiteDatabase sqLiteDatabase,String trxCode){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrderAddress() - strated");
            Cursor cursor = null;
            ArrayList<AddressBookDO> arrAddressBookDOs = null;
            try {
                String query = "SELECT TrxAddressId,TrxCode,AddressType,AddressBookId,SessionId,FirstName,MiddleName," +
                        "LastName,AddressLine1,AddressLine2,AddressLine3,City,AddressLine4,Country,ZipCode,Phone,Email,MobileNumber,FlatNumber,VillaName,Street,AddressLine1_Arabic,AddressLine2_Arabic,AddressLine4_Arabic" +
                        " from tblTrxAddress where TrxCode = '"+trxCode+"'";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrAddressBookDOs = new ArrayList<>();
                    do {
                        AddressBookDO addressBookDO           = new AddressBookDO();
                        addressBookDO.trxAddressId            = cursor.getInt(0);
                        addressBookDO.trxCode                 = cursor.getString(1);
                        addressBookDO.addressType             = cursor.getString(2);
                        addressBookDO.addressBookId           = cursor.getInt(3);
                        addressBookDO.sessionId               = cursor.getString(4);
                        addressBookDO.firstName               = cursor.getString(5);
                        addressBookDO.middleName              = cursor.getString(6);
                        addressBookDO.lastName                = cursor.getString(7);
                        addressBookDO.addressLine1            = cursor.getString(8);
                        addressBookDO.addressLine2            = cursor.getString(9);
                        addressBookDO.addressLine3            = cursor.getString(10);
                        addressBookDO.city                    = cursor.getString(11);
                        addressBookDO.addressLine4            = cursor.getString(12);
                        addressBookDO.country                 = cursor.getString(13);
                        addressBookDO.zipCode                 = cursor.getString(14);
                        addressBookDO.phoneNo                 = cursor.getString(15);
                        addressBookDO.email                   = cursor.getString(16);
                        addressBookDO.mobileNo                = cursor.getString(17);
                        addressBookDO.FlatNumber              = cursor.getString(18);
                        addressBookDO.VillaName               = cursor.getString(19);
                        addressBookDO.Street                  = cursor.getString(20);
                        addressBookDO.addressLine1_Arabic     = cursor.getString(21);
                        addressBookDO.addressLine2_Arabic     = cursor.getString(22);
                        addressBookDO.addressLine4_Arabic     = cursor.getString(23);
                        arrAddressBookDOs.add(addressBookDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
               /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getOrderAddress() - ended");
            }
            return arrAddressBookDOs;
        }
    }

    public ArrayList<String> getCancellationReasons(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<String> arrOrderCancelDOs = null;
            try {
                String query = "SELECT * from tblOrderCancellationReasonCategory";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrOrderCancelDOs = new ArrayList<>();
                    arrOrderCancelDOs.add("Select Reason for Cancellation");
                    do {
                        arrOrderCancelDOs.add(cursor.getString(1));
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - ended");
            }
            return arrOrderCancelDOs;
        }
    }

    public HashMap<String,Integer> getMaxQuantityForEachProduct(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getting max quantity for item () - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            HashMap<String,Integer> hmMaxQty = new HashMap<String,Integer>();
            try {
                String query = "SELECT tp.code, tol.Quantity from tblOrderLimit  tol inner join tblProduct tp on tp.productId = tol.productId";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    do {
                        hmMaxQty.put (cursor.getString(0),cursor.getInt(1));
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return hmMaxQty;
        }
    }
    public ArrayList<OrderCancelDO> getCancellationReasonsObjs(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<OrderCancelDO> arrOrderCancelDOs = null;
            OrderCancelDO orderCancelDO = null;
            try {
                String query = "SELECT * from tblOrderCancellationReasonCategory";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrOrderCancelDOs = new ArrayList<>();
                    orderCancelDO =new OrderCancelDO();
                    orderCancelDO.Category = "Select Reason for Cancellation";
                    orderCancelDO.AltDescription = "الرجاءاختيار السبب";
                    arrOrderCancelDOs.add(orderCancelDO);
                    do {
                        orderCancelDO = new OrderCancelDO();
                        orderCancelDO.Category = cursor.getString(cursor.getColumnIndex("Category"));
                        orderCancelDO.AltDescription = cursor.getString(cursor.getColumnIndex("AltDescription"));
                        arrOrderCancelDOs.add(orderCancelDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
              /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - ended");
            }
            return arrOrderCancelDOs;
        }
    }

    /*public ArrayList<OrderCancelDO> getCancellationReasons(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<OrderCancelDO> arrOrderCancelDOs = null;
            try {
                String query = "SELECT * from tblOrderCancellationReasonCategory";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrOrderCancelDOs = new ArrayList<>();
                    do {
                        OrderCancelDO cancelDO                  = new OrderCancelDO();
                        cancelDO.Id            = cursor.getInt(0);
                        cancelDO.Category                 = cursor.getString(1);
                        cancelDO.ParentId           = cursor.getInt(3);
                        cancelDO.CreatedDate               = cursor.getString(4);
                        cancelDO.CreatedBy           = cursor.getInt(3);
                        cancelDO.ModifiedDate              = cursor.getString(18);
                        cancelDO.ModifiedBy           = cursor.getInt(3);
                        arrOrderCancelDOs.add(cancelDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "OrderDA - getCancellationReasons() - ended");
            }
            return arrOrderCancelDOs;
        }
    }*/

}
