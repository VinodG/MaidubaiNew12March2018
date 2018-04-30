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
import com.winit.maidubai.businesslayer.DataListener;
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

/**
 * Created by Girish Velivela on 8/23/2016.
 */
public class SNSignUpConfirmationActivity extends BaseActivity implements DataListener{

    private ScrollView svSNSignUpConfirmation;
    private LinearLayout llArea,llSubArea,llEmirates;
    private EditText etName,etEmail,etMobile,etCity,etSubArea, etLandmarks,etFlatNumber,etVillaName,etStreet;
    private Button btnContinue;
    private ImageView ivLoc,ivSubAreaArrow;
    private TextView tvAreaValue,tvLandMark,tvArea,tvSubArea, tvEmiratesValue, tvEmirates,tvStreet;
    private GPSLocationService gpsLocationService;
    private Location location;
    private AreaDO selArea;
    private Emirates emirates;
    private ArrayList<Emirates> arrEmirates;
    private double latitude,longitude;
    private String type;

    @Override
    public void initialise() {
        gpsLocationService = new GPSLocationService(SNSignUpConfirmationActivity.this);
        svSNSignUpConfirmation = (ScrollView) inflater.inflate(R.layout.sn_signup_confirmation_activity, null);
        llBody.addView(svSNSignUpConfirmation, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvTitle.setVisibility(View.VISIBLE);
        setTypeFaceNormal(svSNSignUpConfirmation);
        tvTitle.setText(getResources().getString(R.string.confirm_details));
        ivMenu.setVisibility(View.GONE);
        setStatusBarColor();
        lockDrawer("SNSignUpConfirmation");
        customerDO = (CustomerDO)getIntent().getSerializableExtra("Customer");
        type = getIntent().getStringExtra("Type");
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {

        llArea = (LinearLayout) findViewById(R.id.llArea);
        llSubArea = (LinearLayout)findViewById(R.id.llSubArea);
        etName= (EditText)findViewById(R.id.etName);
        tvLandMark= (TextView)findViewById(R.id.tvLandMark);
        tvStreet= (TextView)findViewById(R.id.tvStreet);
        etStreet= (EditText)findViewById(R.id.etStreet);
        etEmail= (EditText)findViewById(R.id.etEmail);
        etMobile= (EditText)findViewById(R.id.etMobile);
        ivLoc = (ImageView) findViewById(R.id.ivLoc);
        ivSubAreaArrow = (ImageView) findViewById(R.id.ivSubAreaArrow);
        etCity = (EditText) findViewById(R.id.etCity);
        tvAreaValue = (TextView) findViewById(R.id.tvAreaValue);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvSubArea = (TextView) findViewById(R.id.tvSubArea);
        tvAreaValue = (TextView) findViewById(R.id.tvAreaValue);
        etSubArea = (EditText) findViewById(R.id.etSubAreaValue);
        etLandmarks = (EditText) findViewById(R.id.etLandmarks);
        etFlatNumber = (EditText) findViewById(R.id.etFlatNumber);
        etVillaName = (EditText) findViewById(R.id.etVillaName);
        btnContinue= (Button)findViewById(R.id.btnContinue);
        llEmirates = (LinearLayout)svSNSignUpConfirmation.findViewById(R.id.llEmirates);
        tvEmirates = (TextView) svSNSignUpConfirmation.findViewById(R.id.tvEmirates);
        tvEmiratesValue = (TextView) svSNSignUpConfirmation.findViewById(R.id.tvEmiratesValue);

        etName.setText(customerDO.siteName);
        etEmail.setText(customerDO.cutomerEmailId);
        etMobile.setText(customerDO.mobileNumber);
        if(!StringUtils.isEmpty(customerDO.cutomerEmailId)){
            etEmail.setEnabled(false);
            etEmail.setFocusable(false);
            etEmail.setTextColor(getResources().getColor(R.color.black_light));

        }

        etLandmarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(StringUtils.isEmpty(s.toString()))
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

        llEmirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SNSignUpConfirmationActivity.this, SelectEmiratesActivity.class);
                Collections.sort(arrEmirates, new EmirateComparator());
                intent.putExtra("Emirates", arrEmirates);
                startActivityForResult(intent, 1001);
            }
        });
        llArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emirates== null){
                    showCustomDialog(SNSignUpConfirmationActivity.this,"",getString(R.string.plz_select_Emirates),getString(R.string.OK),"","");
                }else {
                    Intent intent = new Intent(SNSignUpConfirmationActivity.this, SelectAreaActivity.class);
                    Collections.sort(emirates.arrAreaDOs, new AreaComparator());
                    intent.putExtra("areas", emirates.arrAreaDOs);
                    intent.putExtra("type", "Area");
                    startActivityForResult(intent, 1000);
                }
            }
        });

        llSubArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvAreaValue.getText().toString();
                if (StringUtils.isEmpty(area)) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, "", getString(R.string.plz_select_area), getString(R.string.OK), "", "");
                } else {
                    Intent intent = new Intent(SNSignUpConfirmationActivity.this, SelectAreaActivity.class);
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
                if (location == null)
                    location = gpsLocationService.getLocation();
                if (location != null) {
                    showLoader("Loading...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LocationAddress locationAddress = new LocationAddress();
                            final AddressBookDO addressBookDO = locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoader();
                                    if (addressBookDO != null) {
                                        tvEmiratesValue.setText(addressBookDO.addressLine3);
                                        etCity.setText(addressBookDO.city);
                                        tvAreaValue.setText(addressBookDO.addressLine1);
                                        etLandmarks.setText(addressBookDO.addressLine2);
                                    } else {
                                        showCustomDialog(SNSignUpConfirmationActivity.this, "", getString(R.string.Unable_to_address), getString(R.string.OK), "", "");
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    showCustomDialog(SNSignUpConfirmationActivity.this, "", getString(R.string.Unable_to_address), getString(R.string.OK), "", "");
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(etName.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentername), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etEmail.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                } else if (!isEmailValid(etEmail.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etMobile.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentermobile), getResources().getString(R.string.ok), "", "");
                }else if(etMobile.getText().toString().trim().length()<8){
                    showCustomDialog(SNSignUpConfirmationActivity.this,getString(R.string.alert),getResources().getString(R.string.plzentervalidmobile),getResources().getString(R.string.ok),"","");
                }else if (StringUtils.isEmpty(tvEmiratesValue.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremirate), getResources().getString(R.string.ok), "", "");
                }
                /*else if (StringUtils.isEmpty(etCity.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercity), getResources().getString(R.string.ok), "", "");
                } */
                else if (StringUtils.isEmpty(tvAreaValue.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterarea), getResources().getString(R.string.ok), "", "");
                }else if (StringUtils.isEmpty(etFlatNumber.getText().toString().trim())) {
                    showCustomDialog(SNSignUpConfirmationActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterFlat), getResources().getString(R.string.ok), "", "");
                } else if(checkNetworkConnection()){
                    showLoader("Siging up.....\nPlease wait....");
                    customerDO.siteName = etName.getText().toString();
                    customerDO.cutomerEmailId = etEmail.getText().toString();
                    customerDO.mobileNumber = etMobile.getText().toString();
                    customerDO.emirates = tvEmiratesValue.getText().toString();
                    customerDO.city = etCity.getText().toString();
                    customerDO.FlatNumber = etFlatNumber.getText().toString();
                    customerDO.VillaName = etVillaName.getText().toString();
                    customerDO.area = tvAreaValue.getText().toString();
                    customerDO.subArea = etSubArea.getText().toString();
                    customerDO.landmark = etLandmarks.getText().toString();
                    customerDO.Street = etStreet.getText().toString();
                    customerDO.GeoCodeX = latitude + "";
                    customerDO.GeoCodeY = longitude + "";
                    String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                    String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    customerDO.DeviceId = StringUtils.isEmpty(token)?DeviceId:token;
                    String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                    if(language.equalsIgnoreCase("en"))
                        customerDO.PreferedLanguage = "English";
                    else
                        customerDO.PreferedLanguage = "Arabic";
                    new CommonBL(SNSignUpConfirmationActivity.this, SNSignUpConfirmationActivity.this).snRegisterCustomer(customerDO);
                }
            }
        });

    }
    
    @Override
    public void loadData() {
        showLoader(getString(R.string.fetching_areas));
        new CommonBL(SNSignUpConfirmationActivity.this,SNSignUpConfirmationActivity.this).getEmirates();
    }

    @Override
    public void dataRetrieved(final ResponseDO response) {
        if (response.method != null && (response.method == ServiceMethods.WS_SN_SIGNUP &&response.data != null)) {
            if(response.data instanceof String){
                hideLoader();
                showCustomDialog(SNSignUpConfirmationActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                hideLoader();
                CustomerDO resCustomerDO = (CustomerDO)response.data;
                if(!StringUtils.isEmpty(resCustomerDO.loginorRegister)) {
                    hideLoader();
                    if(type.equalsIgnoreCase("Register") && resCustomerDO.loginorRegister.equalsIgnoreCase("Login"))
                    {
                        showCustomDialog(SNSignUpConfirmationActivity.this,"",getString(R.string.Already_emailId),getString(R.string.OK),"","");
                    }else {
                        customerDO = resCustomerDO;
                        showLoader("Downloading Master data....");
                        downloadMasterData(customerDO.customerId, "0", "0");
                    }
                }
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_EMIRATES &&response.data != null)) {
            hideLoader();
            if(response.data instanceof String){
                showCustomDialog(SNSignUpConfirmationActivity.this,"",(String)response.data,getString(R.string.OK),"","Area");
            }else{
                Object[] data = (Object[]) response.data;
                if(data != null && data.length == 2) {
                    arrEmirates = (ArrayList) data[0];
                }
            }
        }
        super.dataRetrieved(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsLocationService.connectGoogleClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsLocationService.stopLocationUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1001) {
            emirates = (Emirates)data.getSerializableExtra("Emirate");
            tvEmiratesValue.setText(emirates.Name);
            tvEmiratesValue.setVisibility(View.VISIBLE);
            tvEmirates.setTextSize(13);

            tvAreaValue.setVisibility(View.GONE);
            tvArea.setTextSize(15);
            tvSubArea.setTextSize(15);
            tvAreaValue.setText("");
            etSubArea.setText("");
            etSubArea.setVisibility(View.GONE);
            llSubArea.setEnabled(true);
            ivSubAreaArrow.setVisibility(View.VISIBLE);

        }else if(resultCode == RESULT_OK && requestCode == 1000){
            String type = data.getStringExtra("type");
            if(type.equalsIgnoreCase("Area")) {
                selArea = (AreaDO)data.getSerializableExtra("Area");
                tvAreaValue.setVisibility(View.VISIBLE);
                tvArea.setTextSize(13);
                tvSubArea.setTextSize(13);
                tvAreaValue.setText(selArea.Name);
                ivLoc.setEnabled(true);
                etSubArea.setVisibility(View.VISIBLE);
                ArrayList<AreaDO> subAreas = selArea.arrAreaDOs;
                if(subAreas != null){
                    llSubArea.setEnabled(true);
                    ivSubAreaArrow.setVisibility(View.VISIBLE);
                }else{
                    etSubArea.setText(selArea.Name);
                    llSubArea.setEnabled(false);
                    ivSubAreaArrow.setVisibility(View.GONE);
                }
            }else {
                AreaDO areaDO = (AreaDO)data.getSerializableExtra("Area");
                etSubArea.setText(areaDO.Name);
            }
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Area"))
            finish();
    }

}
