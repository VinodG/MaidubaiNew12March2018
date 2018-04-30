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
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;

/**
 * Created by girish Velivela on 02-09-2016.
 */
public class SelectEmiratesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Emirates> arrEmirateDOs;

    public SelectEmiratesAdapter(Context context, ArrayList<Emirates> arrEmirateDOs){
        this.context = context;
        this.arrEmirateDOs = arrEmirateDOs;
    }

    @Override
    public int getCount() {
        if(arrEmirateDOs != null)
            return arrEmirateDOs.size();
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
        Emirates emirates = arrEmirateDOs.get(position);
        if(convertView==null){
            holder=new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_cities, null);
            holder.tvArea =(TextView)convertView.findViewById(R.id.tvSelectCity);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        String language = ((BaseActivity)context).getPreference().getStringFromPreference(Preference.LANGUAGE, "");
        if(language.equalsIgnoreCase("ar"))
            holder.tvArea.setText(StringUtils.isEmpty(emirates.ArabicName)?emirates.Name:emirates.ArabicName);
        else
            holder.tvArea.setText(emirates.Name);
        convertView.setTag(R.string.area,emirates);
        return convertView;
    }

    public void refresh(ArrayList<Emirates> arrEmirateDOs){
        this.arrEmirateDOs = arrEmirateDOs;
        notifyDataSetChanged();
    }

    private static class Holder{
        public TextView tvArea;
    }
}
