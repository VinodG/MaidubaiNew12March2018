package com.winit.maidubai;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.winit.maidubai.adapter.BtlGridAdapter;
import com.winit.maidubai.adapter.BtlListAdapter;
import com.winit.maidubai.adapter.DashboardPagerAdapter;
import com.winit.maidubai.adapter.ProductAttributeAdapter;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.LocationAddress;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CartDA;
import com.winit.maidubai.dataaccesslayer.ProductDA;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.CartResponseDO;
import com.winit.maidubai.dataobject.CategoryDO;
import com.winit.maidubai.dataobject.ProductAttribute;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.Weather;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;
import com.winit.maidubai.webaccessLayer.ServiceUrls;
import com.winit.maidubai.webaccessLayer.WeatherServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DashBoardActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout llMain, llWeather, llPreviosOrder, llTemp;
    RelativeLayout rlViewPager;
    private ViewPager viewPager;
    private TextView tvBtlSize, btlRate, tvTemperature, tvDisplayOptions, tvWhetherDate, tvOrder, tvPrivious, tvstatus, tvBtlSubSize;
    private GridView gvBtlDesc;
    private ListView lvBtl;
    private int selectedCategory, selPagerPostion = 0;
    private ImageView ivGoToCart;
    private ImageView ivPlaceYrOrder, ivVan_horizontal, ivPagerIcon, ivListIcon, ivGridIcon, ivTempIcon;
    boolean isFirst,isShowDeliveryPopup;
    private ArrayList<ProductDO> arrProductDOs;

    private DashboardPagerAdapter dashboardPagerAdapter;
    private BtlListAdapter listadapter;
    private BtlGridAdapter gridadapter;

    private GoogleApiClient apiClient;
    private LocationRequest mLocationRequest;
    private Location _currentLocation;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //    private GPSLocationService gpsLocationService;
    private Location location;
    private HashMap<String, CartListDO> hmCartList;
    private HashMap<Integer, CategoryDO> hmCategories;

    @Override
    public void initialise() {

        llBase.setBackgroundResource(R.drawable.dashboard_bg3x1);
        rlCArt.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.VISIBLE);
        llToggle.setVisibility(View.VISIBLE);
        llToggleLeft.setBackgroundResource(R.drawable.toggle_select);
        llToggleRight.setBackgroundResource(R.drawable.toggle_unselect);
        tvToggleLeft.setText(getString(R.string.water_range));
        tvToggleLeft.setTypeface(AppConstants.DinproMedium);
        tvToggleRight.setText(getString(R.string.accessories));
        tvToggleLeft.setTextColor(getResources().getColor(R.color.white));
        tvToggleRight.setTextColor(getResources().getColor(R.color.light_weight));
        llMainCenter.setVisibility(View.GONE);

//        gpsLocationService = new GPSLocationService(DashBoardActivity.this);
        llBody.setBackgroundResource(0);
        llMain = (LinearLayout) inflater.inflate(R.layout.activity_dash_board, null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(llMain, param);
        setTypeFaceNormal(llMain);
        initialiseControls();
        setStatusBarColor();

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());
        viewPager.setPageMargin((int) (-1 * px));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipChildren(false);
        viewPager.setClipToPadding(false);
        isShowDeliveryPopup = getIntent().getBooleanExtra("ShowDeliveryPopup",false);
        apiClient = new GoogleApiClient.Builder(DashBoardActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        dashboardPagerAdapter = new DashboardPagerAdapter(this, arrProductDOs);
        viewPager.setAdapter(dashboardPagerAdapter);

        listadapter = new BtlListAdapter(DashBoardActivity.this, arrProductDOs);
        lvBtl.setAdapter(listadapter);

        gridadapter = new BtlGridAdapter(DashBoardActivity.this, arrProductDOs);
        gvBtlDesc.setAdapter(gridadapter);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View page, float position) {
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String name = "";
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(arrProductDOs.get(position).AltDescription))
                {
                    name = arrProductDOs.get(position).AltDescription;
                    tvBtlSize.setText(name);
                }else
                {
                    name = arrProductDOs.get(position).name;
                    setName(name);
                }
                btlRate.setText(getResources().getString(R.string.currency) + arrProductDOs.get(position).price);
                selPagerPostion = position;
                setCartButtonOnBtlSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        ivPagerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPagerIcon.setImageResource(R.drawable.pager_select);
                ivListIcon.setImageResource(R.drawable.list);
                ivGridIcon.setImageResource(R.drawable.grid);

                lvBtl.setVisibility(View.GONE);
                gvBtlDesc.setVisibility(View.GONE);
                rlViewPager.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(1);
                String name = "";
                if(arrProductDOs != null) {
                    if (preference.getStringFromPreference(Preference.LANGUAGE, "").equalsIgnoreCase("ar") && !StringUtils.isEmpty(arrProductDOs.get(1).AltDescription)) {
                        name = arrProductDOs.get(1).AltDescription;
                    } else {
                        name = arrProductDOs.get(1).name;
                    }
                }

                setName(name);
                btlRate.setText(getResources().getString(R.string.currency)+ arrProductDOs.get(1).price);

            }
        });
        ivListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPagerIcon.setImageResource(R.drawable.prview_unselect);
                ivListIcon.setImageResource(R.drawable.list_select);
                ivGridIcon.setImageResource(R.drawable.grid);

                rlViewPager.setVisibility(View.GONE);
                gvBtlDesc.setVisibility(View.GONE);
                lvBtl.setVisibility(View.VISIBLE);

            }
        });
        ivGridIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPagerIcon.setImageResource(R.drawable.prview_unselect);
                ivListIcon.setImageResource(R.drawable.list);
                ivGridIcon.setImageResource(R.drawable.select_grid);

                lvBtl.setVisibility(View.GONE);
                gvBtlDesc.setVisibility(View.VISIBLE);
                rlViewPager.setVisibility(View.GONE);

            }
        });

        llWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
        isFirst = preference.getbooleanFromPreference("FIRST_TIME_DISPLAY", true);
        if (isFirst) {
            String userName = preference.getStringFromPreference(Preference.CUSTOMER_NAME, "Mai Dubai");
            String emailId = preference.getStringFromPreference(Preference.EMAIL_ID, "MaiDubai@gmail.com");
           /* VisitorInfo visitorInfo = new VisitorInfo.Builder()
                    .name(userName)
                    .email(emailId)
                    .build();
            ZopimChat.setVisitorInfo(visitorInfo);*/
            ivFirstTime.setVisibility(View.VISIBLE);
        }
        ivFirstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFirstTime.setVisibility(View.GONE);
                preference.saveBooleanInPreference("FIRST_TIME_DISPLAY", false);
                boolean flag = preference.getbooleanFromPreference(Preference.SHOWDELIVERY,true);
                if(flag) {
                    showDeliveryDialog();
                }
            }
        });
        llPreviosOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });
        selPagerPostion = 1;
        viewPager.setCurrentItem(1);

        llToggleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llToggleLeft.setEnabled(false);
                llToggleLeft.setClickable(false);
                llToggleRight.setEnabled(true);
                llToggleRight.setClickable(true);
                llToggleLeft.setBackgroundResource(R.drawable.toggle_select);
                llToggleRight.setBackgroundResource(R.drawable.toggle_unselect);
                tvToggleLeft.setTextColor(getResources().getColor(R.color.white));
                tvToggleRight.setTextColor(getResources().getColor(R.color.light_weight));
                selectedCategory = CategoryDO.WATER_RANGE;
                if (hmCategories != null && hmCategories.size() > 0) {
                    arrProductDOs = hmCategories.get(selectedCategory).arrProductDOs;
                    if (dashboardPagerAdapter != null)
                        dashboardPagerAdapter.refresh(arrProductDOs);
                    if (listadapter != null)
                        listadapter.refresh(arrProductDOs);
                    if (gridadapter != null)
                        gridadapter.refresh(arrProductDOs);
                    String name = "";
                    if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(arrProductDOs.get(selPagerPostion).AltDescription))
                    {
                        name = arrProductDOs.get(selPagerPostion).AltDescription;
                    }else
                    {
                        name = arrProductDOs.get(selPagerPostion).name;
                    }

                    setName(name);
                    btlRate.setText(getResources().getString(R.string.currency) + arrProductDOs.get(selPagerPostion).price);
                }

            }
        });

        llToggleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llToggleLeft.setEnabled(true);
                llToggleLeft.setClickable(true);
                llToggleRight.setEnabled(false);
                llToggleRight.setClickable(false);
                llToggleLeft.setBackgroundResource(R.drawable.toggle_unselect_left);
                llToggleRight.setBackgroundResource(R.drawable.toggle_select_right);
                tvToggleRight.setTextColor(getResources().getColor(R.color.white));
                tvToggleLeft.setTextColor(getResources().getColor(R.color.light_weight));
                selectedCategory = CategoryDO.ACCESSORIES;
                if (hmCategories != null && hmCategories.size() > 0) {
                    arrProductDOs = hmCategories.get(selectedCategory).arrProductDOs;

                    if (dashboardPagerAdapter != null)
                        dashboardPagerAdapter.refresh(arrProductDOs);
                    if (listadapter != null)
                        listadapter.refresh(arrProductDOs);
                    if (gridadapter != null)
                        gridadapter.refresh(arrProductDOs);
                    String name = "";
                    if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(arrProductDOs.get(selPagerPostion).AltDescription))
                    {
                        name = arrProductDOs.get(selPagerPostion).AltDescription;
                    }else
                    {
                        name = arrProductDOs.get(selPagerPostion).name;
                    }

                    setName(name);

                    btlRate.setText(getResources().getString(R.string.currency) + arrProductDOs.get(selPagerPostion).price);
                }
            }
        });

        boolean flag = preference.getbooleanFromPreference(Preference.SHOWDELIVERY,true);
        if(isShowDeliveryPopup && flag && !isFirst) {
            showDeliveryDialog();
        }
    }

    private void setName(String name) {
        tvBtlSubSize.setVisibility(View.GONE);

        if (name.contains("(")) {
            tvBtlSize.setText(Html.fromHtml(name.substring(0, name.indexOf("("))+" <small>"+name.substring(name.indexOf("("),name.lastIndexOf(")")+1)));
           /* tvBtlSize.setText(name.substring(0, name.indexOf("(")));
            tvBtlSubSize.setText(name.substring(name.indexOf("("), name.lastIndexOf(")") + 1));*/
        } else if (name.contains("-")) {
            tvBtlSize.setText(Html.fromHtml(name.substring(0, name.indexOf("-")) + " <small>" + name.substring(name.indexOf("-"),name.length())));
            /*tvBtlSize.setText(name.substring(0, name.indexOf("-")));
            tvBtlSubSize.setText(name.substring(name.indexOf("-"), name.length()));*/
        } else {
            tvBtlSize.setText(name);
            //tvBtlSubSize.setText("");
        }
    }

    @Override
    public void initialiseControls() {

        llPreviosOrder = (LinearLayout) findViewById(R.id.llPreviosOrder);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tvBtlSize = (TextView) findViewById(R.id.tvBtlSize);
        tvBtlSubSize = (TextView) findViewById(R.id.tvBtlSubSize);
        tvDisplayOptions = (TextView) findViewById(R.id.tvDisplayOptions);
        tvWhetherDate = (TextView) findViewById(R.id.tvWhetherDate);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        llTemp = (LinearLayout) findViewById(R.id.llTemp);
        btlRate = (TextView) findViewById(R.id.tvRate);
        tvPrivious = (TextView) findViewById(R.id.tvPrivious);
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvstatus = (TextView) findViewById(R.id.tvstatus);
        gvBtlDesc = (GridView) findViewById(R.id.gvBtlDesc);
        lvBtl = (ListView) findViewById(R.id.lvBtl);
        rlViewPager = (RelativeLayout) findViewById(R.id.rlViewPager);
        llWeather = (LinearLayout) findViewById(R.id.llWeather);
        ivPlaceYrOrder = (ImageView) llMain.findViewById(R.id.ivPlaceYrOrder);
        ivTempIcon = (ImageView) llMain.findViewById(R.id.ivTempIcon);
        ivVan_horizontal = (ImageView) llMain.findViewById(R.id.ivVan_horizontal);
        ivGoToCart = (ImageView) llMain.findViewById(R.id.ivGoToCart);
        ivPagerIcon = (ImageView) findViewById(R.id.ivPagerIcon);
        ivListIcon = (ImageView) findViewById(R.id.ivListIcon);
        ivGridIcon = (ImageView) findViewById(R.id.ivGridIcon);

        tvDisplayOptions.setTypeface(AppConstants.DinproMedium);
        tvWhetherDate.setTypeface(AppConstants.DinproMedium);
        tvstatus.setTypeface(AppConstants.DinproMedium);
        tvPrivious.setTypeface(AppConstants.DinproMedium);
        tvTemperature.setTypeface(AppConstants.DinproMedium);
        tvWhetherDate.setTypeface(AppConstants.DinproMedium);
        tvOrder.setTypeface(AppConstants.DinproMedium);
        String date = CalendarUtil.getTodayDate(Locale.getDefault());

        tvWhetherDate.setText(date.substring(0, date.length() - 6));
        btlRate.setTypeface(AppConstants.DinproBold);
        tvOrder.setTypeface(AppConstants.DinproBold);
        setTypeFaceNormal(llTemp);

        selectedCategory = CategoryDO.WATER_RANGE;

        ivPlaceYrOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("item", arrProductDOs.get(selPagerPostion));
                bundle.putInt("position", selPagerPostion);
                BottomSheetDialogFragment bottomSheetDialogFragment = new OrderQuantityBottomSheet();
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        ivGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, MyBasketActivity.class);
                startActivity(intent);
            }
        });
    }

    private void RequestLocationUpdates() {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if (apiClient != null && apiClient.isConnected()) {
            if (_currentLocation == null)
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
        }
    }

    public void getTemperature() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (_currentLocation != null) {
                    final double latitude = _currentLocation.getLatitude();
                    final double longitude = _currentLocation.getLongitude();
//                    final double latitude = 17.4570916;
//                    final double longitude = 78.3650631;
                    LocationAddress locationAddress = new LocationAddress();
                    AddressBookDO addressBookDO = locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext());
                    if (addressBookDO != null && checkNetworkConnection()) {
                        WeatherServices weatherServices = new WeatherServices();
                        ArrayList<Weather> arrCityWeather = weatherServices.getWeatherData(latitude, longitude, AppConstants.DAY_WEHEATER_TYPE);
                        if (arrCityWeather != null && arrCityWeather.size() > 0) {
                            Weather dayWeather = arrCityWeather.get(0);
                            preference.saveStringInPreference(Preference.CityName, addressBookDO.city);
//                                preference.saveStringInPreference(Preference.CityName, "Hyderabad");
                            final int temp = Integer.parseInt(String.format("%.0f", dayWeather.temperature));
                            preference.saveIntInPreference(Preference.TEMPERATURE, temp);
                            preference.saveDoubleInPreference(Preference.Latitude, latitude + "");
                            preference.saveDoubleInPreference(Preference.Longitude, longitude + "");
                            preference.saveIntInPreference(Preference.TEMPERATURE_ICON, dayWeather.iconId);
                            if (temp == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showSnackMessage("Unable to fetch temperature.");
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTemperature.setText(temp + "");
                                        ivTempIcon.setImageResource(preference.getIntFromPreference(Preference.TEMPERATURE_ICON, 0));
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void loadData() {
        showLoader("loading data.....");
//        location = gpsLocationService.getLocation();
        int temperature = preference.getIntFromPreference(preference.TEMPERATURE, 0);
        tvTemperature.setText(temperature + "");
        ivTempIcon.setImageResource(preference.getIntFromPreference(Preference.TEMPERATURE_ICON, 0));
        if (_currentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            _currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        }
        if(_currentLocation != null)
            getTemperature();
        getTemperature();
        new Thread(new Runnable() {
            @Override
            public void run() {

                /*
                * need to be fetch cartList from database because
                * onResume every time should not do network operation.
                * And any change in the cartList will result in the database
                * */
                hmCartList = new CartDA(DashBoardActivity.this).getCartList();
                hmCategories = new ProductDA(DashBoardActivity.this).getProductsByCategory("");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if (hmCategories != null && hmCategories.size() > 0) {
                            arrProductDOs = hmCategories.get(selectedCategory).arrProductDOs;
                            dashboardPagerAdapter.refresh(arrProductDOs);
                            refreshAllList();
                            selPagerPostion = viewPager.getCurrentItem();
                            String name = "";
                            if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(arrProductDOs.get(selPagerPostion).AltDescription))
                            {
                                name = arrProductDOs.get(selPagerPostion).AltDescription;
                            }else
                            {
                                name = arrProductDOs.get(selPagerPostion).name;
                            }

                            setName(name);
                            btlRate.setText(getResources().getString(R.string.currency) + arrProductDOs.get(selPagerPostion).price);
                            setCartButtonOnBtlSelected(selPagerPostion);
                            updateCartCount();
                        }
                    }
                });
            }
        }).start();
    }


    public void showDeliveryDialog(){
        String deliveryDays = preference.getStringFromPreference(Preference.DELIVERY_DAYS, "");
        if(!preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("en"))
        {
            deliveryDays = getDelivarydatesArabic(deliveryDays);
        }
        String message = Html.fromHtml(getString(R.string.app_lauch_msg)+" "+"<trxDetailsDO.productALTDescription>"+deliveryDays+"</b>").toString();
        showCustomDialog(DashBoardActivity.this,"",message,getString(R.string.OK),getString(R.string.dnt_show_again),"DeliveryPopup");
    }

    @Override
    protected void onResume() {
        if(!apiClient.isConnected())
            apiClient.connect();
        super.onResume();
        boolean isSyncing = preference.getbooleanFromPreference(Preference.IS_SYNCING,false);
        if(isSyncing || !NetworkUtil.isNetworkConnectionAvailable(DashBoardActivity.this))
            loadData();
        else {
            refreshData(new SyncIntentService.SyncListener() {
                @Override
                public void onStart() {
                    showLoader("Syncing...");
                }

                @Override
                public void onError(String message) {
                    hideLoader();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    });
                }

                @Override
                public void onEnd() {
                    hideLoader();
                    updateUI();
                }
            });
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }

    public void showProductDescription(String name, String imageUrl, ArrayList<ProductAttribute> arrProductAttributes){
        View view = inflater.inflate(R.layout.dashboard_popup,null);
        ImageView ivclosePopup= (ImageView) view.findViewById(R.id.ivclosePopup);
        ImageView ivImage= (ImageView) view.findViewById(R.id.ivImage);
        TextView tvName= (TextView) view.findViewById(R.id.tvName);
        ListView lvAttribute = (ListView)view.findViewById(R.id.lvAttribute);
        lvAttribute.setAdapter(new ProductAttributeAdapter(DashBoardActivity.this, arrProductAttributes));
        final CustomDialog customDialog = new CustomDialog(DashBoardActivity.this, view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        customDialog.setCancelable(true);
        tvName.setTypeface(AppConstants.DinproRegular);

        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
        {
            tvName.setText(name);
        }
        else
        {

            if(name.contains("(")) {
                tvName.setText(Html.fromHtml(name.substring(0, name.indexOf("(")) + " <small>" + name.substring(name.indexOf("("), name.lastIndexOf(")") + 1)));
//            holder.btlSize.setText(Html.fromHtml() name.substring(0, name.indexOf("(")));
//            holder.tvBtlSubSize.setText(name.substring(name.indexOf("("),name.lastIndexOf(")")+1));
            }else if(name.contains("-")){

                tvName.setText(Html.fromHtml(name.substring(0, name.indexOf("-")) + " <small>" + name.substring(name.indexOf("-"), name.length())));
//           holder.btlSize.setText(name.substring(0,name.indexOf("-")));
//            holder.tvBtlSubSize.setText(name.substring(name.indexOf("-"),name.length()));
            }else{
                tvName.setText(name);
            }
        }

        Picasso.with(DashBoardActivity.this).load(ServiceUrls.MAIN_URL + imageUrl)
                .error(R.drawable.no_image)
                .into(ivImage);
        ivclosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        if(customDialog != null && !customDialog.isShowing())
        {
            customDialog.show();
        }
    }

    public void startVan(final int position,final ProductDO productDO){
        if(rlViewPager.getVisibility() == View.VISIBLE) {
            int width = preference.getIntFromPreference(Preference.DEVICE_DISPLAY_WIDTH, 0);

            int[] coordinates = new int[2];
            ivVan_horizontal.getLocationOnScreen(coordinates);
            float toXDelta = 0.0f;
            if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("en"))
                toXDelta = width-(ivVan_horizontal.getRight()+10);
            else {
                toXDelta = - (ivVan_horizontal.getLeft() - 10);
            }

            TranslateAnimation translateAnimation = new TranslateAnimation(0,toXDelta, 0, 0); // check the getRight() before changing
            translateAnimation.setDuration(800);
            translateAnimation.setFillAfter(true);
            translateAnimation.setStartOffset(300);

            TranslateAnimation translateAnimationy = new TranslateAnimation(0, 0, 0, -(coordinates[1]));
            translateAnimationy.setDuration(1200);
            translateAnimationy.setFillAfter(true);
            translateAnimationy.setStartOffset(1100);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(translateAnimationy);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ivVan_horizontal.setImageResource(R.drawable.van_vertical);
                }
            });

            animationSet.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ivVan_horizontal.setImageResource(R.drawable.van_horizontal);
                    ivVan_horizontal.setVisibility(View.GONE);
                    addToCart(productDO);
                    selPagerPostion = position;
                }
            });
            ivVan_horizontal.startAnimation(animationSet);
        }else{
            addToCart(productDO);
            selPagerPostion = position;
        }
    }

    @Override
    protected void onStart() {
//        gpsLocationService.connectGoogleClient();
        apiClient.connect();
        super.onStart();
    }

    public void addToCart(ProductDO productDO){
        ArrayList<ProductDO> cartProduct = new ArrayList<>();
        cartProduct.add(productDO);
        int id = preference.getIntFromPreference(Preference.CUSTOMER_ID,0);
        String sessionId = preference.getStringFromPreference(Preference.SESSION_ID, "");
        new CommonBL(DashBoardActivity.this,DashBoardActivity.this).addToCart(cartProduct, 0, sessionId, id);
    }

    public void setCartButtonOnBtlSelected(int position){
        if(arrProductDOs !=null && arrProductDOs.size()>0){
            if(hmCartList!= null && hmCartList.containsKey(arrProductDOs.get(position).code)){
                ivVan_horizontal.setVisibility(View.GONE);
                ivPlaceYrOrder.setVisibility(View.GONE);
                ivGoToCart.setVisibility(View.VISIBLE);
            }else{
                ivGoToCart.setVisibility(View.GONE);
                ivVan_horizontal.setVisibility(View.VISIBLE);
                ivPlaceYrOrder.setVisibility(View.VISIBLE);
            }
        }else{
            ivGoToCart.setVisibility(View.GONE);
            ivVan_horizontal.setVisibility(View.VISIBLE);
            ivPlaceYrOrder.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        showCustomDialog(DashBoardActivity.this,getString(R.string.alert),getString(R.string.exit_confirm),getString(R.string.OK),getString(R.string.cancel),"Exit");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        gpsLocationService.stopLocationUpdate();
    }

    @Override
    protected void onStop() {
        LogUtils.debug("DashboardActivity", "onStop()");
        if (apiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
            apiClient.disconnect();
        }
        super.onStop();
    }

    public void refreshAllList(){
        gridadapter.setHmCartListDO(hmCartList);
        listadapter.setHmCartListDO(hmCartList);
        listadapter.refresh(arrProductDOs);
        gridadapter.refresh(arrProductDOs);
    }

    public void updateCartCount(){
        if(hmCartList != null && hmCartList.size()>0){
            tvCArtCount.setVisibility(View.VISIBLE);
            tvCArtCount.setText(""+hmCartList.size());
        }else{
            tvCArtCount.setText(""+0);
        }
    }


    @Override
    public void onButtonYesClick(String from) {
        super.onButtonYesClick(from);
        if(from.equalsIgnoreCase("Exit")){
            finish();
        }
    }

    @Override
    public void onButtonNoClick(String from) {
        if(from.equalsIgnoreCase("DeliveryPopup")){
            preference.saveBooleanInPreference(Preference.SHOWDELIVERY,false);
        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && response.method == ServiceMethods.WS_ADD_TO_CART) {
            if (response.data != null && response.data instanceof CartResponseDO) {
                final ArrayList<CartListDO> arrCartListDOs = ((CartResponseDO)response.data).cartListDOs;
                if(arrCartListDOs != null && arrCartListDOs.size() > 0) {
                    // needed not be wait untill data inserted into the database.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new CartDA(DashBoardActivity.this).insertCartList(arrCartListDOs);
                        }
                    }).start();
                    if (arrCartListDOs != null) {
                        hmCartList = new HashMap<>();
                        for (CartListDO cartListDO : arrCartListDOs) {
                            hmCartList.put(cartListDO.productCode, cartListDO);
                        }
                    }
                    Toast.makeText(DashBoardActivity.this, getResources().getString(R.string.addedtocart), Toast.LENGTH_LONG).show();
                    refreshAllList();
                    updateCartCount();
                    setCartButtonOnBtlSelected(selPagerPostion);
                }
            }else{
                ivGoToCart.setVisibility(View.GONE);
                ivVan_horizontal.setVisibility(View.VISIBLE);
                ivPlaceYrOrder.setVisibility(View.VISIBLE);
                Toast.makeText(DashBoardActivity.this,"Failed to add the item to Cart.",Toast.LENGTH_LONG).show();
            }
        }
        super.dataRetrieved(response);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(_currentLocation == null) {
            _currentLocation = location;
            getTemperature();
        }else{
            _currentLocation = location;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        RequestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity)DashBoardActivity.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
