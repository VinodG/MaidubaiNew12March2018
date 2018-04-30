package com.winit.maidubai;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.maidubai.adapter.WeatherAdapter;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.webaccessLayer.WeatherServices;
import com.winit.maidubai.dataobject.Weather;
import com.winit.maidubai.utilities.CalendarUtil;

import java.util.ArrayList;
import java.util.Locale;

public class WeatherActivity extends BaseActivity {
    private LinearLayout llMain;
    private ListView lvWeather;
    public ArrayList<Weather> weathers;
    WeatherAdapter adapter;
    // String apiUrlIcon = "http://openweathermap.org/img/w/";
    private TextView tvCityName, tvCurrentDate, tvTemperature;
    ImageView ivTempIcon;

    @Override
    public void initialise() {
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        llBase.setBackgroundResource(R.drawable.weather_bg);
        llBody.setBackgroundResource(0);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.weather);
        rlCArt.setVisibility(View.GONE);
        ivCancel.setVisibility(View.VISIBLE);
        tvCArtCount.setVisibility(View.GONE);
        ivCancel.setImageResource(R.drawable.search);
        lockDrawer("WeatherActivity");
        tvCArtCount.setVisibility(View.GONE);
        setStatusBarColor();

        llMain = (LinearLayout) inflater.inflate(R.layout.weather_layout, null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(llMain, param);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                Intent intent = new Intent(WeatherActivity.this, SearchCityActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
        //  weathers = new ArrayList<Weather>();
        setTypeFaceNormal(llMain);
        initialiseControls();
        loadData();
    }


    @Override
    public void initialiseControls() {
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvCurrentDate = (TextView) findViewById(R.id.tvCurrentDate);
        tvTemperature = (TextView) findViewById(R.id.tvTemprature);
        ivTempIcon = (ImageView) findViewById(R.id.ivTempIcon);
        tvCurrentDate.setText(CalendarUtil.getTodayDate(Locale.getDefault()));

        tvCityName.setText(preference.getStringFromPreference(Preference.CityName, "") + "");

        //tvTemperature.setText(preference.getIntFromPreference(Preference.TEMPERATURE, 0) + "");

        tvCityName.setTypeface(AppConstants.DinproRegular);
        tvCurrentDate.setTypeface(AppConstants.DinproRegular);
        tvTemperature.setTypeface(AppConstants.DinproLight);

        lvWeather = (ListView) findViewById(R.id.lvWeather);
        adapter = new WeatherAdapter(WeatherActivity.this, weathers);
        lvWeather.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        double latitide = preference.getDoubleFromPreference(Preference.Latitude,0);
        double longitude = preference.getDoubleFromPreference(Preference.Longitude,0);
        updateData(latitide,longitude);
//        updateData(preference.getStringFromPreference(Preference.CityName, "") + "");
    }

    public void updateData(final double latitude,final double longitude) {
        showLoader("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(checkNetworkConnection()){
                    weathers = new WeatherServices().getWeatherData(latitude, longitude, AppConstants.WEEK_WEHEATER_TYPE);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if (weathers != null && weathers.size() > 0) {
                            Weather weather = weathers.get(0);
                            tvTemperature.setText(String.format(Locale.ENGLISH,"%.0f", weather.temperature));
                            ivTempIcon.setImageResource(weather.iconId);
                            weathers.remove(0);
                            adapter.refresh(weathers);
                        } else {
                            showCustomDialog(WeatherActivity.this,"",getString(R.string.check_network),getString(R.string.OK),"",AppConstants.INTERNET_WARNING,false);
                        }
                    }
                });
            }
        }).start();
    }

    public void updateData(final String city) {
        showLoader("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(checkNetworkConnection()){
                    weathers = new WeatherServices().updateWeatherData(city, AppConstants.WEEK_WEHEATER_TYPE);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if (weathers != null && weathers.size() > 0) {
                            Weather weather = weathers.get(0);
                            tvTemperature.setText(String.format("%.0f", weather.temperature));
                            preference.saveIntInPreference(Preference.TEMPERATURE, Integer.parseInt((String.format("%.0f", weather.temperature))));
                            preference.saveIntInPreference(Preference.TEMPERATURE_ICON, weather.iconId);
                            ivTempIcon.setImageResource(weather.iconId);
                            weathers.remove(0);
                            adapter.refresh(weathers);
                        } else {
                           showCustomDialog(WeatherActivity.this,"",getString(R.string.check_network),getString(R.string.OK),"",AppConstants.INTERNET_WARNING,false);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            String cityName = data.getStringExtra("cityName");
            double latitude = data.getDoubleExtra("Latitude", 0);
            double Longitude = data.getDoubleExtra("Longitude", 0);
            tvCityName.setText(cityName);
            updateData(latitude,Longitude);
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase(AppConstants.INTERNET_ERROR) || from.equalsIgnoreCase(AppConstants.INTERNET_WARNING)){
            finish();
        }
    }
}
