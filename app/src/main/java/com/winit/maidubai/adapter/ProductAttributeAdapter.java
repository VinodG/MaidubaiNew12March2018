package com.winit.maidubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winit.maidubai.R;
import com.winit.maidubai.dataobject.ProductAttribute;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 8/30/2016.
 */
public class ProductAttributeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProductAttribute> arrProductAttributes;
    public ProductAttributeAdapter(Context context,ArrayList<ProductAttribute> arrProductAttributes){
        this.context = context;
        this.arrProductAttributes = arrProductAttributes;
    }

    @Override
    public int getCount() {
        if(arrProductAttributes!= null)
            return arrProductAttributes.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //need to change as holder
        ProductAttribute productAttribute = arrProductAttributes.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.product_attribute_cell,null);
        ImageView ivIcon            = (ImageView) convertView.findViewById(R.id.ivIcon);
        TextView tvProductAtrrName  = (TextView) convertView.findViewById(R.id.tvProductAtrrName);
        TextView tvDescription      = (TextView)convertView.findViewById(R.id.tvDescription);
        if(!StringUtils.isEmpty(productAttribute.Icon)){
            ivIcon.setVisibility(View.VISIBLE);
            Picasso.with(context).load(ServiceUrls.MAIN_URL + productAttribute.Icon)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.loader)
                    .into(ivIcon);
        }else{
            ivIcon.setVisibility(View.GONE);
        }

        tvProductAtrrName.setText(productAttribute.Name);
        tvDescription.setText(productAttribute.Description);

        return convertView;
    }

}
