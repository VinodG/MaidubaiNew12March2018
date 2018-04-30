package com.winit.maidubai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendFeedbackActivity extends BaseActivity {
    private LinearLayout llFeedbackLAyout, llAppForword, llDeliveryForword, llQuearyForword, llFeedbackForword, llDelivery, llAppFeedback, llFeedback;
    private RadioGroup rgDeliveryFeedback, rgAppFeedback, rgFeedback;
    private EditText etComment, etEmailFeedback, etCommentAppFeedback, etEmailAppFeedback, etCommentFeedback, etEmailDeliveryFeedback;
    private TextView tvAddPhoto, tvSubmitFeedback, tvAddPhotoAppFeedback, tvSubmitAppFeedback, tvAddPhotoFeedback, tvSubmitDeliveryFeedback;
    private ImageView ivAppForword, ivDeliveryForword, ivQuearyForword, ivFeedbackForword;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String feedbackImage = "",deviceId;
    private boolean isAppFeedback =false,isDelivaryFeedback=false;

    @Override
    public void initialise() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.send_feedback);
        ivMenu.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.INVISIBLE);
        tvCancel.setClickable(false);
        tvCancel.setText(getResources().getString(R.string.save));
        llFeedbackLAyout = (LinearLayout) inflater.inflate(R.layout.activity_send_feedback, null);
        llBody.addView(llFeedbackLAyout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceNormal(llFeedbackLAyout);
        setStatusBarColor();

        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        llAppForword = (LinearLayout) findViewById(R.id.llAppForword);
        llDeliveryForword = (LinearLayout) findViewById(R.id.llDeliveryForword);
        llQuearyForword = (LinearLayout) findViewById(R.id.llAppForword);
        llFeedbackForword = (LinearLayout) findViewById(R.id.llFeedbackForword);
        llDelivery = (LinearLayout) findViewById(R.id.llDelivery);
        llAppFeedback = (LinearLayout) findViewById(R.id.llAppFeedback);
        llFeedback = (LinearLayout) findViewById(R.id.llFeedback);

        rgDeliveryFeedback = (RadioGroup) findViewById(R.id.rgDelivery);
        rgAppFeedback = (RadioGroup) findViewById(R.id.rgRateAppFeedback);
        rgFeedback = (RadioGroup) findViewById(R.id.rgRateFeedback);


        etComment = (EditText) findViewById(R.id.etComment);
        etEmailFeedback = (EditText) findViewById(R.id.etEmailFeedback);
        etCommentAppFeedback = (EditText) findViewById(R.id.etCommentAppFeedback);
        etEmailAppFeedback = (EditText) findViewById(R.id.etEmailAppFeedback);
        etCommentFeedback = (EditText) findViewById(R.id.etCommentFeedback);
        etEmailDeliveryFeedback = (EditText) findViewById(R.id.etEmailDeliveryFeedback);


        tvAddPhoto = (TextView) findViewById(R.id.tvAddPhoto);
        tvSubmitFeedback = (TextView) findViewById(R.id.tvSubmitFeedback);
        tvAddPhotoAppFeedback = (TextView) findViewById(R.id.tvAddPhotoAppFeedback);
        tvSubmitAppFeedback = (TextView) findViewById(R.id.tvSubmitAppFeedback);
        tvAddPhotoFeedback = (TextView) findViewById(R.id.tvAddPhotoFeedback);
        tvSubmitDeliveryFeedback = (TextView) findViewById(R.id.tvSubmitDeliveryFeedback);

        ivAppForword = (ImageView) findViewById(R.id.ivAppForword);
        ivDeliveryForword = (ImageView) findViewById(R.id.ivDeliveryForword);
        ivQuearyForword = (ImageView) findViewById(R.id.ivQuearyForword);
        ivFeedbackForword = (ImageView) findViewById(R.id.ivFeedbackForword);

        String token = preference.getStringFromPreference(Preference.GCM_TOKEN,"");
        //deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        deviceId = token;
        llDeliveryForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llDelivery.getVisibility() == View.GONE) {
                    llDelivery.setVisibility(View.VISIBLE);
                    ivDeliveryForword.setImageResource(R.drawable.go_backword);
                } else {
                    llDelivery.setVisibility(View.GONE);
                    ivDeliveryForword.setImageResource(R.drawable.go_forword);
                }
                if(llAppFeedback.getVisibility() == View.VISIBLE)
                {
                    llAppFeedback.setVisibility(View.GONE);
                    ivAppForword.setImageResource(R.drawable.go_forword);
                }
            }
        });

        tvAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureImageOptions();

            }
        });

        tvSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = radioGroupSelection(rgFeedback);
                String comment = etCommentFeedback.getText().toString().trim();
                String email = etEmailFeedback.getText().toString().trim();
                if (StringUtils.isEmpty(selection)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentersmile), getResources().getString(R.string.ok), "", "");
                } else if (StringUtils.isEmpty(comment)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercmnt), getResources().getString(R.string.ok), "", "");
                } else {
                    if (StringUtils.isEmpty(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                    } else if (!isEmailValid(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                    } else
                    {
                        if (checkNetworkConnection()) {
                            showLoader(getString(R.string.plz_wait));
                            new CommonBL(SendFeedbackActivity.this, SendFeedbackActivity.this).feedBack(customerDO.Id,"Feedback", selection, comment, email,deviceId);
                            // showPopUp("Your feedback has been delivered \nsuceessfully.");
                        }
                    }
                }

            }
        });

        llAppForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llAppFeedback.getVisibility() == View.GONE) {
                    llAppFeedback.setVisibility(View.VISIBLE);
                    ivAppForword.setImageResource(R.drawable.go_backword);
                } else {
                    llAppFeedback.setVisibility(View.GONE);
                    ivAppForword.setImageResource(R.drawable.go_forword);
                }
                if(llDelivery.getVisibility() == View.VISIBLE)
                {
                    llDelivery.setVisibility(View.GONE);
                    ivDeliveryForword.setImageResource(R.drawable.go_forword);
                }
            }
        });
        tvAddPhotoAppFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCaptureImageOptions();

            }
        });

        tvSubmitAppFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = radioGroupSelection(rgAppFeedback);
                String comment = etCommentAppFeedback.getText().toString().trim();
                String email = etEmailAppFeedback.getText().toString().trim();
                if (StringUtils.isEmpty(selection)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentersmile), getResources().getString(R.string.ok), "", "");
                } /*else if (StringUtils.isEmpty(comment)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercmnt), getResources().getString(R.string.ok), "", "");
                } */else {
                    if (StringUtils.isEmpty(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                    } else if (!isEmailValid(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                    } else {
                        if (checkNetworkConnection()) {
                            showLoader(getString(R.string.plz_wait));
                            isAppFeedback =true;
                            new CommonBL(SendFeedbackActivity.this, SendFeedbackActivity.this).feedBack(customerDO.Id,"App feedback", selection, comment, email,deviceId);
                            // showPopUp("Your feedback has been delivered \nsuceessfully.");
                        }
                    }


                }
            }
        });

        llFeedbackForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llFeedback.getVisibility() == View.GONE) {
                    llFeedback.setVisibility(View.VISIBLE);
                    ivFeedbackForword.setImageResource(R.drawable.go_backword);
                } else {
                    llFeedback.setVisibility(View.GONE);
                    ivFeedbackForword.setImageResource(R.drawable.go_forword);
                }
            }
        });
        tvAddPhotoFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureImageOptions();

            }
        });

        tvSubmitDeliveryFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = radioGroupSelection(rgDeliveryFeedback);
                String comment = etComment.getText().toString().trim();
                String email = etEmailDeliveryFeedback.getText().toString().trim();
                if (StringUtils.isEmpty(selection)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentersmile), getResources().getString(R.string.ok), "", "");
                } /*else if (StringUtils.isEmpty(comment)) {
                    showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercmnt), getResources().getString(R.string.ok), "", "");
                }*/ else {
                    if (StringUtils.isEmpty(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzenteremail), getResources().getString(R.string.ok), "", "");
                    } else if (!isEmailValid(email)) {
                        showCustomDialog(SendFeedbackActivity.this, getString(R.string.alert), getResources().getString(R.string.plzentercorrectemail), getResources().getString(R.string.ok), "", "");
                    } else
                    {
                        if (checkNetworkConnection()) {
                            showLoader(getString(R.string.plz_wait));
                            isDelivaryFeedback = true;
                            new CommonBL(SendFeedbackActivity.this, SendFeedbackActivity.this).feedBack(customerDO.Id,"Delivery feedback", selection, comment, email,deviceId);
                            // showPopUp("Your feedback has been delivered \nsuceessfully.");
                        }
                    }
                }
            }
        });

    }

    private String radioGroupSelection(RadioGroup rgName) {
        if (rgName.getCheckedRadioButtonId() != -1) {
            int id = rgName.getCheckedRadioButtonId();
            View radioButton = rgName.findViewById(id);
            int radioId = rgName.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rgName.getChildAt(radioId);
            String selection =  btn.getTag().toString();
            return selection;
        }
        return "";
    }

    @Override
    public void loadData() {

        showLoader("Loading data....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerDO = new CustomerDA(SendFeedbackActivity.this).getCustomer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        etEmailAppFeedback.setText(customerDO.cutomerEmailId);
                        etEmailDeliveryFeedback.setText(customerDO.cutomerEmailId);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //  ivUserImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap,189,189,true));
              /*  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(40,40,0,0);*/
                // ivUserImage.setLayoutParams(layoutParams);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            File image = new File(userImage);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, 189, 189, true);
            // ivUserImage.setImageBitmap(bitmap);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40, 40, 0, 0);
            // ivUserImage.setLayoutParams(layoutParams);
        }
    }

    private void showPopUp(String msz) {

        View layout = inflater.inflate(R.layout.feedback_success_poppup, null);
        final CustomDialog customDialog = new CustomDialog(SendFeedbackActivity.this, layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();

        final TextView tvDone = (TextView) layout.findViewById(R.id.tvDone);
        final TextView tvmsz = (TextView) layout.findViewById(R.id.tvmsz);
        // tvDeliveryDate.setText("Your Order will be delivered on \n" + msz);
        tvDone.setText(R.string.ok);
        tvmsz.setText(msz);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llAppFeedback.getVisibility() == View.VISIBLE){
                    llAppFeedback.setVisibility(View.GONE);
                    ivAppForword.setImageResource(R.drawable.go_forword);
                }
                if(llDelivery.getVisibility() == View.VISIBLE){
                    llDelivery.setVisibility(View.GONE);
                    ivDeliveryForword.setImageResource(R.drawable.go_forword);
                }
                customDialog.dismiss();

            }
        });
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        LogUtils.errorLog("valid", "called");
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{1,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void dataRetrieved(final ResponseDO response) {
        hideLoader();
        if (response.method != null && (response.method == ServiceMethods.WS_FEEDBACK && response.data.equals(""))) {
            if (response.data instanceof String) {
                showPopUp(getString(R.string.feedback_success));
                if(isDelivaryFeedback)
                {
                    isDelivaryFeedback = false;
                    rgDeliveryFeedback.clearCheck();
                    etComment.setText("");
                }
                else if(isAppFeedback)
                {
                    isAppFeedback = false;
                    rgAppFeedback.clearCheck();
                    etCommentAppFeedback.setText("");
                }
            }
        } else
            showCustomDialog(SendFeedbackActivity.this, "", (String) response.data, getString(R.string.OK), "", "");

        //super.dataRetrieved(response);
    }


}

