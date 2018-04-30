package com.winit.maidubai;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CartDA;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MyRecurringOrdersActivity extends BaseActivity implements DataListener{

    private LinearLayout llMyOrders,llNoOrders;
    private ListView lvOrders;
    private MyPastOrderAddapter myPastOrderAddapter;
    private ArrayList<TrxHeaderDO> arrTrxHeaderDOs;
    TrxHeaderDO selTrxHeaderDO;
    private HashMap<String,CartListDO> hmCartList ;
    @Override
    public void initialise() {
        llMyOrders = (LinearLayout) inflater.inflate(R.layout.activity_my_orders,null);
        llMyOrders.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llMyOrders);
        setTypeFaceNormal(llMyOrders);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.myrecurringorders));
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setImageResource(R.drawable.menu_white);
        rlCArt.setVisibility(View.VISIBLE);
        setStatusBarColor();
        setTypeFaceNormal(llMyOrders);
        initialiseControls();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void syncOrders(){
        if (NetworkUtil.isNetworkConnectionAvailable(MyRecurringOrdersActivity.this)) {
            int customerId = preference.getIntFromPreference(Preference.CUSTOMER_ID, 0);
            String syncTime = preference.getStringFromPreference(Preference.LAST_ORDERS_SYNC, "");
            //syncTime = CalendarUtil.getDate(syncTime, CalendarUtil.DATE_PATTERN_dd_MM_YYYY, CalendarUtil.SYNCH_DATE_TIME_PATTERN);
            new CommonBL(MyRecurringOrdersActivity.this, MyRecurringOrdersActivity.this).getOrders(customerId + "",
                    customerDO.AuthToken, "O", "last30days", "", "all", syncTime);
        }
    }

    @Override
    public void initialiseControls() {
        llNoOrders =(LinearLayout) findViewById(R.id.llNoOrders);
        lvOrders =(ListView)findViewById(R.id.lvPastOrder);
        myPastOrderAddapter =new MyPastOrderAddapter(this,null);
        lvOrders.setAdapter(myPastOrderAddapter);
    }

    public void updateOrderUI(){
        if(arrTrxHeaderDOs!= null && arrTrxHeaderDOs.size()>0) {
            myPastOrderAddapter.refresh(arrTrxHeaderDOs);
            lvOrders.setVisibility(View.VISIBLE);
            llNoOrders.setVisibility(View.GONE);
        }else{
            lvOrders.setVisibility(View.GONE);
            llNoOrders.setVisibility(View.VISIBLE);
        }
        updateCartCount();
    }

    @Override
    public void loadData() {
        showLoader("Loading Data.....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                hmCartList = new CartDA(MyRecurringOrdersActivity.this).getCartList();
                arrTrxHeaderDOs = new OrderDA(MyRecurringOrdersActivity.this).getRecurringOrders();
                customerDO = new CustomerDA(MyRecurringOrdersActivity.this).getCustomer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        syncOrders();
                        updateOrderUI();
                    }
                });
            }
        }).start();
    }

    public void cancelOrder(TrxHeaderDO trxHeaderDO){
        selTrxHeaderDO = trxHeaderDO;
        showCustomDialog(MyRecurringOrdersActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_cancel_recurring), getString(R.string.yes), getString(R.string.no), "Cancel Recurring Order");

    }
    public void updateCartCount(){
        if(hmCartList != null && hmCartList.size()>0){
            tvCArtCount.setVisibility(View.VISIBLE);
            tvCArtCount.setText(""+hmCartList.size());
        }else{
            tvCArtCount.setText("" + 0);
        }
    }

    @Override
    public void onButtonYesClick(String from) {
         if(from.equalsIgnoreCase("Cancel Recurring Order")) {
            if(checkNetworkConnection())
            {
                showLoader("Cancelling Recurring...");
                new CommonBL(MyRecurringOrdersActivity.this,MyRecurringOrdersActivity.this).cancelRecuuring(selTrxHeaderDO.trxCode, CalendarUtil.getDate(new Date(), CalendarUtil.DATE_PATTERN));
            }
        }
        super.onButtonYesClick(from);
    }

    @Override
    public void onButtonNoClick(String from) {
        if(from.equalsIgnoreCase("Cancel Recurring Order")) {
            selTrxHeaderDO = null;
        }
        super.onButtonNoClick(from);
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if(response.method!=null && response.method == ServiceMethods.WS_CANCEL_ORDER) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() >0) {
                            placeOrderDO.arrTrxHeaderDOs.get(0).isCancelled = true;
                            new OrderDA(MyRecurringOrdersActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            arrTrxHeaderDOs = new OrderDA(MyRecurringOrdersActivity.this).getRecurringOrders();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                updateOrderUI();
                            }
                        });
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(MyRecurringOrdersActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }else if(response.method!=null && response.method == ServiceMethods.WS_CANCEL_RECURRING) {
            if (response.data != null && response.data instanceof CommonResponseDO) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(selTrxHeaderDO != null) {
                            ArrayList<TrxHeaderDO> trxHeaderDOs = new ArrayList<TrxHeaderDO>();
                            if(selTrxHeaderDO.recurringOrderDO != null){
                                selTrxHeaderDO.recurringOrderDO.RemovedFromRecurring = true;
                            }
                            trxHeaderDOs.add(selTrxHeaderDO);
                            new OrderDA(MyRecurringOrdersActivity.this).insertOrders(trxHeaderDOs);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                updateOrderUI();
                            }
                        });
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(MyRecurringOrdersActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }else if(response.method!=null && response.method == ServiceMethods.WS_GET_ORDERS) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                String syncTime = CalendarUtil.getDate(placeOrderDO.ServerTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.SYNCH_DATE_TIME_PATTERN,5*60*1000, Locale.ENGLISH);
                preference.saveStringInPreference(Preference.LAST_ORDERS_SYNC,syncTime);
                final ArrayList<TrxHeaderDO> trxHeaderDOs = placeOrderDO.arrTrxHeaderDOs;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new OrderDA(MyRecurringOrdersActivity.this).insertOrders(trxHeaderDOs);
                        arrTrxHeaderDOs = new OrderDA(MyRecurringOrdersActivity.this).getRecurringOrders();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                updateOrderUI();
                            }
                        });
                    }
                }).start();
            }
        }else if(response.method!=null && response.method == ServiceMethods.WS_REPEAT_ORDER) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() >0) {
                            new OrderDA(MyRecurringOrdersActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MyRecurringOrdersActivity.this, OrderSuccessActivity.class);
                                    intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoader();
                                    showCustomDialog(MyRecurringOrdersActivity.this, "", "There seams some problem in placing the order.\nKindly try again after some time.", getString(R.string.OK), "", "");
                                }
                            });
                        }
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(MyRecurringOrdersActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }
    }
}
