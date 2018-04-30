package com.winit.maidubai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.winit.maidubai.businesslayer.DataListener;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.PlaceOrderDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

public class RossomPaymentActivity extends BaseActivity implements DataListener
{
    private LinearLayout llWeb;
    WebView wv;
    ProgressDialog pd;
    PlaceOrderDO placeOrderDO;

    private TrxHeaderDO trxHeaderDO;

    @SuppressLint("JavascriptInterface")
    @Override
    public void initialise() {
        // arabic xml file need to be added
        llWeb = (LinearLayout) inflater.inflate(R.layout.activity_rossom_payment,null);
        llWeb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llWeb);
        setTypeFaceNormal(llWeb);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.payment));
        ivMenu.setVisibility(View.GONE);
        ivMenu.setImageResource(R.drawable.menu_white);
        rlCArt.setVisibility(View.GONE);
        setStatusBarColor();
        setTypeFaceNormal(llWeb);
        initialiseControls();

        placeOrderDO =(PlaceOrderDO)getIntent().getExtras().get("PLACE_ORDER");
        if(placeOrderDO !=null && placeOrderDO.arrTrxHeaderDOs.get(0)!= null && placeOrderDO.arrTrxHeaderDOs.get(0).trxCode !=null)
        {
            pd=new ProgressDialog(this);
            pd.setMessage("Please Wait...");
            wv.setWebViewClient(new MywebViewClient());
            wv.getSettings().setJavaScriptEnabled(true);

            JavaScriptInterface jsInterface = new JavaScriptInterface(this);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.addJavascriptInterface(new WebAppInterface(this), "Android");

            //Class to be injected in Web page


            trxHeaderDO = (TrxHeaderDO)getIntent().getSerializableExtra("Order");

            //------------
//            String transid= trxHeaderDO.orderNumberDO.OrderNumber;
            String transid=  placeOrderDO.arrTrxHeaderDOs.get(0).trxCode;
            String CustId= preference.getIntFromPreference(Preference.CUSTOMER_ID,0)+"" ;
            String doccharge= "0";
            String servicecost=placeOrderDO.arrTrxHeaderDOs.get(0).totalTrxAmount+""; // trxHeaderDO.totalTrxAmount+"";//"9.0";
            //------------
            String url = ServiceUrls.MAIN_URL+ "/PaymentGateway/Index?TrxCode="+transid;/*+"&customerReferenceNumber="+CustId+"&serviceCost="+servicecost+"&documentationCharges=0\n"*/
            wv.loadUrl(url);
            Log.e("WebpageLoading : ", url );



        }


    }
    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void moveToNextScreen()
        {
//            Intent intent = new Intent(RossomPaymentActivity.this, OrderSuccessActivity.class);
            Intent intent = new Intent(RossomPaymentActivity.this, MyOrdersActivity.class);
            intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
            startActivity(intent);
            finish();
        }
    }
    public class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activiy) {
            this.activity = activiy;
        }

        public void startVideo(String videoAddress){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoAddress), "video/3gpp");
            activity.startActivity(intent);
        }
    }
    @Override
    public void initialiseControls() {
        wv=(WebView)llWeb.findViewById(R.id.wv);

    }
    class MywebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.dismiss();
        }
    }
    @Override
    public void onBackPressed()
    {
        if(wv.canGoBack())
            wv.goBack();
        else {
            super.onBackPressed();
            Toast.makeText(RossomPaymentActivity.this, "Payment has been concelled...Your order will not be confirmed", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void loadData() {
//        Intent intent = new Intent(RossomPaymentActivity.this, OrderSuccessActivity.class);
//        intent.putExtra("Order", placeOrderDO.arrTrxHeaderDOs.get(0));
//        startActivity(intent);

    }
    @JavascriptInterface
    public void moveToNext(String response )
    {
        finish();
        // Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
    }
    private void syncData(final TrxHeaderDO trxHeaderDO)
    {
        boolean isSyncing = preference.getbooleanFromPreference(Preference.IS_SYNCING,false);
        if(isSyncing || !NetworkUtil.isNetworkConnectionAvailable(RossomPaymentActivity.this))
        {

        }
//            loadData();
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
//                            loadData();
                            showAlert("Error occured while syncing ",null);
                        }
                    });
                }

                @Override
                public void onEnd() {
                    hideLoader();
                    AppConstants.IS_TO_EDIT_ORDER =false;
                    moveToOrderSummary(trxHeaderDO);
//                    updateUI();
                }


            });
        }

    }
    private void moveToOrderSummary(TrxHeaderDO trxHeaderDO)
    {
        if(AppConstants.IS_TO_EDIT_ORDER)
        {
            syncData(trxHeaderDO);
        }
        else
        {

            Intent intent = new Intent(RossomPaymentActivity.this, MyOrdersActivity.class);
            intent.putExtra("Order", trxHeaderDO);
            startActivity(intent);
        }
    }

}
