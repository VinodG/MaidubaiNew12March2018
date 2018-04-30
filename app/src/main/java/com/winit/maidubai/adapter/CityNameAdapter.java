package com.winit.maidubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.maidubai.R;
import com.winit.maidubai.SearchCityActivity;

import java.util.ArrayList;

/**
 * Created by Sridhar.V on 7/5/2016.
 */
public class CityNameAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<String> arrCityNames;
    ArrayList<SearchCityActivity.City> arrCities;

    public CityNameAdapter(Context context,ArrayList<String> arrCityNames,ArrayList<SearchCityActivity.City> arrCities)
    {
        this.context=context;
        this.arrCityNames = arrCityNames;
        this.arrCities = arrCities;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CityNameAdapter(Context context,ArrayList<String> arrCityNames)
    {
        this.context=context;
        this.arrCityNames = arrCityNames;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(arrCities != null)
            return arrCities.size();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
//        String cityName= arrCityNames.get(position);
        SearchCityActivity.City city = arrCities.get(position);
        if(convertView==null){
            holder=new Holder();
            convertView= inflater.inflate(R.layout.list_cities,null);
            holder.cityName=(TextView)convertView.findViewById(R.id.tvSelectCity);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        holder.cityName.setText(city.cityName);
        convertView.setTag(R.string.city_name,city);
        return convertView;
    }
    static class Holder{
        public TextView cityName;
    }

   /* public void refresh(ArrayList<String> arrCityNames){
        this.arrCityNames = arrCityNames;
        notifyDataSetChanged();
    }*/

    public void refresh(ArrayList<SearchCityActivity.City> arrCities){
        this.arrCities = arrCities;
        notifyDataSetChanged();
    }
}
