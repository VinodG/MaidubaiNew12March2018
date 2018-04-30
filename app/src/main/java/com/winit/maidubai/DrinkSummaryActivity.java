package com.winit.maidubai;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.winit.maidubai.adapter.DrinkSummaryAdapter;
import com.winit.maidubai.dataaccesslayer.HydrationMeterDA;
import com.winit.maidubai.dataobject.HydrationMeterReadingDO;
import com.winit.maidubai.utilities.CalendarUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Girish Velivela on 08-07-2016.
 */
public class DrinkSummaryActivity extends BaseActivity{

    private LinearLayout llDrinkSummary,llNoData;
    private ListView lvDrinkSummary;
    private ArrayList<HydrationMeterReadingDO> arrDaySummary;
    private ArrayList<HydrationMeterReadingDO> arrMonthSummary;
    private DrinkSummaryAdapter drinkSummaryAdapter;

    @Override
    public void initialise() {
        llDrinkSummary =(LinearLayout)inflater.inflate(R.layout.drink_summary,null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(llDrinkSummary, param);
        llToggle.setVisibility(View.VISIBLE);
        setStatusBarColor();
        setTypeFaceNormal(llDrinkSummary);
        llMainCenter.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);
//        ivSearch.setImageResource(R.drawable.close_white);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        drinkSummaryAdapter = new DrinkSummaryAdapter(DrinkSummaryActivity.this,null);
        lockDrawer("DrinkSummaryActivity");
        initialiseControls();
        loadData();
        tvToggleLeft.setTextColor(getResources().getColor(R.color.white));
        tvToggleRight.setTextColor(getResources().getColor(R.color.light_weight));
    }

    @Override
    public void initialiseControls() {

        lvDrinkSummary = (ListView)llDrinkSummary.findViewById(R.id.lvDrinkSummary);
        llNoData       = (LinearLayout)llDrinkSummary.findViewById(R.id.llNoData);
        llToggleLeft.setBackgroundResource(R.drawable.toggle_select);
        llToggleRight.setBackgroundResource(R.drawable.toggle_unselect);
        tvToggleLeft.setText(getString(R.string.by_day));
        tvToggleRight.setText(getString(R.string.by_month));
        lvDrinkSummary.setAdapter(drinkSummaryAdapter);
        llToggleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llToggleLeft.setEnabled(false);
                llToggleLeft.setClickable(false);
                llToggleRight.setEnabled(true);
                llToggleRight.setClickable(true);
                llToggleLeft.setBackgroundResource(R.drawable.toggle_select);
                llToggleRight.setBackgroundResource(R.drawable.toggle_unselect);
                tvToggleLeft.setTextColor(getResources().getColor(R.color.white));
                tvToggleRight.setTextColor(getResources().getColor(R.color.light_weight));
                if(arrDaySummary !=null && arrDaySummary.size() >0){
                    lvDrinkSummary.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    drinkSummaryAdapter.refresh(arrDaySummary);
                }else{
                    lvDrinkSummary.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }
        });

        llToggleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llToggleLeft.setEnabled(true);
                llToggleLeft.setClickable(true);
                llToggleRight.setEnabled(false);
                llToggleRight.setClickable(false);
                llToggleLeft.setBackgroundResource(R.drawable.toggle_unselect_left);
                llToggleRight.setBackgroundResource(R.drawable.toggle_select_right);
                tvToggleRight.setTextColor(getResources().getColor(R.color.white));
                tvToggleLeft.setTextColor(getResources().getColor(R.color.light_weight));

                if(arrMonthSummary !=null && arrMonthSummary.size() >0){
                    lvDrinkSummary.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                    drinkSummaryAdapter.refresh(arrMonthSummary);
                }else{
                    lvDrinkSummary.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    @Override
    public void loadData() {

       /* int current[] = CalendarUtil.getCurrentDayMonthYear();
        String currentMonth = CalendarUtil.getCurrentMonth();
        arrDaySummary = new ArrayList<>();
        arrMonthSummary = new ArrayList<>();
        int Min =20;
        int Max = 100;
        for(int i = 1; i<current[0];i++){
            String[] dayWise = new String[2];
            dayWise[0] = i +" "+currentMonth;
            dayWise[1] = (Min + (int)(Math.random() * ((Max - Min) + 1)))+"";
            arrDaySummary.add(dayWise);
        }
        for(int i = 0; i<current[1];i++){
            String[] monthWise = new String[2];
            monthWise[0] = CalendarUtil.getMonth(i, current[2]);
            monthWise[1] = (Min + (int)(Math.random() * ((Max - Min) + 1)))+"";
            arrMonthSummary.add(monthWise);
        }*/
        showLoader(getString(R.string.Loading_Data));
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object[] drinkSumarray = new HydrationMeterDA(DrinkSummaryActivity.this).getDrinkSummary(CalendarUtil.getDate(new Date(),CalendarUtil.MM_YYYY_PATTERN));
                arrDaySummary = (ArrayList)drinkSumarray[0];
                arrMonthSummary = (ArrayList)drinkSumarray[1];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoader();
                        if(arrDaySummary !=null && arrDaySummary.size() >0){
                            lvDrinkSummary.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            drinkSummaryAdapter.refresh(arrDaySummary);
                        }else{
                            lvDrinkSummary.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

}
