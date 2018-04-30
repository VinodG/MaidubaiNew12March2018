package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.CategoryDO;
import com.winit.maidubai.dataobject.ProductAttribute;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ProductOrderLimitDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Girish Velivela on 23-08-2016.
 */
public class ProductDA {

    private Context context;
    public ProductDA(Context context){
        this.context = context;
    }

    public Vector<ProductDO> getProducts(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProducts() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            Vector<ProductDO> arrProducts = null;
            try {
                String query = "SELECT Distinct P.ProductId, P.Code, P.Name, P.Description, P.AltDescription, PI.OriginalImage," +
                        "PI.SmallImage,PI.MediumImage, PI.LargeImage,PR.UnitPrice,U.UOM " +
                        "FROM tblProduct P Inner Join tblItemUOM U ON P.Code = U.ItemCode " +
                        "Inner Join tblPrice PR ON P.ProductId = PR.ProductId AND U.UOM = PR.UOM " +
                        "Inner Join tblProductImages PI ON P.ProductId = PI.ProductId AND PI.ISDefault = 'True' collate nocase order by Sequence";

                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrProducts = new Vector<>();
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
                        arrProducts.add(productDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
               /* e.printStackTrace();*/   throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getMedia() - ended");
            }
            return arrProducts;
        }
    }

    public HashMap<Integer,CategoryDO> getProductsByCategory(String categoryId){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProducts() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            HashMap<Integer,CategoryDO> hmProducts = null;
            try {
                String query = "SELECT Distinct P.ProductId, P.Code, P.Name, P.Description, P.AltDescription," +
                        "PI.OriginalImage,PI.SmallImage,PI.MediumImage, PI.LargeImage," +
                        "PR.UnitPrice,U.UOM," +
//                        "C.CategoryId, C.Name,PR.EndDate " +
                        "C.CategoryId, C.Name,PR.EndDate,P.VAT " +
                        "FROM tblCategory C Inner Join tblProductCategory PC on C.CategoryId = PC.CategoryId " +
                        "Inner Join tblProduct P on PC.ProductId = P.ProductId " +
                        "Inner Join tblItemUOM U ON P.Code = U.ItemCode " +
                        "Inner Join tblPrice PR ON P.ProductId = PR.ProductId AND U.UOM = PR.UOM " +
                        "left outer Join tblProductImages PI ON P.ProductId = PI.ProductId AND PI.ISDefault = 'True' collate nocase and PI.IsActive = 'True'  collate nocase " +
                        "where P.IsActive = 'True'  collate nocase";

                if(!StringUtils.isEmpty(categoryId))
                    query += " and C.CategoryId = '"+categoryId+"'";

                query+= " order by P.Sequence";

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
                        productDO.pagerImage = cursor.getString(5);
                        productDO.pagerImage = StringUtils.replaceAll("(%20)|[~]",productDO.pagerImage,"");
                        productDO.gridImage = cursor.getString(5);
                        productDO.gridImage = StringUtils.replaceAll("(%20)|[~]",productDO.gridImage,"");
                        productDO.listImage = productDO.gridImage;
                        productDO.btlSize = productDO.gridImage;
                        productDO.price = cursor.getFloat(9); // medium (7) Large(8) image skipped
                        productDO.UOM = cursor.getString(10);
                        productDO.categoryId = cursor.getInt(11);
                        CategoryDO categoryDO = hmProducts.get(productDO.categoryId);
                        if(categoryDO == null){
                            categoryDO = new CategoryDO();
                            categoryDO.categoryId = cursor.getInt(11);
                            categoryDO.name = cursor.getString(12);
                            categoryDO.arrProductDOs = new ArrayList<>();
                        }
                        if(CalendarUtil.getdifference("",cursor.getString(13))>0) {
                            productDO.productOrderLimitDO = getProductOrderLimit(sqLiteDatabase, productDO.id);
                            productDO.arrProductAttributes = getProductAttribute(sqLiteDatabase, productDO.id);
                            productDO.minquantity = getProductMinQuantity(sqLiteDatabase, productDO.id);
                            categoryDO.arrProductDOs.add(productDO);
                            hmProducts.put(categoryDO.categoryId, categoryDO);
                        }
                        productDO.VAT = cursor.getInt(14);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/   throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getMedia() - ended");
            }
            return hmProducts;
        }
    }


    private ProductOrderLimitDO getProductOrderLimit(SQLiteDatabase sqLiteDatabase, String productId){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProductOrderLimit() - strated");
            Cursor cursor = null;
            ProductOrderLimitDO productOrderLimitDO = null;
            try {
                String date = CalendarUtil.getDate(new Date(),CalendarUtil.YYYY_MM_DD_FULL_PATTERN);
                String query = "select * from tblOrderLimit where  StartDate <= '"+date+"' and '"+date+"' <=EndDate and ProductId ="+productId;
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    productOrderLimitDO = new ProductOrderLimitDO();
                    productOrderLimitDO.OrderLimitId = cursor.getInt(0);
                    productOrderLimitDO.ProductId = cursor.getInt(1);
                    productOrderLimitDO.Quantity = cursor.getInt(2);
                    productOrderLimitDO.StartDate = cursor.getString(3);
                    productOrderLimitDO.EndDate = cursor.getString(4);
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProductOrderLimit() - ended");
            }
            return productOrderLimitDO;
        }
    }
    private int getProductMinQuantity(SQLiteDatabase sqLiteDatabase, String productId){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProductOrderLimit() - strated");

            Cursor cursor = null;
            int  minQnty = 1;
            try {
                String date = CalendarUtil.getDate(new Date(),CalendarUtil.YYYY_MM_DD_FULL_PATTERN);
                String query = "SELECT Quantity from  tblOrderMinLimit where StartDate <= '"+date+"' and '"+date+"' <=EndDate and ProductId ="+productId;
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    minQnty = cursor.getInt(0);
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/    throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProductOrderLimit() - ended");
            }
            return minQnty;
        }
    }

    public Vector<ProductDO> getProductImages(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProducts() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            Vector<ProductDO> arrProducts = null;
            try {
                String query = "SELECT PI.SmallImage,PI.MediumImage, PI.LargeImage,PI.OriginalImage from tblProductImages PI where PI.ISDefault = 'True' collate nocase";
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrProducts = new Vector<>();
                    do {
                        ProductDO productDO = new ProductDO();
                        productDO.gridImage = cursor.getString(0); // 5 original image skipped
                        productDO.listImage = cursor.getString(0);
                        productDO.pagerImage = cursor.getString(3);// medium (7) large(8) image skipped
                        arrProducts.add(productDO);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
/*                e.printStackTrace(); */   throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getMedia() - ended");
            }
            return arrProducts;
        }
    }

    private ArrayList<ProductAttribute> getProductAttribute(SQLiteDatabase sqLiteDatabase, String productId){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getProductAttribute() - strated");
            Cursor cursor = null;
            ArrayList<ProductAttribute> arrProductAttributes= null;
            try {
                String query = "select PA.ProductAttributeId,ProductId,Name,Icon,PAD.Description from tblProductAttribute PA inner join " +
                        "tblProductAttributeDetail PAD on PA.ProductAttributeId = PAD.ProductAttributeId where ProductId ="+productId;
                cursor = sqLiteDatabase.rawQuery(query,null);
                if (cursor.moveToFirst()) {
                    arrProductAttributes = new ArrayList<>();
                    do {
                        ProductAttribute productAttribute = new ProductAttribute();
                        productAttribute.ProductAttributeId = cursor.getInt(0);
                        productAttribute.ProductId = cursor.getInt(1);
                        productAttribute.Name = cursor.getString(2);
                        productAttribute.Icon = cursor.getString(3);
                        productAttribute.Description = cursor.getString(4);
                        arrProductAttributes.add(productAttribute);
                    } while (cursor.moveToNext());
                }
            }catch (Exception e) {
                /*e.printStackTrace();*/   throw new RuntimeException("This can never happen", e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "ProductDA - getOrderAddress() - ended");
            }
            return arrProductAttributes;
        }
    }


}
