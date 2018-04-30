package com.winit.maidubai;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CartDA;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.CartResponseDO;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MyOrdersActivity extends BaseActivity implements DataListener{

    private LinearLayout llMyOrders,llNoOrders;
    private ListView lvOrders;
    private MyPastOrderAddapter myPastOrderAddapter;
    private ArrayList<TrxHeaderDO> arrTrxHeaderDOs;
    TrxHeaderDO selTrxHeaderDO;
    private String recurrType;
    private HashMap<String,CartListDO> hmCartList ;
    private TrxHeaderDO trxheaderToEdit = new TrxHeaderDO();

    @Override
    public void initialise() {
        llMyOrders = (LinearLayout) inflater.inflate(R.layout.activity_my_orders,null);
        llMyOrders.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llMyOrders);
        setTypeFaceNormal(llMyOrders);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.myorders));
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
//        loadData();

            refreshData(new SyncIntentService.SyncListener() {
                @Override
                public void onStart() {
                    showLoader("Syncing...");
                }

                @Override
                public void onError(String message) {
                    hideLoader();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    });
                }

                @Override
                public void onEnd() {
                    hideLoader();
                    updateUI();
                }
            });

    }

    public void syncOrders(){
        if (NetworkUtil.isNetworkConnectionAvailable(MyOrdersActivity.this)) {
            int customerId = preference.getIntFromPreference(Preference.CUSTOMER_ID, 0);
            String syncTime = preference.getStringFromPreference(Preference.LAST_ORDERS_SYNC, "");
            //syncTime = CalendarUtil.getDate(syncTime, CalendarUtil.DATE_PATTERN_dd_MM_YYYY, CalendarUtil.SYNCH_DATE_TIME_PATTERN);
            new CommonBL(MyOrdersActivity.this, MyOrdersActivity.this).getOrders(customerId + "",
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
                hmCartList = new CartDA(MyOrdersActivity.this).getCartList();
                arrTrxHeaderDOs = new OrderDA(MyOrdersActivity.this).getOrders();
                customerDO = new CustomerDA(MyOrdersActivity.this).getCustomer();
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

    public void repeatorder(TrxHeaderDO trxHeaderDO)
    {
        selTrxHeaderDO = trxHeaderDO;
        showCustomDialog(MyOrdersActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_Repeat_Order), getString(R.string.yes), getString(R.string.no), "Repeat Order");
    }

    public void recurrorder(TrxHeaderDO trxHeaderDO)
    {
        selTrxHeaderDO = trxHeaderDO;
        BottomSheetDialogFragment bottomSheetDialogFragment = new RecurrTypeBottomSheet();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    public void updateCartCount(){
        if(hmCartList != null && hmCartList.size()>0){
            tvCArtCount.setVisibility(View.VISIBLE);
            tvCArtCount.setText(""+hmCartList.size());
        }else{
            tvCArtCount.setText(""+0);
        }
    }

    public void selectedRcurrType(String recurrType){
        this.recurrType = recurrType;
        showCustomDialog(MyOrdersActivity.this, getString(R.string.confirm), getString(R.string.recurringnarration)+"\n"+getString(R.string.DO_You_want_to_recurr_Order), getString(R.string.yes), getString(R.string.no), "Recurr Order");
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Cancel Order")) {
            if(checkNetworkConnection())
            {
                showLoader("Cancelling Order...");
//                new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).cancelOrder(selTrxHeaderDO);
            }
        }else if(from.equalsIgnoreCase("Repeat Order")) {
            if(checkNetworkConnection())
            {
                showLoader("Placing Order...");
                String userCode = preference.getStringFromPreference(Preference.CUSTOMER_CODE,"");
                new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).repeatOrder(selTrxHeaderDO.trxCode, userCode);
            }
        } else if(from.equalsIgnoreCase("Recurr Order")) {
            if(checkNetworkConnection())
            {
                showLoader("Placing for Recurring...");
                new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).recurrOrder(selTrxHeaderDO.trxCode, recurrType);
            }
        }
        else if(from.equalsIgnoreCase("Go Recurring Orders")) {
            finish();
            Intent intent = new Intent(MyOrdersActivity.this, MyRecurringOrdersActivity.class);
            startActivity(intent);
        }
        else if(from.equalsIgnoreCase("Edit_Order")) {

            new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).getRollBackTransactionForEditOrder( trxheaderToEdit.trxCode,preference.getStringFromPreference(Preference.SESSION_ID, ""));
        }
        super.onButtonYesClick(from);
    }

    @Override
    public void onButtonNoClick(String from) {
        if(from.equalsIgnoreCase("Cancel Order") || from.equalsIgnoreCase("Repeat Order") || from.equalsIgnoreCase("Recurr Order")) {
            selTrxHeaderDO = null;
        }
        else if(from.equalsIgnoreCase("Edit_Order") ) {
            trxheaderToEdit = null;
        }
        super.onButtonNoClick(from);
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if(response.method != null && response.method == ServiceMethods.WS_EDIT_ORDER_URL)
        {
            if(response.data!=null && response.data instanceof  CommonResponseDO)
            {
                CommonResponseDO result = (CommonResponseDO) response.data;
                if(result.status ==1)
                {
                    addToCartNew( getProductDetailsToPost(trxheaderToEdit.trxDetailsDOs ));
                }
            }else
            {
                if (response.data != null && response.data instanceof String) {
                    showAlert("" + response.data, null);
                }
            }


        }else if(response.method!=null && response.method == ServiceMethods.WS_CANCEL_ORDER) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() >0) {
                            placeOrderDO.arrTrxHeaderDOs.get(0).isCancelled = true;
                            new OrderDA(MyOrdersActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            arrTrxHeaderDOs = new OrderDA(MyOrdersActivity.this).getOrders();
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
                showCustomDialog(MyOrdersActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }else  if(response.method!=null && response.method == ServiceMethods.WS_GET_ORDERS) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                String syncTime = CalendarUtil.getDate(placeOrderDO.ServerTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.SYNCH_DATE_TIME_PATTERN,10*60*1000, Locale.ENGLISH);
                preference.saveStringInPreference(Preference.LAST_ORDERS_SYNC,syncTime);
                final ArrayList<TrxHeaderDO> trxHeaderDOs = placeOrderDO.arrTrxHeaderDOs;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new OrderDA(MyOrdersActivity.this).insertOrders(trxHeaderDOs);
                        arrTrxHeaderDOs = new OrderDA(MyOrdersActivity.this).getOrders();
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
        }else if(response.method!=null && (response.method == ServiceMethods.WS_REPEAT_ORDER || response.method == ServiceMethods.WS_RECURR_ORDER)) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() > 0) {
                            new OrderDA(MyOrdersActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MyOrdersActivity.this, OrderSuccessActivity.class);
                                    intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoader();
                                    showCustomDialog(MyOrdersActivity.this, "", "There seams some problem in placing the order.\nKindly try again after some time.", getString(R.string.OK), "", "");
                                }
                            });
                        }
                    }
                }).start();
            }
        }
        else if (response.method != null && response.method == ServiceMethods.WS_ADD_TO_CART) {
            if (response.data != null && response.data instanceof CartResponseDO) {
                final ArrayList<CartListDO> arrCartListDOs = ((CartResponseDO)response.data).cartListDOs;
                if(arrCartListDOs != null && arrCartListDOs.size() > 0) {
                    // needed not be wait untill data inserted into the database.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new CartDA(MyOrdersActivity.this).insertCartList(arrCartListDOs);
                        }
                    }).start();
                    if (arrCartListDOs != null) {
                        hmCartList = new HashMap<>();
                        for (CartListDO cartListDO : arrCartListDOs) {
                            hmCartList.put(cartListDO.productCode, cartListDO);
                        }
                    }
                    Toast.makeText(MyOrdersActivity.this, getResources().getString(R.string.addedtocart), Toast.LENGTH_LONG).show();
                    updateCartCount();
                    Intent intent = new Intent(MyOrdersActivity.this,MyBasketActivity.class);
                    AppConstants.IS_TO_EDIT_ORDER =true;
                    startActivity(intent);
                }
            }else{
                Toast.makeText(MyOrdersActivity.this,"Failed to add the item to Cart.",Toast.LENGTH_LONG).show();
            }
        }
        else {
            hideLoader();
            if(((String) response.data).contains("You have already placed maximum number of Recurring Orders"))
                showCustomDialog(MyOrdersActivity.this, "", getString(R.string.maxrecurringorders),getString(R.string.Go_to_recur_orders), getString(R.string.OK), "Go Recurring Orders");
            else
                showCustomDialog(MyOrdersActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
        }
    }

    public void addToCartNew(ArrayList<ProductDO>   listProductDO){
//        ArrayList<ProductDO> cartProduct = new ArrayList<>();
//        cartProduct.add(productDO);
        int id = preference.getIntFromPreference(Preference.CUSTOMER_ID,0);
        String sessionId = preference.getStringFromPreference(Preference.SESSION_ID, "");
        new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).addToCartNew(listProductDO, 0, sessionId, id);
    }

    public void editOrder(TrxHeaderDO trxHeader)
    {
        trxheaderToEdit = trxHeader;

        showCustomDialog(MyOrdersActivity.this,"",getString(R.string.editOrderMsg),getString(R.string.continue_a),getString(R.string.cancel),"Edit_Order" );
//        new CommonBL(MyOrdersActivity.this,MyOrdersActivity.this).getRollBackTransactionForEditOrder( trxHeader.trxCode,preference.getStringFromPreference(Preference.SESSION_ID, ""));
//        getProductDetailsToPost(trxHeader.trxDetailsDOs);
//        addToCart();

    }
    public ArrayList<ProductDO> getProductDetailsToPost(ArrayList<TrxDetailsDO> trxdetails)
    {
        ArrayList<ProductDO> listProductDO = new ArrayList<ProductDO> ();
        if(trxdetails!=null && trxdetails.size()>0)
        {
            for(int i=0;i<trxdetails.size();i++)
            {
                TrxDetailsDO trxDetailsDO=  trxdetails.get(i);
                ProductDO productDO=  new ProductDO() ;
                productDO.code = trxDetailsDO.productCode;
                productDO.btlCount = (int)trxDetailsDO.quantity;
                productDO.UOM = trxDetailsDO.UOM;
                listProductDO.add(productDO);
            }



        }
        return  listProductDO;

    }
}
