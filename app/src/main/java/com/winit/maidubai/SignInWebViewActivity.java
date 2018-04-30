package com.winit.maidubai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.CustomerDO;
import com.winit.maidubai.utilities.LogUtils;
import com.winit.maidubai.webaccessLayer.SocialNetworkCilent;

/**
 * Created by Girish Velivela on 11-07-2016.
 */
public class SignInWebViewActivity extends Activity {

    private WebView webView;
    private String url;
    private int networkType;
    private String[] oauthToken;
    private SocialNetworkCilent socialNetworkCilent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webview);
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebClient(SignInWebViewActivity.this));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");//newly added to  avoid erro : disallowed_useragent
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSavePassword(false);
        mWebSettings.setSaveFormData(false);
        android.webkit.CookieManager.getInstance().removeAllCookie();

        if(getIntent()!=null && getIntent().hasExtra("URL"))
        {
            url = getIntent().getStringExtra("URL");
            networkType = getIntent().getIntExtra("NETWORKTYPE", -1);
            if(networkType != AppConstants.NETWORKTYPE_TWITTER)
                webView.loadUrl(url);
            else if(networkType == AppConstants.NETWORKTYPE_TWITTER){
                socialNetworkCilent = new SocialNetworkCilent();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showLoader();
                        oauthToken = socialNetworkCilent.getRequestToken();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(oauthToken != null && oauthToken.length >=2) {
                                    webView.loadUrl(AppConstants.TWITTER_Authorize_URL + "?oauth_token=" + oauthToken[0]);
                                    hideLoader();
                                }else{
                                    finish();
                                    hideLoader();
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private void showLoader()
    {
        runOnUiThread(new RunShowLoader("Loading..."));
    }

    public void hideLoader()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if(progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    progressDialog = null;
                }
                catch(Exception e)
                {
                   /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
                }
            }
        });
    }

    /** This will shows a progress dialog with loading text, this is useful to call when some other functionality is taking place. **/
    private void showLoader(String msg)
    {
        try
        {
            runOnUiThread(new RunShowLoader(msg));
        } catch (Exception e)
        {
//            e.printStackTrace();
            throw new RuntimeException("This can never happen", e);
        }

    }
    public ProgressDialog progressDialog;
    /**
     * Implementing Runnable for runOnUiThread(), This will show a progress dialog
     */
    class RunShowLoader implements Runnable
    {
        private String strMsg;

        public RunShowLoader(String strMsg) {
            this.strMsg = strMsg;
        }

        @Override
        public void run() {
            try {

                if (progressDialog == null || (progressDialog != null && !progressDialog.isShowing()))
                {
                    progressDialog = ProgressDialog.show(SignInWebViewActivity.this, "", strMsg);
                }
                else if (progressDialog == null || (progressDialog != null && progressDialog.isShowing()))
                {
                    progressDialog.setMessage(strMsg);
                }

            } catch (Exception e) {
                /*e.printStackTrace(); */     Log.d("This can never happen", e.getMessage());
                progressDialog = null;
            }
        }
    }

    private class WebClient extends WebViewClient{

        private Context context;

        public WebClient(Context context) {
            this.context = context;
        }
        boolean isDone=false;
        boolean isFromError = false;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            LogUtils.debug("shouldOverrideUrlLoading", url);
            if(!isDone)
                return super.shouldOverrideUrlLoading(view, url);
            else
                return false;
        }

        @Override
        public void onReceivedError( WebView view, int errorCode,
                                     String description, String failingUrl) {

            LogUtils.debug("failingUrl", failingUrl);
            webView.setVisibility(View.GONE);
            isFromError = true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            showLoader();
            LogUtils.debug("onPageStartedUrl", "" + url);

            switch (networkType) {
                case AppConstants.NETWORKTYPE_GOOGLEPLUS:
                    isDone =performGooglePlusLogin(url);
                    break;
                case AppConstants.NETWORKTYPE_FACEBOOK:
                    isDone = performFaceBookLogin(url);
                    break;
                case AppConstants.NETWORKTYPE_TWITTER:
                    isDone = performTwitterLogin(url);
                    break;
            }
            if(!isDone)
                super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(!isDone)
                hideLoader();
            super.onPageFinished(view, url);
        }
    }

    private boolean performGooglePlusLogin(String url){
        if (url.startsWith(AppConstants.REDIRECT_URL) && url.contains("code"))
        {
            String mainUrl  = url.split("&")[0];
            final String authCode = mainUrl.substring(mainUrl.lastIndexOf("=") + 1, mainUrl.length());
            if (authCode != null && !authCode.equalsIgnoreCase(""))
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showLoader();
                        CustomerDO customerDO = new SocialNetworkCilent().getUserDetailsFromGooglePlus(authCode);
                        if(customerDO != null)
                            setResult(RESULT_OK,getIntent().putExtra("CUSTOMER",customerDO));
                        hideLoader();
                        finish();
                    }
                }).start();
                return true;
            }
        }
        return false;
    }
    private boolean performFaceBookLogin(String url){

        if (url.startsWith(AppConstants.REDIRECT_URL) && url.contains("access_token")) {
            final String accessToken = url.split("&")[0].split("=")[1];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    showLoader();
                    CustomerDO customerDO = new SocialNetworkCilent().getUserDetailsFromFaceBook(accessToken);
                    if(customerDO != null)
                        setResult(RESULT_OK,getIntent().putExtra("CUSTOMER",customerDO));
                    hideLoader();
                    finish();
                }
            }).start();
            return true;
        }
        return false;
    }
    private boolean performTwitterLogin(String url){
        if (url.contains("oauth_token") && url.contains("oauth_verifier"))
        {
            String queryParam = url.substring(url.indexOf("?"),url.length());
            String[] queryParams = queryParam.split("&");
            final String authToken = queryParams[0].split("=")[1];
            final String verifier = queryParams[1].split("=")[1];
            if(authToken.equalsIgnoreCase(oauthToken[0])){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showLoader();
                        CustomerDO customerDO = socialNetworkCilent.getUserDetailsFromTwitter(authToken,verifier,oauthToken[1]);
                        if(customerDO != null)
                            setResult(RESULT_OK,getIntent().putExtra("CUSTOMER",customerDO));
                        hideLoader();
                        finish();
                    }
                }).start();
            }
            return true;
        }
        return false;
    }

}
