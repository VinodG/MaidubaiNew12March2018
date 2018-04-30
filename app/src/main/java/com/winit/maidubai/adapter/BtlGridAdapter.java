package com.winit.maidubai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.maidubai.DashBoardActivity;
import com.winit.maidubai.MyBasketActivity;
import com.winit.maidubai.OrderQuantityBottomSheet;
import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.CustomDialog;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jayasai on 6/29/2016.
 */
public class BtlGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private CustomDialog customDialog;
    private List<ProductDO> vecProducts;
    private HashMap<String,CartListDO> hmCartListDO;
    private Preference preference;

    public BtlGridAdapter(Context context,List<ProductDO> items){
        this.context=context;
        this.vecProducts =items;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preference = new Preference(context);
    }

    @Override
    public int getCount() {
        if(vecProducts != null && vecProducts.size()>0)
            return vecProducts.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return vecProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final ProductDO productDO=(ProductDO)getItem(position);
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.single_grid_btl,null);
            holder=new ViewHolder();

            holder.btlImage    = (ImageView)convertView.findViewById(R.id.ivBtl2);
            holder.btlRate     = (TextView)convertView.findViewById(R.id.tv_btlRate1);
            holder.btlSize     = (TextView)convertView.findViewById(R.id.tv_btlSize1);
            holder.ivAddCArtGv    = (ImageView)convertView.findViewById(R.id.ivAddCArtGv);
            holder.ivGoToCArtGv    = (ImageView)convertView.findViewById(R.id.ivGoToCArtGv);
            holder.btlSize.setTypeface(AppConstants.DinproRegular);
            holder.btlRate.setTypeface(AppConstants.DinproMedium);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();


        String name = "";
        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(productDO.AltDescription))
        {
            name = productDO.AltDescription;
            holder.btlSize.setText(name);
        }else
        {
            name = productDO.name;
            if(name.contains("(")) {
                holder.btlSize.setText(Html.fromHtml("<p>" + name.substring(0, name.indexOf("(")) + " <small>" + name.substring(name.indexOf("("), name.lastIndexOf(")") + 1) + "</p>"));
            }else if(name.contains("-")){
                holder.btlSize.setText(Html.fromHtml("<p>" + name.substring(0, name.indexOf("-")) + " <small>" + name.substring(name.indexOf("-"),name.length()) + "</p>"));
            }else{
                holder.btlSize.setText(name);
            }
        }

        holder.btlRate.setText(context.getResources().getString(R.string.currency)+productDO.price);
        if(hmCartListDO!= null && hmCartListDO.containsKey(productDO.code)){
            holder.ivAddCArtGv.setVisibility(View.GONE);
            holder.ivGoToCArtGv.setVisibility(View.VISIBLE);
        } else{
            holder.ivAddCArtGv.setVisibility(View.VISIBLE);
            holder.ivGoToCArtGv.setVisibility(View.GONE);
        }
        holder.btlImage.setOnClickListener(new View.OnClickListener() {
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
                    ((DashBoardActivity)context).showProductDescription(name,productDO.btlSize,productDO.arrProductAttributes);
                }*/
            }
        });
        holder.ivAddCArtGv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("item", vecProducts.get(position));
                bundle.putInt("position", position);
                BottomSheetDialogFragment bottomSheetDialogFragment = new OrderQuantityBottomSheet();
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(((DashBoardActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        holder.ivGoToCArtGv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MyBasketActivity.class);
                context.startActivity(intent);
            }
        });
/*        MaiDubaiApplication.picasso.load(ServiceUrls.MAIN_URL + productDO.gridImage)
                .into(holder.btlImage);*/
        String imagePath = ServiceUrls.MAIN_URL.substring(0,ServiceUrls.MAIN_URL.length()-1) + productDO.pagerImage/*+"?t="+ System.currentTimeMillis()*/;
        final Uri uri = Uri.parse(imagePath);
        Bitmap bitmap = null;
        if (uri != null) {
//            bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, holder.btlImage,imagePath));
//            if (bitmap != null) {
//                holder.btlImage.setImageBitmap(bitmap);
//            } else {
                imagePath = AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR + productDO.pagerImage;
//                imagePath = context.getFilesDir().toString()+AppConstants.IMAGE_DIR + productDO.listImage;
//                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, holder.btlImage,imagePath));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(imagePath, options);
                if (bitmap != null) {
                    holder.btlImage.setImageBitmap(bitmap);
                }else
                    holder.btlImage.setImageResource(R.drawable.no_image);
//            }
            /*if(NetworkUtil.isNetworkConnectionAvailable(context))
            {
                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, holder.btlImage,imagePath));
                if (bitmap != null)
                {
                    holder.btlImage.setImageBitmap(bitmap);
                } else
                {
                    holder.btlImage.setImageResource(R.drawable.no_image);
                }
            }
            else
            {
                imagePath = context.getFilesDir().toString()+AppConstants.IMAGE_DIR + productDO.pagerImage;
                bitmap = ((BaseActivity)context).getHttpImageManager().loadImage(new HttpImageManager.LoadRequest(uri, holder.btlImage,imagePath));
                if (bitmap != null) {
                    holder.btlImage.setImageBitmap(bitmap);
                }else
                    holder.btlImage.setImageResource(R.drawable.no_image);
            }*/
        }else{
            holder.btlImage.setImageResource(R.drawable.no_image);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView btlRate,btlSize;
        private ImageView btlImage,ivAddCArtGv,ivGoToCArtGv;
    }

    public void setHmCartListDO(HashMap<String,CartListDO> hmCartListDO){
        this.hmCartListDO = hmCartListDO;
    }

    public void refresh(List<ProductDO> items){
        this.vecProducts = items;
        notifyDataSetChanged();
    }
}
