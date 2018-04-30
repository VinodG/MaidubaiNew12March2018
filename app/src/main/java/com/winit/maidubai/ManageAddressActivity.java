package com.winit.maidubai;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.List;

public class ManageAddressActivity extends BaseActivity {
    public LinearLayout selectAddress, llNoAddress;
    private Button btnContinue, btnNewAddress;
    private ListView lvAddress;
    private List<AddressBookDO> arrAddressBookDOs = new ArrayList<>();
    private AddressListAdapter addressListAdapter;
    private boolean isSignUp;
    private CustomerDO customerDO;
    private int addressId,activityType;
    private AddressBookDO selAddressBookDO;
    private TextView tvAddNewAddress;

    @Override
    public void initialise() {
        selectAddress = (LinearLayout) inflater.inflate(R.layout.select_address, null);
        llBody.addView(selectAddress, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceMedium(selectAddress);
        setStatusBarColor();
        tvTitle.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);
        lockDrawer("Manage Address");
        initialiseControls();
        activityType = getIntent().getIntExtra("ActivityType", 0);
        if(activityType == AppConstants.SELECT_ADDRESS) {
            tvTitle.setText(getResources().getString(R.string.selectaddress));
            btnNewAddress.setText(R.string.continue_a);
            tvAddNewAddress.setVisibility(View.VISIBLE);
        }else
            tvTitle.setText(getResources().getString(R.string.manageaddress));

        isSignUp = preference.getbooleanFromPreference(Preference.ISSINGUP, false);
        if (isSignUp) {
            btnNewAddress.setVisibility(View.GONE);
        }
        customerDO = (CustomerDO) getIntent().getSerializableExtra("CustomerDo");

        addressListAdapter = new AddressListAdapter(null);
        lvAddress.setAdapter(addressListAdapter);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void initialiseControls() {
        btnContinue = (Button) selectAddress.findViewById(R.id.btnContinue);
        btnNewAddress = (Button) selectAddress.findViewById(R.id.btnNewAddress);
        tvAddNewAddress = (TextView) selectAddress.findViewById(R.id.tvAddNewAddress);
        lvAddress = (ListView) selectAddress.findViewById(R.id.lvAddress);
        llNoAddress = (LinearLayout) selectAddress.findViewById(R.id.llNoAddress);
        btnNewAddress.setTypeface(AppConstants.DinproMedium);

        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityType == AppConstants.SELECT_ADDRESS) {
                    selAddressBookDO.phoneNo = customerDO.mobileNumber;
                    selAddressBookDO.email = customerDO.cutomerEmailId;
                    selAddressBookDO.mobileNo = customerDO.mobileNumber;
                    Intent intent = new Intent(ManageAddressActivity.this, SelectAddressActivity.class);
                    intent.putExtra("SelAddress", selAddressBookDO);
                    startActivityForResult(intent, AppConstants.destoryActivity);
                }else {
                    if (arrAddressBookDOs.size() > 1) {
                        addressId = arrAddressBookDOs.get(1).addressBookId;
                        showCustomDialog(ManageAddressActivity.this, "", getString(R.string.address_override), getString(R.string.OK), getString(R.string.cancel), "AddressOverride");
                    } else {
                        Intent intent = new Intent(ManageAddressActivity.this, SaveAddressActivity.class);
                        intent.putExtra("CustomerDo", customerDO);
                        intent.putExtra("AddressId", 0);
                        startActivity(intent);
                    }
                }
            }
        });

        tvAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrAddressBookDOs.size() > 1) {
                    addressId = arrAddressBookDOs.get(1).addressBookId;
                    showCustomDialog(ManageAddressActivity.this, "", getString(R.string.address_override), getString(R.string.OK), getString(R.string.cancel), "AddressOverrideOrder");
                } else {
                    Intent intent = new Intent(ManageAddressActivity.this, SaveAddressActivity.class);
                    intent.putExtra("CustomerDo", customerDO);
                    intent.putExtra("ActivityType", AppConstants.SELECT_ADDRESS);
                    startActivityForResult(intent, AppConstants.destoryActivity);
                }
            }
        });

    }

    @Override
    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(customerDO == null)
                    customerDO = new CustomerDA(ManageAddressActivity.this).getCustomer();
                if (customerDO.addressBookDO != null) {
                    selAddressBookDO = customerDO.addressBookDO;
                    selAddressBookDO.isExisting = true;
                    arrAddressBookDOs.add(customerDO.addressBookDO);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (NetworkUtil.isNetworkConnectionAvailable(ManageAddressActivity.this)){
                            showLoader(getString(R.string.Loading_Data));
                            new CommonBL(ManageAddressActivity.this, ManageAddressActivity.this).getAddressBookList(customerDO.Id, customerDO.AuthToken);
                        }
                        if(arrAddressBookDOs!= null && arrAddressBookDOs.size() > 0) {
                            llNoAddress.setVisibility(View.GONE);
                            lvAddress.setVisibility(View.VISIBLE);
                            addressListAdapter.refresh(arrAddressBookDOs);
                        }else{
                            llNoAddress.setVisibility(View.VISIBLE);
                            lvAddress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if(from.equalsIgnoreCase("Remove Addres")) {
            if(checkNetworkConnection())
            {
                showLoader("Please wait...");
                new CommonBL(ManageAddressActivity.this,ManageAddressActivity.this).removeAddress(customerDO.Id, selAddressBookDO.addressBookId, customerDO.AuthToken);
            }
        }else if(from.equalsIgnoreCase("AddressOverride")) {

            Intent intent = new Intent(ManageAddressActivity.this, SaveAddressActivity.class);
            intent.putExtra("CustomerDo", customerDO);
            intent.putExtra("AddressId", addressId);
            startActivity(intent);
        }else if(from.equalsIgnoreCase("AddressOverrideOrder")) {

            Intent intent = new Intent(ManageAddressActivity.this, SaveAddressActivity.class);
            intent.putExtra("CustomerDo", customerDO);
            intent.putExtra("AddressId", addressId);
            intent.putExtra("ActivityType", AppConstants.SELECT_ADDRESS);
            startActivityForResult(intent, AppConstants.destoryActivity);
        }
    }

    class AddressListAdapter extends BaseAdapter {
        List<AddressBookDO> arrAddressBookDOs;

        public AddressListAdapter(List<AddressBookDO> arrAddressBookDOs) {
            this.arrAddressBookDOs = arrAddressBookDOs;
        }

        @Override
        public int getCount() {
            if(arrAddressBookDOs !=null)
                return arrAddressBookDOs.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrAddressBookDOs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void refresh( List<AddressBookDO> arrAddressBookDOs)
        {
            this.arrAddressBookDOs = arrAddressBookDOs;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AddressBookDO addressBookDO = arrAddressBookDOs.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.select_address_row, null);
                viewHolder = new ViewHolder();
                viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tvArea);
                viewHolder.tvSubArea = (TextView) convertView.findViewById(R.id.tvSubArea);
                viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                viewHolder.ivCancel = (ImageView) convertView.findViewById(R.id.ivCancel);
                viewHolder.chkAddress = (CheckBox) convertView.findViewById(R.id.chkAddress);
                viewHolder.tvAddress.setTypeface(AppConstants.DinproRegular);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(activityType == AppConstants.SELECT_ADDRESS){
                viewHolder.chkAddress.setVisibility(View.VISIBLE);
                viewHolder.ivCancel.setVisibility(View.GONE);
                if(selAddressBookDO != null && selAddressBookDO.addressBookId == addressBookDO.addressBookId){
                    viewHolder.chkAddress.setChecked(true);
                }else{
                    viewHolder.chkAddress.setChecked(false);
                }
            }else {
                if(addressBookDO.isActive)
                    viewHolder.ivCancel.setVisibility(View.VISIBLE);
                else
                    viewHolder.ivCancel.setVisibility(View.GONE);

                convertView.setEnabled(false);
            }

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
            viewHolder.tvAddress.setText(address);
            viewHolder.tvArea.setText(address1);
            viewHolder.tvSubArea.setText(address2);
            if(addressBookDO.isActive)
            {
                viewHolder.tvStatus.setText(getString(R.string.pending));
                viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.order_detail_red));
            }else
            {
                viewHolder.tvStatus.setText(getString(R.string.active));
                viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.paid_via_text));
            }
            viewHolder.chkAddress.setTag(R.string.Address_TAG,convertView);
            viewHolder.ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selAddressBookDO = addressBookDO;
                    showCustomDialog(ManageAddressActivity.this, getString(R.string.confirm), getString(R.string.DO_You_want_to_remove_address), getString(R.string.yes), getString(R.string.no), "Remove Addres");
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selAddressBookDO = addressBookDO;
                    if(addressBookDO.isActive){
                        selAddressBookDO.isExisting = false;
                    }else{
                        selAddressBookDO.isExisting = true;
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.chkAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((View)v.getTag(R.string.Address_TAG)).performClick();
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvAddress,tvStatus,tvArea,tvSubArea;
        ImageView ivCancel;
        CheckBox chkAddress;
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        hideLoader();
        if (response.method != null && response.method == ServiceMethods.WS_REMOVE_ADDRESS && response.data != null && response.data instanceof CommonResponseDO) {
            hideLoader();
            CommonResponseDO commonResponseDO = (CommonResponseDO) response.data;
            if(commonResponseDO.message.toUpperCase().contains("SUCCESS")){
                arrAddressBookDOs.remove(selAddressBookDO);
                addressListAdapter.refresh(arrAddressBookDOs);
                showCustomDialog(ManageAddressActivity.this, "", getString(R.string.address_removed_sucess), getString(R.string.OK), "", "");
            }else
                showCustomDialog(ManageAddressActivity.this, "", commonResponseDO.message, getString(R.string.OK), "", "");
        }else if (response.method != null && (response.method == ServiceMethods.WS_ADDRESS_BOOK_LIST &&response.data != null)) {
            arrAddressBookDOs.clear();
            if(response.data instanceof ArrayList){
                ArrayList<AddressBookDO> arrAddress = (ArrayList<AddressBookDO>) response.data;
                if(arrAddress != null) {
                    /*for (int i=0; i<arrAddress.size();i++){
                        arrAddressBookDOs.add(new AddressBookDO());
                    }*/
                    for(AddressBookDO addressBookDO : arrAddress) {
                        if(!addressBookDO.isActive) {
                            customerDO.address1 = addressBookDO.addressLine1;
                            customerDO.address1_Arabic = addressBookDO.addressLine1_Arabic == null?"":addressBookDO.addressLine1_Arabic;
                            customerDO.address2 = addressBookDO.addressLine2 == null ? "" : addressBookDO.addressLine2;
                            customerDO.address2_Arabic = addressBookDO.addressLine2_Arabic == null?"":addressBookDO.addressLine2_Arabic;;
                            customerDO.address3 = addressBookDO.addressLine3 == null ? "" : addressBookDO.addressLine3;
                            customerDO.address4 = addressBookDO.addressLine4 == null ? "" : addressBookDO.addressLine4;
                            customerDO.address4_Arabic = addressBookDO.addressLine4_Arabic == null ? "" : addressBookDO.addressLine4_Arabic;
                            customerDO.VillaName = addressBookDO.VillaName == null ? "" : addressBookDO.VillaName;
                            customerDO.Street = addressBookDO.Street == null ? "" : addressBookDO.Street;
                            customerDO.FlatNumber = addressBookDO.FlatNumber;
                            selAddressBookDO = addressBookDO;
                            selAddressBookDO.isExisting = true;
                            arrAddressBookDOs.add(0,addressBookDO);
                            updateCustomer();
                        }else
                            arrAddressBookDOs.add(addressBookDO);
                    }
                }
            }
            llNoAddress.setVisibility(View.GONE);
            lvAddress.setVisibility(View.VISIBLE);
            if(arrAddressBookDOs.size() <=0) {
                arrAddressBookDOs.add(customerDO.addressBookDO);
                addressListAdapter.refresh(arrAddressBookDOs);
            }else{
                addressListAdapter.refresh(arrAddressBookDOs);
            }
        }
    }

    public void updateCustomer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomerDA customerDA = new CustomerDA(ManageAddressActivity.this);
                ArrayList<CustomerDO> customerDOs= new ArrayList<>();
                customerDOs.add(customerDO);
                customerDA.insertCustomer(customerDOs);
                String deliveryDays = customerDA.getDeliveryDays();
                preference.saveStringInPreference(Preference.DELIVERY_DAYS,deliveryDays);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == AppConstants.destoryActivity){
            setResult(RESULT_OK,getIntent());
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
