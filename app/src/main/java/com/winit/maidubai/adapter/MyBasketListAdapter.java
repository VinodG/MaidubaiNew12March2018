package com.winit.maidubai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winit.maidubai.BaseActivity;
import com.winit.maidubai.MyBasketActivity;
import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Vikas Shah on 6/17/2016.
 */
public class MyBasketListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CartListDO> arrCartListDOs;
    protected Preference preference;
    private DecimalFormat amountFormat;

    public MyBasketListAdapter(Context context, List<CartListDO> arrCartListDOs){
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.arrCartListDOs = arrCartListDOs;
        preference                  = new Preference(this.context);
        this.amountFormat = ((BaseActivity)context).amountFormat;
    }

    @Override
    public int getCount() {
        if(arrCartListDOs != null)
            return arrCartListDOs.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return arrCartListDOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder ;
        final CartListDO cartListDO= (CartListDO) getItem(position);
        if(convertView==null){
//            int no=0;
            convertView=inflater.inflate(R.layout.mybasket_items,null);
            holder=new ViewHolder();
            holder.ivMinus           =(ImageView) convertView.findViewById(R.id.ivMinus);
            holder.ivPlus            =(ImageView) convertView.findViewById(R.id.ivPlus);
            holder.ivDelete            =(ImageView) convertView.findViewById(R.id.ivDelete);
            holder.tvCount           =(TextView)convertView.findViewById(R.id.tvCount);
            holder.tvPrice           =(TextView)convertView.findViewById(R.id.tvPrice);
            holder.ivBottolIcons     =(ImageView)convertView.findViewById(R.id.ivBottolIcons);
            holder.tvQuantity        =(TextView)convertView.findViewById(R.id.tvQuantity);
            holder.tvDescription     =(TextView)convertView.findViewById(R.id.tvDescription);
            holder.tvPrice.setTypeface(AppConstants.DinproMedium);
            holder.tvQuantity.setTypeface(AppConstants.DinproMedium);
            holder.tvDescription.setTypeface(AppConstants.DinproMedium);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }

        String imagePath = AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR + cartListDO.originalImage;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (bitmap != null) {
            holder.ivBottolIcons.setImageBitmap(bitmap);
        }else
            holder.ivBottolIcons.setImageResource(R.drawable.no_image);

        holder.tvCount.setText(cartListDO.quantity+"");
        holder.tvPrice.setText(context.getResources().getString(R.string.total_colon)+" "+context.getResources().getString(R.string.currency)+" "+(cartListDO.totalPrice));
//        holder.tvPrice.setText(context.getResources().getString(R.string.total_colon)+" "+context.getResources().getString(R.string.currency)+" "+
//                (((BaseActivity)context).calculateAmount(cartListDO.quantity,(float) cartListDO.unitPrice,cartListDO.vatPerc)  ));
        holder.tvQuantity.setText(context.getResources().getString(R.string.currency)+" "+cartListDO.unitPrice+"");
        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(cartListDO.AltDescription))
        {
            holder.tvDescription.setText(cartListDO.AltDescription);
        }else
        {
            holder.tvDescription.setText(cartListDO.productName);
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyBasketActivity)context).confirmToRemove(cartListDO);
            }
        });
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartListDO.quantity>1) {
                    cartListDO.quantity--;
                    ((MyBasketActivity) context).updateProductCount(cartListDO);
                }else if(cartListDO.quantity == 1){
                    if(AppConstants.MYBASKET==2){
                        ((MyBasketActivity)context).confirmToRemove(cartListDO);
                    }
                }
            }
        });
        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartListDO.quantity++;
                ((MyBasketActivity) context).updateProductCount(cartListDO);
            }
        });

        return convertView;
    }
    static class ViewHolder {
        private ImageView ivPlus,ivMinus,ivDelete;
        private  TextView tvCount,tvPrice,tvQuantity,tvDescription;
        private ImageView ivBottolIcons;
    }

    public void refresh(List<CartListDO> arrCartListDOs){
        this.arrCartListDOs = arrCartListDOs;
        notifyDataSetChanged();
    }
}
