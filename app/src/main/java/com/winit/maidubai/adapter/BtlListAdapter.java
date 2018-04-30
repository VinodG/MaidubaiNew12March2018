package com.winit.maidubai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class BtlListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ProductDO> vecProducts;
    private CustomDialog customDialog;
    private HashMap<String,CartListDO> hmCartListDO;
    private int mHeight, mWidth;
    private Preference preference;

    public BtlListAdapter(Context context, List<ProductDO> vecProducts){
        this.context=context;
        this.vecProducts = vecProducts;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Drawable d = context.getResources().getDrawable(R.drawable.listbot1);
        mHeight = d.getIntrinsicHeight();
        mWidth = d.getIntrinsicWidth();
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
            convertView= layoutInflater.inflate(R.layout.single_list_btl,null);
            holder=new ViewHolder();

            holder.llItemDetails    = (LinearLayout)convertView.findViewById(R.id.llItemDetails);
            holder.btlImage    = (ImageView)convertView.findViewById(R.id.lvBtlIv);
            holder.btlRate     = (TextView)convertView.findViewById(R.id.lvBtlRateTv);
            holder.btlSize     = (TextView)convertView.findViewById(R.id.lvBtlSizeTv);
            holder.tvBtlSubSize     = (TextView)convertView.findViewById(R.id.tvBtlSubSize);
            holder.btnAddCArt     = (ImageView)convertView.findViewById(R.id.btnAddCArt);
            holder.ivGoToCArtList     = (ImageView)convertView.findViewById(R.id.ivGoToCArtList);

            holder.btlSize.setTypeface(AppConstants.DinproRegular);
//            holder.tvBtlSubSize.setTypeface(AppConstants.DinproRegular);
            holder.btlRate.setTypeface(AppConstants.DinproMedium);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
//        if(StringUtils.isEmpty(productDO.description))
        String name = "";
        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(productDO.AltDescription))
        {
            name = productDO.AltDescription;
            holder.btlSize.setText(name);
        }else
        {
            name = productDO.name;
            if(name.contains("(")) {
                holder.btlSize.setText(Html.fromHtml(name.substring(0, name.indexOf("("))+" <small>"+name.substring(name.indexOf("("),name.lastIndexOf(")")+1)));
//            holder.btlSize.setText(Html.fromHtml() name.substring(0, name.indexOf("(")));
//            holder.tvBtlSubSize.setText(name.substring(name.indexOf("("),name.lastIndexOf(")")+1));
            }else if(name.contains("-")){

                holder.btlSize.setText(Html.fromHtml(name.substring(0, name.indexOf("-")) + " <small>" + name.substring(name.indexOf("-"),name.length())));
//           holder.btlSize.setText(name.substring(0,name.indexOf("-")));
//            holder.tvBtlSubSize.setText(name.substring(name.indexOf("-"),name.length()));
            }else{
                holder.btlSize.setText(name);
//            holder.tvBtlSubSize.setText("");
            }
        }
//        else
//        holder.btlSize.setText(productDO.name+"("+productDO.description+")");
        holder.llItemDetails.setOnClickListener(new View.OnClickListener() {
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
                    ((DashBoardActivity)context).showProductDescription(name, productDO.btlSize, productDO.arrProductAttributes);
                }*/
            }
        });
        holder.btlRate.setText(context.getResources().getString(R.string.currency)+productDO.price);
        if(hmCartListDO!= null && hmCartListDO.containsKey(productDO.code)){
            holder.btnAddCArt.setVisibility(View.GONE);
            holder.ivGoToCArtList.setVisibility(View.VISIBLE);

        } else{
            holder.btnAddCArt.setVisibility(View.VISIBLE);
            holder.ivGoToCArtList.setVisibility(View.GONE);
        }
        holder.btlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(productDO.categoryId == 106)
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
        holder.btnAddCArt.setOnClickListener(new View.OnClickListener() {
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

        holder.ivGoToCArtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MyBasketActivity.class);
                context.startActivity(intent);
            }
        });

        //Picasso.with(context).load(ServiceUrls.MAIN_URL + productDO.listImage).resize(mWidth, mHeight).into(holder.btlImage);
        /*MaiDubaiApplication.picasso.load(ServiceUrls.MAIN_URL + productDO.listImage)
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

    public void setHmCartListDO(HashMap<String,CartListDO> hmCartListDO){
        this.hmCartListDO = hmCartListDO;
    }

    static class ViewHolder {
        private TextView btlRate,btlSize,tvBtlSubSize;
        private ImageView btlImage,btnAddCArt,ivGoToCArtList;
        private LinearLayout llItemDetails;
    }
    public void refresh(List<ProductDO> items){
        this.vecProducts = items;
        notifyDataSetChanged();
    }
}
