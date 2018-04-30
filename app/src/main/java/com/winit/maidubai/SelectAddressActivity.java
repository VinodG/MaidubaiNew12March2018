package com.winit.maidubai;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CartDA;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CartDetailsDO;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Girish Velivela on 05-08-2016.
 */
public class SelectAddressActivity extends BaseActivity{

    private ScrollView svSelectAddreess;
    private LinearLayout llSecondary;

    private TextView tvAddress,tvArea,tvSubArea,tvAddressTtl, tvNotesTtl,tvDevliveryNote,tvOrderName,tvRecurrType,tvRecurr,tvDeliveryDate;
    private EditText etAddNotes,etAddNotesSecondary,etOrderName,etDeliveryDate;
    private Button btnContinue;
    private ImageView ivAddNotes,ivSubNotes,ivRecurrInfo;
    private String sessionId,deviceId;
    private AddressBookDO addressBookDO;
    private boolean isRecurrType;

    private boolean paymentModeIsCOD;
    private RadioGroup rgPaymentMode;
    private int year, month, day;
    String deliveryDate;
    @Override
    public void initialise() {
        svSelectAddreess = (ScrollView) inflater.inflate(R.layout.selectaddress_activity, null);
        llBody.addView(svSelectAddreess, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceNormal(svSelectAddreess);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.selectaddress));
        lockDrawer("SelectAddressActivity");
        addressBookDO = (AddressBookDO)getIntent().getSerializableExtra("SelAddress");
        setStatusBarColor();
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {

        llSecondary = (LinearLayout) svSelectAddreess.findViewById(R.id.llSecondary);
        tvAddressTtl = (TextView) svSelectAddreess.findViewById(R.id.tvAddressTtl);
        tvNotesTtl = (TextView) svSelectAddreess.findViewById(R.id.tvNotesTtl);
        tvAddress = (TextView) svSelectAddreess.findViewById(R.id.tvAddress);
        tvArea = (TextView) svSelectAddreess.findViewById(R.id.tvArea);
        tvSubArea = (TextView) svSelectAddreess.findViewById(R.id.tvSubArea);
        tvOrderName = (TextView) svSelectAddreess.findViewById(R.id.tvOrderName);
        tvDevliveryNote = (TextView) svSelectAddreess.findViewById(R.id.tvDevliveryNote);
        etAddNotes = (EditText) svSelectAddreess.findViewById(R.id.etAddNotes);
//        etDeliveryDate = (EditText) svSelectAddreess.findViewById(R.id.etDeliveryDate);
        etOrderName = (EditText) svSelectAddreess.findViewById(R.id.etOrderName);
        tvRecurr = (TextView) svSelectAddreess.findViewById(R.id.tvRecurr);
        tvDeliveryDate= (TextView) svSelectAddreess.findViewById(R.id.tvDeliveryDate);
        tvRecurrType = (TextView) svSelectAddreess.findViewById(R.id.tvRecurrType);
        etAddNotesSecondary = (EditText) svSelectAddreess.findViewById(R.id.etAddNotesSecondary);
        ivAddNotes = (ImageView) svSelectAddreess.findViewById(R.id.ivAddNotes);
        ivSubNotes = (ImageView) svSelectAddreess.findViewById(R.id.ivSubNotes);
        ivRecurrInfo = (ImageView) svSelectAddreess.findViewById(R.id.ivRecurrInfo);
        btnContinue = (Button) svSelectAddreess.findViewById(R.id.btnContinue);
        rgPaymentMode = (RadioGroup)svSelectAddreess.findViewById(R.id.rgPaymentMode);

        String token = preference.getStringFromPreference(Preference.GCM_TOKEN, "");
        String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        deviceId = StringUtils.isEmpty(token) ? DeviceId : token;
        tvAddressTtl.setTypeface(AppConstants.DinproBold);
        tvNotesTtl.setTypeface(AppConstants.DinproBold);

        tvAddress.setTypeface(AppConstants.DinproRegular);
        tvArea.setTypeface(AppConstants.DinproRegular);
        tvSubArea.setTypeface(AppConstants.DinproRegular);
        tvDevliveryNote.setTypeface(AppConstants.DinproRegular);
        tvOrderName.setTypeface(AppConstants.DinproRegular);

        tvRecurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressBookDO != null && addressBookDO.isExisting) {
                    if (isRecurrType) {
                        tvRecurr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncheck, 0, 0, 0);
                        tvRecurrType.setText("");
                        isRecurrType = false;
                    } else {
                        BottomSheetDialogFragment bottomSheetDialogFragment = new RecurrTypeBottomSheet();
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    }
                } else
                    showCustomDialog(SelectAddressActivity.this, "", getString(R.string.recurr_warning), getString(R.string.OK), "", "");
            }
        });

        ivAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes = etAddNotes.getText().toString();
                if (!StringUtils.isEmpty(notes)) {
                    ivAddNotes.setVisibility(View.GONE);
                    llSecondary.setVisibility(View.VISIBLE);
                } else {
                    showCustomDialog(SelectAddressActivity.this, "", getString(R.string.enter_notes), getString(R.string.OK), "", "");
                }
            }
        });

        ivSubNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddNotesSecondary.setText("");
                ivAddNotes.setVisibility(View.VISIBLE);
                llSecondary.setVisibility(View.GONE);
            }
        });

        ivRecurrInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(SelectAddressActivity.this, "", getString(R.string.recurringnarration), getString(R.string.OK), "", "");
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (StringUtils.isEmpty(etOrderName.getText().toString().trim())) {
                    showCustomDialog(SelectAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterorderName), getResources().getString(R.string.ok), "", "");
                }*/
                ArrayList<ProductDO> productDOs =  new CartDA(SelectAddressActivity.this).getProductOrderLimit();;
                if(TextUtils.isEmpty(tvDeliveryDate.getText().toString()))
                {
                    showAlert(getResources().getString(R.string.select_delivery_date),null);
               }else  if (productDOs != null && productDOs.size() > 0) {
                        showCustomDialog(SelectAddressActivity.this, "", getString(R.string.order_limit), getString(R.string.OK), "", "Order Limit");
                    } else {
                        int selectedId = rgPaymentMode.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(selectedId);
                        if (rb == null) {
                            Toast.makeText(SelectAddressActivity.this, "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                        } else {
                            if (rb.getText() != null && rb.getText().toString().equalsIgnoreCase(getString(R.string.rbIsCode))) {
                                paymentModeIsCOD = true;

                            }
                            if (rb.getText() != null && rb.getText().toString().equalsIgnoreCase(getString(R.string.rbRossom))) {
                                paymentModeIsCOD = false;

                            }
                            if (checkNetworkConnection()) {
                                showLoader(getResources().getString(R.string.order_placing_text));
                                CartDetailsDO cartDetailsDO = new CartDetailsDO();
                                cartDetailsDO.sessionId = sessionId;
                                cartDetailsDO.deviceId = deviceId;
                                new CommonBL(SelectAddressActivity.this, SelectAddressActivity.this).updateCartDetails(cartDetailsDO);
                            }
                        }
                    }
            }
        });
    }


    public void selectedRcurrType(String recurrType){
        tvRecurrType.setText(recurrType);
        isRecurrType = true;
        tvRecurr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checksel, 0, 0, 0);
    }

    @Override
    public void loadData() {
        sessionId = preference.getStringFromPreference(Preference.SESSION_ID, "");
        addressBookDO.sessionId = sessionId;
        String address = "";
        String address1 = "";
        String address2 = "";
        if(!StringUtils.isEmpty(addressBookDO.FlatNumber))
            address += addressBookDO.FlatNumber;
        if(!StringUtils.isEmpty(addressBookDO.VillaName)) {
            if(StringUtils.isEmpty(address))
                address += addressBookDO.VillaName;
            else
                address += ", " + addressBookDO.VillaName;
        }
        if(!StringUtils.isEmpty(addressBookDO.Street)) {
            if(StringUtils.isEmpty(address))
                address += addressBookDO.Street;
            else
                address += ", " + addressBookDO.Street;
        }

        if(!StringUtils.isEmpty(addressBookDO.addressLine3))
            address1 += addressBookDO.addressLine3;
        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
        {
            if(!StringUtils.isEmpty(addressBookDO.addressLine2_Arabic)) {
                if(StringUtils.isEmpty(address1))
                    address1 += addressBookDO.addressLine2_Arabic;
                else
                    address1 += ", " + addressBookDO.addressLine2_Arabic;
            }
            else {
                if(!StringUtils.isEmpty(addressBookDO.addressLine2)) {
                    if(StringUtils.isEmpty(address1))
                        address1 += addressBookDO.addressLine2;
                    else
                        address1 += ", " + addressBookDO.addressLine2;
                }
            }
            if(!StringUtils.isEmpty(addressBookDO.addressLine1_Arabic))
                address2 += addressBookDO.addressLine1_Arabic;
            else
            {
                if(!StringUtils.isEmpty(addressBookDO.addressLine1))
                    address2 += addressBookDO.addressLine1;
            }

            if(!StringUtils.isEmpty(addressBookDO.addressLine4_Arabic)){
                if(StringUtils.isEmpty(address2))
                    address2 += addressBookDO.addressLine4_Arabic;
                else
                    address2 += ","+addressBookDO.addressLine4_Arabic;
            }
            else
            {
                if(!StringUtils.isEmpty(addressBookDO.addressLine4)){
                    if(StringUtils.isEmpty(address2))
                        address2 += addressBookDO.addressLine4;
                    else
                        address2 += ","+addressBookDO.addressLine4;
                }
            }
        }
        else
        {
            if(!StringUtils.isEmpty(addressBookDO.addressLine2)) {
                if(StringUtils.isEmpty(address1))
                    address1 += addressBookDO.addressLine2;
                else
                    address1 += ", " + addressBookDO.addressLine2;
            }
            if(!StringUtils.isEmpty(addressBookDO.addressLine1))
                address2 += addressBookDO.addressLine1;

            if(!StringUtils.isEmpty(addressBookDO.addressLine4)){
                if(StringUtils.isEmpty(address2))
                    address2 += addressBookDO.addressLine4;
                else
                    address2 += ","+addressBookDO.addressLine4;
            }
        }

        tvAddress.setText(address);
        tvArea.setText(address1);
        tvSubArea.setText(address2);
/*
        showLoader("Loading data....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerDO = new CustomerDA(SelectAddressActivity.this).getCustomer();
                customerDO.sessionId = sessionId;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if(customerDO.addressBookDO != null){
                            customerDO.addressBookDO.sessionId = sessionId;
                            String address = "";
                            String address1 = "";
                            String address2 = "";
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.FlatNumber))
                                address += customerDO.addressBookDO.FlatNumber;
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.VillaName)) {
                                if(StringUtils.isEmpty(address))
                                    address += customerDO.addressBookDO.VillaName;
                                else
                                    address += ", " + customerDO.addressBookDO.VillaName;
                            }
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.addressLine1))
                                address += customerDO.addressBookDO.addressLine1;
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.addressLine2))
                                address1 += customerDO.addressBookDO.addressLine2;
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.addressLine3)) {
                                if(StringUtils.isEmpty(address1))
                                    address1 += customerDO.addressBookDO.addressLine3;
                                else
                                    address1 += "," + customerDO.addressBookDO.addressLine3;
                            }
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.city))
                                address2 += customerDO.addressBookDO.city;
                            if(!StringUtils.isEmpty(customerDO.addressBookDO.addressLine4)){
                                if(StringUtils.isEmpty(address1))
                                    address2 += customerDO.addressBookDO.addressLine4;
                                else
                                    address2 += ","+customerDO.addressBookDO.addressLine4;
                            }
                            tvAddress.setText(address);
                            tvArea.setText(address1);
                            tvSubArea.setText(address2);

//                            String format = "%-14.14s %2.2s %-14.14s";
//                            tvArea.setText(String.format(format,"Area ",": ",customerDO.addressDO.area));
//                            tvSubArea.setText(String.format(format,"Sub Area ",": ",customerDO.addressDO.landmark));
                        }else{
                            showCustomDialog(SelectAddressActivity.this,"",getString(R.string.please_select_address),getString(R.string.OK),"","Add Address");
                        }
                    }
                });
            }
        }).start();*/
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Add Address")){
            Intent intent = new Intent(SelectAddressActivity.this,ManageAddressActivity.class);
            intent.putExtra("CustomerDo", customerDO);
            startActivity(intent);
        }else if(from.equalsIgnoreCase( "Order Limit")) {
            if (checkNetworkConnection()) {
                showLoader(getResources().getString(R.string.order_placing_text));
                CartDetailsDO cartDetailsDO = new CartDetailsDO();
                cartDetailsDO.sessionId = sessionId;
                cartDetailsDO.deviceId = deviceId;
                new CommonBL(SelectAddressActivity.this, SelectAddressActivity.this).updateCartDetails(cartDetailsDO);
            }
        }
        else if(from.equalsIgnoreCase( "Go Recurring Orders"))
        {
            setResult(RESULT_OK, getIntent());
            finish();
            Intent intent = new Intent(SelectAddressActivity.this, MyRecurringOrdersActivity.class);
            startActivity(intent);
        }
        super.onButtonYesClick(from);
    }
    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && (response.method == ServiceMethods.WS_UPDATE_CART_DETAILS &&response.data != null)) {
            if(response.data instanceof CartDetailsDO){
                addressBookDO.addressType = AddressBookDO.BILLING_ADDRESS_TYPE;
                new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).addOrderAddress(addressBookDO);
            }else{
                hideLoader();
                showCustomDialog(SelectAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_ADD_ORDER_ADDRESS &&response.data != null)) {
            if(response.data instanceof AddressBookDO){
                if(addressBookDO.addressType.equalsIgnoreCase(AddressBookDO.BILLING_ADDRESS_TYPE)){
                    addressBookDO.addressType = AddressBookDO.SIPPINGING_ADDRESS_TYPE;
                    new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).addOrderAddress(addressBookDO);
                }else{
                    PlaceOrderDO placeOrderDO = new PlaceOrderDO();
                    placeOrderDO.sessionId = sessionId;
                    placeOrderDO.trxDate = CalendarUtil.getDate(new Date(),CalendarUtil.SYNCH_DATE_TIME_PATTERN);
                    placeOrderDO.isCOD = paymentModeIsCOD;
                    placeOrderDO.useWallet = false;
                    placeOrderDO.deviceId = deviceId;
                    placeOrderDO.SpecialInstructions = etAddNotes.getText().toString();
                    placeOrderDO.OrderName = etOrderName.getText().toString();
                    placeOrderDO.existingAddress = addressBookDO.isExisting;
                    placeOrderDO.deliverydate = deliveryDate;//newly added
                    placeOrderDO.isRecurring = isRecurrType;

                    if(isRecurrType)
                        placeOrderDO.frequency = tvRecurrType.getText().toString().trim();
                    if(language.equalsIgnoreCase("en")){
                        placeOrderDO.PreferredLanguage = "English";
                    }else{
                        placeOrderDO.PreferredLanguage = "Arabic";
                    }
//                    new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).placeCODOrder(placeOrderDO);
//                    vinod
                    if(paymentModeIsCOD)
                        new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).placeCODOrder(placeOrderDO);
                    else
                    {
                        new CommonBL(SelectAddressActivity.this, SelectAddressActivity.this).placeCODOrder(placeOrderDO);
//                            callWebViewForRossom();
                    }

                }
            }else{
                hideLoader();
                showCustomDialog(SelectAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        }/* else if(response.method!=null && response.method == ServiceMethods.WS_PLACE_ORDER){
            if(response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new OrderDA(SelectAddressActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                        new CartDA(SelectAddressActivity.this).deleteProduct("");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                setResult(RESULT_OK, getIntent());
                                finish();
                                Intent intent = new Intent(SelectAddressActivity.this, OrderSuccessActivity.class);
                                intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }*/
        //vinod
        else if(response.method!=null && response.method == ServiceMethods.WS_PLACE_ORDER){
            if(response.data != null && response.data instanceof PlaceOrderDO)
            {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                placeOrderDO.isCOD= placeOrderDO.arrTrxHeaderDOs.get(0).isCOD;
                if(placeOrderDO!=null &&   placeOrderDO.isCOD == true ) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new OrderDA(SelectAddressActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                            new CartDA(SelectAddressActivity.this).deleteProduct("");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoader();
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                    if(AppConstants.IS_TO_EDIT_ORDER)
                                    {
                                        syncData(placeOrderDO.arrTrxHeaderDOs.get(0));
                                    }
                                    else {
//                                    Intent intent = new Intent(SelectAddressActivity.this, OrderSuccessActivity.class);
                                        Intent intent = new Intent(SelectAddressActivity.this, MyOrdersActivity.class);
                                        intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }).start();
                }else
                if(placeOrderDO!=null &&   placeOrderDO.isCOD == false)
                {
                    // code for payment
                    hideLoader();
                    setResult(RESULT_OK, getIntent());
                    finish();
                    Intent intent = new Intent(SelectAddressActivity.this, RossomPaymentActivity.class);
                    intent.putExtra("PLACE_ORDER",placeOrderDO);
                    startActivity(intent);
                }



            }
            //---------------





            else if(response.data.toString().contains("-16")){
                hideLoader();
                String msg = response.data.toString().replace("-16","");
                if(msg.contains("You have already placed maximum number of Recurring Orders") || msg.contains("More Than 3 Orders"))
                    showCustomDialog(SelectAddressActivity.this, "", getString(R.string.maxrecurringorders),getString(R.string.Go_to_recur_orders), getString(R.string.OK), "Go Recurring Orders");
                else
                    showCustomDialog(SelectAddressActivity.this, "", msg, getString(R.string.OK), "", "");
            }else
            {
                hideLoader();
                showCustomDialog(SelectAddressActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }


    }
    /*

    before payment activity is added

    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && (response.method == ServiceMethods.WS_UPDATE_CART_DETAILS &&response.data != null)) {
            if(response.data instanceof CartDetailsDO){
                addressBookDO.addressType = AddressBookDO.BILLING_ADDRESS_TYPE;
                new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).addOrderAddress(addressBookDO);
            }else{
                hideLoader();
                showCustomDialog(SelectAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_ADD_ORDER_ADDRESS &&response.data != null)) {
            if(response.data instanceof AddressBookDO){
                if(addressBookDO.addressType.equalsIgnoreCase(AddressBookDO.BILLING_ADDRESS_TYPE)){
                    addressBookDO.addressType = AddressBookDO.SIPPINGING_ADDRESS_TYPE;
                    new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).addOrderAddress(addressBookDO);
                }else{
                    PlaceOrderDO placeOrderDO = new PlaceOrderDO();
                    placeOrderDO.sessionId = sessionId;
                    placeOrderDO.trxDate = CalendarUtil.getDate(new Date(),CalendarUtil.SYNCH_DATE_TIME_PATTERN);
                    placeOrderDO.isCOD = true;
                    placeOrderDO.useWallet = false;
                    placeOrderDO.deviceId = deviceId;
                    placeOrderDO.SpecialInstructions = etAddNotes.getText().toString();
                    placeOrderDO.OrderName = etOrderName.getText().toString();
                    placeOrderDO.existingAddress = addressBookDO.isExisting;
                    placeOrderDO.isRecurring = isRecurrType;
                    if(isRecurrType)
                        placeOrderDO.frequency = tvRecurrType.getText().toString().trim();
                    if(language.equalsIgnoreCase("en")){
                        placeOrderDO.PreferredLanguage = "English";
                    }else{
                        placeOrderDO.PreferredLanguage = "Arabic";
                    }
                    new CommonBL(SelectAddressActivity.this,SelectAddressActivity.this).placeCODOrder(placeOrderDO);
                }
            }else{
                hideLoader();
                showCustomDialog(SelectAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        } else if(response.method!=null && response.method == ServiceMethods.WS_PLACE_ORDER){
            if(response.data != null && response.data instanceof PlaceOrderDO) {
                final PlaceOrderDO placeOrderDO = (PlaceOrderDO) response.data;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new OrderDA(SelectAddressActivity.this).insertOrders(placeOrderDO.arrTrxHeaderDOs);
                        new CartDA(SelectAddressActivity.this).deleteProduct("");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                setResult(RESULT_OK, getIntent());
                                finish();
                                Intent intent = new Intent(SelectAddressActivity.this, OrderSuccessActivity.class);
                                intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            } else if(response.data.toString().contains("-16")){
                hideLoader();
                String msg = response.data.toString().replace("-16","");
                if(msg.contains("You have already placed maximum number of Recurring Orders") || msg.contains("More Than 3 Orders"))
                    showCustomDialog(SelectAddressActivity.this, "", getString(R.string.maxrecurringorders),getString(R.string.Go_to_recur_orders), getString(R.string.OK), "Go Recurring Orders");
                else
                    showCustomDialog(SelectAddressActivity.this, "", msg, getString(R.string.OK), "", "");
            }else
            {
                hideLoader();
                showCustomDialog(SelectAddressActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }
    }*/
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca",
//                Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    showDate(i, i1+1, i2);
                }

            /*    @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                }*/
            };
    private void showDate(int year, int month, int day) {
          deliveryDate = CalendarUtil.getServiceDate(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day).append("T00:00:00").toString())+"T00:00:00";
        tvDeliveryDate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
/*
        etDeliveryDate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
*/
    }
    private void syncData(final TrxHeaderDO trxHeaderDO)
    {
        boolean isSyncing = preference.getbooleanFromPreference(Preference.IS_SYNCING,false);
        if(isSyncing || !NetworkUtil.isNetworkConnectionAvailable(SelectAddressActivity.this))
        {

        }
//            loadData();
        else {
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
//                            loadData();
                            showAlert("Error occured while syncing ",null);
                        }
                    });
                }

                @Override
                public void onEnd() {
                    hideLoader();
                    AppConstants.IS_TO_EDIT_ORDER =false;
                    moveToOrderSummary(trxHeaderDO);
//                    updateUI();
                }


            });
        }

    }
    private void moveToOrderSummary(TrxHeaderDO trxHeaderDO)
    {
        Intent intent = new Intent(SelectAddressActivity.this, MyOrdersActivity.class);
        intent.putExtra("Order", trxHeaderDO);
        startActivity(intent);
    }

}
