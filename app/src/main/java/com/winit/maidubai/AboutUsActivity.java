package com.winit.maidubai;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.winit.maidubai.common.AppConstants;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class AboutUsActivity extends BaseActivity{
    private ScrollView svAboutUsActivity;
    private LinearLayout llPagerTab;
    private WebView webView;
    private TextView tvHeader,tvabout,tvabout2;
    private MyPagerAdater imageAdapter;
    private ViewPager pager;
    private Timer timer;

    @Override
    public void initialise() {
        svAboutUsActivity= (ScrollView) inflater.inflate(R.layout.activity_about_us,null);
        webView = (WebView)svAboutUsActivity.findViewById(R.id.webview);
        tvHeader  = (TextView)svAboutUsActivity.findViewById(R.id.tvHeader);
        svAboutUsActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(svAboutUsActivity);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.aboutusttl));
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setImageResource(R.drawable.menu_white);
        tvCancel.setVisibility(View.INVISIBLE);
        tvCancel.setClickable(false);
        tvCancel.setText(getResources().getString(R.string.save));
        setStatusBarColor();
        setTypeFaceNormal(svAboutUsActivity);
        tvHeader.setTypeface(AppConstants.DinproBold);
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        tvabout= (TextView)findViewById(R.id.tvabout);
        tvabout2 = (TextView)findViewById(R.id.tvabout2);
        tvabout.setTypeface(AppConstants.DinproRegular);
        tvabout2.setTypeface(AppConstants.DinproRegular);
        String text = "<html>" +
                " <head>\n" +
                "        <style type=\"text/css\">\n" +
                "        @font-face {\n" +
                "            font-family: MyFont;\n" +
                "            src: url(\"file:///android_asset/fonts/DINPro_Regular.otf\")\n" +
                "        }\n" +
                "        body {\n" +
                "            font-family: MyFont;\n" +
                "            font-size: medium;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "        </style>\n" +
                "        </head>" +
                "<body>"
                + "<p align=\"justify\">"
                + getString(R.string.aboutus)
                + "</p> "
                + "<p align=\"justify\">"
                + getString(R.string.aboutUs2)
                + "</p> "
                + "</body></html>";

        webView.loadData(text, "text/html", "utf-8");
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        pager = (ViewPager)findViewById(R.id.pager);
        llPagerTab = (LinearLayout)findViewById(R.id.llPagerTab);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int postion)
            {

                for (int i = 0; i <= (imageAdapter.getCount()-1); i++)
                {
                    ((ImageView)llPagerTab.getChildAt(i)).setImageResource(R.drawable.pager_dot);
                }
                ((ImageView)llPagerTab.getChildAt(postion)).setImageResource(R.drawable.pager_dot_h);
            }

            @Override
            public void onPageScrolled(int postion, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int postion)
            {
            }
        });
    }
    int []images = {R.drawable.about_one,R.drawable.about_two,R.drawable.about_three,R.drawable.about_four};
    @Override
    public void loadData() {
        imageAdapter = new MyPagerAdater(images);
        pager.setAdapter(imageAdapter);
        refreshPageController(llPagerTab,pager);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            synchronized public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int pos = pager.getCurrentItem();
                        if(pos<3){
                            pager.setCurrentItem(++pos);
                        }else{
                            imageAdapter.refresh(images);
                            pager.setCurrentItem(0);
                        }
                    }
                });
            }

        }, 5000, 5000);
    }

    private class MyPagerAdater extends PagerAdapter
    {
        private int[] images;
        public MyPagerAdater(int[] images)
        {
            this.images = images;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            if(images != null)
                return images.length;
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            LinearLayout llPagerCell = (LinearLayout) inflater.inflate(R.layout.image_popup_cell, null);
            ((ImageView)llPagerCell.findViewById(R.id.ivPagerImage)).setImageResource(images[position]);
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

        public void refresh(int[] images){
            this.images = images;
            notifyDataSetChanged();
        }

    }


    private void refreshPageController(LinearLayout llPagerTab,ViewPager pager)
    {
        llPagerTab.removeAllViews();
        for (int i = 0; i <= (imageAdapter.getCount()-1); i++)
        {
            final ImageView imgvPagerController = new ImageView(AboutUsActivity.this);
            imgvPagerController.setPadding(2,2,2,2);
            imgvPagerController.setImageResource(R.drawable.pager_dot);
            llPagerTab.addView(imgvPagerController);
        }
        ((ImageView)llPagerTab.getChildAt(pager.getCurrentItem())).setImageResource(R.drawable.pager_dot_h);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    private class WebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            showLoader("loading....");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoader();
            super.onPageFinished(view, url);
        }
    }
}
