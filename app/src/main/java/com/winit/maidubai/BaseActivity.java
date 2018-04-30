package com.winit.maidubai;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.databaseaccess.DatabaseHelper;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.dataobject.DataSyncDO;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.dataobject.MasterTableDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.httpimage.HttpImageManager;
import com.winit.maidubai.utilities.BitmapUtilsLatLang;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.CircleTransform;
import com.winit.maidubai.utilities.FileUtils;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.HttpHelper;
import com.winit.maidubai.webaccessLayer.ServiceMethods;
import com.winit.maidubai.webaccessLayer.ServiceUrls;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class BaseActivity extends FragmentActivity implements  DataListener, ComponentCallbacks {
    protected ArrayList<DrawerRowList> arrMenu;
    private ListView mDrawerList;
    public DrawerLayout drawerLayout;

    private ProgressDialog progressdialog;
    protected LinearLayout llBody,llBase,lldrawerHeader,llToggleLeft,llToggleRight,llToggle,llMainCenter,llMenuFooter,llBack,llDrawerHeader;
    protected Toolbar toolbar;
    protected ImageView ivMenu, ivSearch, ivCancel, ivCart, ivLogo, ivDrawerClose,ivFirstTime, ivBack, ivUserImage;
    protected TextView tvTitle, tvCArtCount, tvUserNameLoggedIn, tvCancel,tvToggleLeft,tvToggleRight,tvLogout;
    Button btnGallery,btnCamera;
    protected RelativeLayout rlCArt;
    protected LayoutInflater inflater;
    protected Preference preference;
    protected boolean isLoggedIn;
    protected static String userName="";
    public DecimalFormat decimalFormat;

    protected String otpString="";
    protected static final int CAMERA_REQUEST = 1888;
    protected static int RESULT_LOAD_IMAGE = 1;
    protected static int GALLERY_KITKAT_INTENT_CALLED = 2;
    public static int NOTIFICATION_ID = 10000;
    public String language = "";
    protected Resources resources;
    protected  String imageurl ="";

    protected CustomerDO customerDO;

    protected static int i=0;
    public DecimalFormat amountFormat = new DecimalFormat("0.000");

    //private PendingIntent pIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  pIntent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), SplashActivity.class), getIntent().getFlags());
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseActivity.this));
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        if(availableMegs<10){
            deleteDirectoryTree((BaseActivity.this).getCacheDir());
        }
        setContentView(R.layout.activity_base);
        resources = getResources();

        /************************************ Unique for every Active *********************************/

        inflater                    = this.getLayoutInflater();
        preference                  = new Preference(BaseActivity.this);
        NumberFormat nf             = NumberFormat.getInstance(Locale.ENGLISH);
        decimalFormat               =  (DecimalFormat)nf;
        decimalFormat.applyPattern("##.###");
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);


        /***************************************** UI ****************************/

        toolbar                     = (Toolbar)findViewById(R.id.toolbar);
        llBody                      = (LinearLayout)findViewById(R.id.llBody);
        llToggle                    = (LinearLayout)findViewById(R.id.llToggle);
        llToggleLeft                = (LinearLayout)findViewById(R.id.llToggleLeft);
        llToggleRight               = (LinearLayout)findViewById(R.id.llToggleRight);
        llMainCenter                = (LinearLayout)findViewById(R.id.llMainCenter);
        llMenuFooter                = (LinearLayout)inflater.inflate(R.layout.menu_footer, null);
        llMainCenter.setVisibility(View.VISIBLE);
        ivMenu                      = (ImageView) toolbar.findViewById(R.id.ivMenu);
        llBack                      = (LinearLayout) toolbar.findViewById(R.id.llBack);
        ivLogo                      = (ImageView)toolbar.findViewById(R.id.ivLogo);
        ivCancel                      = (ImageView)toolbar.findViewById(R.id.ivCancel);
        rlCArt                      = (RelativeLayout)toolbar.findViewById(R.id.rlCArt);
        tvTitle                     = (TextView)findViewById(R.id.tvTitle);
        ivSearch                    = (ImageView)findViewById(R.id.ivSearch);
        ivCart                      = (ImageView)findViewById(R.id.ivCart);
        llBase                      = (LinearLayout)findViewById(R.id.llBase);
        ivFirstTime                 = (ImageView)findViewById(R.id.ivFirstTime);
        tvCArtCount                 = (TextView)findViewById(R.id.tvCArtCount);
        tvCancel                    =(TextView)findViewById(R.id.tvCancel);
        tvToggleLeft                =(TextView)findViewById(R.id.tvToggleLeft);
        tvToggleRight               =(TextView)findViewById(R.id.tvToggleRight);
        ivBack                      =(ImageView)findViewById(R.id.ivBack);

        tvToggleLeft.setTypeface(AppConstants.DinproMedium);
        tvToggleRight.setTypeface(AppConstants.DinproMedium);
//*************************************for left drawer************************************************
        llDrawerHeader= (LinearLayout) findViewById(R.id.llDrawerHeader);
        ivDrawerClose = (ImageView) findViewById(R.id.ivDrawerClose);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
        tvUserNameLoggedIn = (TextView) findViewById(R.id.tvUserNameLoggedIn);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerListClickListner();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        arrMenu = new ArrayList<DrawerRowList>();
        getMenuListUser();
        mDrawerList.setAdapter(new DrawerAdapter());
        TextView tvDeliveryDays = (TextView)llMenuFooter.findViewById(R.id.tvDeliveryDays);
        TextView tvDelieryHead = (TextView)llMenuFooter.findViewById(R.id.tvDelieryHead);
        tvDelieryHead.setPadding(0,10,0,0);
        int width = preference.getIntFromPreference(Preference.DEVICE_DISPLAY_WIDTH,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width-width/4, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvDelieryHead.setTypeface(AppConstants.DinproMedium);
        tvDeliveryDays.setTypeface(AppConstants.DinproMedium);
        tvDelieryHead.setLayoutParams(params);
        String deliveryDays = preference.getStringFromPreference(Preference.DELIVERY_DAYS, "");
        if(!preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("en"))
        {
            deliveryDays = getDelivarydatesArabic(deliveryDays);
        }
        if(StringUtils.isEmpty(deliveryDays))
            tvDeliveryDays.setText("N/A");
        else
            tvDeliveryDays.setText(deliveryDays);
        mDrawerList.addFooterView(llMenuFooter,null,false);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCaptureImageOptions();
            }
        });
        llDrawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.exactDatabase();
                    }
                }).start();
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BaseActivity.this,MyBasketActivity.class);
                startActivity(intent);
            }
        });


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                if(language.equalsIgnoreCase("en")){
                    drawerLayout.openDrawer(Gravity.LEFT);
                }else
                {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        ivDrawerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                if(language.equalsIgnoreCase("en")){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else
                {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                if(language.equalsIgnoreCase("en")){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else
                {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
                showCustomDialog(BaseActivity.this,getString(R.string.alert),getString(R.string.logout_msg),getString(R.string.logout),getString(R.string.cancel),"LOGOUT");
            }
        });


        initialise();
    }

    public String getDelivarydatesArabic(String deliverydays)
    {
        String deliveryday = "";
        if(deliverydays.contains(","))
        {
            String[] delarr = deliverydays.split(",");
            for(String day:delarr)
            {
                if(StringUtils.isEmpty(deliveryday))
                {
                    deliveryday = getArabicDay(day);
                }
                else
                {
                    deliveryday = ","+getArabicDay(day);
                }
            }
        }
        else
        {
            deliveryday = getArabicDay(deliverydays);
        }

        return deliveryday;
    }

    public String getArabicDay(String day)
    {
        String arabicDay = "";
        if(day.equalsIgnoreCase("sunday"))
        {
            arabicDay = getResources().getString(R.string.Sunday);
        }
        else if(day.equalsIgnoreCase("monday"))
        {
            arabicDay = getResources().getString(R.string.Monday);
        }
        else if(day.equalsIgnoreCase("tuesday"))
        {
            arabicDay = getResources().getString(R.string.Tuesday);
        }
        else if(day.equalsIgnoreCase("Wednesday"))
        {
            arabicDay = getResources().getString(R.string.Wednesday);
        }
        else if(day.equalsIgnoreCase("Thursday"))
        {
            arabicDay = getResources().getString(R.string.Thursday);
        }
        else if(day.equalsIgnoreCase("Friday"))
        {
            arabicDay = day;
        }
        else if(day.equalsIgnoreCase("Saturday"))
        {
            arabicDay = getResources().getString(R.string.Saturday);
        }

        return arabicDay;
    }
    public String userImage = "";
    public void showCaptureImageOptions(){
        LinearLayout drawerPopUp = (LinearLayout) inflater.inflate(R.layout.drawer_popup, null);
        ImageView btnGallery= (ImageView) drawerPopUp.findViewById(R.id.btnGallery);
        ImageView btnCamera= (ImageView) drawerPopUp.findViewById(R.id.btnCamera);
        final CustomDialog customDialog = new CustomDialog(BaseActivity.this, drawerPopUp,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        customDialog.setCancelable(true);
        customDialog.show();

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                File root = android.os.Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/Mai Dubai");
                if (!dir.exists())
                    dir.mkdirs();
                File userImgFile = new File(dir.getAbsolutePath() + "/"+ preference.getStringFromPreference(Preference.CUSTOMER_CODE,"")+".png");
                userImage = userImgFile.getAbsolutePath();
                Uri fileUri = Uri.fromFile(userImgFile);
                // String path=getPath(fileUri);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                intent.putExtra("fileName", userImgFile.getName());
                intent.putExtra("filePath", userImgFile.getAbsolutePath());
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
               /* Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);*/

                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/jpeg");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }



            }
        });
    }

    protected boolean isProfilePicUpload = false;

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri uri = data.getData();
            showLoader("uploading....");
            isProfilePicUpload = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = getRealPathFromURI(uri);

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,480);
                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,filepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getIntFromPreference(Preference.CUSTOMER_ID,0)+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivUserImage,R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(BaseActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();

                /*ivUserImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap,189,189,true));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(40,40,0,0);
                ivUserImage.setLayoutParams(layoutParams);*/
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK ) {
            showLoader("uploading....");
            isProfilePicUpload = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = userImage;

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,480);
                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,filepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getIntFromPreference(Preference.CUSTOMER_ID,0)+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivUserImage,R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(BaseActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();




            //bitmap = Bitmap.createScaledBitmap(bitmap,189,189,true);
            // ivUserImage.setImageBitmap(bitmap);
            /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40,40,0,0);
            ivUserImage.setLayoutParams(layoutParams);*/
        }
        if(requestCode == GALLERY_KITKAT_INTENT_CALLED && resultCode == RESULT_OK){
            showLoader("uploading....");
            isProfilePicUpload = true;
            final Uri originalUri = data.getData();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filepath = getRealPathFromURI(originalUri);

                    File f = new File(filepath);
                    Bitmap bmp = BitmapUtilsLatLang.decodeSampledBitmapFromResource(f, 320,480);
                    filepath = BitmapUtilsLatLang.saveVerifySignature(bmp,filepath);

                    imageurl =  new HttpHelper().uploadImage(ServiceUrls.MAIN_URL,preference.getIntFromPreference(Preference.CUSTOMER_ID,0)+"",filepath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoader();
                            isProfilePicUpload = false;
                            if(!StringUtils.isEmpty(imageurl))
                            {
                                preference.saveStringInPreference(Preference.USER_IMAGE,imageurl);
                                setImage(imageurl,ivUserImage,R.drawable.draweruser);
                            }
                            else
                            {
                                showCustomDialog(BaseActivity.this,"","Something went wrong. Please try again...","Ok","","");
                            }
                        }
                    });
                }
            }).start();

        }
    }


    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

//*******************************************************************************************************


    public void showSnackMessage(String message){
        Snackbar snackbar = Snackbar.make(llBody,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public boolean checkNetworkConnection() {
        if (!NetworkUtil.isNetworkConnectionAvailable(BaseActivity.this)) {
            showCustomDialog(BaseActivity.this, getString(R.string.alert), getString(R.string.No_Network), getString(R.string.OK), "", AppConstants.INTERNET_ERROR,false);
            return false;
        }
        return true;
    }

    public boolean checkGpsStatus() {
        if (!NetworkUtil.isGpsOn(BaseActivity.this)) {
            showCustomDialog(BaseActivity.this, getString(R.string.alert), getString(R.string.No_GPS), getString(R.string.OK), "", AppConstants.GPS_ERROR,false);
            return false;
        }
        return true;
    }

    public void clearPreference(){
        preference.removeFromPreference(Preference.IS_LOGGED_IN);
        preference.removeFromPreference(Preference.EMAIL_ID);
//        preference.removeFromPreference(Preference.CUSTOMER_ID);
        preference.removeFromPreference(Preference.SESSION_ID);
        preference.removeFromPreference(Preference.CUSTOMER_NAME);
        preference.removeFromPreference(Preference.CUSTOMER_MOBILE);
        preference.removeFromPreference(Preference.USER_IMAGE);
        preference.removeFromPreference(Preference.SHOWDELIVERY);
        preference.removeFromPreference(Preference.NOTIFICATION_FLAG);
        preference.removeFromPreference(Preference.HYDRATION_TIME_PERIOD);
        //       preference.removeFromPreference(Preference.FIRST_TIME_DISPLAY);
        preference.removeFromPreference(Preference.IS_DISCLAIMER);
    }

    public Preference getPreference(){
        return preference;
    }
    //Placed your drawer menu click listener
    private void drawerListClickListner() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = (String) view.getTag(R.string.menu_name);
                Intent intent;
                String language = preference.getStringFromPreference(Preference.LANGUAGE,"");
                if(language.equalsIgnoreCase("en")){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else
                {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
                boolean stop = true;
                switch (menuName){
                    case "My Profile":
                        if(!(BaseActivity.this instanceof ProfileActivity))
                        {
                            intent = new Intent(BaseActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        else{
                            stop = false;
                        }
                        break;
                    case "Order Now":
                        if(!(BaseActivity.this instanceof DashBoardActivity))
                        {
                            intent = new Intent(BaseActivity.this, DashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        break;
                    case "My Orders":
                        if(!(BaseActivity.this instanceof MyOrdersActivity)) {
                            intent = new Intent(BaseActivity.this, MyOrdersActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "My Recurring Orders":
                        if(!(BaseActivity.this instanceof MyRecurringOrdersActivity)) {
                            intent = new Intent(BaseActivity.this, MyRecurringOrdersActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Chat":
                        intent = new Intent(BaseActivity.this, ZopimChatActivity.class);
                        startActivity(intent);
                        break;
                    case "Settings":
                        if(!(BaseActivity.this instanceof SettingsActivity)) {
                            intent = new Intent(BaseActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Social Network":
                        if(!(BaseActivity.this instanceof SocialNetworkActivity)) {
                            intent = new Intent(BaseActivity.this, SocialNetworkActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "About us":
                        if(!(BaseActivity.this instanceof AboutUsActivity)) {
                            intent = new Intent(BaseActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Contact us":
                        if(!(BaseActivity.this instanceof ContactUs)) {
                            intent = new Intent(BaseActivity.this, ContactUs.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Hydration Meter":
                        if(!(BaseActivity.this instanceof HydrationMeterActivity)) {
                            intent = new Intent(BaseActivity.this, HydrationMeterActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Feedback":
                        if(!(BaseActivity.this instanceof SendFeedbackActivity)) {
                            intent = new Intent(BaseActivity.this, SendFeedbackActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Upcoming Deliveries":
                        if(!(BaseActivity.this instanceof DeliveryDaysActivity)) {
                            intent = new Intent(BaseActivity.this, DeliveryDaysActivity.class);
                            startActivity(intent);
                        }else{
                            stop = false;
                        }
                        break;
                    case "Sync":
                        if (checkNetworkConnection()) {
                            refreshData(new SyncIntentService.SyncListener() {
                                @Override
                                public void onStart() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showLoader("Syncing...");
                                        }
                                    });
                                }

                                @Override
                                public void onError(String message) {
                                    hideLoader();
                                    updateUI();
                                }

                                @Override
                                public void onEnd() {
                                    hideLoader();
                                    updateUI();
                                }
                            });
                        }
                        stop = false;
                        break;
                   /* case "Logout" :
                        stop = false;
                        showCustomDialog(BaseActivity.this,getString(R.string.alert),getString(R.string.logout_msg),getString(R.string.logout),getString(R.string.cancel),"LOGOUT");
                        break;*/
                    default:
                        Toast.makeText(BaseActivity.this,"Under Development",Toast.LENGTH_LONG).show();
                        break;
                }
                if(!(BaseActivity.this instanceof DashBoardActivity) && stop){
                    finish();
                }
            }
        });
    }

    public void lockDrawer(final String from) {
        LogUtils.errorLog("Menu Drawer Locked in ", from);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //for status bar color
    public void setStatusBarColor(){
        Window window = BaseActivity.this.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(BaseActivity.this.getResources().getColor(R.color.statusbarred));
        }
    }


    public void setTypeFaceBold(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
                ((TextView) v).setTypeface(AppConstants.DinproBold);
            else if (v instanceof ViewGroup)
                setTypeFaceBold((ViewGroup) v);
        }
    }

    public void setTypeFaceNormal(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
                ((TextView) v).setTypeface(AppConstants.DinproRegular);
            else if (v instanceof ViewGroup)
                setTypeFaceNormal((ViewGroup) v);
        }
    }

    public void setTypeFaceLight(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
                ((TextView) v).setTypeface(AppConstants.DinproLight);
            else if (v instanceof ViewGroup)
                setTypeFaceNormal((ViewGroup) v);
        }
    }

    public void setTypeFaceMedium(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button || v instanceof EditText/* etc. */)
                ((TextView) v).setTypeface(AppConstants.DinproMedium);
            else if (v instanceof ViewGroup)
                setTypeFaceNormal((ViewGroup) v);
        }
    }


    private void getMenuListUser() {
        arrMenu.clear();
        String[]titles=getResources().getStringArray(R.array.drawer_menu_titles);
        String[]ar_titles=getResources().getStringArray(R.array.drawer_menu_titles_ar);
        TypedArray icons = getResources().obtainTypedArray(R.array.drawer_menu_icons);
        for (int i=0;i<titles.length;i++){
            DrawerRowList items=new DrawerRowList(icons.getResourceId(i,0),titles[i],ar_titles[i]);
            arrMenu.add(items);
        }
    }

    public void setLocale(String language){
        if(StringUtils.isEmpty(language))
            language = "en";
        preference.saveStringInPreference(Preference.LANGUAGE,language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }


    public void refreshData(SyncIntentService.SyncListener syncListener){
        boolean isSyncing = preference.getbooleanFromPreference(Preference.IS_SYNCING,false);
        long lastUploadTime = preference.getLongInPreference(Preference.LAST_UPLOAD_TIME);
        long diffTime = (System.currentTimeMillis() - lastUploadTime) / (60 * 1000);
        if (!isSyncing || diffTime >= 2) {
            SyncIntentService.setSyncListener(syncListener);
            Intent intent = new Intent(BaseActivity.this,SyncIntentService.class);
            startService(intent);
        }
    }


    public void updateUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* boolean isLocaleUpdated = preference.getbooleanFromPreference(Preference.LOCALE_UPDATED,false);
                if(isLocaleUpdated){
                    setLocale(preference.getStringFromPreference(Preference.LANGUAGE,"en"));
                    Intent intent = new Intent(BaseActivity.this, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {*/
                    TextView tvDeliveryDays = (TextView) llMenuFooter.findViewById(R.id.tvDeliveryDays);
                    tvDeliveryDays.setTypeface(AppConstants.DinproMedium);
                    String deliveryDays = preference.getStringFromPreference(Preference.DELIVERY_DAYS, "");
                    if(!preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("en"))
                    {
                        deliveryDays = getDelivarydatesArabic(deliveryDays);
                    }
                    if (StringUtils.isEmpty(deliveryDays))
                        tvDeliveryDays.setText("N/A");
                    else
                        tvDeliveryDays.setText(deliveryDays);
                    loadData();
//                }
            }
        });
    }

    class DrawerAdapter extends BaseAdapter
    {
        String lang = "";

        DrawerAdapter(){
            lang = preference.getStringFromPreference(Preference.LANGUAGE,"");
        }

        @Override
        public int getCount() {
            if (arrMenu!=null && arrMenu.size()>0) {
                return arrMenu.size();
            }
            else
                return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return arrMenu.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup arg2) {
            ViewHolder viewHolder;
            if(convertView==null){

                // inflate the layout
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.drawer_menu_item, null);
                viewHolder = new ViewHolder();
                //  viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.tvDrawerListItem);
                viewHolder.icons= (ImageView) convertView.findViewById(R.id.drawerIcon);
                viewHolder.titles= (TextView) convertView.findViewById(R.id.drawerTitles);
                convertView.setTag(viewHolder);

            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            DrawerRowList rowItem = (DrawerRowList) getItem(pos);
            viewHolder.icons.setImageResource(rowItem.getImageid());
            if(lang.equalsIgnoreCase("ar"))
                viewHolder.titles.setText(rowItem.getArbicTitle());
            else
                viewHolder.titles.setText(rowItem.getTitles());
            viewHolder.titles.setTypeface(AppConstants.DinproMedium);
            convertView.setTag(R.string.menu_name,rowItem.getTitles());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView icons;
        TextView titles;
    }




    public abstract void initialise();
    public abstract void initialiseControls();
    public abstract void loadData();

    public void showLoader(String msg) {
        runOnUiThread(new RunShowLoader(msg, ""));
    }
    public void showLoader(String msg, String title) {
        runOnUiThread(new RunShowLoader(msg, title));
    }

    /** For hiding progress dialog (Loader ). **/
    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (progressdialog != null && progressdialog.isShowing())
                        progressdialog.dismiss();
                    progressdialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* Custom Runnable for showing loaders */
    class RunShowLoader implements Runnable {
        private String strMsg;
        private String title;

        public RunShowLoader(String strMsg, String title) {
            this.strMsg = strMsg;
            this.title = title;
        }

        @Override
        public void run() {
            try {
                if (progressdialog == null
                        || (progressdialog != null && !progressdialog
                        .isShowing())) {
                    progressdialog = ProgressDialog.show(BaseActivity.this,
                            title, strMsg);
                } else if (progressdialog == null
                        || (progressdialog != null && progressdialog
                        .isShowing())) {
                    progressdialog.setMessage(strMsg);
                }
            } catch (Exception e) {
                progressdialog = null;
            }
        }
    }

    public void OTPSuccessfull(){
        showCustomDialog(BaseActivity.this, "", "OTP Suceesfull", getString(R.string.OK), "", "");
    }

    public void downloadMasterData(String userCode, String lsd, String lst) {
        final String  customerId = preference.getStringFromPreference(Preference.CUSTOMER_CODE,"");
        if(StringUtils.isEmpty(userCode) || !userCode.equalsIgnoreCase(customerId)) {
            showLoader("Loading general configurations....");
//            new CommonBL(BaseActivity.this, BaseActivity.this).downloadMasterTable(userCode, CalendarUtil.getDate(new Date(),CalendarUtil.DATE_STD_PATTERN));
            new CommonBL(BaseActivity.this, BaseActivity.this).downloadMasterTable(userCode, "01-01-2015");
        }else{
            showLoader("Syncing Data.... Please wait...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String lastSyncTime = new CustomerDA(BaseActivity.this).getLastSyncTime();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(lastSyncTime != null)
                                new CommonBL(BaseActivity.this,BaseActivity.this).dataSync(customerId,lastSyncTime);
                        }
                    });
                }
            }).start();
        }
    }

    public void showOTPPopup(final CustomerDO customerDO,final String type){

        LinearLayout llOtpPopup= (LinearLayout)inflater.inflate(R.layout.otp_popup,null);
        final CustomDialog customDialog = new CustomDialog(BaseActivity.this, llOtpPopup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        customDialog.setCancelable(false);

        final EditText edOtpTextFirst= (EditText)llOtpPopup.findViewById(R.id.edOtpTextFirst);
        final EditText edOtpTextSecond= (EditText)llOtpPopup.findViewById(R.id.edOtpTextSecond);
        final EditText edOtpTextThird= (EditText)llOtpPopup.findViewById(R.id.edOtpTextThird);
        final EditText edOtpTextFourth= (EditText)llOtpPopup.findViewById(R.id.edOtpTextFourth);
        final Button btnResendOtp= (Button) llOtpPopup.findViewById(R.id.btnResendOtp);
        final Button btnVerify= (Button) llOtpPopup.findViewById(R.id.btnVerify);
        final ImageView ivClose= (ImageView) llOtpPopup.findViewById(R.id.ivClose);
        final TextView tvmailinfo= (TextView) llOtpPopup.findViewById(R.id.tvmailinfo);
        tvmailinfo.setTypeface(AppConstants.DinproLight);
        final StringBuilder sb = new StringBuilder();

        edOtpTextFirst.requestFocus();
        edOtpTextFirst.setCursorVisible(true);

        if(type.equalsIgnoreCase("Login")){
            ivClose.setVisibility(View.VISIBLE);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(edOtpTextFirst.getText().toString()) &&
                        StringUtils.isEmpty(edOtpTextSecond.getText().toString()) &&
                        StringUtils.isEmpty(edOtpTextThird.getText().toString()) &&
                        StringUtils.isEmpty(edOtpTextFourth.getText().toString()))
                    showCustomDialog(BaseActivity.this,"",getResources().getString(R.string.plzenterotp),getResources().getString(R.string.ok),"","");
                else
                {
                    if(checkNetworkConnection()) {
                        otpString = edOtpTextFirst.getText().toString() + edOtpTextSecond.getText().toString() + edOtpTextThird.getText().toString() + edOtpTextFourth.getText().toString();
                        String otp = preference.getStringFromPreference(Preference.OTP, "");

                        if(type.equalsIgnoreCase("Login"))
                            new CommonBL(BaseActivity.this, BaseActivity.this).verifyOTPLogin(customerDO.mobileNumber, otpString);
                        else
                            new CommonBL(BaseActivity.this, BaseActivity.this).verifyOTP(customerDO.mobileNumber, otpString);
                    }
                }
            }
        });

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edOtpTextFirst.setText("");
                edOtpTextSecond.setText("");
                edOtpTextThird.setText("");
                edOtpTextFourth.setText("");

                edOtpTextFirst.requestFocus();
                edOtpTextFirst.setCursorVisible(true);

                if(checkNetworkConnection())
                {
                    new CommonBL(BaseActivity.this,BaseActivity.this).resndOTP(customerDO.cutomerEmailId, customerDO.mobileNumber,type);
                }
            }
        });


        //********************************OtpInputAutoNextController*******************************************************

        edOtpTextFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edOtpTextFirst.length()==1)
                {
                    sb.append(s);
                    edOtpTextFirst.clearFocus();
                    edOtpTextSecond.requestFocus();
                    edOtpTextSecond.setCursorVisible(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edOtpTextFirst.requestFocus();
                }
            }
        });
//first close
        edOtpTextSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edOtpTextSecond.length()==1)
                {
                    sb.append(s);
                    edOtpTextSecond.clearFocus();
                    edOtpTextThird.requestFocus();
                    edOtpTextThird.setCursorVisible(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edOtpTextSecond.requestFocus();
                }
                if(s.toString().length() == 0){
                    edOtpTextFirst.requestFocus();
                    edOtpTextFirst.setCursorVisible(true);
                }
            }
        });
        //third edit
        edOtpTextThird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edOtpTextThird.length()==1)
                {
                    sb.append(s);
                    edOtpTextFourth.requestFocus();
                    edOtpTextFourth.setCursorVisible(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edOtpTextThird.requestFocus();
                }
                if(s.toString().length() == 0){
                    edOtpTextSecond.requestFocus();
                    edOtpTextSecond.setCursorVisible(true);
                }
            }
        });

        edOtpTextFourth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0){
                    edOtpTextThird.requestFocus();
                    edOtpTextThird.setCursorVisible(true);
                }
            }
        });


        edOtpTextFourth.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if(edOtpTextFourth.getText().length()==0)
                        {

                            edOtpTextThird.setText("");
                            edOtpTextThird.requestFocus();
                            edOtpTextThird.setCursorVisible(true);
                        }

                    }
                }
                return false;
            }
        });

        edOtpTextThird.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if(edOtpTextThird.getText().length()==0)
                        {

                            edOtpTextSecond.setText("");
                            edOtpTextSecond.requestFocus();
                            edOtpTextSecond.setCursorVisible(true);
                        }

                    }
                }
                return false;
            }
        });

        edOtpTextSecond.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if(edOtpTextSecond.getText().length()==0)
                        {
                            edOtpTextFirst.setText("");
                            edOtpTextFirst.requestFocus();
                            edOtpTextFirst.setCursorVisible(true);
                        }

                    }
                }
                return false;
            }
        });

        customDialog.show();
    }

    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }

    @Override
    public void onTrimMemory(int level) {
        if(level == TRIM_MEMORY_RUNNING_LOW) {
            deleteDirectoryTree((BaseActivity.this).getCacheDir());
        }
        super.onTrimMemory(level);
    }

    public void showAlert(String message, String from){
        showCustomDialog(BaseActivity.this, getString(R.string.alert), message,getString(R.string.OK), "", from);
    }

    /** Method to Show the alert dialog **/
    public void showCustomDialog(Context context, String strTitle,String strMessage, String firstBtnName, String secondBtnName,String from)
    {
        runOnUiThread(new ShowCustomDialogs(context, strTitle, strMessage,firstBtnName, secondBtnName, from, true));
    }
    /** Method to Show the alert dialog **/
    public void showCustomDialog(Context context, String strTitle,String strMessage, String firstBtnName, String secondBtnName,String from, boolean isCancelable)
    {
        runOnUiThread(new ShowCustomDialogs(context, strTitle, strMessage,firstBtnName, secondBtnName, from, isCancelable));
    }

    // For showing Dialog message.
    private class ShowCustomDialogs implements Runnable
    {
        private CustomDialog customDialog;
        private String strTitle;// Title of the dialog
        private String strMessage;// Message to be shown in dialog
        private String firstBtnName;
        private String secondBtnName;
        private String from;
        private boolean isCancelable=false;
        private View.OnClickListener posClickListener;
        private View.OnClickListener negClickListener;

        public ShowCustomDialogs(Context context, String strTitle, String strMessage, String firstBtnName, String secondBtnName, String from, boolean isCancelable)
        {
            this.strTitle 		= strTitle;
            this.strMessage 	= strMessage;
            this.firstBtnName 	= firstBtnName;
            this.secondBtnName	= secondBtnName;
            this.isCancelable 	= isCancelable;
            if (from != null)
                this.from = from;
            else
                this.from = "";
        }

        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setTitle(strTitle);
                alertDialog.setMessage(strMessage);
                alertDialog.setCancelable(isCancelable);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, firstBtnName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onButtonYesClick(from);
                    }
                });
                if (secondBtnName != null && !secondBtnName.equalsIgnoreCase("")) {
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, secondBtnName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onButtonNoClick(from);
                        }
                    });
                }
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.dark_red2));
                        Button button = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        if(button != null) {
                            button.setTextColor(getResources().getColor(R.color.dark_red2));
                        }
                    }
                });

                alertDialog.show();
            } else {
                if (customDialog != null && customDialog.isShowing())
                    customDialog.dismiss();

                View view = inflater.inflate(R.layout.custom_common_popup, null);
                customDialog = new CustomDialog(BaseActivity.this, view, preference
                        .getIntFromPreference(Preference.DEVICE_DISPLAY_WIDTH, 320) - 40, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                customDialog.setCancelable(isCancelable);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitlePopup);
                View ivDivider = view.findViewById(R.id.ivDividerPopUp);
                View view_middle = view.findViewById(R.id.view_middle);
                TextView tvMessage = (TextView) view.findViewById(R.id.tvMessagePopup);
                Button btnYes = (Button) view.findViewById(R.id.btnYesPopup);
                Button btnNo = (Button) view.findViewById(R.id.btnNoPopup);

                tvTitle.setTypeface(AppConstants.DinproMedium);
                tvMessage.setTypeface(AppConstants.DinproRegular);
                btnYes.setTypeface(AppConstants.DinproMedium);
                btnNo.setTypeface(AppConstants.DinproMedium);
                if (!StringUtils.isEmpty(strTitle)) {
                    if (strTitle.equalsIgnoreCase(getString(R.string.alert)) || strTitle.equalsIgnoreCase(getString(R.string.warning)) || strTitle.equalsIgnoreCase(getString(R.string.confirm)))
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.alert, 0, 0);
                    else if (strTitle.equalsIgnoreCase(getString(R.string.success)))
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.alert_success, 0, 0);
                    else
                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvTitle.setText("" + strTitle);
                } else {
                    tvTitle.setVisibility(View.GONE);
                    ivDivider.setVisibility(View.GONE);
                }
                tvMessage.setText("" + strMessage);
                btnYes.setText("" + firstBtnName);

                if (secondBtnName != null && !secondBtnName.equalsIgnoreCase(""))
                    btnNo.setText("" + secondBtnName);
                else {
                    btnNo.setVisibility(View.GONE);
                    view_middle.setVisibility(View.GONE);
                }

                if (posClickListener == null)
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();
                            onButtonYesClick(from);
                        }
                    });
                else
                    btnYes.setOnClickListener(posClickListener);

                if (negClickListener == null)
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();
                            onButtonNoClick(from);
                        }
                    });
                else
                    btnNo.setOnClickListener(negClickListener);
                try {
                    if (!customDialog.isShowing())
                        customDialog.showCustomDialog();
                } catch (Exception e) {
                    throw new RuntimeException("This can never happen", e);
                }
            }
        }
    }

    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("LOGOUT")){
            clearPreference();
            Intent intent = new Intent(BaseActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }

    }
    public void onButtonNoClick(String from) {

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

    /*
     * size must be drawable
     */
    public void setImage(String fileUri,ImageView imageView,int size){
        if (!StringUtils.isEmpty(fileUri)) {

            Drawable d =getResources().getDrawable(size);
            int mHeight = d.getIntrinsicHeight();
            int mWidth = d.getIntrinsicWidth();
           /* try {
                if(fileUri.contains("content://")) {

                    if (Build.VERSION.SDK_INT <19){
                        Uri photoUri = Uri.parse(fileUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                        imageView.setImageBitmap(GetRoundedShape(bitmap,size));
                    }
                    else
                    {
                        Uri photoUri = Uri.parse(fileUri);
                        String wholeID = DocumentsContract.getDocumentId(photoUri);

                        // Split at colon, use second item in the array
                        String id = wholeID.split(":")[1];

                        String[] column = { MediaStore.Images.Media.DATA };

                        // where id is equal to
                        String sel = MediaStore.Images.Media._ID + "=?";

                        Cursor cursor = getContentResolver().
                                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel, new String[]{ id }, null);

                        String filePath = "";

                        int columnIndex = cursor.getColumnIndex(column[0]);

                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        }

                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
                        imageView.setImageBitmap(GetRoundedShape(bitmap,size));

                        cursor.close();
                    }


                   *//* String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(GetRoundedShape(bitmap,size));*//*

                }else{
                    File image = new File(fileUri);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    imageView.setImageBitmap(GetRoundedShape(bitmap,size));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            LogUtils.errorLog("Image Url:",ServiceUrls.MAIN_URL + fileUri+"?t="+ i++);
            Picasso.with(BaseActivity.this).load(ServiceUrls.MAIN_URL + fileUri+"?t="+ i++).transform(new CircleTransform())
                    .resize(mWidth, mHeight)
                    .error(size)
                    .into(imageView);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        String path = "";
        try {
            if(contentUri.toString().contains("content://")) {

                if (Build.VERSION.SDK_INT < 19) {

                    String[] proj = { MediaStore.Images.Media.DATA };
                    cursor = getContentResolver().query(contentUri,  proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                }
                else {
                    String wholeID = DocumentsContract.getDocumentId(contentUri);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    String[] column = { MediaStore.Images.Media.DATA };

                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";

                    Cursor cursornew = getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{ id }, null);


                    int columnIndex = cursornew.getColumnIndex(column[0]);

                    if (cursornew.moveToFirst()) {
                        path = cursornew.getString(columnIndex);
                    }
                    cursornew.close();
                }
            }
            else
            {
                File image = new File(contentUri.toString());
                path = image.getAbsolutePath();
            }

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {

            if("content".equals(contentUri.getScheme())) {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            else{
                return contentUri.getPath();
            }


        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoggedIn = preference.getbooleanFromPreference(Preference.IS_LOGGED_IN,false);
        userName = StringUtils.isEmpty(preference.getStringFromPreference(Preference.CUSTOMER_NAME,""))? preference.getStringFromPreference(Preference.EMAIL_ID,"Guest@gmail.com"):preference.getStringFromPreference(Preference.CUSTOMER_NAME,"Guest");
        tvUserNameLoggedIn.setText(userName);
//        setImage(preference.getStringFromPreference(Preference.USER_IMAGE,""),ivUserImage, R.drawable.draweruser);
    }


    private Dialog imageDialog;
    public void showVideoPopUp(Uri uri)
    {
        LinearLayout convertView		=  (LinearLayout) inflater.inflate(R.layout.image_popup, null);
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.llPhotos);
        VideoView videoView = (VideoView)convertView.findViewById(R.id.videoView);
        linearLayout.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        videoView.setMinimumWidth(width);
        videoView.setMinimumHeight(height);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        if(imageDialog!=null && imageDialog.isShowing())
            imageDialog.dismiss();

        imageDialog= new Dialog(BaseActivity.this);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDialog.setContentView(convertView);
        imageDialog.getWindow().setLayout(preference.getIntFromPreference(Preference.DEVICE_DISPLAY_WIDTH, 800) - 200, preference.getIntFromPreference(Preference.DEVICE_DISPLAY_HEIGHT, 1200) - 400);
        imageDialog.show();

    }

    public HttpImageManager getHttpImageManager() {
        return ((MaiDubaiApplication) (BaseActivity.this)
                .getApplication()).getHttpImageManager();
    }

    public void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void openKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    //********************************for roundes images*************************************
    protected Bitmap GetRoundedShape(Bitmap scaleBitmapImage,int imgId) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),imgId);
        int targetWidth = bitmap.getWidth();
        int targetHeight = bitmap.getHeight();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        android.graphics.Path path = new android.graphics.Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

//    public String generateOTP()
//    {
//        //generate a 4 digit integer 1000 <10000
//        int randomPIN = (int)(Math.random()*9000)+1000;
//        String PINString = String.valueOf(randomPIN);
//
//        return PINString;
//    }
public static void safeClose(InputStream fis) {
    if (fis != null) {
        try {
            fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
}
    /* made common moveToDashboard to maintain preference correct for different signup's and login's*/
    public void moveToDashboard(final CustomerDO customerDO, final InputStream inputStream){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(inputStream != null) {
                    showLoader(getString(R.string.processing_patient));
                    boolean flag = new DatabaseHelper(getApplicationContext()).copyDataBase(inputStream);
                    InputStream imageZip = new HttpHelper().sendRequest(ServiceUrls.MAIN_URL+ServiceUrls.IMAGE_ZIP_URL+customerDO.cutomerEmailId, ServiceUrls.METHOD_GET, null, null);
//                    FileUtils.exractZip(imageZip,getFilesDir().toString()+AppConstants.IMAGE_DIR+"/Data");
                    flag = FileUtils.exractZip(imageZip,AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR+"/Data/ProductImages");
                    hideLoader();
                    if(imageZip!=null)
                        safeClose(imageZip);
                    if(!flag){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showCustomDialog(BaseActivity.this,"",getString(R.string.something_wrong),getString(R.string.OK),"","");
                            }
                        });
                        return;
                    }
                }

                preference.saveBooleanInPreference(Preference.IS_LOGGED_IN, true);
                preference.saveBooleanInPreference(Preference.ISSINGUP, false);
                preference.saveIntInPreference(Preference.CUSTOMER_ID, customerDO.Id);
                preference.saveStringInPreference(Preference.CUSTOMER_CODE, customerDO.customerId);
                preference.saveStringInPreference(Preference.SESSION_ID, customerDO.sessionId);
                preference.saveStringInPreference(Preference.CUSTOMER_NAME,customerDO.siteName);
                preference.saveStringInPreference(Preference.CUSTOMER_MOBILE,customerDO.mobileNumber);
                preference.saveStringInPreference(Preference.EMAIL_ID, customerDO.cutomerEmailId);
                CustomerDA customerDA = new CustomerDA(BaseActivity.this);
                // CustomerDO customerDOProPic = customerDA.getCustomer();
                String deliveryDays = customerDA.getDeliveryDays();
                preference.saveStringInPreference(Preference.DELIVERY_DAYS,deliveryDays);
                /*if(customerDOProPic != null)
                {
                    if(!StringUtils.isEmpty(customerDOProPic.ProfileImage))
                        preference.saveStringInPreference(Preference.USER_IMAGE,customerDOProPic.ProfileImage);
                }*/
                if(!StringUtils.isEmpty(customerDO.PreferedLanguage)) {
                    if(customerDO.PreferedLanguage.equalsIgnoreCase("English"))
                        setLocale("en");
                    else
                        setLocale("ar");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseActivity.this instanceof PhoneNumberDetails || BaseActivity.this instanceof SNSignUpConfirmationActivity){

                            LinearLayout llOTPSuccessfulPopUp= (LinearLayout)inflater.inflate(R.layout.otp_succeessful_popup,null);
                            final CustomDialog customDialog = new CustomDialog(BaseActivity.this, llOTPSuccessfulPopUp, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                            customDialog.setCancelable(false);
                            Button  btnContinue= (Button)llOTPSuccessfulPopUp.findViewById(R.id.btnContinue);
                            TextView tvUserName= (TextView) llOTPSuccessfulPopUp.findViewById(R.id.tvUserName);
                            tvUserName.setText(customerDO.siteName);
                            btnContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialog.dismiss();
//                                    preference.saveBooleanInPreference(Preference.ISSINGUP,true);
                                    Intent intent = new Intent(BaseActivity.this,DashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("ShowDeliveryPopup", true);
                                    startActivity(intent);
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                            customDialog.show();

                        }else {
                            Intent intent = new Intent(BaseActivity.this, DashBoardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("ShowDeliveryPopup", true);
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            }
        }).start();
    }

    public void generateNotification(Context context,String text,String OTP)
    {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = null;

        int count = (int) System.currentTimeMillis();


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Mai Dubai")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentText(text)
                .setSubText(OTP);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if (response.method != null && (response.method == ServiceMethods.WS_MASTER_TABLE &&response.data != null)) {
            if(response.data instanceof MasterTableDO){
                MasterTableDO masterTableDO = (MasterTableDO) response.data;
                String masterDataUrl = masterTableDO.masterTablePath;
                masterDataUrl = masterDataUrl.substring(0,masterDataUrl.lastIndexOf("."));
                masterDataUrl = ServiceUrls.MAIN_URL + masterDataUrl+".zip";
                new CommonBL(BaseActivity.this,BaseActivity.this).downloadMasterTable(masterDataUrl);
                String syncTime = CalendarUtil.getDate(masterTableDO.serverTime, CalendarUtil.DATE_PATTERN_dd_MM_YYYY, CalendarUtil.SYNCH_DATE_TIME_PATTERN,10*60*1000,Locale.ENGLISH);
                preference.saveStringInPreference(Preference.LAST_ORDERS_SYNC,syncTime);
            }else{
                showCustomDialog(BaseActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        }else if (response.method != null && (( response.method == ServiceMethods.WS_OTP_MAIL || response.method == ServiceMethods.WS_OTP_MAIL_LOGIN ) &&response.data != null)) {
            if (!response.isError) {
                OTPSuccessfull();
            }else{
//                Toast.makeText(BaseActivity.this,(String)response.data,Toast.LENGTH_LONG).show();
                showCustomDialog(BaseActivity.this,"",(String)response.data,getString(R.string.OK),"","");
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_DOWNLOAD_MASTER_TABLE &&response.data != null)) {
            if (!response.isError && response.data instanceof InputStream) {
                if (!response.isError && response.data instanceof InputStream) {
                    moveToDashboard(customerDO,(InputStream)response.data);
                }else{
                    showCustomDialog(BaseActivity.this,"",(String)response.data,getString(R.string.OK),"","");
                }
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_RESEND_OTP &&response.data != null)) {
            if (response.data instanceof CommonResponseDO && ((CommonResponseDO)response.data).status != 0) {
                Toast.makeText(BaseActivity.this,getString(R.string.OTP_sent),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(BaseActivity.this,getString(R.string.OTP_sent_fail),Toast.LENGTH_LONG).show();
            }
        }else if (response.method != null && response.method == ServiceMethods.WS_DATASYNC) {
            if (response.data != null && response.data instanceof DataSyncDO) {
                DataSyncDO dataSyncDO = (DataSyncDO)response.data;
                String syncTime = CalendarUtil.getDate(dataSyncDO.serverTime, CalendarUtil.YYYY_MM_DD_FULL_PATTERN, CalendarUtil.SYNCH_DATE_TIME_PATTERN,5*60*1000,Locale.ENGLISH);
                preference.saveStringInPreference(Preference.LAST_ORDERS_SYNC,syncTime);
                moveToDashboard(customerDO, null);
            }else{
                showCustomDialog(BaseActivity.this, "", (String) response.data, getString(R.string.OK), "", "");
            }
        }
    }

    @Override
    protected void onDestroy() {
        MaiDubaiApplication.picassoLruCache.clear();
        super.onDestroy();
    }



    public class EmirateComparator implements Comparator<Emirates> {
        @Override
        public int compare(Emirates u1, Emirates u2) {
            return u1.Name.toLowerCase().compareTo(u2.Name.toLowerCase());
        }
    }

    public class AreaComparator implements Comparator<AreaDO> {
        @Override
        public int compare(AreaDO u1, AreaDO u2) {
            return u1.Name.toLowerCase().compareTo(u2.Name.toLowerCase());
        }
    }
  /*  public String calculateAmount(int qty , float amount , double vat)
    {
        String str = "" +  amountFormat.format (((qty * amount) * (100+vat)) /100);
        return str;
    }
    public double calculateAmt(int qty , float amount , double vat)
    {
        double str =    ((qty * amount) * (100+vat)) /100 ;
        return str;
    }*/

}


