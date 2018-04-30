package com.winit.maidubai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataobject.AddressBookDO;
import com.winit.maidubai.dataobject.TrxDetailsDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sridhar.V on 6/29/2016.
 */
public class MyPastOrderAddapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    List<TrxHeaderDO> arrTrxHeaderDOs;
    public MyPastOrderAddapter(Context context,List<TrxHeaderDO> arrTrxHeaderDOs)
    {
        this.context=context;
        this.arrTrxHeaderDOs = arrTrxHeaderDOs;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if(arrTrxHeaderDOs != null)
            return arrTrxHeaderDOs.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String setOrderDetails(ArrayList<TrxDetailsDO> trxDetailsDOs){
        Preference preference = ((BaseActivity)context).preference;
        if (trxDetailsDOs != null && trxDetailsDOs.size() > 0) {
            String items="";
            for(TrxDetailsDO trxDetailsDO : trxDetailsDOs)
            {
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(trxDetailsDO.productALTDescription))
                {
                    items = items+trxDetailsDO.productALTDescription+", ";
                }else
                {
                    items = items+trxDetailsDO.productName+", ";
                }
            }
            return items.substring(0,items.length()-2);
        }else{
            return context.getResources().getString(R.string.na);
        }
    }

    public String getAddressDetails(ArrayList<AddressBookDO> trxAddressBookDOs){
        Preference preference = ((BaseActivity)context).preference;
        if (trxAddressBookDOs != null && trxAddressBookDOs.size() > 0) {
            AddressBookDO addressBookDO = trxAddressBookDOs.get(0);
            if(addressBookDO != null){
                String address = "";
                if(!StringUtils.isEmpty(addressBookDO.FlatNumber))
                    address += addressBookDO.FlatNumber;
                if(!StringUtils.isEmpty(addressBookDO.VillaName)) {
                    if(StringUtils.isEmpty(address))
                        address +=  addressBookDO.VillaName;
                    else
                        address += ", " + addressBookDO.VillaName;
                }

                if(!StringUtils.isEmpty(addressBookDO.Street)) {

                    if(StringUtils.isEmpty(address)) {
                        address +=  addressBookDO.Street;
                    }
                    else {
                        address += ", " + addressBookDO.Street;
                    }
                }
                if(!StringUtils.isEmpty(addressBookDO.addressLine3)) {
                    if(StringUtils.isEmpty(address)) {
                        address +=  addressBookDO.addressLine3;
                    }
                    else {
                        address += ", " + addressBookDO.addressLine3;
                    }
                }
                if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar"))
                {

                    if(!StringUtils.isEmpty(addressBookDO.addressLine2_Arabic)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine2_Arabic;
                        else
                            address += ", " + addressBookDO.addressLine2_Arabic;
                    }
                    else
                    {
                        if(!StringUtils.isEmpty(addressBookDO.addressLine2)) {
                            if(StringUtils.isEmpty(address))
                                address +=  addressBookDO.addressLine2;
                            else
                                address += ", " + addressBookDO.addressLine2;
                        }
                    }


                    if(!StringUtils.isEmpty(addressBookDO.addressLine1_Arabic)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine1_Arabic;
                        else
                            address += ", " + addressBookDO.addressLine1_Arabic;
                    }
                    else
                    {
                        if(!StringUtils.isEmpty(addressBookDO.addressLine1)) {
                            if(StringUtils.isEmpty(address))
                                address +=  addressBookDO.addressLine1;
                            else
                                address += ", " + addressBookDO.addressLine1;
                        }
                    }
                    if(!StringUtils.isEmpty(addressBookDO.addressLine4_Arabic)){
                        address += ","+addressBookDO.addressLine4_Arabic;
                    }
                    else
                    {
                        if(!StringUtils.isEmpty(addressBookDO.addressLine4))
                            address += ", "+addressBookDO.addressLine4;
                    }
                }
                else
                {
                    if(!StringUtils.isEmpty(addressBookDO.addressLine2)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine2;
                        else
                            address += ", " + addressBookDO.addressLine2;
                    }
                    if(!StringUtils.isEmpty(addressBookDO.addressLine1)) {
                        if(StringUtils.isEmpty(address))
                            address +=  addressBookDO.addressLine1;
                        else
                            address += ", " + addressBookDO.addressLine1;
                    }
                    if(!StringUtils.isEmpty(addressBookDO.addressLine4))
                        address += ", "+addressBookDO.addressLine4;
                }

                return address;
            }
        }else{
            return "N/A";
        }
        return "N/A";
    }

    public void toggeleViews(Holder holder,TrxHeaderDO trxHeaderDO){

        if(trxHeaderDO.trxStatus == 100 || trxHeaderDO.isCancelled){
            holder.btnTrackOrder.setVisibility(View.GONE);
            holder.tvOrderCancel.setVisibility(View.VISIBLE);

            if(trxHeaderDO.trxStatus == 100)
            {
                holder.btnRepeatOrder.setVisibility(View.VISIBLE);
                holder.btn_recurr_order.setVisibility(View.VISIBLE);
                holder.tvOrderCancel.setTextColor(context.getResources().getColor(R.color.paid_via_text));
                holder.tvOrderCancel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_deliver_cell,0,0,0);
                holder.tvOrderCancel.setText(context.getResources().getString(R.string.orderdelivered));
            }
            else
            {
                holder.btnRepeatOrder.setVisibility(View.GONE);
                holder.btn_recurr_order.setVisibility(View.GONE);
                holder.tvOrderCancel.setTextColor(context.getResources().getColor(R.color.order_detail_red));
                holder.tvOrderCancel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_cancel_cell,0,0,0);
                holder.tvOrderCancel.setText(context.getResources().getString(R.string.order_cancel));
            }
        } else{
            holder.tvOrderCancel.setVisibility(View.GONE);
            holder.btnTrackOrder.setVisibility(View.VISIBLE);
            holder.btnRepeatOrder.setVisibility(View.GONE);
            if(trxHeaderDO.trxStatus == 5)
                holder.btn_recurr_order.setVisibility(View.VISIBLE);
            else
                holder.btn_recurr_order.setVisibility(View.GONE);
        }

        if(trxHeaderDO.recurringOrderDO != null){
            holder.btn_recurr_order.setVisibility(View.GONE);
            holder.btEdit.setVisibility(View.GONE);
            holder.btnRepeatOrder.setVisibility(View.GONE);
            holder.tvCancelRecurring.setVisibility(View.VISIBLE);
            holder.llrecurringtype.setVisibility(View.VISIBLE);

            if(StringUtils.isEmpty(trxHeaderDO.recurringOrderDO.Frequency))
                holder.tv_recurring_type.setText(context.getResources().getString(R.string.na));
            else
                holder.tv_recurring_type.setText(getArabicFrequency(trxHeaderDO.recurringOrderDO.Frequency));

            if(trxHeaderDO.recurringOrderDO.RemovedFromRecurring){
                holder.tvOrderCancel.setVisibility(View.VISIBLE);
                holder.tvCancelRecurring.setVisibility(View.GONE);
                holder.tvOrderCancel.setTextColor(context.getResources().getColor(R.color.order_detail_red));
                holder.tvOrderCancel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_cancel_cell,0,0,0);
                if(trxHeaderDO.isCancelled)
                    holder.tvOrderCancel.setText(context.getResources().getString(R.string.order_cancel));
                else
                    holder.tvOrderCancel.setText(context.getResources().getString(R.string.Recurr_Removed));
            }else if(trxHeaderDO.trxStatus != 100){
                holder.tvOrderCancel.setVisibility(View.GONE);
                holder.btnTrackOrder.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        TrxHeaderDO  trxHeaderDO = arrTrxHeaderDOs.get(position);
        if(convertView==null){
            holder=new Holder();
            convertView= inflater.inflate(R.layout.list_past_order,null);
            holder.orderNo=(TextView)convertView.findViewById(R.id.tvOrdercode);
            holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
            holder.bottelPrice=(TextView)convertView.findViewById(R.id.tvBottelPrice);
            holder.bottelSize=(TextView)convertView.findViewById(R.id.tvBottelSize);
            holder.tvaddressOrder=(TextView)convertView.findViewById(R.id.tvaddressOrder);
            holder.tvOrderCancel=(TextView)convertView.findViewById(R.id.tvOrderCancel);
            holder.tvSpecialIns=(TextView)convertView.findViewById(R.id.tvSpecialIns);
            holder.date=(TextView)convertView.findViewById(R.id.tvDate);
            holder.tv_view_detail =(TextView) convertView.findViewById(R.id.tv_view_detail);
            holder.tvCancelRecurring =(TextView) convertView.findViewById(R.id.tvCancelRecurring);
            holder.btnTrackOrder=(Button)convertView.findViewById(R.id.btn_track_order);
            holder.btn_recurr_order=(Button)convertView.findViewById(R.id.btn_recurr_order);
            holder.btnRepeatOrder=(Button)convertView.findViewById(R.id.btnRepeatOrder);
            holder.btEdit=(Button)convertView.findViewById(R.id.btEdit);
            holder.llrecurringtype = (LinearLayout)convertView.findViewById(R.id.llrecurringtype);
            holder.llSpecialInstruction = (LinearLayout)convertView.findViewById(R.id.llSpecialInstruction);
            holder.tv_recurring_type = (TextView) convertView.findViewById(R.id.tv_recurring_type);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }

        holder.orderNo.setTypeface(AppConstants.DinproMedium);
        holder.tv_view_detail.setTypeface(AppConstants.DinproMedium);
        holder.tvName.setTypeface(AppConstants.DinproRegular);
        holder.tvOrderCancel.setTypeface(AppConstants.DinproRegular);
        holder.tvaddressOrder.setTypeface(AppConstants.DinproRegular);
        holder.date.setTypeface(AppConstants.DinproRegular);
        holder.bottelSize.setTypeface(AppConstants.DinproRegular);
        holder.tvSpecialIns.setTypeface(AppConstants.DinproRegular);

        Preference preference = new Preference(context);
        String name = preference.getStringFromPreference(Preference.CUSTOMER_NAME,"");
        if(!StringUtils.isEmpty(name))
            holder.tvName.setText(name);
        if(StringUtils.isEmpty(trxHeaderDO.orderName))
            holder.orderNo.setText(trxHeaderDO.trxDisplayCode);
        else
            holder.orderNo.setText(trxHeaderDO.orderName+" ("+trxHeaderDO.trxDisplayCode+")");
        if(trxHeaderDO.isCancelled)
            holder.date.setText(CalendarUtil.convertToTrxDateToShowNew(trxHeaderDO.CancelledOn,Locale.getDefault()));
        else
        holder.date.setText(CalendarUtil.convertToTrxDateToShow(trxHeaderDO.trxDate,Locale.getDefault()));
        //need to change here
        holder.bottelPrice.setText(context.getResources().getString(R.string.currency)+" "+ ((BaseActivity) context).decimalFormat.format(trxHeaderDO.totalTrxAmount) + "");
//        holder.bottelPrice.setText(context.getResources().getString(R.string.currency)+" "+ ((BaseActivity) context).decimalFormat.format(trxHeaderDO.totalTrxAmount+trxHeaderDO.TotalVAT) + "");
        holder.bottelSize.setText(setOrderDetails(trxHeaderDO.trxDetailsDOs));

        if(StringUtils.isEmpty(trxHeaderDO.specialInstructions))
            holder.llSpecialInstruction.setVisibility(View.GONE);
        else {
            holder.llSpecialInstruction.setVisibility(View.VISIBLE);
            holder.tvSpecialIns.setText(trxHeaderDO.specialInstructions);
        }
        holder.btEdit.setVisibility(View.VISIBLE);
        holder.tvaddressOrder.setText(getAddressDetails(trxHeaderDO.trxAddressBookDOs));


        toggeleViews(holder,trxHeaderDO);


        holder.tv_view_detail.setTag(R.string.ordered_products, trxHeaderDO);
        holder.btnTrackOrder.setTag(R.string.ordered_products, trxHeaderDO);
        holder.btnRepeatOrder.setTag(R.string.ordered_products,trxHeaderDO);
        holder.btn_recurr_order.setTag(R.string.ordered_products,trxHeaderDO);
        holder.tvCancelRecurring.setTag(R.string.ordered_products,trxHeaderDO);
        holder.btEdit.setTag(R.string.ordered_products,trxHeaderDO);

        holder.tv_view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderDetailActivity.class);
                TrxHeaderDO trxHeaderDO = (TrxHeaderDO) v.getTag(R.string.ordered_products);
                i.putExtra("Order", trxHeaderDO);
                context.startActivity(i);
            }
        });
        holder.btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrxHeaderDO trxHeaderDO = (TrxHeaderDO) v.getTag(R.string.ordered_products);
                Intent i = new Intent(context, OrderCancelActivity.class);
                i.putExtra("Order", trxHeaderDO);
                context.startActivity(i);
            }
        });
        holder.tvCancelRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrxHeaderDO trxHeaderDO = (TrxHeaderDO) view.getTag(R.string.ordered_products);
                ((MyRecurringOrdersActivity)context).cancelOrder(trxHeaderDO);
            }
        });
        holder.btnRepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyOrdersActivity) context).repeatorder((TrxHeaderDO) v.getTag(R.string.ordered_products));
            }
        });
        holder.btn_recurr_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyOrdersActivity) context).recurrorder((TrxHeaderDO) v.getTag(R.string.ordered_products));
            }
        });
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((BaseActivity)context).checkNetworkConnection())
                {

                    ((MyOrdersActivity) context).editOrder((TrxHeaderDO) view.getTag(R.string.ordered_products));
                }
                else
                {
                    ((BaseActivity)context).showAlert("Please Check Your Internet Connection",null);
                }
            }
        });
        return convertView;
    }

    static class Holder{
        public TextView orderNo,tvName;
        public TextView bottelPrice;
        public TextView bottelSize,tvaddressOrder,tvOrderCancel,tvSpecialIns,tvCancelRecurring;
        public TextView date,tv_view_detail,tv_recurring_type;
        public Button btnTrackOrder,btnRepeatOrder,btn_recurr_order,btEdit;
        public LinearLayout llrecurringtype,llSpecialInstruction;
    }

    public void refresh(List<TrxHeaderDO> arrTrxHeaderDOs){
        this.arrTrxHeaderDOs = arrTrxHeaderDOs;
        notifyDataSetChanged();
    }

    private String getArabicFrequency(String engFrequency){
        String arabicFrequency = "";

        if(engFrequency.equalsIgnoreCase("Once in a Week"))
            arabicFrequency = context.getResources().getString(R.string.Once_in_a_Week);
        else if(engFrequency.equalsIgnoreCase("Once in two Weeks"))
            arabicFrequency = context.getResources().getString(R.string.Once_in_two_Weeks);
        else if(engFrequency.equalsIgnoreCase("Once in 3 Weeks"))
            arabicFrequency = context.getResources().getString(R.string.Once_in_3_Weeks);
        else if(engFrequency.equalsIgnoreCase("Once in a Month"))
            arabicFrequency = context.getResources().getString(R.string.Once_in_a_Month);

        return arabicFrequency;
    }
}
