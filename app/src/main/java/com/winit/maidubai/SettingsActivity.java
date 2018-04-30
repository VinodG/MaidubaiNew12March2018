package com.winit.maidubai;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.ArrayList;

public class SettingsActivity extends BaseActivity {
    private LinearLayout lLMain;
    private TextView tvEngLang, tvNotifyOff, tvNotifyOn, tvArbicLang,tvPreferedTime;
    private CustomerDO customerDO;
    private String language = "",preferTime = "";
    private boolean isNotify;
    @Override
    public void initialise() {
        lLMain = (LinearLayout) inflater.inflate(R.layout.activity_settings, null);
        llBody.addView(lLMain, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //llBack.setVisibility(View.VISIBLE);
        setStatusBarColor();
        ivMenu.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.settings));

        tvCancel.setText(getResources().getString(R.string.save));
        setTypeFaceNormal(lLMain);
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {

        tvEngLang = (TextView)lLMain.findViewById(R.id.tvEngLang);
        tvArbicLang = (TextView)lLMain.findViewById(R.id.tvArbicLang);
        tvNotifyOn = (TextView)lLMain.findViewById(R.id.tvNotifyOn);
        tvNotifyOff = (TextView)lLMain.findViewById(R.id.tvNotifyOff);
        tvPreferedTime = (TextView)lLMain.findViewById(R.id.tvPreferedTime);

        tvEngLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(true);
            }
        });
        tvArbicLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(false);
            }
        });
        tvNotifyOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotification(true);
            }
        });
        tvNotifyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotification(false);
            }
        });

        tvPreferedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] time = tvPreferedTime.getText().toString().split("to");
                Bundle bundle = new Bundle();
                if (time != null && time.length == 2) {
                    bundle.putString("StratTime", time[0].trim());
                    bundle.putString("endTime", time[1].trim());
                }
                BottomSheetDialogFragment bottomSheetDialogFragment = new PrefferdTimeBtmSheetDlgFrgmnt();
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(language.equalsIgnoreCase(customerDO.PreferedLanguage) && isNotify == customerDO.Notification) {
                    showCustomDialog(SettingsActivity.this,"",getString(R.string.no_changes),getString(R.string.OK),"","");
                }else if(checkNetworkConnection()) {
                    showLoader(getString(R.string.save_settings));
                    new CommonBL(SettingsActivity.this, SettingsActivity.this).updateProfileSettings(customerDO.customerId,language,preferTime,isNotify);
                }
            }
        });

    }

    public void setPrefferdTime(String setPrefferTime){
        preferTime = setPrefferTime;
        tvPreferedTime.setText(setPrefferTime);
    }

    public void setLanguage(boolean flag){
        if(flag){
            language = "English";
            tvEngLang.setBackgroundResource(R.drawable.settings_btn_select_bg);
            tvArbicLang.setBackgroundResource(R.drawable.settings_btn_unselect_bg);
            tvEngLang.setTextColor(getResources().getColor(R.color.white));
            tvArbicLang.setTextColor(getResources().getColor(R.color.red));
        }else{
            language = "Arabic";
            tvArbicLang.setBackgroundResource(R.drawable.settings_btn_select_bg);
            tvEngLang.setBackgroundResource(R.drawable.settings_btn_unselect_bg);
            tvEngLang.setTextColor(getResources().getColor(R.color.red));
            tvArbicLang.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void setNotification(boolean flag){
        if(flag){
            isNotify = true;
            tvNotifyOn.setBackgroundResource(R.drawable.settings_btn_select_bg);
            tvNotifyOff.setBackgroundResource(R.drawable.settings_btn_unselect_bg);
            tvNotifyOn.setTextColor(getResources().getColor(R.color.white));
            tvNotifyOff.setTextColor(getResources().getColor(R.color.red));
        }else{
            isNotify = false;
            tvNotifyOff.setBackgroundResource(R.drawable.settings_btn_select_bg);
            tvNotifyOn.setBackgroundResource(R.drawable.settings_btn_unselect_bg);
            tvNotifyOn.setTextColor(getResources().getColor(R.color.red));
            tvNotifyOff.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void loadData() {

        showLoader("Loading data....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                customerDO = new CustomerDA(SettingsActivity.this).getCustomer();
                language = customerDO.PreferedLanguage;
                isNotify = customerDO.Notification;
                preferTime = customerDO.PreferedTime;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();

                        String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                        if(language.equalsIgnoreCase("en")){
                            setLanguage(true);
                        }else{
                            setLanguage(false);
                        }
                        if(customerDO.Notification) {
                           setNotification(true);
                        }else {
                           setNotification(false);
                        }
                        if(!StringUtils.isEmpty(customerDO.PreferedTime))
                            tvPreferedTime.setText(customerDO.PreferedTime);
                        else
                            tvPreferedTime.setHint(customerDO.PreferedTime);
                    }
                });
            }
        }).start();

    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Sucess")){
            if(!StringUtils.isEmpty(customerDO.PreferedLanguage)) {
                if(customerDO.PreferedLanguage.equalsIgnoreCase("English"))
                    setLocale("en");
                else
                    setLocale("ar");
            }
            Intent intent = new Intent(SettingsActivity.this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
       else if(from.equalsIgnoreCase("LOGOUT"))
        {
            clearPreference();
            Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if(response.method!=null && response.method == ServiceMethods.WS_UPDATE_SETTING && response.data!=null && response.data instanceof CommonResponseDO){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<CustomerDO> arrCustomerDOs = new ArrayList<>();
                    customerDO.PreferedLanguage = language;
                    customerDO.Notification = isNotify;
                    arrCustomerDOs.add(customerDO);
                    boolean flag = new CustomerDA(SettingsActivity.this).insertCustomer(arrCustomerDOs);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            showCustomDialog(SettingsActivity.this,getString(R.string.success),getString(R.string.pref_saved),getString(R.string.OK),"","Sucess");
                        }
                    });
                }
            }).start();
        }else{
            hideLoader();
            showCustomDialog(SettingsActivity.this,"",(String)response.data,getString(R.string.OK),"","");
        }
    }
}
