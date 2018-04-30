package com.winit.maidubai;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.utilities.BitmapUtilsLatLang;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.HttpHelper;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.io.File;


/**
 * Created by Sridhar.V on 7/1/2016.
 */
public class ProfileActivity extends BaseActivity {
    private LinearLayout lProfileActvity, llManageAddress, llOffer, llInviteFriends, llSendFeedback;
    private EditText etUserName;
    private ImageView ivProfile,ivEdit;
    private String fileUri = "";
    private TextView tvemail,tvmobile,tvOffers;
    private CustomerDO customerDO;
    @Override
    public void initialise() {
        lProfileActvity = (LinearLayout) inflater.inflate(R.layout.activity_profile, null);
        lProfileActvity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        llBody.addView(lProfileActvity);
        setTypeFaceNormal(lProfileActvity);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.profile));
        ivMenu.setVisibility(View.VISIBLE);
        //lockDrawer("ProfileActivity");
        tvCancel.setVisibility(View.INVISIBLE);
        tvCancel.setClickable(false);
        tvCancel.setText(getString(R.string.edit));
        setStatusBarColor();
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivEdit = (ImageView) findViewById(R.id.ivEdit);
        llManageAddress = (LinearLayout) findViewById(R.id.llManageAddress);
        llOffer = (LinearLayout) findViewById(R.id.llOffer);
        llInviteFriends = (LinearLayout) findViewById(R.id.llInviteFriends);
        llSendFeedback = (LinearLayout) findViewById(R.id.llSendFeedback);
        etUserName = (EditText) findViewById(R.id.etUserName);
        tvmobile = (TextView) findViewById(R.id.tvmobile);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvOffers = (TextView) findViewById(R.id.tvOffers);


        etUserName.setEnabled(false);
        etUserName.setFocusable(false);
        etUserName.setTypeface(AppConstants.DinproMedium);
        tvmobile.setTypeface(AppConstants.DinproRegular);
        tvmobile.setTypeface(AppConstants.DinproRegular);
        llManageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ManageAddressActivity.class);
                intent.putExtra("CustomerDo", customerDO);
                startActivity(intent);
            }
        });
        llSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SendFeedbackActivity.class);
                startActivity(intent);
            }
        });
        llInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "The unique red color in the corporate identity of \"Mai Dubai\" symbolizes vigor, \n" +
                        "vividness, happiness and prosperity.\n http://www.maidubaiwater.com");
                startActivity(Intent.createChooser(sharingIntent,"Share Via..."));
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureImageOptions();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName.setEnabled(true);
                etUserName.setFocusable(true);
                etUserName.setFocusableInTouchMode(true);
                etUserName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etUserName, 0);
                etUserName.setSelection(etUserName.getText().toString().length());
                tvCancel.setText(getString(R.string.save));
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setClickable(true);
                ivEdit.setVisibility(View.GONE);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvCancel.getText().toString();
                if(text.equalsIgnoreCase(getString(R.string.edit))) {
                    /*etUserName.setEnabled(true);
                    etUserName.setFocusable(true);
                    etUserName.setFocusableInTouchMode(true);
                    etUserName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etUserName, 0);
                    etUserName.setSelection(etUserName.getText().toString().length());
                    tvCancel.setText(getString(R.string.save));
                    tvCancel.setVisibility(View.VISIBLE);*/
                }else{
                    etUserName.setEnabled(false);
                    etUserName.setFocusable(false);
                    preference.saveStringInPreference(Preference.CUSTOMER_NAME, etUserName.getText().toString());
                    userName = etUserName.getText().toString();
                    tvUserNameLoggedIn.setText(userName);
                    tvCancel.setText(getString(R.string.edit));
                    tvCancel.setVisibility(View.INVISIBLE);
                    tvCancel.setClickable(false);
                    ivEdit.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void loadData() {
        showLoader("Loading data....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerDO = new CustomerDA(ProfileActivity.this).getCustomer();
                if(!StringUtils.isEmpty(customerDO.ProfileImage)) {
                    preference.saveStringInPreference(Preference.USER_IMAGE, customerDO.ProfileImage);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        fileUri = preference.getStringFromPreference(Preference.USER_IMAGE, "");

                        String name = "";
                        if (!StringUtils.isEmpty(customerDO.siteName))
                            name = customerDO.siteName;
                        if(!StringUtils.isEmpty(customerDO.customerId))
                            name =name+" ("+customerDO.customerId+")";

                        etUserName.setText(name);

                        if (!StringUtils.isEmpty(customerDO.cutomerEmailId))
                            tvemail.setText(customerDO.cutomerEmailId);
                        if (!StringUtils.isEmpty(customerDO.mobileNumber))
                            tvmobile.setText(customerDO.mobileNumber);

                        setImage(fileUri,ivProfile,R.drawable.profile_pic);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
           /* Uri uri = data.getData();
            setImage(uri.toString(),ivProfile,R.drawable.profile_pic);*/
            //setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivProfile, R.drawable.profile_pic);

            final Uri uri = data.getData();
            showLoader("uploading....");
            isProfilePicUpload = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = getRealPathFromURI(uri);

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,320);

                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/Mai Dubai");
                    if (!dir.exists())
                        dir.mkdirs();
                    File userImgFile = new File(dir.getAbsolutePath() + "/"+ preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+".png");
                    String tempuserImagepath = userImgFile.getAbsolutePath();


                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,tempuserImagepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivProfile, R.drawable.profile_pic);
                                setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivUserImage, R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(ProfileActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();

        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
            //setImage(userImage,ivProfile,R.drawable.profile_pic);
            //setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivProfile, R.drawable.profile_pic);

            showLoader("uploading....");
            isProfilePicUpload = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = userImage;

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,320);

                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,filepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivProfile, R.drawable.profile_pic);
                                setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivUserImage, R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(ProfileActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();
        }
        if(requestCode == GALLERY_KITKAT_INTENT_CALLED && resultCode == RESULT_OK){

           /* Uri originalUri = data.getData();

            preference.saveStringInPreference(Preference.USER_IMAGE,originalUri.toString());
            setImage(originalUri.toString(),ivProfile,R.drawable.profile_pic);*/
            //setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivProfile, R.drawable.profile_pic);


            showLoader("uploading....");
            isProfilePicUpload = true;
            final Uri originalUri = data.getData();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = getRealPathFromURI(originalUri); //commented by vinod
//                    String filepath = getRealPathFromURI(ProfileActivity.this,originalUri);

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,320);

                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/Mai Dubai");
                    if (!dir.exists())
                        dir.mkdirs();
                    File userImgFile = new File(dir.getAbsolutePath() + "/"+ preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+".png");
                    String tempuserImagepath = userImgFile.getAbsolutePath();

                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,tempuserImagepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivProfile, R.drawable.profile_pic);
                                setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivUserImage, R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(ProfileActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
