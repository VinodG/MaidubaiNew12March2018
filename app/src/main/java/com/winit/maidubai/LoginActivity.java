package com.winit.maidubai;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.Settings;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;
import com.winit.maidubai.webaccessLayer.SocialNetworkCilent;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements DataListener {

    private LinearLayout llLoginActivity, llforgotPswd;
    private ImageView ivLoginWithFb, ivLoginWithTwitter, ivLoginWithGPlus, ivPreviewPass;
    private EditText etEmail, etPassword;
    private TextView tvForgetPassword,tvChangeLangueage;
    private Button btnLogin, btnExistingCustomer, btnSignUp;
    private boolean passShow;
    private final int SOCIAL_NETWORK_LOGIN = 1000;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;


    @Override
    public void initialise() {
        callbackManager = CallbackManager.Factory.create();
        llLoginActivity = (LinearLayout) inflater.inflate(R.layout.activity_login, null);
        llLoginActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llLoginActivity);
        setTypeFaceNormal(llLoginActivity);
        toolbar.setVisibility(View.GONE);
        lockDrawer("LoginActivity");
        loginButton  = new LoginButton(LoginActivity.this);
        initialiseControls();

        List<String> list = new ArrayList<String>();
        list.add("user_posts");
        list.add("public_profile");
        list.add("user_location");
        list.add("email");
        loginButton.setReadPermissions(list);



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                showLoader("Loading...");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        customerDO = new SocialNetworkCilent().getUserDetailsFromFaceBook(accessToken.getToken());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(customerDO != null) {
                                    String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                                    if(language.equalsIgnoreCase("en"))
                                        language = "English";
                                    else
                                        language = "Arabic";
                                    String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                                    String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                                    customerDO.DeviceId = StringUtils.isEmpty(token)?DeviceId:token;
                                    customerDO.PreferedLanguage = language;
                                    new CommonBL(LoginActivity.this, LoginActivity.this).snRegisterCustomer(customerDO);
                                }
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCancel() {
                LogUtils.errorLog("LoginActivity", "Canceled");
                //showCustomDialog(LoginActivity.this,getString(R.string.alert),getString(R.string.LOGIN_FAILED),getString(R.string.OK),"","");
            }

            @Override
            public void onError(FacebookException error) {
                LogUtils.errorLog("LoginActivity", error.toString()+"error");
                //showCustomDialog(LoginActivity.this,getString(R.string.alert),getString(R.string.LOGIN_FAILED),getString(R.string.OK),"","");
            }
        });
    }

    @Override
    public void initialiseControls() {
        etEmail = (EditText) llLoginActivity.findViewById(R.id.etEmail);
        etPassword = (EditText) llLoginActivity.findViewById(R.id.etPassword);
        ivPreviewPass = (ImageView) llLoginActivity.findViewById(R.id.ivPreviewPass);
        btnLogin = (Button) llLoginActivity.findViewById(R.id.btnLogin);
        tvForgetPassword = (TextView) llLoginActivity.findViewById(R.id.tvForgetPassword);
        tvChangeLangueage = (TextView) llLoginActivity.findViewById(R.id.tvChangeLangueage);
        btnExistingCustomer = (Button) llLoginActivity.findViewById(R.id.btnExistingCustomer);
        ivLoginWithFb = (ImageView) llLoginActivity.findViewById(R.id.ivLoginWithFb);
        ivLoginWithTwitter = (ImageView) llLoginActivity.findViewById(R.id.ivLoginWithTwitter);
        ivLoginWithGPlus = (ImageView) llLoginActivity.findViewById(R.id.ivLoginWithGPlus);
        btnExistingCustomer = (Button) llLoginActivity.findViewById(R.id.btnExistingCustomer);
        btnSignUp = (Button) llLoginActivity.findViewById(R.id.btnSignUp);
        btnLogin.setTypeface(AppConstants.DinproMedium);
        btnExistingCustomer.setTypeface(AppConstants.DinproMedium);
        btnSignUp.setTypeface(AppConstants.DinproMedium);
        ivPreviewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passShow) {
                    //show password
                    passShow = true;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());
                    ivPreviewPass.setImageResource(R.drawable.preview_h);
                } else {
                    //hide password
                    passShow = false;
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());
                    ivPreviewPass.setImageResource(R.drawable.preview);
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (StringUtils.isEmpty(emailId)) {
                    showCustomDialog(LoginActivity.this,getResources().getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(password)) {
                    showCustomDialog(LoginActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterpassword), getResources().getString(R.string.ok), "", "");
                } else if (!isEmailValid(emailId)) {
                    showCustomDialog(LoginActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                } else {
                    if(checkNetworkConnection()) {
                        showLoader("Login....");
                        hideKeyboard(etPassword);
                        String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                        String DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                        if(language.equalsIgnoreCase("en"))
                            language = "English";
                        else
                            language = "Arabic";
                        new CommonBL(LoginActivity.this, LoginActivity.this).login(emailId, "", password, AppConstants.LOGIN,StringUtils.isEmpty(token)?DeviceId:token,language);
                    }
                }
            }
        });

        ivLoginWithGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection()) {
                    Intent intent = new Intent(LoginActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.GOOGELPLUS_LOGIN_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_GOOGLEPLUS);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);
                }
            }
        });

        ivLoginWithFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection()) {
                    Intent intent = new Intent(LoginActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.FACEBOOK_LOGIN_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_FACEBOOK);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);
//                    loginButton.performClick();
                }
            }
        });

        ivLoginWithTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNetworkConnection()) {
                    Intent intent = new Intent(LoginActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.TWITTER_Request_Token_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_TWITTER);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);

                }
            }
        });

        btnExistingCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ExistingCustomer.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });
        tvChangeLangueage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LanguageSelectionActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void loadData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SOCIAL_NETWORK_LOGIN && resultCode == RESULT_OK){
            if(data.hasExtra("CUSTOMER"))
            {
                customerDO = (CustomerDO)data.getSerializableExtra("CUSTOMER");
                if(customerDO != null) {
                    String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
                    //customerDO.DeviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    customerDO.DeviceId = token;
                    new CommonBL(LoginActivity.this, LoginActivity.this).snRegisterCustomer(customerDO);
                }
            }
        }else  if(requestCode == SOCIAL_NETWORK_LOGIN && resultCode != RESULT_OK)
        {
            showCustomDialog(LoginActivity.this,getString(R.string.alert),getString(R.string.LOGIN_FAILED),getString(R.string.OK),"","");
            super.onActivityResult(requestCode, resultCode, data);
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void showPopUp(){
        llforgotPswd=(LinearLayout)inflater.inflate(R.layout.forgot_password_popup,null);
        final EditText etEmailId  = (EditText)llforgotPswd.findViewById(R.id.etEmailId);
        TextView tvRestPswd = (TextView)llforgotPswd.findViewById(R.id.tvRestPswd);
        final CustomDialog customDialog = new CustomDialog(LoginActivity.this, llforgotPswd, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        customDialog.setCancelable(true);
        customDialog.show();

        tvRestPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(etEmailId.getText().toString())) {
                    showCustomDialog(LoginActivity.this,getResources().getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                } else if(checkNetworkConnection()) {
                    showLoader("Please wait...");
                    String emailId = etEmailId.getText().toString();
                    new CommonBL(LoginActivity.this, LoginActivity.this).forgorPassword(emailId);
                    customDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void dataRetrieved(final ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_LOGIN &&response.data != null)) {
            if(response.data instanceof String){
                showCustomDialog(LoginActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                customerDO = (CustomerDO)response.data;
                downloadMasterData(customerDO.customerId,"0","0");
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_FORGET_PASSWORD &&response.data != null)) {
            if(response.data instanceof String){
                if(((String) response.data).equalsIgnoreCase("Password sent successfully."))
                    showCustomDialog(LoginActivity.this,"",getResources().getString(R.string.passwordsentsuccessfully),getString(R.string.OK),"","");
                else
                    showCustomDialog(LoginActivity.this,"",(String)response.data,getString(R.string.OK),"","");

            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_SN_SIGNUP &&response.data != null)) {
            if(response.data instanceof String && ((String)response.data).equalsIgnoreCase("Mobile Number Should Not Be Empty.")){
                if (customerDO != null) {
                    Intent intent = new Intent(LoginActivity.this, SNSignUpConfirmationActivity.class);
                    intent.putExtra("Customer",customerDO);
                    intent.putExtra("Type","Login");
                    startActivity(intent);
                    //finish();
                }
            }else if(response.data instanceof String){
                showCustomDialog(LoginActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }else{
                customerDO = (CustomerDO)response.data;
                downloadMasterData(customerDO.customerId,"0","0");
            }
        }
        super.dataRetrieved(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }
}
