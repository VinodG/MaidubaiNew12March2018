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
import com.winit.maidubai.dataobject.AreaDO;

import java.util.ArrayList;

/**
 * Created by girish Velivela on 02-09-2016.
 */
public class SelectAreaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AreaDO> arrAreaDOs;

    public SelectAreaAdapter(Context context,ArrayList<AreaDO> arrAreaDOs){
        this.context = context;
        this.arrAreaDOs = arrAreaDOs;
    }

    @Override
    public int getCount() {
        if(arrAreaDOs != null)
            return arrAreaDOs.size();
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
        AreaDO areaDo = arrAreaDOs.get(position);
        if(convertView==null){
            holder=new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_cities, null);
            holder.tvArea =(TextView)convertView.findViewById(R.id.tvSelectCity);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }

        String language = ((BaseActivity)context).getPreference().getStringFromPreference(Preference.LANGUAGE,"");
        if(language.equalsIgnoreCase("ar"))
            holder.tvArea.setText(areaDo.ArabicName);
        else
            holder.tvArea.setText(areaDo.Name);
        convertView.setTag(R.string.area,areaDo);
        return convertView;
    }

    public void refresh(ArrayList<AreaDO> arrAreaDOs){
        this.arrAreaDOs = arrAreaDOs;
        notifyDataSetChanged();
    }

    private static class Holder{
        public TextView tvArea;
    }
}
