package com.winit.maidubai;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.io.InputStream;

public class PhoneNumberDetails extends BaseActivity implements DataListener{

    private LinearLayout llPhoneDetails;
    private Button btnProceed;
    private EditText etLandline,etMobile;
    private TextInputLayout nameLayout;

    @Override
    public void initialise() {
        llPhoneDetails= (LinearLayout) inflater.inflate(R.layout.phone_number_details,null);
        llBody.addView(llPhoneDetails,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lockDrawer("PhoneNumberActivity");
        setTypeFaceNormal(llPhoneDetails);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.headernumberdetails);

        customerDO = (CustomerDO) getIntent().getSerializableExtra("Customer");
        initialiseControls();
    }

    @Override
    public void initialiseControls() {
        btnProceed= (Button)llPhoneDetails.findViewById(R.id.btnProceed);
        btnProceed.setTypeface(AppConstants.DinproMedium);
        etLandline= (EditText) llPhoneDetails.findViewById(R.id.etLandline);
        etMobile= (EditText) llPhoneDetails.findViewById(R.id.etMobile);
        nameLayout= (TextInputLayout) llPhoneDetails.findViewById(R.id.nameLayout);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(StringUtils.isEmpty(etMobile.getText().toString().trim()))
                {
                    showCustomDialog(PhoneNumberDetails.this,getString(R.string.alert),getResources().getString(R.string.plzentermobile),getResources().getString(R.string.ok),"","");
                }else if(etMobile.getText().toString().trim().length()<8)
                {
                    showCustomDialog(PhoneNumberDetails.this,getString(R.string.alert),getResources().getString(R.string.plzentervalidmobile),getResources().getString(R.string.ok),"","");
                }
                else
                {
                    if(checkNetworkConnection()) {
                        showLoader("Siging up.....\nPlease wait....");
                        customerDO.mobileNumber = etMobile.getText().toString();
                        customerDO.landlineNumber = etLandline.getText().toString();
                        String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                        String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        customerDO.DeviceId = StringUtils.isEmpty(token)?DeviceId:token;
                        String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                        if(language.equalsIgnoreCase("en"))
                            customerDO.PreferedLanguage = "English";
                        else
                            customerDO.PreferedLanguage = "Arabic";
                        new CommonBL(PhoneNumberDetails.this, PhoneNumberDetails.this).registerCustomer(customerDO);
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        nameLayout.setFocusableInTouchMode(true);
        nameLayout.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(nameLayout, 0);
    }

    @Override
    public void OTPSuccessfull() {
        showLoader("Downloading Master data....");
        downloadMasterData(customerDO.customerId,"0","0");
    }

    @Override
    public void dataRetrieved(final ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_SIGNUP &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(PhoneNumberDetails.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                CustomerDO resCustomer = (CustomerDO)response.data;
                customerDO.Id = resCustomer.Id;
                customerDO.customerId = resCustomer.customerId;
                customerDO.sessionId = resCustomer.sessionId;
                showOTPPopup(customerDO,"Signup");
            }
        }
        super.dataRetrieved(response);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppConstants.destoryActivity && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
