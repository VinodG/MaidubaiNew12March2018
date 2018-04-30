package com.winit.maidubai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CustomerDA;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.CalendarlistDO;
import com.winit.maidubai.dataobject.DeliveryDay;
import com.winit.maidubai.dataobject.RecurringOrderDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.dataobject.TrxHeaderDO;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Girish Velivela on 27-09-2016.
 */
public class DeliveryDaysActivity extends BaseActivity
{

    private LinearLayout llDelieveryDays;
    private TextView tvDate,tvNoOfDays;
    private ImageView tvPrev,tvNext;
    private GridView gvDeliveryDays;
    private int totalNoOfDays;
    HashMap<String,String> visitedWeeks = new HashMap<>();
    private ArrayList<String> arrDeliveryDays = new ArrayList<>();
    private ArrayList<CalendarlistDO> arrlist;
    private SchduleCalenderGridAdapater calendarGridAdapter;
    Calendar myCalendar;


    enum Days{
        Sun(0), Mon(1), Tue(2), Wed(3), Thu(4),  Fri(5), Sat(6);

        public int value;
        private Days(int value){
            this.value=value;
        }
    }

    @Override
    public void initialise() {
        llDelieveryDays =(LinearLayout)inflater.inflate(R.layout.delivery_day_calender,null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        llBody.addView(llDelieveryDays, param);
        ivMenu.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        setStatusBarColor();
        tvTitle.setText(getResources().getString(R.string.schudle_days));
        initialiseControls();
        myCalendar = Calendar.getInstance();
        arrlist = new ArrayList<>();
        loadData();
    }

    @Override
    public void initialiseControls() {
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvNoOfDays = (TextView) findViewById(R.id.tvNoOfDays);
        gvDeliveryDays = (GridView) findViewById(R.id.gvDeliveryDays);
        tvPrev = (ImageView) findViewById(R.id.ivPrev);
        tvNext = (ImageView) findViewById(R.id.ivNext);
        tvNoOfDays.setTypeface(AppConstants.DinproMedium);

        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(Calendar.MONTH,-1);
                prepareCalendar(myCalendar);
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(Calendar.MONTH,1);
                prepareCalendar(myCalendar);
            }
        });
        SchduleCalenderGridAdapater calendarGridAdapter = new SchduleCalenderGridAdapater(DeliveryDaysActivity.this, arrlist);
        gvDeliveryDays.setAdapter(calendarGridAdapter);
    }

    public boolean find(String date) {
        return arrDeliveryDays != null && arrDeliveryDays.contains(date);
        /*if (arrTrxHeaderDOs != null)
            for (TrxHeaderDO trxHeaderDO : arrTrxHeaderDOs) {
                if(trxHeaderDO.deliveryDate.contains(date))
                    return true;
                else if(trxHeaderDO.isRecurring) {
                    RecurringOrderDO recurringOrderDO = trxHeaderDO.recurringOrderDO;
                    String deliveryDate = trxHeaderDO.deliveryDate.substring(0, trxHeaderDO.deliveryDate.indexOf(" ") + 1).trim();
                    int diffMonths = CalendarUtil.getDifferenceOfMonth(deliveryDate, CalendarUtil.DD_MMM_YYYY_PATTERN, date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                    long differenceInTime = CalendarUtil.getdifference(deliveryDate, CalendarUtil.DD_MMM_YYYY_PATTERN, date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                    if (recurringOrderDO!= null && !StringUtils.isEmpty(recurringOrderDO.Frequency) *//*&& diffMonths <3*//*) {
                        if ( differenceInTime <= 0)
                            return false;
                        if(arrDeliveryDays != null) {
                            int[] weekYear = CalendarUtil.getCurrentDayMonthYear(date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                            String visited = "";
                            boolean flag = false;
                            String fullDay = CalendarUtil.getFullDay(date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                            if (weekYear != null)
                                visited = weekYear[0] + "," + weekYear[1] ;

                            if ((visitedWeeks.containsKey(visited) && visitedWeeks.get(visited).equalsIgnoreCase(fullDay)) || (!visitedWeeks.containsKey(visited) && arrDeliveryDays.contains(fullDay))) {
                                visitedWeeks.put(visited,fullDay);
                                flag  = true;
                            }
                            if (recurringOrderDO.Frequency.equalsIgnoreCase("Once in a Week")) {
                                return flag;
                            } else if (recurringOrderDO.Frequency.equalsIgnoreCase("Once in two Weeks")) {
                                int diffWeeks = CalendarUtil.getDifferenceOfWeek(deliveryDate, CalendarUtil.DD_MMM_YYYY_PATTERN, date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                                if (diffWeeks % 2 == 0) {
                                    return flag;
                                }
                            } else if (recurringOrderDO.Frequency.equalsIgnoreCase("Once in 3 Weeks")) {
                                int diffWeeks = CalendarUtil.getDifferenceOfWeek(deliveryDate, CalendarUtil.DD_MMM_YYYY_PATTERN, date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                                if (diffWeeks % 3 == 0) {
                                    return flag;
                                }
                            } else if (recurringOrderDO.Frequency.equalsIgnoreCase("Once in a Month")) {
                                if (diffMonths > 0) {
                                    int diffDays = CalendarUtil.getDifferenceDaysForRecurr(deliveryDate, CalendarUtil.DD_MMM_YYYY_PATTERN, date, CalendarUtil.DD_MMM_YYYY_PATTERN);
                                    if (diffDays >= 0 && diffDays < 7) {
                                        return flag;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return false;*/
    }

    @Override
    public void loadData() {
        showLoader(getString(R.string.Loading_Data));
        if(NetworkUtil.isNetworkConnectionAvailable(DeliveryDaysActivity.this))
            new CommonBL(DeliveryDaysActivity.this,DeliveryDaysActivity.this).upcommingDeliveryDays(preference.getIntFromPreference(Preference.CUSTOMER_ID,0)+"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrDeliveryDays = new CustomerDA(DeliveryDaysActivity.this).getUpcommingDeliveryDays();
/*                final ArrayList<SchduleDeliveryday> arrSchduleDeliverydays = new ArrayList<>();
                int dayStart = 0;
                int currDay = 0;
                Calendar calender = Calendar.getInstance();
                currDay = calender.get(Calendar.DAY_OF_MONTH);
                calender.set(Calendar.DAY_OF_MONTH, 1); // set to first day to get which day
                SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                String firstDayOfMonth = sdf.format(calender.getTime());
                int daysInMonth = calender.getActualMaximum(Calendar.DAY_OF_MONTH) + dayStart;
                dayStart = Days.valueOf(firstDayOfMonth).value + 7;
                int j = 1;
                for (int i = 0; i < 42; i++) {
                    SchduleDeliveryday deliveryDay = new SchduleDeliveryday();
                    if (i < 7) {
                        deliveryDay.date = (Days.values())[i].toString();
                    } else if (i >= dayStart && j <= daysInMonth) {
                        deliveryDay.date = j + "";
                        if (j >= currDay) {
                            String date = CalendarUtil.getDay(j, 0, 0);
                            deliveryDay.isSechdule = find(date);
                            if (deliveryDay.isSechdule)
                                totalNoOfDays++;
                        }
                        j++;
                    } else {
                        deliveryDay.date = "";
                    }
                    arrSchduleDeliverydays.add(deliveryDay);
                }*/
                prepareCalendar(myCalendar);
            }
        }).start();
    }

    private Calendar getCalendar(Calendar calendar,int month){
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.add(Calendar.MONTH,month);
        return newCalendar;
    }

    private void prepareCalendar(Calendar calendar){
        arrlist.clear();
        totalNoOfDays = 0;
        Calendar lastMonth = getCalendar(calendar,-1);
        int daysInLastMonth = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar newCurrMonth = getCalendar(calendar,0);
        newCurrMonth.set(Calendar.DAY_OF_MONTH, 1); // set to first day to get which day
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String firstDayOfMonth = sdf.format(newCurrMonth.getTime());
        int dayStart = Days.valueOf(firstDayOfMonth).value;
        for(int i=dayStart;i>0;i--){
            int day = daysInLastMonth-(i-1);
            lastMonth.set(Calendar.DAY_OF_MONTH,day);
            CalendarlistDO calendarlistDO = new CalendarlistDO();
            calendarlistDO.day = day;
            calendarlistDO.date = CalendarUtil.getDate(lastMonth.getTime(),CalendarUtil.DD_MMM_YYYY_PATTERN, Locale.ENGLISH);
            arrlist.add(calendarlistDO);
        }
        int daysInCurrMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=1;i<daysInCurrMonth+1;i++){
            newCurrMonth.set(Calendar.DAY_OF_MONTH,i);
            CalendarlistDO calendarlistDO = new CalendarlistDO();
            calendarlistDO.day = i;
            calendarlistDO.date = CalendarUtil.getDate(newCurrMonth.getTime(),CalendarUtil.DD_MMM_YYYY_PATTERN, Locale.ENGLISH);
            calendarlistDO.isDelivered = find(calendarlistDO.date);
            if(calendarlistDO.isDelivered)
                totalNoOfDays++;
            calendarlistDO.isCurrMonth = true;
            arrlist.add(calendarlistDO);
        }
        if(arrlist.size()<42){
            Calendar nextMonth = getCalendar(calendar,1);
            for(int i=1;arrlist.size()<42;i++){
                nextMonth.set(Calendar.DAY_OF_MONTH,i);
                CalendarlistDO calendarlistDO = new CalendarlistDO();
                calendarlistDO.day = i;
                calendarlistDO.date = CalendarUtil.getDate(nextMonth.getTime(),CalendarUtil.DD_MMM_YYYY_PATTERN, Locale.ENGLISH);
                arrlist.add(calendarlistDO);
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoader();
                tvDate.setText((CalendarUtil.getDate(myCalendar.getTime(),CalendarUtil.MMM_YYYY_PATTERN,Locale.ENGLISH)).toUpperCase());
                tvNoOfDays.setText(String.format(getString(R.string.schudle_total_days), totalNoOfDays + ""));
                if(calendarGridAdapter == null)
                    gvDeliveryDays.setAdapter(calendarGridAdapter = new SchduleCalenderGridAdapater(DeliveryDaysActivity.this, arrlist));
                else
                    calendarGridAdapter.refresh(arrlist);
            }
        });
    }


    public class SchduleCalenderGridAdapater extends BaseAdapter {
        private ArrayList<CalendarlistDO> arrlist;
        private Context context;

        public SchduleCalenderGridAdapater(Context context, ArrayList<CalendarlistDO> arrlist)
        {
            this.context = context;
            this.arrlist = arrlist;
        }

        @Override
        public int getCount() {
            if(arrlist != null)
                return arrlist.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendarcell, null);
            final TextView btn_date = (TextView) convertView.findViewById(R.id.tv_date);
            final LinearLayout ll_date = (LinearLayout) convertView.findViewById(R.id.ll_date);
            ImageView ivCalBottom = (ImageView) convertView.findViewById(R.id.ivCalBottom);

            final CalendarlistDO callist = arrlist.get(position);
            btn_date.setText(callist.day + "");

            ll_date.setBackgroundColor(context.getResources().getColor(R.color.white));
            if(callist.isCurrMonth){
                btn_date.setTextColor(context.getResources().getColor(R.color.black));
                if(callist.isDelivered){
                    ll_date.setBackgroundColor(getResources().getColor(R.color.dark_red2));
                    btn_date.setTextColor(context.getResources().getColor(R.color.white));
                }
            }else{
                btn_date.setTextColor(context.getResources().getColor(R.color.brown3));
            }

            return convertView;

        }

        public void refresh(ArrayList<CalendarlistDO> arrlist){
            this.arrlist = arrlist;
            notifyDataSetChanged();
        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        if(response != null && response.method == ServiceMethods.WS_UPCOMMING_DELIVERY_DAYS){
            if(response.data != null && response.data instanceof ArrayList){
                new CustomerDA(DeliveryDaysActivity.this).insertUpcomingDeliveryDays((ArrayList)response.data);
                arrDeliveryDays = new CustomerDA(DeliveryDaysActivity.this).getUpcommingDeliveryDays();
                prepareCalendar(myCalendar);
            }
        }
    }
}
