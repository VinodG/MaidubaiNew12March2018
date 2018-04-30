package com.winit.maidubai;


import android.content.Intent;
import android.location.Location;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.LocationAddress;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AddressDetailsActivity extends BaseActivity {

    private ScrollView svAddressDetails;
    private LinearLayout llDeliveryDays,llArea,llSubArea,llEmirates;
    private Button btnNext;
    private TextView tvDeliveryDays,tvDeliveryDescription,tvSubArea,tvArea,tvAreaValue,tvLandMark,tvStreet,tvEmiratesValue,tvEmirates;
    private EditText etCity, etLandmarks, etSubAreaValue,etFlatNumber,etVillaName,etStreet;
    private ImageView ivLoc,ivSubAreaArrow;
    private GPSLocationService gpsLocationService;
    private Location location;
    private double latitude,longitude;
    private ArrayList<Emirates> arrEmirates;
    private HashMap<Integer,ArrayList<DeliveryDay>> hmDeliveryDays;
    private Emirates emirates;
    private AreaDO selArea;
    String selectedLanguage="en";

    @Override
    public void initialise() {
        gpsLocationService = new GPSLocationService(AddressDetailsActivity.this);
        svAddressDetails = (ScrollView) inflater.inflate(R.layout.address_details_layout, null);
        llBody.addView(svAddressDetails);
        ivLogo.setVisibility(View.VISIBLE);
        setTypeFaceNormal(svAddressDetails);
        setStatusBarColor();
        ivLogo.setImageResource(R.drawable.header_addressdetails);
        // llBack.setVisibility(View.VISIBLE);
        lockDrawer("AddressActivity");
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        btnNext = (Button) svAddressDetails.findViewById(R.id.btnNext);
        btnNext.setTypeface(AppConstants.DinproMedium);
        llDeliveryDays = (LinearLayout) svAddressDetails.findViewById(R.id.llDeliveryDays);
        llEmirates = (LinearLayout) svAddressDetails.findViewById(R.id.llEmirates);
        llArea = (LinearLayout) svAddressDetails.findViewById(R.id.llArea);
        llSubArea = (LinearLayout) svAddressDetails.findViewById(R.id.llSubArea);
        ivLoc = (ImageView) svAddressDetails.findViewById(R.id.ivLoc);
        ivSubAreaArrow = (ImageView) svAddressDetails.findViewById(R.id.ivSubAreaArrow);
        tvEmirates = (TextView) svAddressDetails.findViewById(R.id.tvEmirates);
        tvEmiratesValue = (TextView) svAddressDetails.findViewById(R.id.tvEmiratesValue);
        tvArea = (TextView) svAddressDetails.findViewById(R.id.tvArea);
        tvSubArea = (TextView) svAddressDetails.findViewById(R.id.tvSubArea);
        tvStreet  = (TextView) svAddressDetails.findViewById(R.id.tvStreet);
        etCity = (EditText) svAddressDetails.findViewById(R.id.etCity);
        etFlatNumber = (EditText) svAddressDetails.findViewById(R.id.etFlatNumber);
        tvDeliveryDescription = (TextView) svAddressDetails.findViewById(R.id.tvDeliveryDescription);
        tvDeliveryDays = (TextView) svAddressDetails.findViewById(R.id.tvDeliveryDays);
        tvAreaValue = (TextView) svAddressDetails.findViewById(R.id.tvAreaValue);
        etSubAreaValue = (EditText) svAddressDetails.findViewById(R.id.etSubAreaValue);
        etLandmarks = (EditText) svAddressDetails.findViewById(R.id.etLandmarks);
        etVillaName = (EditText) svAddressDetails.findViewById(R.id.etVillaName);
        etStreet    = (EditText) svAddressDetails.findViewById(R.id.etStreet);
        tvLandMark = (TextView) svAddressDetails.findViewById(R.id.tvLandMark);
        tvDeliveryDescription.setTypeface(AppConstants.DinproRegular);
        tvDeliveryDays.setTypeface(AppConstants.DinproBold);

        llEmirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressDetailsActivity.this, SelectEmiratesActivity.class);
                Collections.sort(arrEmirates, new EmirateComparator());
                intent.putExtra("Emirates", arrEmirates);
                startActivityForResult(intent, 1001);
            }
        });
        llArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emirates== null){
                    showCustomDialog(AddressDetailsActivity.this,"",getString(R.string.plz_select_Emirates),getString(R.string.OK),"","");
                }else{
                    Intent intent = new Intent(AddressDetailsActivity.this, SelectAreaActivity.class);
                    Collections.sort(emirates.arrAreaDOs, new AreaComparator());
                    intent.putExtra("areas", emirates.arrAreaDOs);
                    intent.putExtra("type", "Area");
                    startActivityForResult(intent, 1000);
                }
            }
        });
        etLandmarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(s.toString()))
                    tvLandMark.setVisibility(View.GONE);
                else
                    tvLandMark.setVisibility(View.VISIBLE);

            }
        });
        etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(StringUtils.isEmpty(s.toString()))
                    tvStreet.setVisibility(View.GONE);
                else
                    tvStreet.setVisibility(View.VISIBLE);

            }
        });
        llSubArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvAreaValue.getText().toString();
                if(StringUtils.isEmpty(area)){
                    showCustomDialog(AddressDetailsActivity.this,"",getString(R.string.plz_select_area),getString(R.string.OK),"","");
                }else{
                    Intent intent = new Intent(AddressDetailsActivity.this, SelectAreaActivity.class);
                    Collections.sort(selArea.arrAreaDOs, new AreaComparator());
                    intent.putExtra("areas", selArea.arrAreaDOs);
                    intent.putExtra("type", "SubArea");
                    startActivityForResult(intent, 1000);
                }
            }
        });

        ivLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvAreaValue.getText().toString();
                if(StringUtils.isEmpty(area)){
                    showCustomDialog(AddressDetailsActivity.this,"",getString(R.string.plz_select_area),getString(R.string.OK),"","");
                }else{
                    if(location == null)
                        location = gpsLocationService.getLocation();
                    if(location != null){
                        showLoader("Loading...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                LocationAddress locationAddress = new LocationAddress();
                                final AddressBookDO addressBookDO = locationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoader();
                                        if(addressBookDO != null) {
                                            tvEmiratesValue.setText(addressBookDO.addressLine3);
                                            etCity.setText(addressBookDO.city);
                                            etLandmarks.setText(addressBookDO.addressLine2);
                                        }else{
                                            showCustomDialog(AddressDetailsActivity.this,"",getString(R.string.Unable_to_address),getString(R.string.OK),"","");
                                        }
                                    }
                                });
                            }
                        }).start();
                    }else{
                        showCustomDialog(AddressDetailsActivity.this,"",getString(R.string.Unable_to_address),getString(R.string.OK),"","");
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(tvEmiratesValue.getText().toString().trim())) {
                    showCustomDialog(AddressDetailsActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremirate), getResources().getString(R.string.ok), "", "");
                }
                /*else if (StringUtils.isEmpty(etCity.getText().toString().trim())) {
                    showCustomDialog(AddressDetailsActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercity), getResources().getString(R.string.ok), "", "");
                }*/
                else if (StringUtils.isEmpty(tvAreaValue.getText().toString().trim())) {
                    showCustomDialog(AddressDetailsActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterarea), getResources().getString(R.string.ok), "", "");
                }else if (StringUtils.isEmpty(etVillaName.getText().toString().trim())) {
                    showCustomDialog(AddressDetailsActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterVilla), getResources().getString(R.string.ok), "", "");
                }else if (StringUtils.isEmpty(etFlatNumber.getText().toString().trim())) {
                    showCustomDialog(AddressDetailsActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterFlat), getResources().getString(R.string.ok), "", "");
                }else {
                    CustomerDO customerDO = (CustomerDO) getIntent().getSerializableExtra("Customer");
                    customerDO.emirates = tvEmiratesValue.getText().toString();
                    customerDO.city = etCity.getText().toString();
                    customerDO.FlatNumber = etFlatNumber.getText().toString();
                    customerDO.area = tvAreaValue.getText().toString();
                    customerDO.subArea = etSubAreaValue.getText().toString();
                    customerDO.landmark = etLandmarks.getText().toString();
                    customerDO.VillaName = etVillaName.getText().toString();
                    customerDO.Street    = etStreet.getText().toString();
                    customerDO.GeoCodeX = latitude+"";
                    customerDO.GeoCodeY = longitude+"";
                    Intent intent = new Intent(AddressDetailsActivity.this, PhoneNumberDetails.class);
                    intent.putExtra("Customer",customerDO);
                    startActivityForResult(intent, AppConstants.destoryActivity);
                }
            }
        });
    }

    @Override
    public void loadData() {
        showLoader(getString(R.string.fetching_areas));
        new CommonBL(AddressDetailsActivity.this,AddressDetailsActivity.this).getEmirates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsLocationService.connectGoogleClient();
        location = gpsLocationService.getLocation();
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsLocationService.stopLocationUpdate();
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_EMIRATES &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(AddressDetailsActivity.this,"",(String)response.data,getString(R.string.OK),"","Area");
            }else{
                Object[] data = (Object[]) response.data;
                if(data != null && data.length == 2) {
                    arrEmirates = (ArrayList) data[0];
                    hmDeliveryDays = (HashMap<Integer, ArrayList<DeliveryDay>>) data[1];
                }
            }
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Area"))
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppConstants.destoryActivity && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        if(resultCode == RESULT_OK && requestCode == 1001) {
            emirates = (Emirates)data.getSerializableExtra("Emirate");
            tvEmiratesValue.setText(emirates.Name);
            tvEmiratesValue.setVisibility(View.VISIBLE);
            tvEmirates.setTextSize(13);


            tvAreaValue.setVisibility(View.GONE);
            tvArea.setTextSize(15);
            tvSubArea.setTextSize(15);
            tvAreaValue.setText("");
            etSubAreaValue.setText("");
            etSubAreaValue.setVisibility(View.GONE);
            llSubArea.setEnabled(true);
            ivSubAreaArrow.setVisibility(View.VISIBLE);


        }else if(resultCode == RESULT_OK && requestCode == 1000){
            String type = data.getStringExtra("type");
            if(type.equalsIgnoreCase("Area")) {
                selArea = (AreaDO)data.getSerializableExtra("Area");
                llDeliveryDays.setVisibility(View.VISIBLE);
                ArrayList<DeliveryDay> arrDeliveryDays = hmDeliveryDays != null ? hmDeliveryDays.get(selArea.AreaId) : null;
                String area = "";
                if(arrDeliveryDays != null) {
                    for (DeliveryDay deliveryDay : arrDeliveryDays) {
                        area += deliveryDay.DeliveryDay + ", ";
                    }
                    selectedLanguage = preference.getStringFromPreference(Preference.LANGUAGE,"en");
                    if(selectedLanguage.equalsIgnoreCase("ar"))
                    tvDeliveryDays.setText(getArabicDay(area.substring(0, area.length() - 2)));
                    else
                    tvDeliveryDays.setText(area.substring(0, area.length() - 2));
                }else{
                    tvDeliveryDays.setText("No Delivery Days");
                }
                tvAreaValue.setVisibility(View.VISIBLE);
                tvArea.setTextSize(13);
                tvSubArea.setTextSize(13);
                tvAreaValue.setText(selArea.Name);
                ivLoc.setEnabled(true);
                etSubAreaValue.setVisibility(View.VISIBLE);
                ArrayList<AreaDO> subAreas = selArea.arrAreaDOs;
                if(subAreas != null){
                    llSubArea.setEnabled(true);
                    ivSubAreaArrow.setVisibility(View.VISIBLE);
                }else{
                    etSubAreaValue.setText(selArea.Name);
                    llSubArea.setEnabled(false);
                    ivSubAreaArrow.setVisibility(View.GONE);
                }
            }else {
                AreaDO areaDO = (AreaDO)data.getSerializableExtra("Area");
                etSubAreaValue.setText(areaDO.Name);
            }
        }
    }
}
