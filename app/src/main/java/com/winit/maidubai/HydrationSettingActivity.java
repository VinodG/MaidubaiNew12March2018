package com.winit.maidubai;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomViewPager;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.common.SwipeListener;
import com.winit.maidubai.dataaccesslayer.HydrationMeterDA;
import com.winit.maidubai.dataobject.HydrationMeterDO;
import com.winit.maidubai.utilities.StringUtils;

import java.util.Date;

/**
 * Created by Girish Velivela on 30-09-2016.
 */

public class HydrationSettingActivity extends BaseActivity implements SwipeListener{
    private RelativeLayout rlHydrationSetting;
    private LinearLayout llRedBackgorund;
    private LinearLayout llIndicator;
    private HydrationMeterDO hydrationMeterDO;
    private CustomViewPager vpCustomViewPager;
    private ImageView ivNxt,ivPrev;
    private HydrationSettingAdapter hydrationSettingAdapter;

    @Override
    public void initialise() {
        rlHydrationSetting =(RelativeLayout)inflater.inflate(R.layout.activity_hydration_setting,null);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        llBody.addView(rlHydrationSetting, param);
        setTypeFaceNormal(rlHydrationSetting);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.Hydration_Settings));
        tvCancel.setText(getResources().getString(R.string.save));
        lockDrawer("HydrationSettingActivity");
        setStatusBarColor();
        initialiseControls();
        loadData();
        if(getIntent().hasExtra("hydrationMeterDO"))
            hydrationMeterDO = (HydrationMeterDO)getIntent().getSerializableExtra("hydrationMeterDO");
        if(hydrationMeterDO == null) {
            hydrationMeterDO = new HydrationMeterDO();
        }else{
            hydrationMeterDO.timePeriod = preference.getIntFromPreference(Preference.HYDRATION_TIME_PERIOD,0);
            hydrationMeterDO.notifyMeTime = preference.getbooleanFromPreference(Preference.NOTIFICATION_FLAG,false);
            vpCustomViewPager.setCurrentItem(4);
        }
    }

    @Override
    public void initialiseControls() {
        llRedBackgorund = (LinearLayout)findViewById(R.id.llRedBackgorund);
        llIndicator = (LinearLayout)findViewById(R.id.llIndicator);
        vpCustomViewPager = (CustomViewPager)findViewById(R.id.vpCustomViewPager);
        ivNxt = (ImageView)findViewById(R.id.ivNxt);
        ivPrev = (ImageView)findViewById(R.id.ivPrev);

        hydrationSettingAdapter = new HydrationSettingAdapter();
        vpCustomViewPager.setAdapter(hydrationSettingAdapter);
        vpCustomViewPager.setSwipeListener(this);
        refreshPageController(llIndicator, vpCustomViewPager);

        ivNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPos = vpCustomViewPager.getCurrentItem();
                if (currPos != 4)
                    vpCustomViewPager.setCurrentItem(++currPos);
            }
        });
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPos = vpCustomViewPager.getCurrentItem();
                if (currPos != 0)
                    vpCustomViewPager.setCurrentItem(--currPos);
            }
        });
        vpCustomViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                double weight = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strWeight));
                double height = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strHeight));
                int age = StringUtils.getInt(String.valueOf(hydrationMeterDO.strAge));
                hydrationMeterDO.Weight = weight == 0 ? hydrationMeterDO.Weight : weight;
                hydrationMeterDO.Height = height == 0 ? hydrationMeterDO.Height : height;
                hydrationMeterDO.age = age == 0 ? hydrationMeterDO.age : age;
                tvCancel.setVisibility(View.GONE);
                if (position == 4) {
                    tvCancel.setVisibility(View.VISIBLE);
                    LinearLayout llPagerCell = (LinearLayout) vpCustomViewPager.findViewWithTag(position);
                    if(llPagerCell != null) {
                        TextView tvAge = (TextView) llPagerCell.findViewById(R.id.tvAge);
                        TextView tvGender = (TextView) llPagerCell.findViewById(R.id.tvGender);
                        TextView tvHeight = (TextView) llPagerCell.findViewById(R.id.tvHeight);
                        TextView tvWeight = (TextView) llPagerCell.findViewById(R.id.tvWeight);
                        ImageView ivGender = (ImageView) llPagerCell.findViewById(R.id.ivGender);
                        llRedBackgorund.setBackgroundResource(R.drawable.cnfrm_hy_bg);
                        tvWeight.setText(hydrationMeterDO.Weight + "");
                        tvHeight.setText(hydrationMeterDO.Height + "");
                        tvGender.setText(hydrationMeterDO.gender);
                        tvAge.setText(hydrationMeterDO.age + "");
                        if (validate(position)) {
                            tvCancel.setVisibility(View.VISIBLE);
                        }
                        if (hydrationMeterDO.gender.equalsIgnoreCase(HydrationMeterDO.FEMALE))
                            ivGender.setImageResource(R.drawable.confirm_female);
                        else
                            ivGender.setImageResource(R.drawable.confirm_male);
                    }
                    ivNxt.setVisibility(View.GONE);
                    ivPrev.setVisibility(View.GONE);
                } else {
                    llRedBackgorund.setBackgroundResource(R.drawable.hy_bg);
                    ivNxt.setVisibility(View.VISIBLE);
                    ivPrev.setVisibility(View.VISIBLE);
                }
                refreshPageController(llIndicator, vpCustomViewPager);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hydrationMeterDO == null)
                            hydrationMeterDO = new HydrationMeterDO();
                        hydrationMeterDO.CustomerId = preference.getIntFromPreference(Preference.CUSTOMER_ID, 0);
                        hydrationMeterDO.DailyWaterConsumptionTarget = getSuggestedDrink(hydrationMeterDO.Weight) * 1000;
                         preference.saveIntInPreference(Preference.HYDRATION_TIME_PERIOD, hydrationMeterDO.timePeriod);
                         preference.saveBooleanInPreference(Preference.NOTIFICATION_FLAG, hydrationMeterDO.notifyMeTime);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (hydrationMeterDO.Id == 0)
                                    hydrationMeterDO.Id = 1;
                                new HydrationMeterDA(HydrationSettingActivity.this).insertHydrationMeterSettings(hydrationMeterDO);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(hydrationMeterDO.notifyMeTime) {
                                            Intent alarmIntent = new Intent(HydrationSettingActivity.this, HydrationMeterAlarmReceiver.class);
                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(HydrationSettingActivity.this, AppConstants.HYDRATION_NOTIFICATION, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, (new Date()).getTime(), hydrationMeterDO.timePeriod * (3600 * 1000), pendingIntent);
                                        }else{
                                            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                            Intent intent = new Intent(getBaseContext(), HydrationMeterAlarmReceiver.class);
                                            PendingIntent pIntent = PendingIntent.getBroadcast(HydrationSettingActivity.this,AppConstants.HYDRATION_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            aManager.cancel(pIntent);
                                        }
                                        finish();
                                    }
                                });
                            }
                        }).start();
                        new CommonBL(HydrationSettingActivity.this, HydrationSettingActivity.this).saveHydrationMeterSettings(hydrationMeterDO);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData() {

    }

    private void refreshPageController(LinearLayout llPagerTab,ViewPager pager)
    {
        llPagerTab.removeAllViews();
        for (int i = 0; i <= (hydrationSettingAdapter.getCount()-1); i++)
        {
            final ImageView imgvPagerController = new ImageView(HydrationSettingActivity.this);
            imgvPagerController.setPadding(0,0,10,0);
            imgvPagerController.setImageResource(R.drawable.hydra_page_un_ind);
            llPagerTab.addView(imgvPagerController);
        }
        ((ImageView)llPagerTab.getChildAt(pager.getCurrentItem())).setImageResource(R.drawable.hydra_page_ind);
    }

    @Override
    public boolean onLeftToRightSwipe() {
        return validate();
    }

    @Override
    public boolean onRightToLeftSwipe() {
        return validate();
    }

    public boolean validate(){
        int position = vpCustomViewPager.getCurrentItem();
        switch (position){
            case 0:
                if(hydrationMeterDO.Weight == 0) {
                    showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.weight_please), getResources().getString(R.string.OK), "", "");
                    return false;
                }
                else
                    return true;
            case 1:
                if(hydrationMeterDO.Height == 0) {
                    showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.height_please), getResources().getString(R.string.OK), "", "");
                    return false;
                }
                else
                    return true;
            case 2:
                if(hydrationMeterDO.age == 0) {
                    showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.age_please), getResources().getString(R.string.OK), "", "");
                    return false;
                }
                else
                    return true;
            case 3:
                return true;
            default:
                return true;
        }
    }

    public boolean validate(int position){
        if(hydrationMeterDO.Weight == 0) {
            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.weight_please), getResources().getString(R.string.OK), "", "Weight",false);
            return false;
        }else if(hydrationMeterDO.Weight < 30) {
            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.valid_weight), getResources().getString(R.string.OK), "", "Weight",false);
            return false;
        }else if(hydrationMeterDO.Height == 0) {
            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.height_please), getResources().getString(R.string.OK), "", "Height",false);
            return false;
        }
        else if(hydrationMeterDO.age == 0) {
            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.age_please), getResources().getString(R.string.OK), "", "Age",false);
            return false;
        }else  if(StringUtils.isEmpty(hydrationMeterDO.gender)) {
            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.gender_please), getResources().getString(R.string.OK), "", "Gender",false);
            return false;
        }
        return true;
    }

    private boolean isEdit = false;
/*
ujjwal suggestion no popup's
    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && response.method == ServiceMethods.WS_SAVE_HYDRATION_SETTINGS && response.data != null && response.data instanceof CommonResponseDO) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (hydrationMeterDO.Id == 0)
                        hydrationMeterDO.Id = 1;
                    new HydrationMeterDA(HydrationSettingActivity.this).insertHydrationMeterSettings(hydrationMeterDO);
                    hideLoader();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showCustomDialog(HydrationSettingActivity.this, "", getResources().getString(R.string.successfully_saved), getResources().getString(R.string.OK), "", "Save");
                        }
                    });
                }
            }).start();
        } else {
            hideLoader();
            showCustomDialog(HydrationSettingActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
        }
    }*/

    private double getSuggestedDrink(double weight){
        if(30 <= weight && weight < 37)
            return  1.8d;
        if(37 <= weight && weight < 40 )
            return  1.9d;
        if(40 <= weight && weight < 45 )
            return  2.0d ;
        if(45 <= weight && weight < 50  )
            return  2.1d;
        if(50 <= weight && weight < 58 )
            return   2.3d;
        if(58 <= weight && weight < 61  )
            return   2.4d;
        if(61 <= weight && weight < 64  )
            return  2.5d;
        if(64 <= weight && weight < 67 )
            return  2.6d;
        if(67 <= weight && weight < 70 )
            return   2.7d;
        if(70 <= weight && weight < 73 )
            return     2.8d;
        if(73 <= weight && weight < 76 )
            return     2.9d;
        if(76 <= weight && weight < 80 )
            return   3.0d;
        if(80 <= weight && weight < 83 )
            return   3.1d;
        if(83 <= weight && weight < 86    )
            return   3.2d;
        if(86 <= weight && weight < 90  )
            return   3.3d;
        if(90 <= weight && weight < 93   )
            return  3.4d;
        if(93 <= weight && weight < 96  )
            return   3.5d;
        if(96 <= weight && weight < 99 )
            return   3.6d;
        if(99 <= weight && weight < 101 )
            return   3.7d;
        if(101 <= weight && weight < 104    )
            return   3.8;
        if(104 <= weight && weight < 106 )
            return   3.9d;
        if(106 <= weight && weight < 108  )
            return   4.0d;
        if(108 < weight)
            return   4.1d;
        return 0.0;
    }

    class HydrationSettingAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            LinearLayout llPagerCell = (LinearLayout) inflater.inflate(R.layout.hydration_setting_cell_one, null);
            LinearLayout llInputs = (LinearLayout)llPagerCell.findViewById(R.id.llInputs);
            LinearLayout llWeightValue = (LinearLayout)llPagerCell.findViewById(R.id.llWeightValue);
            LinearLayout llHeightValue = (LinearLayout)llPagerCell.findViewById(R.id.llHeightValue);
            LinearLayout llAgeValue = (LinearLayout)llPagerCell.findViewById(R.id.llAgeValue);
            LinearLayout llGenderValue = (LinearLayout)llPagerCell.findViewById(R.id.llGenderValue);
            final LinearLayout llValue = (LinearLayout)llPagerCell.findViewById(R.id.llValue);
            LinearLayout llData =(LinearLayout)llPagerCell.findViewById(R.id.llData);
            TextView tvWeight = (TextView)llPagerCell.findViewById(R.id.tvWeight);
            TextView tvGender = (TextView)llPagerCell.findViewById(R.id.tvGender);
            TextView tvHeight = (TextView)llPagerCell.findViewById(R.id.tvHeight);
            TextView tvAge = (TextView)llPagerCell.findViewById(R.id.tvAge);
            TextView tvTitle = (TextView)llPagerCell.findViewById(R.id.tvTitle);
            TextView tvmesure = (TextView)llPagerCell.findViewById(R.id.tvmesure);
            final TextView tvValue = (TextView)llPagerCell.findViewById(R.id.tvValue);
            ImageView ivIcon = (ImageView)llPagerCell.findViewById(R.id.ivIcon);
            ImageView ivGender = (ImageView)llPagerCell.findViewById(R.id.ivGender);
            final TextView tvOff = (TextView)llPagerCell.findViewById(R.id.tvOff);
            final TextView tv1Hour = (TextView)llPagerCell.findViewById(R.id.tv1Hour);
            final TextView tv2Hours = (TextView)llPagerCell.findViewById(R.id.tv2Hours);
            final TextView tv4Hours = (TextView)llPagerCell.findViewById(R.id.tv4Hours);

            //Weight
            final LinearLayout llWeight =(LinearLayout)llPagerCell.findViewById(R.id.llWeight);
            final EditText etWeightOne = (EditText)llPagerCell.findViewById(R.id.etWeight_one);
            final EditText etWeightTwo = (EditText)llPagerCell.findViewById(R.id.etWeight_two);
            final EditText etWeightThree = (EditText)llPagerCell.findViewById(R.id.etWeight_three);
            final EditText etWeightFour = (EditText)llPagerCell.findViewById(R.id.etWeight_four);

            //Height
            final LinearLayout llHeight =(LinearLayout)llPagerCell.findViewById(R.id.llHeight);
            final EditText etHeigtOne = (EditText)llPagerCell.findViewById(R.id.etHeigtOne);
            final EditText etHeigtTwo = (EditText)llPagerCell.findViewById(R.id.etHeigtTwo);
            final EditText etHeigtThree = (EditText)llPagerCell.findViewById(R.id.etHeigtThree);
            final EditText etHeigtFour = (EditText)llPagerCell.findViewById(R.id.etHeigtFour);

            //Gender
            LinearLayout llGender =(LinearLayout)llPagerCell.findViewById(R.id.llGender);
            final TextView tvMale = (TextView)llPagerCell.findViewById(R.id.tvMale);
            final TextView tvFemale = (TextView)llPagerCell.findViewById(R.id.tvFemale);

            //Age
            final LinearLayout llAge =(LinearLayout)llPagerCell.findViewById(R.id.llAge);
            final EditText etYearOne = (EditText)llPagerCell.findViewById(R.id.etYearOne);
            final EditText etYearTwo = (EditText)llPagerCell.findViewById(R.id.etYearTwo);

            llPagerCell.setTag(position);

            tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            String weight = (hydrationMeterDO.Weight + "");
                            if (weight.contains("."))
                                weight = weight.replace(".", "");
                            char[] data = weight.toCharArray();
                            int j = 0;
                            for (int i = data.length - 1; i >= 0; i--) {
                                if (j == 0)
                                    etWeightFour.setText(data[i] + "");
                                else if (j == 1)
                                    etWeightThree.setText(data[i] + "");
                                else if (j == 2)
                                    etWeightTwo.setText(data[i] + "");
                                else if (j == 3)
                                    etWeightOne.setText(data[i] + "");
                                j++;
                            }
                            llValue.setVisibility(View.GONE);
                            llWeight.setVisibility(View.VISIBLE);
                            etWeightFour.setEnabled(true);
                            etWeightFour.setFocusable(true);
                            etWeightFour.setFocusableInTouchMode(true);
                            etWeightFour.requestFocus();
                            openKeyboard(etWeightFour);
                            break;
                        case 1:
                            String height = (hydrationMeterDO.Height + "");
                            String[] heights = height.split("\\.");
                            char[] heightData = null;
                            int k=0,i;
                            if(heights != null && heights.length >0)
                                if (heights[0] != null) {
                                    heightData = heights[0].toCharArray();
                                    for (i = heightData.length - 1; i >= 0; i--) {
                                        if (k == 0)
                                            etHeigtTwo.setText(heightData[i] + "");
                                        else if (k == 1)
                                            etHeigtOne.setText(heightData[i] + "");
                                        k++;
                                    }
                                }
                            if(heights != null && heights.length ==2)
                                if (heights[1] != null) {
                                    heightData = heights[1].toCharArray();
                                    for (i = 0; i < heightData.length; i++) {
                                        if (i == 0)
                                            etHeigtThree.setText(heightData[i] + "");
                                        else if (i == 1)
                                            etHeigtFour.setText(heightData[i] + "");
                                    }
                                }
                            llValue.setVisibility(View.GONE);
                            llHeight.setVisibility(View.VISIBLE);
                            etHeigtFour.setEnabled(true);
                            etHeigtFour.setFocusable(true);
                            etHeigtFour.setFocusableInTouchMode(true);
                            etHeigtFour.requestFocus();
                            openKeyboard(etHeigtFour);
                            break;
                        case 3:
                            String Age = (hydrationMeterDO.age + "");
                            char[] ageData = Age.toCharArray();
                            int l = 0;
                            for (int m = ageData.length - 1; m >= 0; m--) {
                                if (l == 0)
                                    etYearTwo.setText(ageData[m] + "");
                                else if (l == 1)
                                    etYearOne.setText(ageData[m] + "");
                                l++;
                            }
                            llValue.setVisibility(View.GONE);
                            llAge.setVisibility(View.VISIBLE);
                            etYearTwo.setEnabled(true);
                            etYearTwo.setFocusable(true);
                            etYearTwo.setFocusableInTouchMode(true);
                            etYearTwo.requestFocus();
                            openKeyboard(etYearTwo);
                            break;
                    }
                }
            });

            if(position == 4){
                llData.setVisibility(View.VISIBLE);
                llInputs.setVisibility(View.GONE);
                tvWeight.setText(hydrationMeterDO.Weight+"");
                tvHeight.setText(hydrationMeterDO.Height+"");
                tvAge.setText(hydrationMeterDO.age + "");
                if(hydrationMeterDO.gender.equalsIgnoreCase(HydrationMeterDO.FEMALE))
                {
                    ivGender.setImageResource(R.drawable.confirm_female);
                    tvGender.setText(getString(R.string.female));
                }
                else
                {
                    ivGender.setImageResource(R.drawable.confirm_male);
                    tvGender.setText(getString(R.string.male));
                }
                llWeightValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpCustomViewPager.setCurrentItem(0);
                    }
                });
                llHeightValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpCustomViewPager.setCurrentItem(1);
                    }
                });
                llGenderValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpCustomViewPager.setCurrentItem(2);
                    }
                });
                llAgeValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpCustomViewPager.setCurrentItem(3);
                    }
                });
                if(hydrationMeterDO.notifyMeTime){
                    switch (hydrationMeterDO.timePeriod){
                        case 1:
                            tv1Hour.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                            break;
                        case 2:
                            tv2Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                            break;
                        case 4:
                            tv4Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                            break;
                    }
                }else{
                    tvOff.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                }
                tvOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(HydrationSettingActivity.this,"adsfadsf",Toast.LENGTH_SHORT).show();
                        hydrationMeterDO.notifyMeTime = false;
                        hydrationMeterDO.timePeriod = 0;
                        tvOff.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                        tv1Hour.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv2Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv4Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                    }
                });
                tv1Hour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hydrationMeterDO.notifyMeTime = true;
                        hydrationMeterDO.timePeriod = 1;
                        tvOff.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv1Hour.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                        tv2Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv4Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                    }
                });
                tv2Hours.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hydrationMeterDO.notifyMeTime = true;
                        hydrationMeterDO.timePeriod = 2;
                        tvOff.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv1Hour.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv2Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                        tv4Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                    }
                });
                tv4Hours.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hydrationMeterDO.notifyMeTime = true;
                        hydrationMeterDO.timePeriod = 3;
                        tvOff.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv1Hour.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv2Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.hydra_balck));
                        tv4Hours.setTextColor(ContextCompat.getColor(HydrationSettingActivity.this, R.color.dark_red2));
                    }
                });

            }else {
                llData.setVisibility(View.GONE);
                llInputs.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        tvTitle.setText(getString(R.string.weight));
                        tvValue.setText(hydrationMeterDO.Weight + "");
                        ivIcon.setImageResource(R.drawable.hydra_weight);
                        tvmesure.setText("Kgs");
                        if(hydrationMeterDO.Weight != 0){
                            llValue.setVisibility(View.VISIBLE);
                            llWeight.setVisibility(View.GONE);
                        }else{
                            llWeight.setVisibility(View.VISIBLE);
                            llValue.setVisibility(View.GONE);
                        }
                        etWeightOne.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length() == 0) {
                                    hydrationMeterDO.strWeight[0] = 0;
                                    etWeightOne.requestFocus();
                                } else {
                                    hydrationMeterDO.strWeight[0] = data.toCharArray()[0];
                                    etWeightOne.clearFocus();
                                    etWeightTwo.requestFocus();
                                    etWeightTwo.setCursorVisible(true);
                                    etWeightTwo.setSelection(etWeightTwo.getText().length());
                                }
                            }
                        });
//first close
                        etWeightTwo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length() == 0) {
                                    hydrationMeterDO.strWeight[1] = 0;
                                    etWeightTwo.requestFocus();
                                } else {
                                    etWeightTwo.clearFocus();
                                    hydrationMeterDO.strWeight[1] = data.toCharArray()[0];
                                    etWeightThree.requestFocus();
                                    etWeightThree.setCursorVisible(true);
                                    etWeightThree.setSelection(etWeightThree.getText().length());
                                }
                            }
                        });
                        //third edit
                        etWeightThree.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length() == 0) {
                                    hydrationMeterDO.strWeight[2] = 0;
                                    etWeightThree.requestFocus();
                                } else {
                                    etWeightThree.clearFocus();
                                    hydrationMeterDO.strWeight[2] = data.toCharArray()[0];
                                    etWeightFour.requestFocus();
                                    etWeightFour.setCursorVisible(true);
                                    etWeightFour.setSelection(etWeightFour.getText().length());
                                }
                            }
                        });

                        etWeightFour.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (s.toString().length() == 0) {
                                    hydrationMeterDO.strWeight[4] = 0;
                                    etWeightFour.requestFocus();
                                } else {
                                    hydrationMeterDO.strWeight[4] = data.toCharArray()[0];
                                }
                            }
                        });

                        etWeightFour.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double weight = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strWeight));
                                        if (weight != 0) {
                                            hydrationMeterDO.Weight = weight;
                                            tvValue.setText(weight + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llWeight.setVisibility(View.GONE);
                                        } else {
                                            llWeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if(etWeightFour.getText().length()==0){
                                            etWeightThree.requestFocus();
                                            etWeightThree.setCursorVisible(true);
                                            etWeightThree.setSelection(etWeightThree.getText().length());
                                        }

                                    }
                                }
                                return false;
                            }
                        });
                        etWeightThree.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double weight = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strWeight));
                                        if (weight != 0) {
                                            hydrationMeterDO.Weight = weight;
                                            tvValue.setText(weight + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llWeight.setVisibility(View.GONE);
                                        } else {
                                            llWeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if(etWeightThree.getText().length()==0){
                                            etWeightTwo.requestFocus();
                                            etWeightTwo.setCursorVisible(true);
                                            etWeightTwo.setSelection(etWeightTwo.getText().length());
                                        }

                                    }
                                }
                                return false;
                            }
                        });
                        etWeightTwo.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double weight = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strWeight));
                                        if (weight != 0) {
                                            hydrationMeterDO.Weight = weight;
                                            tvValue.setText(weight + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llWeight.setVisibility(View.GONE);
                                        } else {
                                            llWeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if(etWeightTwo.getText().length()==0){
                                            etWeightOne.requestFocus();
                                            etWeightOne.setCursorVisible(true);
                                            etWeightOne.setSelection(etWeightOne.getText().length());
                                        }

                                    }
                                }
                                return false;
                            }
                        });
                        etWeightOne.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double weight = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strWeight));
                                        if (weight != 0) {
                                            hydrationMeterDO.Weight = weight;
                                            tvValue.setText(weight + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llWeight.setVisibility(View.GONE);
                                        } else {
                                            llWeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                return false;
                            }
                        });
                        break;
                    case 1:
                        tvTitle.setText(getString(R.string.height));
                        tvValue.setText(hydrationMeterDO.Height + "");
                        ivIcon.setImageResource(R.drawable.hydra_height);
                        tvmesure.setText(getString(R.string.Meters));
                        if(hydrationMeterDO.Height != 0){
                            llValue.setVisibility(View.VISIBLE);
                            llHeight.setVisibility(View.GONE);
                        }else{
                            llHeight.setVisibility(View.VISIBLE);
                            llValue.setVisibility(View.GONE);
                        }
                        etHeigtOne.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if(data.length()==0)
                                {
                                    hydrationMeterDO.strHeight[0]= 0;
                                    etHeigtOne.requestFocus();
                                }else{
                                    hydrationMeterDO.strHeight[0]=data.toCharArray()[0];
                                    etHeigtOne.clearFocus();
                                    etHeigtTwo.requestFocus();
                                    etHeigtTwo.setCursorVisible(true);
                                    etHeigtTwo.setSelection(etHeigtTwo.getText().length());
                                }
                            }
                        });
//first close
                        etHeigtTwo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length()==0)
                                {
                                    hydrationMeterDO.strHeight[1]=0;
                                    etHeigtTwo.requestFocus();
                                }else{
                                    etHeigtTwo.clearFocus();
                                    hydrationMeterDO.strHeight[1]=data.toCharArray()[0];
                                    etHeigtThree.requestFocus();
                                    etHeigtThree.setCursorVisible(true);
                                    etHeigtThree.setSelection(etHeigtThree.getText().length());
                                }
                            }
                        });
                        //third edit
                        etHeigtThree.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length() == 0) {
                                    hydrationMeterDO.strHeight[3] = 0;
                                    etHeigtThree.requestFocus();
                                } else {
                                    etHeigtThree.clearFocus();
                                    hydrationMeterDO.strHeight[3] = data.toCharArray()[0];
                                    etHeigtFour.requestFocus();
                                    etHeigtFour.setCursorVisible(true);
                                    etHeigtFour.setSelection(etHeigtFour.getText().length());
                                }
                            }
                        });

                        etHeigtFour.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (s.toString().length() == 0) {
                                    hydrationMeterDO.strHeight[4] = 0;
                                    etHeigtFour.requestFocus();
                                } else {
                                    hydrationMeterDO.strHeight[4] = data.toCharArray()[0];
                                }
                            }
                        });


                        etHeigtFour.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double height = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strHeight));
                                        if (height != 0) {
                                            hydrationMeterDO.Height = height;
                                            tvValue.setText(height + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llHeight.setVisibility(View.GONE);
                                        } else {
                                            llHeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                    if (etHeigtFour.getText().length() == 0) {
                                        etHeigtThree.requestFocus();
                                        etHeigtThree.setCursorVisible(true);
                                        etHeigtThree.setSelection(etHeigtThree.getText().length());
                                    }

                                }
                                return false;
                            }
                        });
                        etHeigtThree.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double height = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strHeight));
                                        if (height != 0) {
                                            hydrationMeterDO.Height = height;
                                            tvValue.setText(height + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llHeight.setVisibility(View.GONE);
                                        } else {
                                            llHeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if (etHeigtThree.getText().length() == 0) {
                                            etHeigtTwo.requestFocus();
                                            etHeigtTwo.setCursorVisible(true);
                                            etHeigtTwo.setSelection(etHeigtTwo.getText().length());
                                        }

                                    }
                                }
                                return false;
                            }
                        });
                        etHeigtTwo.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double height = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strHeight));
                                        if (height != 0) {
                                            hydrationMeterDO.Height = height;
                                            tvValue.setText(height + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llHeight.setVisibility(View.GONE);
                                        } else {
                                            llHeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if (etHeigtTwo.getText().length() == 0) {
                                            etHeigtOne.requestFocus();
                                            etHeigtOne.setCursorVisible(true);
                                            etHeigtOne.setSelection(etHeigtOne.getText().length());
                                        }

                                    }
                                }
                                return false;
                            }
                        });
                        etHeigtOne.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        double height = StringUtils.getDouble(String.valueOf(hydrationMeterDO.strHeight));
                                        if (height != 0) {
                                            hydrationMeterDO.Height = height;
                                            tvValue.setText(height + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llHeight.setVisibility(View.GONE);
                                        } else {
                                            llHeight.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                return false;
                            }
                        });
                        break;
                    case 2:
                        tvTitle.setText(getString(R.string.gender));
                        llValue.setVisibility(View.GONE);
                        llGender.setVisibility(View.VISIBLE);
                        ivIcon.setImageResource(R.drawable.hyra_gender);
                        if(hydrationMeterDO.gender.equalsIgnoreCase(HydrationMeterDO.MALE)) {
                            tvMale.setTextColor(getResources().getColor(R.color.dark_red2));
                            tvFemale.setTextColor(getResources().getColor(R.color.light_gen_text));
                            tvMale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_male, 0, 0);
                            tvFemale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_unfamle, 0, 0);
                        }else if(hydrationMeterDO.gender.equalsIgnoreCase(HydrationMeterDO.FEMALE)){
                            tvFemale.setTextColor(getResources().getColor(R.color.dark_red2));
                            tvMale.setTextColor(getResources().getColor(R.color.light_gen_text));
                            tvFemale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydr_female, 0, 0);
                            tvMale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_unmale, 0, 0);
                        }
                        tvMale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hydrationMeterDO.gender = HydrationMeterDO.MALE;
                                tvMale.setTextColor(getResources().getColor(R.color.dark_red2));
                                tvFemale.setTextColor(getResources().getColor(R.color.light_gen_text));
                                tvMale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_male, 0, 0);
                                tvFemale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_unfamle, 0, 0);
                            }
                        });
                        tvFemale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hydrationMeterDO.gender = HydrationMeterDO.FEMALE;
                                tvFemale.setTextColor(getResources().getColor(R.color.dark_red2));
                                tvMale.setTextColor(getResources().getColor(R.color.light_gen_text));
                                tvFemale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydr_female, 0, 0);
                                tvMale.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hydra_unmale, 0, 0);
                            }
                        });
                        break;
                    case 3:
                        tvmesure.setText(getString(R.string.Years));
                        tvTitle.setText(getString(R.string.age));
                        tvValue.setText(hydrationMeterDO.age + "");
                        ivIcon.setImageResource(R.drawable.confirm_age);
                        if(hydrationMeterDO.age != 0){
                            llValue.setVisibility(View.VISIBLE);
                            llAge.setVisibility(View.GONE);
                        }else{
                            llValue.setVisibility(View.GONE);
                            llAge.setVisibility(View.VISIBLE);
                        }
                        etYearOne.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (data.length() == 0) {
                                    etYearOne.requestFocus();
                                    hydrationMeterDO.strAge[0] = 0;
                                } else {
                                    hydrationMeterDO.strAge[0]=data.toCharArray()[0];
                                    etYearOne.clearFocus();
                                    etYearTwo.requestFocus();
                                    etYearTwo.setCursorVisible(true);
                                    etYearTwo.setSelection(etYearTwo.getText().length());
                                }
                            }
                        });
                        etYearTwo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String data = s.toString();
                                if (s.toString().length() == 0) {
                                    hydrationMeterDO.strAge[1] = 0;
                                }else{
                                    hydrationMeterDO.strAge[1] = data.toCharArray()[0];
                                }
                            }
                        });
                        etYearTwo.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        int age = StringUtils.getInt(String.valueOf(hydrationMeterDO.strAge));
                                        if (age != 0) {
                                            hydrationMeterDO.age = age;
                                            tvValue.setText(age + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llAge.setVisibility(View.GONE);
                                        } else {
                                            llAge.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }else if (keyCode == KeyEvent.KEYCODE_DEL) {
                                        if (etYearTwo.getText().length() == 0) {
                                            etYearOne.requestFocus();
                                            etYearOne.setCursorVisible(true);
                                            etYearOne.setSelection(etYearOne.getText().length());
                                        }
                                    }
                                }
                                return false;
                            }
                        });
                        etYearOne.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                        int age = StringUtils.getInt(String.valueOf(hydrationMeterDO.strAge));
                                        if (age != 0) {
                                            hydrationMeterDO.age = age;
                                            tvValue.setText(age + "");
                                            llValue.setVisibility(View.VISIBLE);
                                            llAge.setVisibility(View.GONE);
                                        } else {
                                            llAge.setVisibility(View.VISIBLE);
                                            llValue.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                return false;
                            }
                        });
                        break;
                }
            }
            container.addView(llPagerCell);
            return llPagerCell;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((LinearLayout) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Weight")){
            vpCustomViewPager.setCurrentItem(0);
        }else if(from.equalsIgnoreCase("Height")){
            vpCustomViewPager.setCurrentItem(1);
        }else if(from.equalsIgnoreCase("Age")){
            vpCustomViewPager.setCurrentItem(3);
        }else if(from.equalsIgnoreCase("Gender")){
            vpCustomViewPager.setCurrentItem(2);
        }else if (from.equalsIgnoreCase("Save"))
            finish();
        else if (from.equalsIgnoreCase("You Drink")){
            showLoader(getString(R.string.save_settings));
            hydrationMeterDO.DailyWaterConsumptionTarget = (hydrationMeterDO.DailyWaterConsumptionTarget) * 1000;
            new CommonBL(HydrationSettingActivity.this, HydrationSettingActivity.this).saveHydrationMeterSettings(hydrationMeterDO);
        }
    }
}
