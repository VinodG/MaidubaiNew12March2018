package com.winit.maidubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.WeatherDO;

import java.util.List;

/**
 * Created by Jayasai on 6/29/2016.
 */
public class WeatherListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private List<WeatherDO> items;
    public WeatherListAdapter(Context context, List<WeatherDO> items){
        this.context=context;
        this.items=items;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        WeatherDO item=(WeatherDO)getItem(position);
        if(convertView==null){
            convertView= layoutInflater.inflate(R.layout.single_wether_item,null);
            holder=new ViewHolder();

            holder.ivWeather    = (ImageView)convertView.findViewById(R.id.ivWeather);
            holder.tvDays     = (TextView)convertView.findViewById(R.id.tvDAys);
            holder.tvTemp     = (TextView)convertView.findViewById(R.id.tvTemp);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        holder.ivWeather.setImageResource(item.image);
        holder.tvDays.setText(item.day);
        holder.tvTemp.setText(item.getTempture());
        holder.tvDays.setTypeface(AppConstants.DinproRegular);
        holder.tvTemp.setTypeface(AppConstants.DinproRegular);

        return convertView;
    }
    static class ViewHolder {
        private TextView tvDays,tvTemp;
        private ImageView ivWeather;
    }
}
