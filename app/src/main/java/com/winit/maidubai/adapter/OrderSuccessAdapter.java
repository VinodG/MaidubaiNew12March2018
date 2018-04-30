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
import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WINIT on 05-Aug-16.
 */
public class OrderSuccessAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    private List<TrxDetailsDO> arrTrxDetailDOs;
    private Preference preference;


    public OrderSuccessAdapter(Context context, ArrayList<TrxDetailsDO> arrTrxDetailDOs) {
        this.context = context;
        this.arrTrxDetailDOs = arrTrxDetailDOs;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        preference = new Preference(context);
    }

    @Override
    public int getCount() {
        if (arrTrxDetailDOs != null)
            return arrTrxDetailDOs.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return arrTrxDetailDOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final TrxDetailsDO trxDetailsDO = (TrxDetailsDO) getItem(position);
        if (convertView == null) {
//            int no=0;
            convertView = inflater.inflate(R.layout.order_success_row, null);
            holder = new ViewHolder();
            holder.tvJarQuantity = (TextView) convertView.findViewById(R.id.tvJarQuantity);
            holder.tvQuantityCount = (TextView) convertView.findViewById(R.id.tvQuantityCount);
            holder.ivBottolIcons = (ImageView) convertView.findViewById(R.id.ivBottolIcons);
            holder.tvUnitPrice = (TextView) convertView.findViewById(R.id.tvUnitPrice);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tvTotalPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(trxDetailsDO.productALTDescription))
        {
            holder.tvJarQuantity.setText(trxDetailsDO.productALTDescription);
        }else
        {
            holder.tvJarQuantity.setText(trxDetailsDO.productName);
        }

        String format = "%-14.14s %2.2s %-14.14s";
        holder.tvQuantityCount.setText(String.format(format,context.getResources().getString(R.string.Quantity),": ",String.valueOf(trxDetailsDO.quantity)));
        holder.tvUnitPrice.setText(String.format(format,context.getResources().getString(R.string.UnitPrice),": ",((BaseActivity)context).decimalFormat.format(trxDetailsDO.unitPrice)));
        holder.tvTotalPrice.setText(String.format(format,context.getResources().getString(R.string.TotalPrice),": ",((BaseActivity)context).decimalFormat.format(trxDetailsDO.orderAmount)));

        holder.tvJarQuantity.setTypeface(AppConstants.DinproMedium);
        holder.tvQuantityCount.setTypeface(AppConstants.DinproMedium);
        holder.tvUnitPrice.setTypeface(AppConstants.DinproMedium);
        holder.tvTotalPrice.setTypeface(AppConstants.DinproMedium);

        String imagePath = AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR + trxDetailsDO.originalImage;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (bitmap != null) {
            holder.ivBottolIcons.setImageBitmap(bitmap);
        }else
            holder.ivBottolIcons.setImageResource(R.drawable.no_image);

        return convertView;
    }

    static class ViewHolder {
        private TextView tvJarQuantity, tvQuantityCount, tvUnitPrice, tvTotalPrice;
        private ImageView ivBottolIcons;
    }
}
