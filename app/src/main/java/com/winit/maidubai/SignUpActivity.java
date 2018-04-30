package com.winit.maidubai;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.SocialNetworkCilent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class
        SignUpActivity extends BaseActivity {

    public ScrollView signUp;
    public LinearLayout llBirthday;
    private Button btnNext;
    private EditText etName, etEmail, etPassword, etConfirmPass, etBday, etAnniversary;
    private ImageView ivLoginWithFb, ivLoginWithTwitter, ivLoginWithGooglePlus,
            ivPreviewPass, ivPreviewPassConfirm,ivCalAnivers;
    private boolean passShow = false;
    private boolean passShow2 = false;
    private int SOCIAL_NETWORK_LOGIN = 100;
    private CustomerDO customerDO;


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    @Override
    public void initialise() {
        callbackManager = CallbackManager.Factory.create();
        signUp = (ScrollView) inflater.inflate(R.layout.signup_layout, null);
        llBody.addView(signUp, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceNormal(signUp);
        ivLogo.setVisibility(View.VISIBLE);
        ivLogo.setImageResource(R.drawable.header_signup1);
        //  llBack.setVisibility(View.VISIBLE);
        lockDrawer("SignUpActivity");
        loginButton  = new LoginButton(SignUpActivity.this);

        initialiseControls();
        setStatusBarColor();

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
                showLoader("Fetching ");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final CustomerDO customerDO = new SocialNetworkCilent().getUserDetailsFromFaceBook(accessToken.getToken());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(customerDO != null) {
                                    Intent intent = new Intent(SignUpActivity.this, SNSignUpConfirmationActivity.class);
                                    intent.putExtra("Customer",customerDO);
                                    intent.putExtra("Type","Register");
                                    startActivity(intent);
                                    finish();
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
        llBirthday = (LinearLayout) signUp.findViewById(R.id.llBirthday);
        etName = (EditText) signUp.findViewById(R.id.etName);
        etEmail = (EditText) signUp.findViewById(R.id.etEmail);
        etPassword = (EditText) signUp.findViewById(R.id.etPassword);
        etConfirmPass = (EditText) signUp.findViewById(R.id.etConfirmPass);
        etAnniversary = (EditText) signUp.findViewById(R.id.etAnniversary);
        etBday = (EditText) signUp.findViewById(R.id.etBday);
        ivLoginWithFb = (ImageView) signUp.findViewById(R.id.ivLoginWithFb);
        ivCalAnivers = (ImageView) signUp.findViewById(R.id.ivCalAnivers);
        ivLoginWithTwitter = (ImageView) signUp.findViewById(R.id.ivLoginWithTwitter);
        ivLoginWithGooglePlus = (ImageView) signUp.findViewById(R.id.ivLoginWithGooglePlus);
        btnNext = (Button) signUp.findViewById(R.id.btnNext);
        btnNext.setTypeface(AppConstants.DinproMedium);
        ivPreviewPass = (ImageView) signUp.findViewById(R.id.ivPreviewPass);
        ivPreviewPassConfirm = (ImageView) signUp.findViewById(R.id.ivPreviewPassConfirm);

        etBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog(etBday);
            }
        });
        etAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog(etAnniversary);
            }
        });
        llBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                etBday.performClick();
            }
        });
        ivCalAnivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                etAnniversary.performClick();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(etName.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentername), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etEmail.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                } else if (!isEmailValid(etEmail.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etPassword.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterpassword), getResources().getString(R.string.ok), "", "");
                } else if(!StringUtils.isValidPassword(etPassword.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentervalidpassword)+" "+getResources().getString(R.string.plzentervalidpassword1), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(etConfirmPass.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenterconfrmpassword), getResources().getString(R.string.ok), "", "");
                } else if (!etPassword.getText().toString().trim().equals(etConfirmPass.getText().toString().trim())) {
                    showCustomDialog(SignUpActivity.this, getString(R.string.alert), getResources().getString(R.string.paswordandconfrmpassword), getResources().getString(R.string.ok), "", "");
                } else {
                    CustomerDO customerDO = new CustomerDO();
                    customerDO.siteName = etName.getText().toString();
                    customerDO.cutomerEmailId = etEmail.getText().toString();
                    customerDO.password = etPassword.getText().toString();
                    customerDO.dateOfBirth = etBday.getText().toString();
                    customerDO.anniversary = etAnniversary.getText().toString();
                    Intent intent = new Intent(SignUpActivity.this, AddressDetailsActivity.class);
                    intent.putExtra("Customer", customerDO);
                    startActivityForResult(intent, AppConstants.destoryActivity);

                }
            }
        });
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
        ivPreviewPassConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passShow2) {
                    //show password
                    passShow2 = true;
                    etConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etConfirmPass.setSelection(etPassword.getText().toString().length());
                    ivPreviewPassConfirm.setImageResource(R.drawable.preview_h);
                } else {
                    //hide password
                    passShow2 = false;
                    etConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etConfirmPass.setSelection(etPassword.getText().toString().length());
                    ivPreviewPassConfirm.setImageResource(R.drawable.preview);
                }
            }
        });

        ivLoginWithGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    Intent intent = new Intent(SignUpActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.GOOGELPLUS_LOGIN_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_GOOGLEPLUS);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);
                }
            }
        });

        ivLoginWithFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    /*Intent intent = new Intent(SignUpActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.FACEBOOK_LOGIN_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_FACEBOOK);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);*/
                    loginButton.performClick();
                }
            }
        });

        ivLoginWithTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetworkConnection()) {
                    Intent intent = new Intent(SignUpActivity.this, SignInWebViewActivity.class);
                    intent.putExtra("URL", AppConstants.TWITTER_Request_Token_URL);
                    intent.putExtra("NETWORKTYPE", AppConstants.NETWORKTYPE_TWITTER);
                    startActivityForResult(intent, SOCIAL_NETWORK_LOGIN);
                }
            }
        });

    }
    //for date picker dialog and set date to edit text
    private void datePickerDialog(final EditText ed) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog mDatePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ed.setText(new SimpleDateFormat(("dd/MM/yy"), Locale.US).format(myCalendar.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        // mDatePicker.setTitle("Select Birthday Date");
        mDatePicker.show();
    }


    @Override
    public void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean loginFlag = false;
        if(requestCode == AppConstants.destoryActivity && resultCode == RESULT_OK)
            finish();
        if (requestCode == SOCIAL_NETWORK_LOGIN && resultCode == RESULT_OK) {
            if (data.hasExtra("CUSTOMER")) {
                CustomerDO customerDO = (CustomerDO) data.getSerializableExtra("CUSTOMER");
                if (customerDO != null) {
                    Intent intent = new Intent(SignUpActivity.this, SNSignUpConfirmationActivity.class);
                    intent.putExtra("Customer",customerDO);
                    intent.putExtra("Type","Register");
                    startActivity(intent);
                    finish();
                }
            }
        } else if(requestCode == SOCIAL_NETWORK_LOGIN && resultCode != RESULT_OK){
            showCustomDialog(SignUpActivity.this, getString(R.string.alert), getString(R.string.SIGNUP_FAILED), getString(R.string.OK), "", "");
            super.onActivityResult(requestCode, resultCode, data);
        }
        else
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }
}
