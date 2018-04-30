package com.winit.maidubai;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.maidubai.adapter.OrderSuccessAdapter;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.StringUtils;

public class OrderSuccessActivity extends BaseActivity {
    private LinearLayout llMain,llOrdersDetails;
    private TextView tvOrder,tvAddress,tvOrderDetails,tvAdressDetails,tvArea,tvSubArea,tvSpecialIns;
    private ListView lvOrderDetails;
    private TrxHeaderDO trxHeaderDO;
    private Button btnPlaceOrder;

    @Override
    public void initialise() {
        llMain = (LinearLayout) inflater.inflate(R.layout.order_success_layout, null);
        llBody.addView(llMain,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceMedium(llMain);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.order_success);
        tvCancel.setVisibility(View.VISIBLE);
        setStatusBarColor();
        tvCancel.setText(R.string.view_orders);
        lockDrawer("OrderSuccessActivity");
        trxHeaderDO = (TrxHeaderDO)getIntent().getSerializableExtra("Order");
        initialiseControls();
        loadData();

        showCustomDialog(OrderSuccessActivity.this,"",getString(R.string.Please_provide_us_your_valuable_feedback),getString(R.string.OK),getString(R.string.cancel),"SendFeedback");
    }

    public void setListViewHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
//            for (int itemPos = 0; itemPos < numberOfItems && itemPos<2; itemPos++) {
            View item = listAdapter.getView(0, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
//            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (1);
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    @Override
    public void initialiseControls() {
        llOrdersDetails = (LinearLayout)llMain.findViewById(R.id.llOrdersDetails);
        tvOrder= (TextView)llMain.findViewById(R.id.tvOrder);
        tvOrderDetails= (TextView)llMain.findViewById(R.id.tvOrderDetails);
        tvAdressDetails= (TextView)llMain.findViewById(R.id.tvAdressDetails);
        tvAddress= (TextView)llMain.findViewById(R.id.tvAddress);
        tvArea= (TextView)llMain.findViewById(R.id.tvArea);
        tvSubArea= (TextView)llMain.findViewById(R.id.tvSubArea);
        btnPlaceOrder= (Button)llMain.findViewById(R.id.btnPlaceOrder);
        tvSpecialIns=(TextView)findViewById(R.id.tvSpecialIns);
        lvOrderDetails= (ListView)llMain.findViewById(R.id.lvOrderDetails);
        tvOrderDetails.setTypeface(AppConstants.DinproMedium);
        tvOrder.setTypeface(AppConstants.DinproMedium);
        tvSpecialIns.setTypeface(AppConstants.DinproMedium);
        tvAdressDetails.setTypeface(AppConstants.DinproMedium);
        tvArea.setTypeface(AppConstants.DinproRegular);
        tvAddress.setTypeface(AppConstants.DinproRegular);
        tvSubArea.setTypeface(AppConstants.DinproMedium);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, MyOrdersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this,DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void loadData() {
        OrderSuccessAdapter adapter = new OrderSuccessAdapter(OrderSuccessActivity.this,trxHeaderDO.trxDetailsDOs);
        int size = trxHeaderDO.trxDetailsDOs.size();
        if(size>1){
            lvOrderDetails.setDivider(getResources().getDrawable(R.drawable.seprator));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
            params.setMargins(10,0,10,0);
            llOrdersDetails.setLayoutParams(params);
        }else{
            setListViewHeight(lvOrderDetails);
        }
        lvOrderDetails.setAdapter(adapter);
        if(trxHeaderDO.trxAddressBookDOs != null && trxHeaderDO.trxAddressBookDOs.size()>0)
        {
            AddressBookDO addressBookDO = trxHeaderDO.trxAddressBookDOs.get(0);
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
        }


        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
        {
            if(StringUtils.isEmpty(trxHeaderDO.orderName))
                tvOrder.setText(String.format(getResources().getString(R.string.order_suss),(StringUtils.isEmpty(trxHeaderDO.trxDisplayCode)?trxHeaderDO.trxCode:trxHeaderDO.trxDisplayCode)));
            else
                tvOrder.setText(String.format(getResources().getString(R.string.order_suss).replace("",""),trxHeaderDO.orderName+" ("+(StringUtils.isEmpty(trxHeaderDO.trxDisplayCode)?trxHeaderDO.trxCode:trxHeaderDO.trxDisplayCode)+")"));
        }
        else
        {

            if(StringUtils.isEmpty(trxHeaderDO.orderName)) {
                tvOrder.setText(String.format(getResources().getString(R.string.order_suss),""+(StringUtils.isEmpty(trxHeaderDO.trxDisplayCode)?trxHeaderDO.trxCode:trxHeaderDO.trxDisplayCode)));
            }
            else {
                tvOrder.setText(String.format(getResources().getString(R.string.order_suss),trxHeaderDO.orderName+" ("+(StringUtils.isEmpty(trxHeaderDO.trxDisplayCode)?trxHeaderDO.trxCode:trxHeaderDO.trxDisplayCode)+")"));
            }
        }

        if(StringUtils.isEmpty(trxHeaderDO.specialInstructions))
            tvSpecialIns.setText(getString(R.string.special_instr)+getString(R.string.na));
        else
            tvSpecialIns.setText(getString(R.string.special_instr)+trxHeaderDO.specialInstructions);
    }

    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if(from.equalsIgnoreCase("SendFeedback")){
            Intent intent = new Intent(OrderSuccessActivity.this,SendFeedbackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
