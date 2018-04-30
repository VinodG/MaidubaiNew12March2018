package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Girish Velivela on 24-08-2016.
 */
public class CartDA {

    private Context context;
    public CartDA(Context context){
        this.context = context;
    }

    public boolean insertCartList(ArrayList<CartListDO> arrCartListDOs){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - insertCartList() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            SQLiteStatement insertSqLiteStatement = null;
            SQLiteStatement updateSqLiteStatement = null;
            try {
                insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblShoppingCart(ShoppingCartId,SessionId,UserId," +
                        "ProductCode,Quantity,UnitPrice,TotalPriceWODiscount,DiscountRefId,DiscountAmount,TotalPrice,ModifiedOn,UOM,VAT,TotalVAT) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,? ,?,?)");
                updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblShoppingCart set SessionId=?,UserId=?," +
                        "ProductCode=?,Quantity=?,UnitPrice=?,TotalPriceWODiscount=?,DiscountRefId=?,DiscountAmount=?,TotalPrice=?,ModifiedOn=?,UOM=?,VAT=?, TotalVAT=? where ShoppingCartId = ?");

                for(CartListDO cartListDO : arrCartListDOs) {
                    updateSqLiteStatement.bindString(1, cartListDO.sessionId);
                    updateSqLiteStatement.bindLong(2, cartListDO.userId);
                    updateSqLiteStatement.bindString(3, cartListDO.productCode);
                    updateSqLiteStatement.bindDouble(4, cartListDO.quantity);
                    updateSqLiteStatement.bindDouble(5, cartListDO.unitPrice);
                    updateSqLiteStatement.bindDouble(6, cartListDO.totalPriceWODiscount);
                    updateSqLiteStatement.bindDouble(7, cartListDO.discountedUnitPrice);
                    updateSqLiteStatement.bindDouble(8, cartListDO.discountAmount);
                    updateSqLiteStatement.bindDouble(9, cartListDO.totalPrice);
                    updateSqLiteStatement.bindString(10, cartListDO.modifiedOn);
                    updateSqLiteStatement.bindString(11, cartListDO.UOM);
                    updateSqLiteStatement.bindLong(12, cartListDO.vatPerc);
                    updateSqLiteStatement.bindDouble(13, cartListDO.vatAmount);
                    updateSqLiteStatement.bindLong(14, cartListDO.shoppingCartId);
                    if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                        insertSqLiteStatement.bindLong(1, cartListDO.shoppingCartId);
                        insertSqLiteStatement.bindString(2, cartListDO.sessionId);
                        insertSqLiteStatement.bindLong(3, cartListDO.userId);
                        insertSqLiteStatement.bindString(4, cartListDO.productCode);
                        insertSqLiteStatement.bindDouble(5, cartListDO.quantity);
                        insertSqLiteStatement.bindDouble(6, cartListDO.unitPrice);
                        insertSqLiteStatement.bindDouble(7, cartListDO.totalPriceWODiscount);
                        insertSqLiteStatement.bindDouble(8, cartListDO.discountedUnitPrice);
                        insertSqLiteStatement.bindDouble(9, cartListDO.discountAmount);
                        insertSqLiteStatement.bindDouble(10, cartListDO.totalPrice);
                        insertSqLiteStatement.bindString(11, cartListDO.modifiedOn);
                        insertSqLiteStatement.bindString(12, cartListDO.UOM);
                        insertSqLiteStatement.bindLong(13, cartListDO.vatPerc);
                        insertSqLiteStatement.bindDouble(14, cartListDO.vatAmount);
                        insertSqLiteStatement.executeInsert();
                    }
                }
                return true;
            } catch (Exception e) {
               /*e.printStackTrace(); */      Log.d("This can never happen", e.getMessage());
            } finally {
                if (insertSqLiteStatement != null)
                    insertSqLiteStatement.close();
                if (updateSqLiteStatement != null)
                    updateSqLiteStatement.close();
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - insertCartList() - ended");
            }
            return false;
        }
    }


    public HashMap<String,CartListDO> getCartList(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getCartList() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            HashMap<String,CartListDO> hmCartList = null;
            try {
                String query = "select S.ShoppingCartId,S.SessionId,S.UserId,S.ProductCode,S.Quantity,S.UnitPrice,S.TotalPriceWODiscount," +
                        "S.DiscountRefId,S.DiscountAmount,S.TotalPrice,S.ModifiedOn,S.UOM,P.Name,PI.OriginalImage,PI.SmallImage," +
//                        "PI.MediumImage, PI.LargeImage " +
                        "PI.MediumImage, PI.LargeImage, P.VAT " +
                        "from tblShoppingCart S inner join tblProduct P on P.Code = S.ProductCode " +
                        "Inner Join tblProductImages PI ON P.ProductId = PI.ProductId ";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    hmCartList = new HashMap<>();
                    do {
                        CartListDO cartListDO = new CartListDO();
                        cartListDO.shoppingCartId = cursor.getInt(0);
                        cartListDO.sessionId = cursor.getString(1);
                        cartListDO.userId = cursor.getInt(2);
                        cartListDO.productCode = cursor.getString(3);
                        cartListDO.quantity = cursor.getInt(4);
                        cartListDO.unitPrice = cursor.getDouble(5);
                        cartListDO.totalPriceWODiscount = cursor.getDouble(6);
                        cartListDO.discountedUnitPrice= cursor.getDouble(7);
                        cartListDO.discountAmount= cursor.getDouble(8);
                        cartListDO.totalPrice= cursor.getDouble(9);
                        cartListDO.modifiedOn= cursor.getString(10);
                        cartListDO.UOM= cursor.getString(11);
                        cartListDO.productName= cursor.getString(12);
                        cartListDO.originalImage = StringUtils.replaceAll("(%20)|[~]",cursor.getString(13),"");
                        cartListDO.smallImage= cursor.getString(14);
                        cartListDO.mediumImage= cursor.getString(15);
                        cartListDO.largeImage= cursor.getString(16);
                        cartListDO.vatPerc= cursor.getInt(17);
                        hmCartList.put(cartListDO.productCode, cartListDO);
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
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getCartList() - ended");
            }
            return hmCartList;
        }
    }


    public ArrayList<CartListDO> getArrCartList(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getArrCartList() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<CartListDO> arrCartListDOs= null;
            try {
                String query = "select S.ShoppingCartId,S.SessionId,S.UserId,S.ProductCode,S.Quantity,S.UnitPrice,S.TotalPriceWODiscount," +
                "S.DiscountRefId,S.DiscountAmount,S.TotalPrice,S.ModifiedOn,S.UOM,P.Name,PI.OriginalImage,PI.SmallImage," +
//                        "PI.MediumImage, PI.LargeImage,P.AltDescription " +
                        "PI.MediumImage, PI.LargeImage,P.AltDescription, S.VAT, S.TotalVAT " +
                        "from tblShoppingCart S inner join tblProduct P on P.Code = S.ProductCode " +
                        "Inner Join tblProductImages PI ON P.ProductId = PI.ProductId ";

                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    arrCartListDOs = new ArrayList<>();
                    do {
                        CartListDO cartListDO = new CartListDO();
                        cartListDO.shoppingCartId = cursor.getInt(0);
                        cartListDO.sessionId = cursor.getString(1);
                        cartListDO.userId = cursor.getInt(2);
                        cartListDO.productCode = cursor.getString(3);
                        cartListDO.quantity = cursor.getInt(4);
                        cartListDO.unitPrice = cursor.getDouble(5);
                        cartListDO.totalPriceWODiscount = cursor.getDouble(6);
                        cartListDO.discountedUnitPrice= cursor.getDouble(7);
                        cartListDO.discountAmount= cursor.getDouble(8);
                        cartListDO.totalPrice= cursor.getDouble(9);
                        cartListDO.modifiedOn= cursor.getString(10);
                        cartListDO.UOM= cursor.getString(11);
                        cartListDO.productName= cursor.getString(12);
                        cartListDO.originalImage = StringUtils.replaceAll("(%20)|[~]",cursor.getString(13),"");
                        cartListDO.smallImage= cursor.getString(14);
                        cartListDO.mediumImage= cursor.getString(15);
                        cartListDO.largeImage= cursor.getString(16);
                        cartListDO.AltDescription= cursor.getString(17);

                        cartListDO.vatPerc = cursor.getInt(18);
                        cartListDO.vatAmount = cursor.getDouble(19);
//                        cartListDO.unitPrice*cartListDO.vatPerc )/(100+cartListDO.vatPerc );
                        arrCartListDOs.add(cartListDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getArrCartList() - ended");
            }
            return arrCartListDOs;
        }
    }

    public ArrayList<ProductDO> getProductOrderLimit(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getProductOrderLimit() - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<ProductDO> arrProductDOs = null;
            try {
                String date = CalendarUtil.getDate(new Date(), CalendarUtil.YYYY_MM_DD_FULL_PATTERN);
                String query = "SELECT Distinct P.ProductId, P.Code, P.Name, P.Description, P.AltDescription FROM tblProduct P inner join tblOrderLimit OL on P.ProductId = OL.ProductId inner join tblShoppingCart SC on P.Code = SC.ProductCode where  StartDate <= '"+date+"' and '"+date+"' <=EndDate and SC.Quantity >= OL.Quantity";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrProductDOs = new ArrayList<>();
                    do{
                        ProductDO productDO = new ProductDO();
                        productDO.ProductId = cursor.getInt(0);
                        productDO.code = cursor.getString(1);
                        productDO.name = cursor.getString(2);
                        productDO.description = cursor.getString(3);
                        arrProductDOs.add(productDO);
                    }while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getProductOrderLimit() - ended");
            }
            return arrProductDOs;
        }
    }

    public ArrayList<ProductDO> getProductMinimumOrderLimit(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getProductOrderLimit() - strated");
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            ArrayList<ProductDO> arrProductDOs = null;
            try {
                String date = CalendarUtil.getDate(new Date(), CalendarUtil.YYYY_MM_DD_FULL_PATTERN);
                String query = "SELECT Distinct P.ProductId, P.Code, P.Name, P.Description, P.AltDescription,MOL.Quantity FROM tblProduct P inner join tblOrderMinLimit MOL on P.ProductId = MOL.ProductId inner join tblShoppingCart SC on P.Code = SC.ProductCode where  StartDate <= '"+date+"' and '"+date+"' <=EndDate and SC.Quantity < MOL.Quantity";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrProductDOs = new ArrayList<>();
                    do{
                        ProductDO productDO = new ProductDO();
                        productDO.ProductId = cursor.getInt(0);
                        productDO.code = cursor.getString(1);
                        productDO.name = cursor.getString(2);
                        productDO.description = cursor.getString(3);
                        productDO.AltDescription = cursor.getString(4);
                        productDO.btlCount = cursor.getInt(5);
                        arrProductDOs.add(productDO);
                    }while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/ throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - getProductOrderLimit() - ended");
            }
            return arrProductDOs;
        }
    }

    public boolean deleteProduct(String productCode){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "CartDA - deleteProduct() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            try {
                if(StringUtils.isEmpty(productCode))
                    sqLiteDatabase.execSQL("delete from tblShoppingCart");
                else
                    sqLiteDatabase.execSQL("delete from tblShoppingCart where ProductCode = '"+productCode+"'");
            }catch (Exception e) {
                /*e.printStackTrace();*/ throw new RuntimeException("This can never happen", e);
            } finally {
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "CartDA - deleteProduct() - ended");
            }
            return false;
        }
    }

}
