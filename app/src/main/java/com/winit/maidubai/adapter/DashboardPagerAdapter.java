package com.winit.maidubai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.util.List;

/**
 * created by sridhar on 6/27/2016.
 */
public class DashboardPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ProductDO> arrProductDOs;
    private Preference preference;

    public DashboardPagerAdapter(Context context, List<ProductDO> arrProductDOs){
        this.context=context;
        this.arrProductDOs =arrProductDOs;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preference = new Preference(context);
    }

    @Override
    public int getCount() {
        if(arrProductDOs != null)
            return arrProductDOs.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return   view==((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        View helpMain=layoutInflater.inflate(R.layout.pager_help_image,container,false);
        ImageView ivhelpingImage  =  (ImageView)helpMain.findViewById(R.id.ivhelpingImage);
        final ProductDO productDO = arrProductDOs.get(position);
        ivhelpingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(productDO.categoryId == 106)
                {
                    String name = "";
                    if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(productDO.AltDescription))
                    {
                        name = productDO.AltDescription;
                    }else
                    {
                        name = productDO.name;
                    }
                    ((DashBoardActivity) context).showProductDescription(name, productDO.btlSize, productDO.arrProductAttributes);
                }*/
            }
        });
/*        if(productDO.id.equalsIgnoreCase("1747") || productDO.id.equalsIgnoreCase("1748")) {
            if(productDO.id.equalsIgnoreCase("1748"))
                ivhelpingImage.setImageResource(R.drawable.hundred);
            else {
                ivhelpingImage.setImageResource(R.drawable.twohundred);
            }
        }else{*/
/*        MaiDubaiApplication.picasso.load(ServiceUrls.MAIN_URL + arrProductDOs.get(position).pagerImage)
                    .error(R.drawable.no_image)
                    .into(ivhelpingImage);*/
//        }

        String imagePath = ServiceUrls.MAIN_URL.substring(0,ServiceUrls.MAIN_URL.length()-1) + productDO.pagerImage/*+"?t="+ System.currentTimeMillis()*/;
        final Uri uri = Uri.parse(imagePath);
        Bitmap bitmap = null;
        if (uri != null) {
//            bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, ivhelpingImage,imagePath));
//            if (bitmap != null) {
//                ivhelpingImage.setImageBitmap(bitmap);
//            } else {
                imagePath = AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR + productDO.pagerImage;
//                imagePath = context.getFilesDir().toString()+AppConstants.IMAGE_DIR + productDO.pagerImage;
//                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, ivhelpingImage,imagePath));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(imagePath, options);
                if (bitmap != null) {
                    ivhelpingImage.setImageBitmap(bitmap);
                }else
                    ivhelpingImage.setImageResource(R.drawable.no_image);
//            }

            /*if(NetworkUtil.isNetworkConnectionAvailable(context))
            {
                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, ivhelpingImage,imagePath));
                if (bitmap != null)
                {
                    ivhelpingImage.setImageBitmap(bitmap);
                } else
                {
                    ivhelpingImage.setImageResource(R.drawable.no_image);
                }
            }
            else
            {
                imagePath = context.getFilesDir().toString()+AppConstants.IMAGE_DIR + productDO.pagerImage;
                //uri = Uri.parse(imagePath);
                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, ivhelpingImage,imagePath));
                if (bitmap != null) {
                    ivhelpingImage.setImageBitmap(bitmap);
                }else
                    ivhelpingImage.setImageResource(R.drawable.no_image);
            }*/
        }else{
            ivhelpingImage.setImageResource(R.drawable.no_image);
        }
        container.addView(helpMain);
        return helpMain;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
    public void refresh(List<ProductDO> items){
        this.arrProductDOs = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
