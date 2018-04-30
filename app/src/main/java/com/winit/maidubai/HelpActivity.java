package com.winit.maidubai;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.winit.maidubai.adapter.HelpScreenAdapter;
import com.winit.maidubai.common.Preference;

public class HelpActivity extends BaseActivity {

    private RelativeLayout lLMain;
    private  ViewPager vpHelpScreen;
    private CirclePageIndicator titleDots;
    private TextView getStarted;
    //private int[] helpScreenImageID={R.drawable.walktrough_one,R.drawable.walktrough2/*,R.drawable.walktrough3,R.drawable.walktrough4,R.drawable.walktrough5*/};
    private int[] helpScreenImageID={R.drawable.helpscreen1,R.drawable.helpscreen2,R.drawable.helpscreen3};

    private int selectedIndex;
    private boolean mPageEnd;



    private int mCurrentState = -1;
    private static int INTERVAL = ViewConfiguration.getLongPressTimeout();
    private Handler mHandler = new Handler();

    @Override
    public void initialise() {
        lLMain= (RelativeLayout) inflater.inflate(R.layout.activity_help,null);
        setTypeFaceNormal(lLMain);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(lLMain,param );
        toolbar.setVisibility(View.GONE);
        lockDrawer("HelpActivity");
        vpHelpScreen          =        (ViewPager)findViewById(R.id.vp_help);
        titleDots            =        (CirclePageIndicator) findViewById(R.id.titleDots);
        getStarted           =         (TextView)findViewById(R.id.tvGetStarted);
        HelpScreenAdapter adapter=new HelpScreenAdapter(this,helpScreenImageID);
        vpHelpScreen.setAdapter(adapter);
        vpHelpScreen.setPageMargin(0);
        titleDots.setViewPager(vpHelpScreen);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn)
                {
                    Intent intent = new Intent(HelpActivity.this,DashBoardActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(HelpActivity.this,LanguageSelectionActivity.class);
                    startActivity(intent);
                }
                preference.saveBooleanInPreference(Preference.FIRST_TIME_DISPLAY,true);
                finish();
            }
        });



        vpHelpScreen.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
                /*if( mPageEnd && position == selectedIndex && !callHappened)
                {
                    Log.d(getClass().getName(), "Okay");

                    if(isLoggedIn)
                    {
                        Intent intent = new Intent(HelpActivity.this,DashBoardActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(HelpActivity.this,LanguageSelectionActivity.class);
                        startActivity(intent);

                    }
                    preference.saveBooleanInPreference(Preference.FIRST_TIME_DISPLAY,true);
                    finish();
                    mPageEnd = false;//To avoid multiple calls.
                    callHappened = true;
                }else
                {
                    mPageEnd = false;
                }*/
            }

            boolean callHappened;
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                selectedIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                /*if(selectedIndex == helpScreenImageID.length - 1)
                {
                    mPageEnd = true;
                }*/
                mCurrentState = state;
                // if we are at the second page and the user touched the
                // ViewPager post a Runnable with a decent time to schedule our
                // Intent
                if (vpHelpScreen.getCurrentItem() == helpScreenImageID.length-1) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        mHandler.postDelayed(mRun, INTERVAL);
                    }
                }
            }
        });

    }

    @Override
    public void initialiseControls() {

    }

    @Override
    public void loadData() {

    }


    private Runnable mRun = new Runnable() {

        @Override
        public void run() {
            // we got the Runnable to be executed. If we are on the second page
            // and the user let go of the ViewPager in our time frame then start
            // the Activity(also cancel the dozen Runnables that were posted)
            if (mCurrentState == ViewPager.SCROLL_STATE_IDLE
                    && vpHelpScreen.getCurrentItem() == helpScreenImageID.length-1) {
                mHandler.removeCallbacks(mRun);// or always set it to run
                if(isLoggedIn)
                {
                    Intent intent = new Intent(HelpActivity.this,DashBoardActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(HelpActivity.this,LanguageSelectionActivity.class);
                    startActivity(intent);

                }
                preference.saveBooleanInPreference(Preference.FIRST_TIME_DISPLAY,true);
                finish();
            }
        }
    };
}
