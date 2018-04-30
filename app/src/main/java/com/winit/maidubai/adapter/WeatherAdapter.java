package com.winit.maidubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.maidubai.R;
import com.winit.maidubai.dataobject.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by WINIT on 26-Jul-16.
 */
public class WeatherAdapter extends BaseAdapter {
    private ArrayList<Weather> listData;

    private LayoutInflater layoutInflater;
    Context context;
    public WeatherAdapter(Context context, ArrayList<Weather> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        if (listData != null)
            return listData.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.single_wether_item, null);
            holder = new ViewHolder();
            holder.ivWeather = (ImageView) convertView.findViewById(R.id.ivWeather);
            holder.tvDays = (TextView) convertView.findViewById(R.id.tvDAys);
            holder.tvTemp = (TextView) convertView.findViewById(R.id.tvTemp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Weather weather = listData.get(position);
        holder.tvDays.setText(weather.Day);
        holder.tvTemp.setText(String.format(Locale.ENGLISH,"%.0f", weather.tempmax) + (char) 0x00B0 + "/" + String.format(Locale.ENGLISH,"%.0f", weather.tempmin) + (char) 0x00B0 );
//        holder.tvTemp.setText((int)weather.temperature +"");
        if (weather.iconId != 0)
            holder.ivWeather.setImageResource(weather.iconId);

        return convertView;
    }

    static class ViewHolder {
        private TextView tvDays, tvTemp;
        private ImageView ivWeather;
    }

    public void refresh(ArrayList<Weather> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

}

