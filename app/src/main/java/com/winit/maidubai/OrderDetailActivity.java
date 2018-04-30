package com.winit.maidubai;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.wallet.GiftCardWalletObject;
import com.winit.maidubai.adapter.OrderDetailAdapter;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends BaseActivity implements DataListener {
    private ListView listOrderDetail;
    private LinearLayout llOrderDetail,llTotalPayment,llOrderFooter,llOrderDate,llrecurringtype,llDeliveryCharges,llSubTotal;
    private Button btn_track_order,btnRepeatOrder,btnRecurringOrder;
    private TextView tvStatus,tvOrderDate,tvOrderCode,tvCancelRecurring,tvBottelPrice,tvBillDetails,tvSpecialIns,tvPamentType,tvDeliveryTime,tvDeliveryDate,tvDeliveryCharges,tvTotalAmt,tvBillingAmt,tvProductAmtTotal,tv_recurring_type,tvTotalVATAmount;
    private OrderDetailAdapter orderDetailAdapter;
    private TrxHeaderDO trxHeaderDO;

    @Override
    public void initialise() {
        llOrderDetail= (LinearLayout) inflater.inflate(R.layout.activity_order_detail,null);
        llOrderDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llOrderDetail);
        setTypeFaceNormal(llOrderDetail);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.order_detaills));
        lockDrawer("Order Detail");
        setStatusBarColor();
        if(getIntent().hasExtra("Order"))
            trxHeaderDO = (TrxHeaderDO)getIntent().getSerializableExtra("Order");
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        llOrderFooter= (LinearLayout)inflater.inflate(R.layout.order_details_footer, null);
        llOrderDate= (LinearLayout)llOrderFooter.findViewById(R.id.llOrderDate);
        btn_track_order=(Button)findViewById(R.id.btn_track_order);
        btnRecurringOrder=(Button)findViewById(R.id.btnRecurringOrder);
        btnRepeatOrder=(Button)findViewById(R.id.btnRepeatOrder);
        tvOrderCode=(TextView)findViewById(R.id.tvOrderCode);
        tvBottelPrice=(TextView)findViewById(R.id.tvBottelPrice);
        tvBillDetails=(TextView)findViewById(R.id.tvBillDetails);
        tvCancelRecurring=(TextView)findViewById(R.id.tvCancelRecurring);
        tvPamentType=(TextView)llOrderFooter.findViewById(R.id.tvPamentType);
        tvOrderDate=(TextView)findViewById(R.id.tvOrderDate);
        tvSpecialIns=(TextView)findViewById(R.id.tvSpecialIns);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        tvProductAmtTotal=(TextView)llOrderFooter.findViewById(R.id.tvProductAmtTotal);
        tvBillingAmt=(TextView)llOrderFooter.findViewById(R.id.tvBillingAmt);
        tvTotalAmt=(TextView)llOrderFooter.findViewById(R.id.tvTotalAmt);
        tvDeliveryCharges=(TextView)llOrderFooter.findViewById(R.id.tvDeliveryCharges);
        tvDeliveryDate=(TextView)llOrderFooter.findViewById(R.id.tvDeliveryDate);
        tvDeliveryTime=(TextView)findViewById(R.id.tvDeliveryTime);
        listOrderDetail=(ListView)findViewById(R.id.lv_order_detail);
        llTotalPayment=(LinearLayout) llOrderFooter.findViewById(R.id.llTotalPayment);
        tvTotalVATAmount=(TextView) llOrderFooter.findViewById(R.id.tvTotalVATAmount);
        llrecurringtype = (LinearLayout)findViewById(R.id.llrecurringtype);
        llDeliveryCharges= (LinearLayout)llOrderFooter.findViewById(R.id.llDeliveryCharges);
        llSubTotal= (LinearLayout)llOrderFooter.findViewById(R.id.llSubTotal);
        tv_recurring_type = (TextView)findViewById(R.id.tv_recurring_type);

        llSubTotal.setVisibility(View.GONE);
        llDeliveryCharges.setVisibility(View.GONE);
        orderDetailAdapter = new OrderDetailAdapter(this,null);
        listOrderDetail.addFooterView(llOrderFooter);
        listOrderDetail.setAdapter(orderDetailAdapter);
        tvOrderCode.setTypeface(AppConstants.DinproBold);
        tvBottelPrice.setTypeface(AppConstants.DinproBold);
        tvBillDetails.setTypeface(AppConstants.DinproBold);
        setTypeFaceBold(llTotalPayment);
        btn_track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetailActivity.this, OrderCancelActivity.class);
                i.putExtra("Order", trxHeaderDO);
                startActivityForResult(i,AppConstants.canceloreder);
            }
        });

        btnRepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(OrderDetailActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_Repeat_Order), getString(R.string.yes), getString(R.string.no), "Repeat Order");
            }
        });
        tvCancelRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(OrderDetailActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_cancel_recurring), getString(R.string.yes), getString(R.string.no), "Cancel Recurring Order");
            }
        });
        btnRecurringOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new RecurrTypeBottomSheet();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }
    String recurrType = "";
    public void selectedRcurrType(String recurrType){
        this.recurrType = recurrType;
        showCustomDialog(OrderDetailActivity.this, getString(R.string.confirm), getString(R.string.recurringnarration)+"\n"+getString(R.string.DO_You_want_to_recurr_Order), getString(R.string.yes), getString(R.string.no), "Recurr Order");
    }

    private void setStatus(){
        if(trxHeaderDO.trxStatus == 100 || trxHeaderDO.isCancelled){
            btn_track_order.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            llOrderDate.setVisibility(View.GONE);

            if(trxHeaderDO.trxStatus == 100)
            {
                btnRepeatOrder.setVisibility(View.VISIBLE);
                btnRecurringOrder.setVisibility(View.VISIBLE);
                tvStatus.setTextColor(getResources().getColor(R.color.paid_via_text));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_deliver_cell,0,0,0);
                tvStatus.setText(getResources().getString(R.string.orderdelivered));
            }
            else
            {
                btnRepeatOrder.setVisibility(View.GONE);
                btnRecurringOrder.setVisibility(View.GONE);
                tvStatus.setTextColor(getResources().getColor(R.color.order_detail_red));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_cancel_cell,0,0,0);
                tvStatus.setText(getResources().getString(R.string.order_cancel));
            }
        } else{
            tvStatus.setVisibility(View.GONE);
            btn_track_order.setVisibility(View.VISIBLE);
            btnRepeatOrder.setVisibility(View.GONE);
            if(trxHeaderDO.trxStatus == 5)
                btnRecurringOrder.setVisibility(View.VISIBLE);
            else
                btnRecurringOrder.setVisibility(View.GONE);
        }

        if(trxHeaderDO.recurringOrderDO != null){
            btnRecurringOrder.setVisibility(View.GONE);
            btnRepeatOrder.setVisibility(View.GONE);
            tvCancelRecurring.setVisibility(View.VISIBLE);
            llrecurringtype.setVisibility(View.VISIBLE);

            if(StringUtils.isEmpty(trxHeaderDO.recurringOrderDO.Frequency))
                tv_recurring_type.setText(getResources().getString(R.string.na));
            else
                tv_recurring_type.setText(getArabicFrequency(trxHeaderDO.recurringOrderDO.Frequency));

            if(trxHeaderDO.recurringOrderDO.RemovedFromRecurring){
                tvStatus.setVisibility(View.VISIBLE);
                tvCancelRecurring.setVisibility(View.GONE);
                tvStatus.setTextColor(getResources().getColor(R.color.order_detail_red));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_cancel_cell,0,0,0);
                if(trxHeaderDO.isCancelled)
                    tvStatus.setText(getResources().getString(R.string.order_cancel));
                else
                    tvStatus.setText(getResources().getString(R.string.Recurr_Removed));
            }else if(trxHeaderDO.trxStatus != 100){
                tvStatus.setVisibility(View.GONE);
                btn_track_order.setVisibility(View.VISIBLE);
            }

        }
        
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void cancelOrder(){
        showCustomDialog(OrderDetailActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_cancel_Order), getString(R.string.yes), getString(R.string.no), "Cancel Order");
    }

    @Override
    public void onButtonYesClick(String from) {
        //setStatus();
        if(from.equalsIgnoreCase("Cancel Order"))
            cancelOrder(trxHeaderDO);
        else if(from.equalsIgnoreCase("Repeat Order")) {
                if(checkNetworkConnection())
                {
                    showLoader("Placing Order...");
                    String userCode = preference.getStringFromPreference(Preference.CUSTOMER_CODE,"");
                    new CommonBL(OrderDetailActivity.this,OrderDetailActivity.this).repeatOrder(trxHeaderDO.trxCode, userCode);
                }
            }
        else if(from.equalsIgnoreCase("Cancel Recurring Order")) {
                if(checkNetworkConnection())
                {
                    showLoader("Cancelling Recurring...");
                    new CommonBL(OrderDetailActivity.this,OrderDetailActivity.this).cancelRecuuring(trxHeaderDO.trxCode, CalendarUtil.getDate(new Date(), CalendarUtil.DATE_PATTERN));
                }
            }
        else if(from.equalsIgnoreCase("Recurr Order")) {
            if(checkNetworkConnection())
            {
                showLoader("Placing for Recurring...");
                new CommonBL(OrderDetailActivity.this,OrderDetailActivity.this).recurrOrder(trxHeaderDO.trxCode, recurrType);
            }
        }else if(from.equalsIgnoreCase("Go Recurring Orders")) {
                finish();
                Intent intent = new Intent(OrderDetailActivity.this, MyRecurringOrdersActivity.class);
                startActivity(intent);
        }
        super.onButtonYesClick(from);
    }

    @Override
    public void loadData() {
        if(StringUtils.isEmpty(trxHeaderDO.orderName))
            tvOrderCode.setText(trxHeaderDO.trxDisplayCode);
        else
            tvOrderCode.setText(trxHeaderDO.orderName+" ("+trxHeaderDO.trxDisplayCode+")");
        tvOrderDate.setText(CalendarUtil.convertToTrxDateToShow(trxHeaderDO.trxDate, Locale.getDefault()));
       
        if(StringUtils.isEmpty(trxHeaderDO.specialInstructions))
            tvSpecialIns.setText(getResources().getString(R.string.na));
        else
            tvSpecialIns.setText(trxHeaderDO.specialInstructions);
        // String strPayableAmount = decimalFormat.format(trxHeaderDO.totalAmountWODiscount);
        String strPayableAmount = decimalFormat.format(trxHeaderDO.productSubTotalWDiscount);
        tvProductAmtTotal.setText(getResources().getString(R.string.currency)+" "+strPayableAmount+"");
        tvBillingAmt.setText(getResources().getString(R.string.currency)+" "+strPayableAmount+"");
        tvDeliveryCharges.setText(getResources().getString(R.string.currency)+" "+0.00+"");
        //tvTotalAmt.setText("AED "+decimalFormat.format(trxHeaderDO.totalAmountWODiscount)+"");
        tvTotalAmt.setText(getResources().getString(R.string.currency)+" "+decimalFormat.format(trxHeaderDO.totalTrxAmount)+"");
        tvBottelPrice.setText(getResources().getString(R.string.currency)+" "+decimalFormat.format(trxHeaderDO.productSubTotalWDiscount)+"");
        tvTotalVATAmount.setText(getResources().getString(R.string.currency)+" "+decimalFormat.format(trxHeaderDO.TotalVAT)+"");
        tvDeliveryDate.setText(CalendarUtil.convertToDeliDateToShow(trxHeaderDO.deliveryDate, Locale.getDefault()));
        if(trxHeaderDO.isCOD == true)
            tvPamentType.setText(getResources().getString(R.string.paidvia));
        else
            tvPamentType.setText(getResources().getString(R.string.paidvia_online));

        orderDetailAdapter.refresh(trxHeaderDO.trxDetailsDOs);
        setStatus();
    }

    private void cancelOrder(TrxHeaderDO trxHeaderDO)
    {
        Intent i=new Intent(OrderDetailActivity.this,OrderCancelActivity.class);
        i.putExtra("Order",trxHeaderDO);
        startActivityForResult(i,AppConstants.canceloreder);
    }

    @Override
    public void dataRetrieved(ResponseDO response) {

        super.dataRetrieved(response);
        if(response.method!=null && response.method == ServiceMethods.WS_CANCEL_ORDER) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() >0) {
                            placeOrderDO.arrTrxHeaderDOs.get(0).isCancelled = true;
                            new OrderDA(OrderDetailActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                trxHeaderDO = placeOrderDO.arrTrxHeaderDOs.get(0);
                                loadData();
                            }
                        });
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(OrderDetailActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }else if(response.method!=null && response.method == ServiceMethods.WS_CANCEL_RECURRING) {
            if (response.data != null && response.data instanceof CommonResponseDO) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(trxHeaderDO != null) {
                            ArrayList<TrxHeaderDO> trxHeaderDOs = new ArrayList<TrxHeaderDO>();
                            if(trxHeaderDO.recurringOrderDO != null){
                                trxHeaderDO.recurringOrderDO.RemovedFromRecurring = true;
                            }
                            trxHeaderDOs.add(trxHeaderDO);
                            new OrderDA(OrderDetailActivity.this).insertOrders(trxHeaderDOs);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                loadData();
                            }
                        });
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(OrderDetailActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }else if(response.method!=null && (response.method == ServiceMethods.WS_REPEAT_ORDER || response.method == ServiceMethods.WS_RECURR_ORDER)) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() >0) {
                            new OrderDA(OrderDetailActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(OrderDetailActivity.this, OrderSuccessActivity.class);
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
                                    showCustomDialog(OrderDetailActivity.this, "", "There seams some problem in placing the order.\nKindly try again after some time.", getString(R.string.OK), "", "");
                                }
                            });
                        }
                    }
                }).start();
            } else {
                hideLoader();
                if(((String) response.data).contains("You have already placed maximum number of Recurring Orders"))
                    showCustomDialog(OrderDetailActivity.this, "", getString(R.string.maxrecurringorders),getString(R.string.Go_to_recur_orders), getString(R.string.OK), "Go Recurring Orders");
                else
                showCustomDialog(OrderDetailActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== AppConstants.canceloreder && resultCode == RESULT_OK) {
            trxHeaderDO.isCancelled = true;
            loadData();
        }
    }

    private String getArabicFrequency(String engFrequency)
    {
        String arabicFrequency = "";

        if(engFrequency.equalsIgnoreCase("Once in a Week"))
            arabicFrequency = getResources().getString(R.string.Once_in_a_Week);
        else if(engFrequency.equalsIgnoreCase("Once in two Weeks"))
            arabicFrequency = getResources().getString(R.string.Once_in_two_Weeks);
        else if(engFrequency.equalsIgnoreCase("Once in 3 Weeks"))
            arabicFrequency = getResources().getString(R.string.Once_in_3_Weeks);
        else if(engFrequency.equalsIgnoreCase("Once in a Month"))
            arabicFrequency = getResources().getString(R.string.Once_in_a_Month);

        return arabicFrequency;
    }
}
