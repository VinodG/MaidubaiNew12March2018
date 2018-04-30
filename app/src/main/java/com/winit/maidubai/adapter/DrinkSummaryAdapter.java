package com.winit.maidubai.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winit.maidubai.R;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 08-07-2016.
 */
public class DrinkSummaryAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<HydrationMeterReadingDO> arrDrinkSummary;
    private int pointer = 1;

    public DrinkSummaryAdapter(Context context,ArrayList<HydrationMeterReadingDO> arrDrinkSummary){
        this.context = context;
        this.arrDrinkSummary = arrDrinkSummary;
    }
    
    @Override
    public int getCount() {
        if(arrDrinkSummary != null)
            return arrDrinkSummary.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    public int getColor(int position){
        switch (position){
            case 1:
                pointer++;
                return context.getResources().getColor(R.color.skyblue);
            case 2:
                pointer++;
                return context.getResources().getColor(R.color.light_drak);
            case 3:
                pointer++;
                return context.getResources().getColor(R.color.drak);
            case 4:
                pointer++;
                return context.getResources().getColor(R.color.last);
            default:
                pointer = 2;
                return context.getResources().getColor(R.color.skyblue);

        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HydrationMeterReadingDO drinkSummary = arrDrinkSummary.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.drink_summary_cell,null);
        ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressbar);
        TextView tvDayMothLabel = (TextView)convertView.findViewById(R.id.tvDayMothLabel);
        ImageView ivSucces = (ImageView)convertView.findViewById(R.id.ivSucces);
        tvDayMothLabel.setText(drinkSummary.ConsumptionDate + " - " + (int) drinkSummary.WaterConsumedPercent + " %");
        progressBar.setProgress((int)drinkSummary.WaterConsumedPercent);
        if(drinkSummary.WaterConsumedPercent >= 100){
            ivSucces.setVisibility(View.VISIBLE);
        }else{
            ivSucces.setVisibility(View.GONE);
        }

        if(drinkSummary.WaterConsumedPercent < 50){
            tvDayMothLabel.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            tvDayMothLabel.setTextColor(context.getResources().getColor(R.color.white));
        }

        progressBar.getProgressDrawable().setColorFilter(getColor(pointer), PorterDuff.Mode.SRC_IN);
        return convertView;
    }

    public void refresh(ArrayList<HydrationMeterReadingDO> arrDrinkSummary){
        this.arrDrinkSummary = arrDrinkSummary;
        notifyDataSetChanged();
    }
}
