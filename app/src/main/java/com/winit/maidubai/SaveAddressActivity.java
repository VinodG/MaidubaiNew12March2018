package com.winit.maidubai;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.LocationAddress;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;
import java.util.Collections;

public class SaveAddressActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout saveAddress, llArea, llSubArea, llEmirates;
    private Button btnSaveAddress;
    private ImageView ivLoc, ivSubAreaArrow;
    private GoogleMap mMap;
    private TextView tvAreaValue, tvLandMark, tvArea, tvSubArea, tvEmiratesValue, tvEmirates,tvStreet,tvMessage;
    private EditText etSubAreaValue, etCity, etLandmarks, etFlatNumber, etVillaName,etStreet;
    //    private AddressBookDO addressBookDO;
    private CustomerDO customerDO;
    private Emirates emirates;
    private AreaDO selArea;
    private int addressId,activityType;
    private ArrayList<Emirates> arrEmirates;

    private GoogleApiClient apiClient;
    private LocationRequest mLocationRequest;
    private Location _currentLocation;
    private double latitude, longitude;
    private AddressBookDO addressBookDO;

    @Override
    public void initialise() {
        saveAddress = (LinearLayout) inflater.inflate(R.layout.save_address, null);
        llBody.addView(saveAddress, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceNormal(saveAddress);
        tvTitle.setVisibility(View.VISIBLE);
        setStatusBarColor();
        //tvTitle.setText(getResources().getString(R.string.saveaddress));
        lockDrawer("SaveAddressActivity");
        customerDO = (CustomerDO) getIntent().getSerializableExtra("CustomerDo");
        activityType = getIntent().getIntExtra("ActivityType", 0);


        apiClient = new GoogleApiClient.Builder(SaveAddressActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        initialiseControls();
        addressId = getIntent().getIntExtra("AddressId", 0);
        if(activityType == AppConstants.SELECT_ADDRESS) {
            tvTitle.setText(getResources().getString(R.string.newaddress));
            tvMessage.setVisibility(View.GONE);
            btnSaveAddress.setText(getResources().getString(R.string.continue_a));
        }else
        {
            tvTitle.setText(getResources().getString(R.string.saveaddress));
            tvMessage.setVisibility(View.VISIBLE);
            btnSaveAddress.setText(getResources().getString(R.string.save_address));
        }
        loadData();
        setUpMapIfNeeded();
    }

    protected void onStart() {
        apiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (apiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
            apiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!apiClient.isConnected())
            apiClient.connect();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getView().setVisibility(View.GONE);
    }


    @Override
    public void initialiseControls() {
        llArea = (LinearLayout) findViewById(R.id.llArea);
        llSubArea = (LinearLayout) findViewById(R.id.llSubArea);
        etCity = (EditText) findViewById(R.id.etCity);
        tvAreaValue = (TextView) findViewById(R.id.tvAreaValue);
        tvStreet = (TextView) findViewById(R.id.tvStreet);
        etStreet=(EditText) findViewById(R.id.etStreet);
        etSubAreaValue = (EditText) findViewById(R.id.etSubAreaValue);
        etLandmarks = (EditText) findViewById(R.id.etLandmarks);
        etFlatNumber = (EditText) findViewById(R.id.etFlatNumber);
        etVillaName = (EditText) findViewById(R.id.etVillaName);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvSubArea = (TextView) findViewById(R.id.tvSubArea);
        btnSaveAddress = (Button) saveAddress.findViewById(R.id.btnSaveAddress);
        ivLoc = (ImageView) saveAddress.findViewById(R.id.ivLoc);
        ivSubAreaArrow = (ImageView) saveAddress.findViewById(R.id.ivSubAreaArrow);
        tvLandMark = (TextView) saveAddress.findViewById(R.id.tvLandMark);
        llEmirates = (LinearLayout) findViewById(R.id.llEmirates);
        tvEmirates = (TextView) saveAddress.findViewById(R.id.tvEmirates);
        tvMessage = (TextView) saveAddress.findViewById(R.id.tvMessage);
        tvEmiratesValue = (TextView) saveAddress.findViewById(R.id.tvEmiratesValue);
        btnSaveAddress.setTypeface(AppConstants.DinproMedium);

        llEmirates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveAddressActivity.this, SelectEmiratesActivity.class);
                Collections.sort(arrEmirates, new EmirateComparator());
                intent.putExtra("Emirates", arrEmirates);
                startActivityForResult(intent, 1001);
            }
        });
        llArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emirates== null){
                    showCustomDialog(SaveAddressActivity.this,"",getString(R.string.plz_select_Emirates),getString(R.string.OK),"","");
                }else {
                    Intent intent = new Intent(SaveAddressActivity.this, SelectAreaActivity.class);
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
        llSubArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvAreaValue.getText().toString();
                if(emirates== null){
                    showCustomDialog(SaveAddressActivity.this,"",getString(R.string.plz_select_Emirates),getString(R.string.OK),"","");
                }else if (StringUtils.isEmpty(area)) {
                    showCustomDialog(SaveAddressActivity.this, "", getString(R.string.plz_select_area), getString(R.string.OK), "", "");
                } else if(emirates.hmAreaDOs != null){
                    etSubAreaValue.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(SaveAddressActivity.this, SelectAreaActivity.class);
                    Collections.sort(emirates.hmAreaDOs.get(selArea.AreaId), new AreaComparator());
                    intent.putExtra("areas", emirates.hmAreaDOs.get(selArea.AreaId));
                    intent.putExtra("type", "SubArea");
                    startActivityForResult(intent, 1000);
                }
            }
        });

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(tvEmiratesValue.getText().toString().trim())) {
                    showCustomDialog(SaveAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremirate), getResources().getString(R.string.ok), "", "");
                }
               /* else if (StringUtils.isEmpty(etCity.getText().toString().trim())) {
                    showCustomDialog(SaveAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercity), getResources().getString(R.string.ok), "", "");
                }*/
                else if (StringUtils.isEmpty(tvAreaValue.getText().toString().trim())) {
                    showCustomDialog(SaveAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterarea), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etFlatNumber.getText().toString().trim())) {
                    showCustomDialog(SaveAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterFlat), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etVillaName.getText().toString().trim())) {
                    showCustomDialog(SaveAddressActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterVilla), getResources().getString(R.string.ok), "", "");
                } else {
                    addressBookDO = new AddressBookDO();
                    addressBookDO.addressBookId = addressId;
                    addressBookDO.userId = customerDO.Id;
                    addressBookDO.firstName = customerDO.siteName;
                    addressBookDO.AuthToken = customerDO.AuthToken;
                    addressBookDO.FlatNumber = etFlatNumber.getText().toString();
                    //addressBookDO.addressLine1 = tvAreaValue.getText().toString(); //Old
                    addressBookDO.addressLine1 = tvAreaValue.getTag().toString();
                    addressBookDO.addressLine1_Arabic = tvAreaValue.getText().toString();
                    //addressBookDO.addressLine2 = etSubAreaValue.getText().toString(); //Old
                    addressBookDO.addressLine2 = etSubAreaValue.getTag().toString();
                    addressBookDO.addressLine2_Arabic = etSubAreaValue.getText().toString();
                    addressBookDO.city = etCity.getText().toString();
                    addressBookDO.addressLine3 = etLandmarks.getText().toString();
                    //addressBookDO.addressLine4 = tvEmiratesValue.getText().toString(); //Old
                    addressBookDO.addressLine4 = tvEmiratesValue.getTag().toString();
                    addressBookDO.addressLine4_Arabic = tvEmiratesValue.getText().toString();
                    addressBookDO.Street = etStreet.getText().toString();
                    addressBookDO.VillaName = etVillaName.getText().toString();
                    addressBookDO.phoneNo = customerDO.mobileNumber;
                    addressBookDO.email = customerDO.cutomerEmailId;
                    addressBookDO.mobileNo = customerDO.mobileNumber;
                    /*if(activityType == AppConstants.SELECT_ADDRESS)
                    {
                        addressBookDO.phoneNo = customerDO.mobileNumber;
                        addressBookDO.email = customerDO.cutomerEmailId;
                        addressBookDO.mobileNo = customerDO.mobileNumber;
                        addressBookDO.isExisting = false;

                        Intent intent = new Intent(SaveAddressActivity.this, SelectAddressActivity.class);
                        intent.putExtra("SelAddress", addressBookDO);
                        startActivityForResult(intent, AppConstants.destoryActivity);
                    }
                    else
                    {*/
                        if (checkNetworkConnection()) {
                            showLoader(getString(R.string.save_address));
                            new CommonBL(SaveAddressActivity.this, SaveAddressActivity.this).manageAddressBookList(addressBookDO);
                        }
                   /* }*/
                }
            }
        });

        ivLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = tvAreaValue.getText().toString();
                if (StringUtils.isEmpty(area)) {
                    showCustomDialog(SaveAddressActivity.this, "", getString(R.string.plz_select_area), getString(R.string.OK), "", "");
                } else {

                        if (ActivityCompat.checkSelfPermission(SaveAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SaveAddressActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    if (_currentLocation == null)
                        _currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                    if (_currentLocation != null) {
                        showLoader("Loading...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                latitude = _currentLocation.getLatitude();
                                longitude = _currentLocation.getLongitude();
                                LocationAddress locationAddress = new LocationAddress();
                                final AddressBookDO addressBookDO = locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoader();
                                        if (addressBookDO != null) {
                                            tvEmiratesValue.setText(addressBookDO.addressLine3);
                                            etCity.setText(addressBookDO.city);
                                            etLandmarks.setText(addressBookDO.addressLine2);
                                        } else {
                                            showCustomDialog(SaveAddressActivity.this, "", getString(R.string.Unable_to_address), getString(R.string.OK), "", "");
                                        }
                                    }
                                });
                            }
                        }).start();
                    } else {
                        showCustomDialog(SaveAddressActivity.this, "", getString(R.string.Unable_to_address), getString(R.string.OK), "", "");
                    }
                }
            }
        });

    }


    private void setUpMarker(double latitude, double longtitude,String Address) {
        if(mMap != null) {
            LatLng latLng = new LatLng(latitude, longtitude);
            MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void loadData() {
        showLoader(getString(R.string.fetching_areas));
        new Thread(new Runnable() {
            @Override
            public void run() {
                    arrEmirates = new CustomerDA(SaveAddressActivity.this).getEmirates();
                    hideLoader();
            }
        }).start();
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_MANAGE_ADDRESS_BOOK &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(SaveAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                if(activityType == AppConstants.SELECT_ADDRESS)
                {
                    addressBookDO.phoneNo = customerDO.mobileNumber;
                    addressBookDO.email = customerDO.cutomerEmailId;
                    addressBookDO.mobileNo = customerDO.mobileNumber;
                    addressBookDO.isExisting = false;

                    Intent intent = new Intent(SaveAddressActivity.this, SelectAddressActivity.class);
                    intent.putExtra("SelAddress", addressBookDO);
                    startActivityForResult(intent, AppConstants.destoryActivity);
                }
                else
                    showCustomDialog(SaveAddressActivity.this, "", getString(R.string.sucessfully_address_saved), getString(R.string.OK), "", "SAVE ADDRESS");
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_EMIRATES &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(SaveAddressActivity.this,"",(String)response.data,getString(R.string.OK),"","Area");
            }else{
                arrEmirates = (ArrayList)response.data;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1001) {
            emirates = (Emirates)data.getSerializableExtra("Emirate");
            if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                tvEmiratesValue.setText(StringUtils.isEmpty(emirates.ArabicName)?emirates.Name:emirates.ArabicName);
            else
                tvEmiratesValue.setText(emirates.Name);


            tvEmiratesValue.setVisibility(View.VISIBLE);
            tvEmirates.setTextSize(13);

            tvEmiratesValue.setTag(emirates.Name);
            tvAreaValue.setTag("");
            etSubAreaValue.setTag("");

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
                tvAreaValue.setVisibility(View.VISIBLE);
                tvArea.setTextSize(13);
                tvSubArea.setTextSize(13);
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                    tvAreaValue.setText(StringUtils.isEmpty(selArea.ArabicName)?selArea.Name:selArea.ArabicName);
                else
                    tvAreaValue.setText(selArea.Name);


                tvAreaValue.setTag(selArea.Name);
                etSubAreaValue.setTag("");

                ivLoc.setEnabled(true);
                etSubAreaValue.setVisibility(View.VISIBLE);
                ArrayList<AreaDO> subAreas = emirates.hmAreaDOs.get(selArea.AreaId);
                if(subAreas != null){
                    llSubArea.setEnabled(true);
                    ivSubAreaArrow.setVisibility(View.VISIBLE);
                }else{
                    if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                        etSubAreaValue.setText(StringUtils.isEmpty(selArea.ArabicName)?selArea.Name:selArea.ArabicName);
                    else
                        etSubAreaValue.setText(selArea.Name);

                    etSubAreaValue.setTag(selArea.Name);
                    llSubArea.setEnabled(false);
                    ivSubAreaArrow.setVisibility(View.GONE);
                }
            }else {
                AreaDO areaDO = (AreaDO)data.getSerializableExtra("Area");
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                    etSubAreaValue.setText(StringUtils.isEmpty(areaDO.ArabicName)?areaDO.Name:areaDO.ArabicName);
                else
                    etSubAreaValue.setText(areaDO.Name);
                etSubAreaValue.setTag(areaDO.Name);
                etSubAreaValue.setTag(areaDO.Name);
            }
        }
        else if(resultCode == RESULT_OK && requestCode == AppConstants.destoryActivity){
            setResult(RESULT_OK,getIntent());
            finish();
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("SAVE ADDRESS")){
            Intent intent = new Intent(SaveAddressActivity.this,ManageAddressActivity.class);
            intent.putExtra("CustomerDo",customerDO);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if(from.equalsIgnoreCase("Area"))
            finish();
        else if(from.equalsIgnoreCase( AppConstants.INTERNET_ERROR)){
            finish();
        }
    }

    private void RequestLocationUpdates(){

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SaveAddressActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SaveAddressActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if(apiClient != null && apiClient.isConnected()) {
            if (_currentLocation == null)
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        _currentLocation = location;
        setUpMarker(location.getLatitude(),location.getLongitude(),"");
    }

    @Override
    public void onConnected(Bundle bundle) {
        RequestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity)SaveAddressActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
             /*e.printStackTrace(); */     Log.d("This can never happen", e.getMessage());

            }
        } else {
        }

    }

}
