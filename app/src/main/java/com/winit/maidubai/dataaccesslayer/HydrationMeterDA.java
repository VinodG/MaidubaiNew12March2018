package com.winit.maidubai.dataaccesslayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.winit.maidubai.MaiDubaiApplication;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Girish Velivela on 09-09-2016.
 */
public class HydrationMeterDA {

    private Context context;
    public HydrationMeterDA(Context context){
        this.context = context;
    }

    public boolean insertHydrationMeterSettings(HydrationMeterDO hydrationMeterDO){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - insertHydrationMeterSettings() - strated");
  Boolean flag=false;
            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            SQLiteStatement insertSqLiteStatement = null;
            SQLiteStatement updateSqLiteStatement = null;
            try {

                insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHydrationMeterSetting(HydrationMeterSettingId,CustomerId,Weight," +
                        "Height,DailyWaterConsumptionTarget,CreatedDate,CreatedBy,ModifiedDate,ModifiedBy,Notification,Age,Gender) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?)");
                updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHydrationMeterSetting set CustomerId=?,Weight=?,Height=?,DailyWaterConsumptionTarget=?,CreatedDate=?," +
                        "CreatedBy=?,ModifiedDate=?,ModifiedBy=?,Notification=?,Age=?,Gender=? where HydrationMeterSettingId = ?");
                updateSqLiteStatement.bindLong(1, hydrationMeterDO.CustomerId);
                updateSqLiteStatement.bindDouble(2, hydrationMeterDO.Weight);
                updateSqLiteStatement.bindDouble(3, hydrationMeterDO.Height);
                updateSqLiteStatement.bindDouble(4, hydrationMeterDO.DailyWaterConsumptionTarget);
                updateSqLiteStatement.bindString(5, hydrationMeterDO.CreatedDate);
                updateSqLiteStatement.bindLong(6, hydrationMeterDO.CreatedBy);
                updateSqLiteStatement.bindString(7, hydrationMeterDO.ModifiedDate);
                updateSqLiteStatement.bindLong(8, hydrationMeterDO.ModifiedBy);
                updateSqLiteStatement.bindString(9, hydrationMeterDO.notifyMeTime + "");
                updateSqLiteStatement.bindString(10, hydrationMeterDO.age + "");
                updateSqLiteStatement.bindString(11, hydrationMeterDO.gender + "");
                updateSqLiteStatement.bindLong(12, hydrationMeterDO.Id);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, hydrationMeterDO.Id);
                    insertSqLiteStatement.bindLong(2, hydrationMeterDO.CustomerId);
                    insertSqLiteStatement.bindDouble(3, hydrationMeterDO.Weight);
                    insertSqLiteStatement.bindDouble(4, hydrationMeterDO.Height);
                    insertSqLiteStatement.bindDouble(5, hydrationMeterDO.DailyWaterConsumptionTarget);
                    insertSqLiteStatement.bindString(6, hydrationMeterDO.CreatedDate);
                    insertSqLiteStatement.bindLong(7, hydrationMeterDO.CreatedBy);
                    insertSqLiteStatement.bindString(8, hydrationMeterDO.ModifiedDate);
                    insertSqLiteStatement.bindLong(9, hydrationMeterDO.ModifiedBy);
                    insertSqLiteStatement.bindString(10, hydrationMeterDO.notifyMeTime + "");
                    insertSqLiteStatement.bindString(11, hydrationMeterDO.age + "");
                    insertSqLiteStatement.bindString(12, hydrationMeterDO.gender+"");
                    insertSqLiteStatement.executeInsert();
                }
                flag=true;
                return flag;
            } catch (Exception e) {
                flag=false;
               /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            } finally {
                if (insertSqLiteStatement != null)
                    insertSqLiteStatement.close();
                if (updateSqLiteStatement != null)
                    updateSqLiteStatement.close();
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - insertHydrationMeterSettings() - ended");
                return flag;
            }
        }
    }


    public HydrationMeterDO getHydrationMeterSettings(){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterSettings() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            try {
               cursor = sqLiteDatabase.rawQuery("select * from tblHydrationMeterSetting",null);
                if(cursor.moveToFirst()){
                    HydrationMeterDO hydrationMeterDO = new HydrationMeterDO();
                    hydrationMeterDO.Id = cursor.getInt(0);
                    hydrationMeterDO.CustomerId = cursor.getInt(1);
                    hydrationMeterDO.Weight = cursor.getDouble(2);
                    hydrationMeterDO.Height = cursor.getDouble(3);
                    hydrationMeterDO.DailyWaterConsumptionTarget = cursor.getDouble(4);
                    hydrationMeterDO.CreatedDate = cursor.getString(5);
                    hydrationMeterDO.CreatedBy = cursor.getInt(6);
                    hydrationMeterDO.ModifiedDate = cursor.getString(7);
                    hydrationMeterDO.ModifiedBy = cursor.getInt(8);
                    hydrationMeterDO.age = cursor.getInt(10);
                    hydrationMeterDO.gender = cursor.getString(11);
                    return hydrationMeterDO;
                }
            } catch (Exception e) {
               /*  e.printStackTrace();*/  Log.d("This can never happen", ""+e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterSettings() - ended");
            }
            return null;
        }
    }

    public HydrationMeterReadingDO getHydrationMeterReading(String date){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterReading() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            try {
               cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHydrationMeterReading where ConsumptionDate like '%"+date+"%'",null);
                if(cursor.moveToFirst()){
                    HydrationMeterReadingDO hydrationMeterReadingDO = new HydrationMeterReadingDO();
                    hydrationMeterReadingDO.HydrationMeterReadingId = cursor.getInt(0);
                    hydrationMeterReadingDO.CustomerId = cursor.getInt(1);
                    hydrationMeterReadingDO.WaterConsumed = cursor.getDouble(2);
                    hydrationMeterReadingDO.ConsumptionDate = cursor.getString(3);
                    hydrationMeterReadingDO.WaterConsumedPercent = cursor.getDouble(4);
                    return hydrationMeterReadingDO;
                }
            } catch (Exception e) {
                /*  e.printStackTrace();*/  Log.d("This can never happen", ""+e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterReading() - ended");
            }
            return null;
        }
    }

    public boolean insertHydrationMeterReading(HydrationMeterReadingDO hydrationMeterReadingDO){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - insertHydrationMeterSettings() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            SQLiteStatement insertSqLiteStatement = null;
            SQLiteStatement updateSqLiteStatement = null;
            try {
                insertSqLiteStatement = sqLiteDatabase.compileStatement("insert into tblHydrationMeterReading(HydrationMeterReadingId,CustomerId,WaterConsumed," +
                        "Percentage,ConsumptionDate) values(?,?,?,?,?)");
                updateSqLiteStatement = sqLiteDatabase.compileStatement("update tblHydrationMeterReading set CustomerId=?,WaterConsumed=?," +
                        "Percentage=?,ConsumptionDate=? where HydrationMeterReadingId = ?");
                updateSqLiteStatement.bindDouble(1, hydrationMeterReadingDO.CustomerId);
                updateSqLiteStatement.bindDouble(2, hydrationMeterReadingDO.WaterConsumed);
                updateSqLiteStatement.bindDouble(3, hydrationMeterReadingDO.WaterConsumedPercent);
                updateSqLiteStatement.bindString(4, hydrationMeterReadingDO.ConsumptionDate);
                updateSqLiteStatement.bindLong(5, hydrationMeterReadingDO.HydrationMeterReadingId);
                if (updateSqLiteStatement.executeUpdateDelete() <= 0) {
                    insertSqLiteStatement.bindLong(1, hydrationMeterReadingDO.HydrationMeterReadingId);
                    insertSqLiteStatement.bindLong(2, hydrationMeterReadingDO.CustomerId);
                    insertSqLiteStatement.bindDouble(3, hydrationMeterReadingDO.WaterConsumed);
                    insertSqLiteStatement.bindDouble(4, hydrationMeterReadingDO.WaterConsumedPercent);
                    insertSqLiteStatement.bindString(5, hydrationMeterReadingDO.ConsumptionDate);
                    insertSqLiteStatement.executeInsert();
                }
                return true;
            } catch (Exception e) {
              /*  e.printStackTrace();*/  Log.d("This can never happen", ""+e);
            } finally {
                if (insertSqLiteStatement != null)
                    insertSqLiteStatement.close();
                if (updateSqLiteStatement != null)
                    updateSqLiteStatement.close();
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - insertHydrationMeterSettings() - ended");
                return false;
            }
        }
    }

    public Object[] getDrinkSummary(String month){
        synchronized (MaiDubaiApplication.APP_DB_LOCK) {
            LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterReading() - strated");

            SQLiteDatabase sqLiteDatabase = new DatabaseHelper(context).openDataBase();
            Cursor cursor = null;
            Object[] summary = new Object[2];
            try {
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHydrationMeterReading where ConsumptionDate like '%"+month+"%' order by ConsumptionDate",null);
                if(cursor.moveToFirst()){
                    ArrayList<HydrationMeterReadingDO> hydrationMeterReadingDOs = new ArrayList<>();
                    do {
                        HydrationMeterReadingDO hydrationMeterReadingDO = new HydrationMeterReadingDO();
                        hydrationMeterReadingDO.HydrationMeterReadingId = cursor.getInt(0);
                        hydrationMeterReadingDO.CustomerId = cursor.getInt(1);
                        hydrationMeterReadingDO.WaterConsumed = cursor.getDouble(2);
                        //hydrationMeterReadingDO.ConsumptionDate = CalendarUtil.getDate(cursor.getString(3), CalendarUtil.DATE_PATTERN_dd_MM_YYYY,CalendarUtil.dd_MMM_PATTERN, Locale.getDefault());
                        hydrationMeterReadingDO.ConsumptionDate = CalendarUtil.getHydrationDate(cursor.getString(3), CalendarUtil.DATE_PATTERN_dd_MM_YYYY, Locale.getDefault());
                        hydrationMeterReadingDO.WaterConsumedPercent = cursor.getDouble(4);
                        hydrationMeterReadingDOs.add(hydrationMeterReadingDO);
                    }while (cursor.moveToNext());
                    summary[0] = hydrationMeterReadingDOs;
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                cursor = sqLiteDatabase.rawQuery("SELECT sum(WaterConsumed),sum(Percentage), substr(ConsumptionDate, 4, 8) mon_yr FROM tblHydrationMeterReading where ConsumptionDate like '%"+month+"%' group by mon_yr",null);
                if(cursor.moveToFirst()){
                    ArrayList<HydrationMeterReadingDO> hydrationMeterReadingDOs = new ArrayList<>();
                    do {
                        HydrationMeterReadingDO hydrationMeterReadingDO = new HydrationMeterReadingDO();
                        hydrationMeterReadingDO.WaterConsumed = cursor.getDouble(0);
                        hydrationMeterReadingDO.WaterConsumedPercent = (cursor.getDouble(1)/30);
                        //hydrationMeterReadingDO.ConsumptionDate = CalendarUtil.getDate(cursor.getString(2), CalendarUtil.MM_YYYY_PATTERN, CalendarUtil.MMM_PATTERN,Locale.getDefault());
                        hydrationMeterReadingDO.ConsumptionDate = CalendarUtil.getHydrationMonthYear(cursor.getString(2), CalendarUtil.MM_YYYY_PATTERN,Locale.getDefault());
                        hydrationMeterReadingDOs.add(hydrationMeterReadingDO);
                    }while (cursor.moveToNext());
                    summary[1] = hydrationMeterReadingDOs;
                }
                return summary;
            } catch (Exception e) {
                    /*  e.printStackTrace();*/  Log.d("This can never happen", ""+e);
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                    sqLiteDatabase.close();
                }
                LogUtils.debug(LogUtils.LOG_TAG, "HydrationMeterDA - getHydrationMeterReading() - ended");
                return summary;
            }
        }
    }

}
