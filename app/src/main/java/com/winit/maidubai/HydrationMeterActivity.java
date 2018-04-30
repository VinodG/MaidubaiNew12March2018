package com.winit.maidubai;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.HydrationMeterDA;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.util.Date;

public class HydrationMeterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llHydrationMeter,llDrinkMeter,llSocial;

    private TextView tvAddedDrink,tvAddDrinkPercent,tvMessage,tvDisclaimer,tvTarget;
    private Button btnAddDrink;
    private ImageView ivSettings,ivSummary,ivCup;
    private double suggestedDrink;
    private double drinkAdded;
    private HydrationMeterDO hydrationMeterDO;
    private HydrationMeterReadingDO hydrationMeterReadingDO;
    private String currDate,serviceCurrDate;
    @Override
    public void initialise() {
        llHydrationMeter =(LinearLayout)inflater.inflate(R.layout.activity_hydration_meter, null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(llHydrationMeter, param);
        setTypeFaceNormal(llHydrationMeter);
        ivMenu.setVisibility(View.VISIBLE);
        setStatusBarColor();
        ivMenu.setImageResource(R.drawable.menu_white);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.hydration_meter));
        tvCancel.setText(getResources().getString(R.string.rest));
        initialiseControls();
        if(!preference.getbooleanFromPreference(Preference.IS_DISCLAIMER,false)) {
            showCustomDialog(HydrationMeterActivity.this, getString(R.string.disclaimer), getString(R.string.disclaimer_msg), getString(R.string.accept), getString(R.string.cancel), "Disclaimer", false);
            tvDisclaimer.setVisibility(View.GONE);
        }else
            tvDisclaimer.setVisibility(View.VISIBLE);
    }

    @Override
    public void initialiseControls() {

        tvAddedDrink = (TextView)llHydrationMeter.findViewById(R.id.tvAddedDrink);
        tvAddDrinkPercent = (TextView)llHydrationMeter.findViewById(R.id.tvAddDrinkPercent);
        tvTarget = (TextView)llHydrationMeter.findViewById(R.id.tvTarget);
        tvMessage = (TextView)llHydrationMeter.findViewById(R.id.tvMessage);
        tvDisclaimer = (TextView)llHydrationMeter.findViewById(R.id.tvDisclaimer);
        btnAddDrink = (Button)llHydrationMeter.findViewById(R.id.btnAddDrink);
        ivSettings = (ImageView)llHydrationMeter.findViewById(R.id.ivSettings);
        ivCup = (ImageView)llHydrationMeter.findViewById(R.id.ivCup);
        ivSummary = (ImageView)llHydrationMeter.findViewById(R.id.ivSummary);
        llDrinkMeter = (LinearLayout)llHydrationMeter.findViewById(R.id.llDrinkMeter);
        llSocial = (LinearLayout)llHydrationMeter.findViewById(R.id.llSocial);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(HydrationMeterActivity.this, "", getString(R.string.DO_You_want_to_reset), getString(R.string.OK), getString(R.string.cancel), "Reset Reading");
            }
        });
        tvDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(HydrationMeterActivity.this, getString(R.string.disclaimer), getString(R.string.disclaimer_msg), getString(R.string.ok), "", "",false);
            }
        });

        btnAddDrink.setOnClickListener(this);
        ivSummary.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
        setLocale(preference.getStringFromPreference(Preference.LANGUAGE,""));
    }

    @Override
    public void loadData() {
        showLoader(getString(R.string.Loading_Data));
        new Thread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
                HydrationMeterDA hydrationMeterDA = new HydrationMeterDA(HydrationMeterActivity.this);
                hydrationMeterDO = hydrationMeterDA.getHydrationMeterSettings();
                currDate = CalendarUtil.getDate(new Date(), CalendarUtil.DD_MM_YYYY_PATTERN);
                serviceCurrDate = CalendarUtil.getDate(new Date(), CalendarUtil.MM_DD_YYYY_PATTERN);
                hydrationMeterReadingDO = hydrationMeterDA.getHydrationMeterReading(currDate);
                currDate = CalendarUtil.getDate(new Date(), CalendarUtil.DATE_PATTERN_dd_MM_YYYY);
                if(hydrationMeterReadingDO != null)
                    drinkAdded = hydrationMeterReadingDO.WaterConsumed;
                else
                    drinkAdded = 0;

                if(hydrationMeterDO != null)
                    suggestedDrink = hydrationMeterDO.DailyWaterConsumptionTarget;
                else
                    suggestedDrink = 0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(preference.getbooleanFromPreference(Preference.IS_DISCLAIMER,false)) {
                                if(hydrationMeterDO == null)
                                    showCustomDialog(HydrationMeterActivity.this, getString(R.string.alert), "Go to settings", getString(R.string.ok), getString(R.string.cancel), "FirstTime", false);
                            }
                            if(suggestedDrink >0) {
                                if (NetworkUtil.isNetworkConnectionAvailable(HydrationMeterActivity.this)) {
                                    addDrinkToMeter(0);
                                }
                                refreshDrinkUI();
                            }
                        }
                    });
                }
        }).start();
    }

    private void addDrink(){
        if(suggestedDrink>0) {
            if(drinkAdded>=suggestedDrink)
            showCustomDialog(HydrationMeterActivity.this, "Alert", getResources().getString(R.string.more_sugest), getResources().getString(R.string.rest_hydra), getResources().getString(R.string.cancel), "More Drink");
            else{
                BottomSheetDialogFragment bottomSheetDialogFragment = new AddDrinkBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putDouble("SuggestedDrink",suggestedDrink);
                bundle.putDouble("AddedDrink",drinkAdded);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }

        }else
            showCustomDialog(HydrationMeterActivity.this, "Alert", getResources().getString(R.string.suggestion_warning), getResources().getString(R.string.OK), "", "Add Drink");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void refreshDrinkUI(){
        tvTarget.setText(String.format(getString(R.string.you_drink), hydrationMeterDO.DailyWaterConsumptionTarget/1000));
        if(drinkAdded>=suggestedDrink){
            ivCup.setVisibility(View.VISIBLE);
//  ujjwal suggestion          tvMessage.setText(getString(R.string.performed_normal));
            tvAddDrinkPercent.setVisibility(View.GONE);
            llSocial.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
            tvTarget.setText(getResources().getString(R.string.more_sugest));
        }else if(suggestedDrink > 0 && drinkAdded > 0) {
            ivCup.setVisibility(View.GONE);
//  ujjwal suggestion          tvMessage.setText(getString(R.string.well_done));
            tvAddDrinkPercent.setVisibility(View.VISIBLE);
            llSocial.setVisibility(View.GONE);
            btnAddDrink.setEnabled(true);
            tvCancel.setVisibility(View.VISIBLE);
        }else {
            tvCancel.setVisibility(View.GONE);
            ivCup.setVisibility(View.GONE);
//  ujjwal suggestion          tvMessage.setText(getString(R.string.add_one_glass_of_water));
            tvAddDrinkPercent.setVisibility(View.VISIBLE);
            llSocial.setVisibility(View.GONE);
            btnAddDrink.setEnabled(true);
        }
//        tvAddedDrink.setText(String.format(getString(R.string.drink),decimalFormat.format((drinkAdded/1000f)),decimalFormat.format((suggestedDrink/1000f))));
        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("en"))
        {
            tvAddedDrink.setText(String.format("%sl of %sl",decimalFormat.format((drinkAdded/1000f)),decimalFormat.format((suggestedDrink/1000f))));
        }
        else
        {
            tvAddedDrink.setText(String.format("%sلتر من %sلتر",decimalFormat.format((drinkAdded/1000f)),decimalFormat.format((suggestedDrink/1000f))));
        }
        if(hydrationMeterReadingDO != null)
            tvAddDrinkPercent.setText((int)hydrationMeterReadingDO.WaterConsumedPercent+"%");
        else
            tvAddDrinkPercent.setText(0 + "%");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id){
            case R.id.btnAddDrink:
                addDrink();
                break;
            case R.id.ivSummary:
                intent = new Intent(HydrationMeterActivity.this,DrinkSummaryActivity.class);
                break;
            case R.id.ivSettings:
                intent = new Intent(HydrationMeterActivity.this, HydrationSettingActivity.class);
                if(hydrationMeterDO != null)
                    intent.putExtra("hydrationMeterDO",hydrationMeterDO);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

    public void addDrinkToMeter(int addLitres){
        if(checkNetworkConnection()) {
            if(addLitres != 0)
                showLoader(getString(R.string.save_settings));
            if (hydrationMeterReadingDO == null) {
                hydrationMeterReadingDO = new HydrationMeterReadingDO();
                hydrationMeterReadingDO.CustomerId = preference.getIntFromPreference(Preference.CUSTOMER_ID, 0);
            }
            hydrationMeterReadingDO.ConsumptionDate = serviceCurrDate;
            hydrationMeterReadingDO.WaterConsumed = addLitres;
            new CommonBL(HydrationMeterActivity.this, HydrationMeterActivity.this).addDrink(hydrationMeterReadingDO);
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if(from.equalsIgnoreCase("Reset Reading") || from.equalsIgnoreCase("More Drink")){
            addDrinkToMeter((-1) * ((int) drinkAdded));
        }else if(from.equalsIgnoreCase("Disclaimer")){
            preference.saveBooleanInPreference(Preference.IS_DISCLAIMER, true);
            tvDisclaimer.setVisibility(View.VISIBLE);
            if(hydrationMeterDO == null)
                showCustomDialog(HydrationMeterActivity.this, getString(R.string.alert), getString(R.string.gotosettings), getString(R.string.ok), getString(R.string.cancel), "FirstTime", false);
        }
        else if(from.equalsIgnoreCase("FirstTime")) {
            Intent intent = new Intent(HydrationMeterActivity.this, HydrationSettingActivity.class);
            if(hydrationMeterDO != null)
                intent.putExtra("hydrationMeterDO",hydrationMeterDO);
            startActivity(intent);
        }
    }

    @Override
    public void onButtonNoClick(String from) {
        super.onButtonNoClick(from);
        if(from.equalsIgnoreCase("Disclaimer")){
            finish();
        }
        else if(from.equalsIgnoreCase("FirstTime")) {
            finish();
        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if(response != null && response.method == ServiceMethods.WS_ADD_DRINK && response.data !=null){
            if(response.data instanceof HydrationMeterReadingDO){
                HydrationMeterReadingDO hydrationMeterReading = (HydrationMeterReadingDO)response.data;
                drinkAdded = hydrationMeterReading.WaterConsumed;
                hydrationMeterReadingDO = hydrationMeterReading;
                hydrationMeterReadingDO.ConsumptionDate = currDate;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new HydrationMeterDA(HydrationMeterActivity.this).insertHydrationMeterReading(hydrationMeterReadingDO);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoader();
                                refreshDrinkUI();
                            }
                        });
                    }
                }).start();
            }else{
                hideLoader();
                if(!StringUtils.isEmpty((String) response.data))
                    showCustomDialog(HydrationMeterActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }

        }
    }
}