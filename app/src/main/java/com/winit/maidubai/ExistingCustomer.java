package com.winit.maidubai;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;


public class ExistingCustomer extends BaseActivity implements DataListener{
    private LinearLayout llExistCustomer;
    private Button btnNext;
    private TextView tvtitile;
    private  EditText etMobile,etEmail;

    @Override
    public void initialise() {
        llExistCustomer = (LinearLayout) inflater.inflate(R.layout.existing_customer, null);
        llBody.addView(llExistCustomer,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvTitle.setVisibility(View.VISIBLE);
        setTypeFaceNormal(llExistCustomer);
        tvTitle.setText(  getResources().getString(R.string.existingcustomer));
        ivMenu.setVisibility(View.GONE);
        // llBack.setVisibility(View.VISIBLE);
        setStatusBarColor();
        lockDrawer("ExistingCustomer");
        initialiseControls();
    }

    @Override
    public void initialiseControls() {
        etMobile= (EditText) llExistCustomer.findViewById(R.id.etMobile);
        etEmail= (EditText) llExistCustomer.findViewById(R.id.etEmail);
        btnNext= (Button) llExistCustomer.findViewById(R.id.btnNext);
        tvtitile= (TextView) llExistCustomer.findViewById(R.id.tvtitile);
        tvtitile.setTypeface(AppConstants.DinproLight);
        btnNext.setTypeface(AppConstants.DinproMedium);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setEnabled(false);
                btnNext.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnNext.setEnabled(true);
                        btnNext.setClickable(true);
                    }
                }, 200);
                if(StringUtils.isEmpty(etMobile.getText().toString().trim()) && StringUtils.isEmpty(etEmail.getText().toString().trim()))
                {
                    showCustomDialog(ExistingCustomer.this,getString(R.string.alert),getResources().getString(R.string.plzreenterregmobileoremail),getResources().getString(R.string.ok),"","");
                }
                else {

                    if (checkNetworkConnection()) {
                        customerDO = new CustomerDO();
                        customerDO.mobileNumber = etMobile.getText().toString().trim();
                        customerDO.cutomerEmailId = etEmail.getText().toString().trim();
                        showLoader("Loading....");
                        String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                        String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                        if(language.equalsIgnoreCase("en"))
                            language = "English";
                        else
                            language = "Arabic";
                        new CommonBL(ExistingCustomer.this, ExistingCustomer.this).login(customerDO.cutomerEmailId, customerDO.mobileNumber, "", AppConstants.EXISTING,token,language);
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    public void showInvalidNumberPopup(){
        LinearLayout drawerPopUp = (LinearLayout) inflater.inflate(R.layout.invalid_mobile, null);
        TextView tvInCorrt = (TextView)drawerPopUp.findViewById(R.id.tvIncorrt);
        TextView tvPleaseEnter = (TextView)drawerPopUp.findViewById(R.id.tvPleaseEnter);
        TextView tvMoreInfo = (TextView)drawerPopUp.findViewById(R.id.tvMoreInfo);
        TextView tvMobile = (TextView)drawerPopUp.findViewById(R.id.tvMobile);
        tvInCorrt.setTypeface(AppConstants.DinproMedium);
        tvPleaseEnter.setTypeface(AppConstants.DinproLight);
        tvMoreInfo.setTypeface(AppConstants.DinproLight);
        tvMobile.setTypeface(AppConstants.DinproRegular);
        CustomDialog customDialog = new CustomDialog(ExistingCustomer.this, drawerPopUp,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        customDialog.setCancelable(true);
        customDialog.show();
    }

    @Override
    public void OTPSuccessfull() {
        showLoader("Downloading Master data....");
        downloadMasterData(customerDO.customerId,"0","0");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void dataRetrieved(final ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_LOGIN &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(ExistingCustomer.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                customerDO = (CustomerDO)response.data;
                showOTPPopup(customerDO,"Login");
            }
        }
        super.dataRetrieved(response);
    }
}
