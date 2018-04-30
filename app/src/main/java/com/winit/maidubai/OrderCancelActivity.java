package com.winit.maidubai;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.OrderCancelDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Girish Velivela on 23-09-2016.
 */
public class OrderCancelActivity extends BaseActivity{

    private LinearLayout llOrderCancel;
    private TextView tvOrderNo,tvName,tvBottelPrice,tvBottelSize,tvAddressOrder,tvDate,tvCategoryCaption;
    private Spinner spReasons;
    private TrxHeaderDO trxHeaderDO;
    private EditText etAdditionalInformation;
    private Button btnConfirm;
    private ArrayList<OrderCancelDO> orderCancelDOs;

    @Override
    public void initialise() {
        llOrderCancel = (LinearLayout) inflater.inflate(R.layout.order_cancel_activity,null);
        llOrderCancel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llOrderCancel);

        ivMenu.setVisibility(View.GONE);
        setTypeFaceMedium(llOrderCancel);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.title_cancel_order);
        tvCancel.setVisibility(View.GONE);
        setStatusBarColor();
        lockDrawer("OrderCancelActivity");

        trxHeaderDO = (TrxHeaderDO)getIntent().getSerializableExtra("Order");

        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {

        tvOrderNo       =(TextView)llOrderCancel.findViewById(R.id.tvOrdercode);
        tvName          =(TextView)llOrderCancel.findViewById(R.id.tvName);
        tvBottelPrice   =(TextView)llOrderCancel.findViewById(R.id.tvBottelPrice);
        tvBottelSize    =(TextView)llOrderCancel.findViewById(R.id.tvBottelSize);
        tvAddressOrder  =(TextView)llOrderCancel.findViewById(R.id.tvaddressOrder);
        tvDate          =(TextView)llOrderCancel.findViewById(R.id.tvDate);
        tvCategoryCaption          =(TextView)llOrderCancel.findViewById(R.id.tvCategoryCaption);
        spReasons       =(Spinner) llOrderCancel.findViewById(R.id.spReasons);
        etAdditionalInformation       =(EditText) llOrderCancel.findViewById(R.id.etAddNotes);
        btnConfirm       =(Button) llOrderCancel.findViewById(R.id.btnConfirm);

        tvOrderNo.setTypeface(AppConstants.DinproMedium);
        tvBottelPrice.setTypeface(AppConstants.DinproMedium);
        tvName.setTypeface(AppConstants.DinproRegular);
        tvAddressOrder.setTypeface(AppConstants.DinproRegular);
        tvDate.setTypeface(AppConstants.DinproRegular);
        tvBottelSize.setTypeface(AppConstants.DinproRegular);

        String name = preference.getStringFromPreference(Preference.CUSTOMER_NAME,"");
        if(!StringUtils.isEmpty(name))
            tvName.setText(name);
        tvOrderNo.setText(trxHeaderDO.trxDisplayCode);
        tvDate.setText(CalendarUtil.convertToTrxDateToShow(trxHeaderDO.trxDate, Locale.getDefault()));
        tvBottelPrice.setText(getResources().getString(R.string.currency)+" "+decimalFormat.format(trxHeaderDO.totalTrxAmount)+"");

        if (trxHeaderDO.trxDetailsDOs != null && trxHeaderDO.trxDetailsDOs.size() > 0) {
            String items="";
            for(TrxDetailsDO trxDetailsDO : trxHeaderDO.trxDetailsDOs)
            {
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(trxDetailsDO.productALTDescription))
                {
                    items = items+ trxDetailsDO.initialQuantity +" X "+trxDetailsDO.productALTDescription+", ";
                }
                else
                {
                    items = items+ trxDetailsDO.initialQuantity +" X "+trxDetailsDO.productName+", ";
                }
            }
            tvBottelSize.setText(items.substring(0,items.length()-2));
        }else{
            tvBottelSize.setText("N/A");
        }

        if (trxHeaderDO.trxAddressBookDOs != null && trxHeaderDO.trxAddressBookDOs.size() > 0) {
            AddressBookDO addressBookDO = trxHeaderDO.trxAddressBookDOs.get(0);
            if(addressBookDO != null){
                String address = "";
                if(!StringUtils.isEmpty(addressBookDO.FlatNumber))
                    address += addressBookDO.FlatNumber;
                if(!StringUtils.isEmpty(addressBookDO.VillaName)) {
                    if(StringUtils.isEmpty(address))
                        address +=  addressBookDO.VillaName;
                    else
                        address += ", " + addressBookDO.VillaName;
                }
                if(!StringUtils.isEmpty(addressBookDO.Street)) {
                    if(StringUtils.isEmpty(address))
                        address +=  addressBookDO.Street;
                    else
                        address += ", " + addressBookDO.Street;
                }

                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                {
                    if(!StringUtils.isEmpty(addressBookDO.addressLine1_Arabic)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine1_Arabic;
                        else
                            address += ", " + addressBookDO.addressLine1_Arabic;
                    }
                    else
                    {
                        if(!StringUtils.isEmpty(addressBookDO.addressLine1)) {
                            if(StringUtils.isEmpty(address))
                                address +=  addressBookDO.addressLine1;
                            else
                                address += ", " + addressBookDO.addressLine1;
                        }
                    }
                    if(!StringUtils.isEmpty(addressBookDO.addressLine2_Arabic))
                        address += ", "+addressBookDO.addressLine2_Arabic;
                    else
                        if(!StringUtils.isEmpty(addressBookDO.addressLine2))
                        address += ", "+addressBookDO.addressLine2;

                    if(!StringUtils.isEmpty(addressBookDO.addressLine3))
                        address += ", "+addressBookDO.addressLine3;
                    if(!StringUtils.isEmpty(addressBookDO.city))
                        address += ", "+addressBookDO.city;


                    if(!StringUtils.isEmpty(addressBookDO.addressLine4_Arabic))
                        address += ", "+addressBookDO.addressLine4_Arabic;
                    else
                        if(!StringUtils.isEmpty(addressBookDO.addressLine4))
                        address += ", "+addressBookDO.addressLine4;
                }
                else
                {

                    if(!StringUtils.isEmpty(addressBookDO.addressLine1)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine1;
                        else
                            address += ", " + addressBookDO.addressLine1;
                    }
                    if(!StringUtils.isEmpty(addressBookDO.addressLine2))
                        address += ", "+addressBookDO.addressLine2;
                    if(!StringUtils.isEmpty(addressBookDO.addressLine3))
                        address += ", "+addressBookDO.addressLine3;
                    if(!StringUtils.isEmpty(addressBookDO.city))
                        address += ", "+addressBookDO.city;


                    if(!StringUtils.isEmpty(addressBookDO.addressLine4))
                        address += ", "+addressBookDO.addressLine4;
                }




                tvAddressOrder.setText(address);
            }
        }else{
            tvAddressOrder.setText("N/A");
        }

        spReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0){
                    tvCategoryCaption.setVisibility(View.INVISIBLE);
                }else
                    tvCategoryCaption.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedpos = spReasons.getSelectedItemPosition();
                String category = orderCancelDOs.get(selectedpos).Category;
                String additionalInfo = etAdditionalInformation.getText().toString();
                 if (StringUtils.isEmpty(category) || category.equalsIgnoreCase("Select Reason for Cancellation")) {
                    showCustomDialog(OrderCancelActivity.this,getResources().getString(R.string.alert), getResources().getString(R.string.plzReason), getResources().getString(R.string.ok), "", "");
                } if (StringUtils.isEmpty(additionalInfo)) {
                    showCustomDialog(OrderCancelActivity.this,getResources().getString(R.string.alert), getResources().getString(R.string.plzaddtitionalInfo), getResources().getString(R.string.ok), "", "");
                }else if (checkNetworkConnection()) {
                    showLoader("Cancelling Order...");
                    String userCode = preference.getStringFromPreference(Preference.CUSTOMER_CODE,"");
                     String Language = "";
                     if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                     {
                         Language = "Arabic";
                     }
                     else
                     {
                         Language = "English";
                     }
                    new CommonBL(OrderCancelActivity.this, OrderCancelActivity.this).cancelOrder(trxHeaderDO, userCode, category, additionalInfo,Language);
                }
            }
        });

    }

    @Override
    public void loadData() {

        showLoader(getString(R.string.Loading_Data));
        new Thread(new Runnable() {
            @Override
            public void run() {

                orderCancelDOs = new OrderDA(OrderCancelActivity.this).getCancellationReasonsObjs();
                final ArrayList<String> reasons = new ArrayList<String>();
                for(OrderCancelDO orderCancelDO : orderCancelDOs)
                {
                    if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(orderCancelDO.AltDescription))
                    {
                        reasons.add(orderCancelDO.AltDescription);
                    }
                    else
                    {
                        reasons.add(orderCancelDO.Category);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if(reasons != null && reasons.size()>0){
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderCancelActivity.this,android.R.layout.simple_spinner_item, reasons);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spReasons.setAdapter(dataAdapter);
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if(from.equalsIgnoreCase("Sucess")){

            setResult(RESULT_OK);
            //Commented by Sridhar
            /*Intent intent = new Intent(OrderCancelActivity.this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            finish();
        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && response.method == ServiceMethods.WS_CANCEL_ORDER) {
            if (response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (placeOrderDO.arrTrxHeaderDOs != null && placeOrderDO.arrTrxHeaderDOs.size() > 0) {
                            placeOrderDO.arrTrxHeaderDOs.get(0).isCancelled = true;
                            new OrderDA(OrderCancelActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                showCustomDialog(OrderCancelActivity.this, getString(R.string.success), getString(R.string.order_cancel), getString(R.string.OK), "", "Sucess");
                            }
                        });
                    }
                }).start();
            } else {
                hideLoader();
                showCustomDialog(OrderCancelActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }
    }
}
