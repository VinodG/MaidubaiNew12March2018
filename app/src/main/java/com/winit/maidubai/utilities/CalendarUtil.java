package com.winit.maidubai.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Girish Velivela on 08-07-2016.
 */
public class CalendarUtil {
    private final static String DATE_PATTERN_SERVICE = "yyyy-MM-dd";
    public static final String DATE_STD_PATTERN = "yyyy-MM-dd";
    public static final String DD_MM_YYYY_PATTERN = "dd-MM-yyyy";
    public static final String DD_MMM_YYYY_PATTERN = "dd-MMM-yyyy";
    public static final String MM_DD_YYYY_PATTERN = "MM-dd-yyyy";
    public static final String DATE_STD_PATTERN_MONTH = "yyyy-MM";
    public static final String MM_YYYY_PATTERN = "MM-yyyy";
    public static final String YYYY_PATTERN = "yyyy";
    public static final String MMM_YYYY_PATTERN = "MMM-yyyy";
    public static final String MMM_PATTERN = "MMM";
    public static final String DATE_STD_PATTERN_FULLDATE = "MMM dd,yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN_dd_MMM_YYYY = "dd-MMM-yyyy HH:mm:ss";
    public static final String DATE_PATTERN_dd_MM_YYYY = "dd-MM-yyyy HH:mm:ss";
    public static final String YYYY_MM_DD_FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_MMM_dd = "MMM dd";
    public static final String dd_MMM_PATTERN = "dd MMM";
    public static final String EEEE_PATTERN = "EEEE";
    public static final String SYNCH_DATE_TIME_PATTERN = "yyyy-MM-dd hh:mm:ss aa";



    public static String getDate(Date date,String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String getDate(Date date,String pattern, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,locale);
        return simpleDateFormat.format(date);
    }

    public static String getDate(long time,String pattern, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,locale);
        return simpleDateFormat.format(time);
    }

    public static String getDate(String date,String parsePattern,String pattern,Locale locale) {
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(parsePattern);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,locale);
            return simpleDateFormat.format(parseDateFormat.parse(date));
        }catch (ParseException e){
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return "";
    }

    public static String getDate(String strDate,String parsePattern,String pattern,long delay,Locale locale) {
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(parsePattern);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,locale);
            Date date = parseDateFormat.parse(strDate);
            return simpleDateFormat.format(date.getTime() - delay);
        }catch (ParseException e){
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return "";
    }

    public static String getOrderDeliveryDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+5);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN_dd_MM_YYYY);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static int[] getCurrentWeekYear(){
        Calendar calendar = Calendar.getInstance();
        int[] dayMonth = new int[3];
        dayMonth[0] = calendar.get(Calendar.DAY_OF_MONTH);
        dayMonth[1] = calendar.get(Calendar.MONTH);
        dayMonth[2] = calendar.get(Calendar.YEAR);
        return dayMonth;
    }
    public static int[] getCurrentDayMonthYear(String date, String pattern){
        SimpleDateFormat parseDateFormat = new SimpleDateFormat(pattern);
        Date startDate = null;
        try {
            if(!StringUtils.isEmpty(date))
                startDate = parseDateFormat.parse(date);
        }catch (Exception e){
           /*e.printStackTrace(); */     Log.d("This can never happen", e.getMessage());
        }
        if(startDate == null)
            startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int[] dayMonth = new int[2];
        dayMonth[0] = calendar.get(Calendar.WEEK_OF_YEAR);
        dayMonth[1] = calendar.get(Calendar.YEAR);
        return dayMonth;
    }

    public static boolean compareDate(String startDateStr, String endDateStr){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(DATE_PATTERN_dd_MM_YYYY);
            long startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr).getTime();
            else
                startDate = new Date().getTime();
            long endDate;
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr).getTime();
            else
                endDate = new Date().getTime();
            if(startDate < endDate)
                return true;
        }catch (ParseException e){
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return false;
    }

    public static long getdifference(String startDateStr, String endDateStr){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(DATE_PATTERN_dd_MMM_YYYY,Locale.ENGLISH);
            long startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr).getTime();
            else
                startDate = new Date().getTime();
            long endDate;
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr).getTime();
            else
                endDate = new Date().getTime();
            return (endDate - startDate);
        }catch (ParseException e){
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return 0;
    }

    public static long getdifference(String startDateStr, String stratDayPattern, String endDateStr, String endDayPattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(stratDayPattern);
            long startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr).getTime();
            else
                startDate = new Date().getTime();
            long endDate;
            parseDateFormat = new SimpleDateFormat(endDayPattern);
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr).getTime();
            else
                endDate = new Date().getTime();
            return (endDate - startDate);
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return 0;
    }


    public static String getCurrentMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        return simpleDateFormat.format(new Date());
    }
    public static int getDifferenceOfWeek(String startDateStr, String stratDayPattern, String endDateStr, String endDayPattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(stratDayPattern);
            Date startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr);
            else
                startDate = new Date();
            Date endDate;
            parseDateFormat = new SimpleDateFormat(endDayPattern);
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr);
            else
                endDate = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            return endCalendar.get(Calendar.WEEK_OF_YEAR)-startCalendar.get(Calendar.WEEK_OF_YEAR);
        }catch (ParseException e){
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return 0;
    }
    public static int getDifferenceOfMonth(String startDateStr, String stratDayPattern, String endDateStr, String endDayPattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(stratDayPattern);
            Date startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr);
            else
                startDate = new Date();
            Date endDate;
            parseDateFormat = new SimpleDateFormat(endDayPattern);
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr);
            else
                endDate = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            int endYear = endCalendar.get(Calendar.YEAR);
//            if(startCalendar.get(Calendar.YEAR)<endYear)
//                return ((endYear - startCalendar.get(Calendar.YEAR))*(endCalendar.get(Calendar.MONTH)+1))+(11-startCalendar.get(Calendar.MONTH));
//            return endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH);
            return (((endYear - startCalendar.get(Calendar.YEAR)) * 12)+(endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH)));
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return 0;
    }

    public static int getDifferenceDaysForRecurr(String startDateStr, String stratDayPattern, String endDateStr, String endDayPattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(stratDayPattern);
            Date startDate;
            if(!StringUtils.isEmpty(startDateStr))
                startDate = parseDateFormat.parse(startDateStr);
            else
                startDate = new Date();
            Date endDate;
            parseDateFormat = new SimpleDateFormat(endDayPattern);
            if(!StringUtils.isEmpty(endDateStr))
                endDate = parseDateFormat.parse(endDateStr);
            else
                endDate = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            int endYear = startCalendar.get(Calendar.YEAR);
            if(startCalendar.get(Calendar.YEAR)!=endYear)
                startCalendar.set(Calendar.YEAR, endYear);
            startCalendar.set(Calendar.MONTH, endCalendar.get(Calendar.MONTH));
            return endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
        }catch (ParseException e){
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return 0;
    }

    public static String getMonth(int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        if(year != 0)
            calendar.set(Calendar.YEAR,year);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDay(int day,int month,int year){
        Calendar calendar = Calendar.getInstance();
        if(day != 0)
            calendar.set(Calendar.DAY_OF_MONTH,day);
        if(month != 0)
            calendar.set(Calendar.MONTH,month);
        if(year != 0)
            calendar.set(Calendar.YEAR,year);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDate(int day,int month,int year){
        Calendar calendar = Calendar.getInstance();
        if(day != 0)
            calendar.set(Calendar.DAY_OF_MONTH,day);
//        if(month != 0)
            calendar.set(Calendar.MONTH,month);
        if(year != 0)
            calendar.set(Calendar.YEAR,year);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_MMM_YYYY_PATTERN,Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDay(String date, String pattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(pattern);
            Date startDate;
            if(!StringUtils.isEmpty(date))
                startDate = parseDateFormat.parse(date);
            else
                startDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE",Locale.ENGLISH);
            return simpleDateFormat.format(startDate);
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return "";
    }
    public static String getFullDay(String date, String pattern){
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(pattern);
            Date startDate;
            if(!StringUtils.isEmpty(date))
                startDate = parseDateFormat.parse(date);
            else
                startDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            switch (day) {
                case Calendar.SUNDAY:
                    return "Sunday";
                case Calendar.MONDAY:
                    return "Monday";
                case Calendar.TUESDAY:
                    return "Tuesday";
                case Calendar.WEDNESDAY:
                    return "Wednesday";
                case Calendar.THURSDAY:
                    return "Thursday";
                case Calendar.FRIDAY:
                    return "Friday";
                case Calendar.SATURDAY:
                    return "Saturday";
            }
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return "";
    }

    public static String getTodayWheterDate() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        String d = null;
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                d = "Sunday";
                break;
            case Calendar.MONDAY:
                d = "Monday";
                break;

            case Calendar.TUESDAY:
                d = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                d = "Wednesday";
                break;
            case Calendar.THURSDAY:
                d = "Thursday";
                break;
            case Calendar.FRIDAY:
                d = "Friday";
                break;
            case Calendar.SATURDAY:
                d = "Saturday";
                break;
        }
        String month = String.format(Locale.US, "%tb", calendar);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return (sb.append(d).append(",").append(" ").append(month).append(" ").append(date).append(",").append(" ").append(year).toString());

    }
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        String d = null;
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                d = "Sunday";
                break;
            case Calendar.MONDAY:
                d = "Monday";
                break;
            case Calendar.TUESDAY:
                d = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                d = "Wednesday";
                break;
            case Calendar.THURSDAY:
                d = "Thursday";
                break;
            case Calendar.FRIDAY:
                d = "Friday";
                break;
            case Calendar.SATURDAY:
                d = "Saturday";
                break;
        }
        String month = String.format(Locale.US, "%tb", calendar);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return (sb.append(d).append(",").append(" ").append(month).append(" ").append(date).append(",").append(" ").append(year).toString());

    }


    public static String getTodayDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        StringBuilder sb = new StringBuilder();
        String d = null;
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                d = "Sunday";
                break;
            case Calendar.MONDAY:
                d = "Monday";
                break;

            case Calendar.TUESDAY:
                d = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                d = "Wednesday";
                break;
            case Calendar.THURSDAY:
                d = "Thursday";
                break;
            case Calendar.FRIDAY:
                d = "Friday";
                break;
            case Calendar.SATURDAY:
                d = "Saturday";
                break;
        }
        String month = String.format(Locale.US, "%tb", calendar);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return (sb.append(d).append(",").append(" ").append(month).append(" ").append(date).append(",").append(" ").append(year).toString());

    }
    public static String convertToTrxDate(String fromDateFormat){

        String formattedTime ="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
            Date d = sdf.parse(fromDateFormat);
            formattedTime = output.format(d);
            }
        catch (ParseException e){
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return formattedTime;

    }

    public static String convertToTrxDateToShow(String fromDateFormat,Locale locale){

        String formattedTime ="";
        try {
            if(fromDateFormat.contains("T"))
                fromDateFormat = fromDateFormat.replace("T"," ");
            /*Temp Solution*/
            if(fromDateFormat.contains("/"))
                fromDateFormat = fromDateFormat.replace("/","-");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
            if(locale.equals(new Locale("ar")))
            {
                SimpleDateFormat outputmonth = new SimpleDateFormat("MMMM",locale);
                SimpleDateFormat outputdate = new SimpleDateFormat("dd",Locale.ENGLISH);
                SimpleDateFormat outputyear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
                SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                Date d = sdf.parse(fromDateFormat);

                formattedTime = outputdate.format(d)+" "+outputmonth.format(d)+" "+outputyear.format(d)+"، "+outputTime.format(d);
            }
            else
            {
                SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy hh:mm a",locale);
                Date d = sdf.parse(fromDateFormat);
                formattedTime = output.format(d);
            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return formattedTime;

    }
    public static String convertToTrxDateToShowNew(String fromDateFormat,Locale locale){

        String formattedTime ="";
        try {
            if(fromDateFormat.contains("T"))
                fromDateFormat = fromDateFormat.replace("T"," ");
            /*Temp Solution*/
            if(fromDateFormat.contains("/"))
                fromDateFormat = fromDateFormat.replace("/","-");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
            if(locale.equals(new Locale("ar")))
            {
                SimpleDateFormat outputmonth = new SimpleDateFormat("MMMM",locale);
                SimpleDateFormat outputdate = new SimpleDateFormat("dd",Locale.ENGLISH);
                SimpleDateFormat outputyear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
                SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                Date d = sdf.parse(fromDateFormat);

                formattedTime = outputdate.format(d)+" "+outputmonth.format(d)+" "+outputyear.format(d)+"، "+outputTime.format(d);
            }
            else
            {
                SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy hh:mm a",locale);
                Date d = sdf.parse(fromDateFormat);
                formattedTime = output.format(d);
            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return formattedTime;

    }
    public static String convertToDeliDateToShow(String fromDateFormat,Locale locale){

        String formattedTime ="";
        try {
            if(fromDateFormat.contains("T"))
                fromDateFormat = fromDateFormat.replace("T"," ");
            /*Temp Solution*/
            if(fromDateFormat.contains("/"))
                fromDateFormat = fromDateFormat.replace("/","-");
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_dd_MMM_YYYY,Locale.ENGLISH);
            Date d = sdf.parse(fromDateFormat);
            if(locale.equals(new Locale("ar")))
            {
                SimpleDateFormat outputmonth = new SimpleDateFormat("MMMM",locale);
                SimpleDateFormat outputdate = new SimpleDateFormat("dd",Locale.ENGLISH);
                SimpleDateFormat outputyear = new SimpleDateFormat("yyyy",Locale.ENGLISH);

                formattedTime = outputdate.format(d)+" "+outputmonth.format(d)+"، "+outputyear.format(d);
            }
            else
            {
                SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy",locale);
                formattedTime = output.format(d);
            }
        }
        catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return formattedTime;

    }
    public static String convertToDeliDateToInsert(String fromDateFormat){

        String formattedTime ="";
        try {
            if(fromDateFormat.contains("T"))
                fromDateFormat = fromDateFormat.replace("T"," ");
            /*Temp Solution*/
            if(fromDateFormat.contains("/"))
                fromDateFormat = fromDateFormat.replace("/","-");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat(DATE_PATTERN_dd_MMM_YYYY,Locale.ENGLISH);
            Date d = sdf.parse(fromDateFormat);
            formattedTime = output.format(d);
        }
        catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return formattedTime;

    }
    public static String getServiceDate(String datetime)
    {
        int year = 0, month = 0, day = 0;
        String sdf = "";
        if(datetime.contains("T"))
            datetime = datetime.split("T")[0];
        if(datetime.split("-").length == 3)
        {
            year = StringUtils.getInt(datetime.split("-")[0]);
            month = StringUtils.getInt(datetime.split("-")[1]);
            day = StringUtils.getInt(datetime.split("-")[2]);

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
            Date date = calendar.getTime();
            sdf = new SimpleDateFormat(DATE_PATTERN_SERVICE).format(date);
        }
        return sdf;
    }

    public static String getTodayDate(Locale locale) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String formattedTime ="";

        SimpleDateFormat outputday = new SimpleDateFormat("EEEE",locale);
        SimpleDateFormat outputdate = new SimpleDateFormat("dd",Locale.ENGLISH);
        SimpleDateFormat outputmonth;
        SimpleDateFormat outputyear = new SimpleDateFormat("yyyy",Locale.ENGLISH);

        if(locale.equals(new Locale("ar")))
        {
            outputmonth = new SimpleDateFormat("MMMM",locale);
            formattedTime = outputday.format(date)+"، "+outputmonth.format(date)+" "+outputdate.format(date)+"، "+outputyear.format(date);
        }
        else
        {
            outputmonth = new SimpleDateFormat("MMM",locale);
            formattedTime = outputday.format(date)+", "+outputmonth.format(date)+" "+outputdate.format(date)+", "+outputyear.format(date);
        }


        return formattedTime;

    }
    public static String getHydrationDate(String date,String parsePattern,Locale locale) {
        String formattedTime ="";
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(parsePattern);
            SimpleDateFormat outputdate = new SimpleDateFormat("dd",Locale.ENGLISH);
            SimpleDateFormat outputmonth;

            Date day = parseDateFormat.parse(date);
            if(locale.equals(new Locale("ar")))
            {
                outputmonth = new SimpleDateFormat("MMMM",locale);
            }else
            {
                outputmonth = new SimpleDateFormat("MMM",locale);
            }

            formattedTime = outputdate.format(day)+" "+outputmonth.format(day);

            return formattedTime;
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return formattedTime;
    }
    public static String getHydrationMonthYear(String date,String parsePattern,Locale locale) {
        String formattedTime ="";
        try {
            SimpleDateFormat parseDateFormat = new SimpleDateFormat(parsePattern);
            SimpleDateFormat outputmonth;

            Date day = parseDateFormat.parse(date);
            if(locale.equals(new Locale("ar")))
            {
                outputmonth = new SimpleDateFormat("MMMM",locale);
            }else
            {
                outputmonth = new SimpleDateFormat("MMM",locale);
            }

            formattedTime = outputmonth.format(day);

            return formattedTime;
        }catch (ParseException e){
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return formattedTime;
    }

}
