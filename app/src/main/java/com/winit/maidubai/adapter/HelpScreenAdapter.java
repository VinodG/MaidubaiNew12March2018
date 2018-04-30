package com.winit.maidubai.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.winit.maidubai.BaseActivity;
import com.winit.maidubai.R;
import com.winit.maidubai.common.Preference;

/**
 * Created by Jayasai on 6/27/2016.
 */
public class HelpScreenAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] helpScreenImageID;//={R.drawable.walktrough_one,R.drawable.walktrough2,R.drawable.walktrough3,R.drawable.walktrough4,R.drawable.walktrough5};
    public HelpScreenAdapter(Context context,int[] helpScreenImageID){
        this.context=context;
        this.helpScreenImageID=helpScreenImageID;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

   // public HelpScreenAdapter()
    @Override
    public int getCount() {
        return helpScreenImageID.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return   view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View helpMain=layoutInflater.inflate(R.layout.help_screen_image,container,false);
        ImageView ivhelpingImage  =  (ImageView)helpMain.findViewById(R.id.ivhelpingImage);
        ivhelpingImage.setBackgroundResource(helpScreenImageID[position]);
/*        Preference preference = ((BaseActivity)context).getPreference();
        int width = preference.getIntFromPreference(Preference.DEVICE_DISPLAY_WIDTH,0);
        int height = preference.getIntFromPreference(Preference.DEVICE_DISPLAY_HEIGHT,0);
        helpMain.setLayoutParams(new LinearLayout.LayoutParams(width,height));*/
        container.addView(helpMain);
        return helpMain;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
