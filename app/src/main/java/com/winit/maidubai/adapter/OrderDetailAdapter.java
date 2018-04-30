package com.winit.maidubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.maidubai.BaseActivity;
import com.winit.maidubai.R;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.utilities.StringUtils;

import java.util.List;

/**
 * Created by Sridhar.V on 6/30/2016.
 */
public class OrderDetailAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    List<TrxDetailsDO> arrTrxDetailsDOs;
    private Preference preference;
    public OrderDetailAdapter(Context context,List<TrxDetailsDO> arrTrxDetailsDOs)
    {
        this.context=context;
        this.arrTrxDetailsDOs = arrTrxDetailsDOs;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preference = new Preference(context);

    }
    @Override
    public int getCount() {
        if(arrTrxDetailsDOs != null)
            return arrTrxDetailsDOs.size();
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
        Holder holder;
        TrxDetailsDO trxDetailsDO = arrTrxDetailsDOs.get(position);
        if(convertView==null){
            holder=new Holder();
            convertView= inflater.inflate(R.layout.list_order_detail,null);
            holder.noOfOrder=(TextView)convertView.findViewById(R.id.tvOrderProduct);
            holder.eachGallonAmount=(TextView)convertView.findViewById(R.id.tvProductAmt);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }

        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(trxDetailsDO.productALTDescription))
        {
            holder.noOfOrder.setText(trxDetailsDO.productALTDescription+trxDetailsDO.initialQuantity+"X ");
        }else
        {
            holder.noOfOrder.setText(trxDetailsDO.initialQuantity +" X "+ trxDetailsDO.productName);
        }

        if(trxDetailsDO.orderAmount == 0)
            holder.eachGallonAmount.setText(context.getResources().getString(R.string.currency)+" "+((BaseActivity)context).decimalFormat.format(trxDetailsDO.initialQuantity * trxDetailsDO.unitPrice));
        else
            holder.eachGallonAmount.setText(context.getResources().getString(R.string.currency)+" "+((BaseActivity)context).decimalFormat.format(trxDetailsDO.totalAmountWODiscount));
        return convertView;
    }
    static class Holder{
        public TextView noOfOrder;
        public TextView eachGallonAmount;
    }

    public void refresh(List<TrxDetailsDO> arrTrxDetailsDOs){
        this.arrTrxDetailsDOs = arrTrxDetailsDOs;
        notifyDataSetChanged();
    }
}
