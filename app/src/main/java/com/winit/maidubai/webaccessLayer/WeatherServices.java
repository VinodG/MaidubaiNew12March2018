package com.winit.maidubai.webaccessLayer;

import android.util.Log;

import com.winit.maidubai.R;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.Weather;
import com.winit.maidubai.utilities.CalendarUtil;
import com.winit.maidubai.utilities.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;


/**
 * Created by WINIT on 27-Jul-16.
 */
public class WeatherServices {
    JSONObject json;
    private final String tag = "WeatherServices client";

    private static final String wearherDayUrl = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String weatherWeekUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&";

    private static final String wearherUrl = "https://api.forecast.io/forecast/68df1f8bd6815bfe3903b2e410f8449e/%s,%s";

    public ArrayList<Weather> getWeatherData(double latitude, double longitude, int type) {
        JSONObject jsonObject = getJSON(String.format(wearherUrl,latitude,longitude));
        if(jsonObject != null){
            if(type==AppConstants.DAY_WEHEATER_TYPE)
                return parseCurrDay(jsonObject);
            else
                return parseCurrWeekly(jsonObject);
        }
        return null;
    }

    public ArrayList<Weather> parseCurrDay(JSONObject jsonObject){
        try {
            JSONObject currWheather = jsonObject.getJSONObject("currently");
            Weather weather = new Weather();
            weather.temperature = (currWheather.getDouble("temperature") -  32)*5/9;
            weather.description = currWheather.getString("summary");
            weather.date = CalendarUtil.getTodayDate(currWheather.getLong("time") * 1000);
            weather.day = new Date(currWheather.getLong("time") * 1000);
            weather.iconId = weatherIconId(currWheather.getString("icon"));
            ArrayList<Weather> arrWeathers = new ArrayList<>();
            arrWeathers.add(weather);
            return arrWeathers;
        }catch (JSONException jsonException){
            /*jsonException.printStackTrace(); */Log.d("This can never happen", jsonException.getMessage());
        }
        return null;
    }

    public ArrayList<Weather> parseCurrWeekly(JSONObject jsonObject){
        try {
            ArrayList<Weather> arrWeathers = parseCurrDay(jsonObject);
            Weather weather = null;
            if(arrWeathers == null)
                arrWeathers = new ArrayList<>();
            else {
                weather = arrWeathers.size() >0? arrWeathers.get(0): new Weather() ;
            }
            JSONArray jsonArray = jsonObject.getJSONObject("daily").getJSONArray("data");
            int jsonArraylen = jsonArray.length();
            for(int i=0 ;i <jsonArraylen-1 ;i++) {
                JSONObject currWheather = jsonArray.getJSONObject(i);
                Weather currwheather = new Weather();
                currwheather.tempmin = (currWheather.getDouble("temperatureMin") - 32) * 5 / 9;
                currwheather.tempmax = (currWheather.getDouble("temperatureMax") - 32) * 5 / 9;
                currwheather.description = currWheather.getString("summary");
                currwheather.date = CalendarUtil.getTodayDate(currWheather.getLong("time") * 1000l);
                currwheather.Day = CalendarUtil.getDate(new Date(currWheather.getLong("sunriseTime") * 1000l), CalendarUtil.EEEE_PATTERN, Locale.getDefault());
                currwheather.day = new Date(currWheather.getLong("time") * 1000l);
                currwheather.iconId = weatherIconId(currWheather.getString("icon"));
                    arrWeathers.add(currwheather);
            }
            return arrWeathers;
        }catch (JSONException jsonException){
            /*jsonException.printStackTrace(); */Log.d("This can never happen", jsonException.getMessage());
        }
        return null;
    }

    public ArrayList<Weather> updateWeatherData(final String city, int type) {
        if(type==AppConstants.DAY_WEHEATER_TYPE){
            json = getJSON(city, wearherDayUrl);
            if(json!=null) {
                return renderWeatherDay(json);
            }
        }
        else {
            json = getJSON(city, weatherWeekUrl);
            if (json != null) {
                return renderWeatherweek(json);
            }
        }
        return null;
    }

    private int weatherIconId(String type) {
        int icon;
        switch (type) {
            case "clear-day":
                icon = R.drawable.sun;
                break;
            case "clear-night":
                icon = R.drawable.clearnight;
                break;
            case "rain":
                icon = R.drawable.rain;
                break;
            case "snow":
                icon = R.drawable.snow;
                break;
            case "sleet":
                icon = R.drawable.slleet;
                break;
            case "wind":
                icon = R.drawable.wind;
                break;
            case "fog":
                icon = R.drawable.fog;
                break;
            case "cloudy":
                icon = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                icon = R.drawable.partialcloud;
                break;
            case "partly-cloudy-night":
                icon = R.drawable.partialcloudnight;
                break;
            default:
                icon = R.drawable.sun;
        }
        return icon;
    }

    private ArrayList<Weather> renderWeatherweek(JSONObject json) {
        ArrayList<Weather> weathers = new ArrayList<>();
        try {
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject weatherData = list.getJSONObject(i);
                Weather dayWeather = new Weather();
                dayWeather.temperature = kelvinToCelsius(weatherData.getJSONObject("temp").getDouble("day"));
                dayWeather.tempmax = kelvinToCelsius(weatherData.getJSONObject("temp").getDouble("min"));
                dayWeather.tempmin = kelvinToCelsius(weatherData.getJSONObject("temp").getDouble("max"));
                final Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(weatherData.getLong("dt") * 1000);
                dayWeather.day = cal.getTime();
                dayWeather.iconId = weatherIconId(weatherData.getJSONArray("weather").getJSONObject(0).getInt("id"));
                dayWeather.description = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                dayWeather.isFetched = true;
                weathers.add(dayWeather);
            }
            return weathers;
        } catch (Exception e) {
            Log.d("SimpleWeather", "One or more fields not found in the JSON data");
            return null;
        }
    }

    private ArrayList<Weather>  renderWeatherDay(JSONObject json) {
        ArrayList<Weather> weathers = new ArrayList<>();
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            Weather dayWeather = new Weather();
            dayWeather.temperature = Double.parseDouble(String.format("%.0f", main.getDouble("temp")));
            dayWeather.iconId = weatherIconId(details.getInt("id"));
            dayWeather.isFetched = true;
            weathers.add(dayWeather);
            return weathers;
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            return null;
        }

    }

    public JSONObject getJSON(String wethearUrl){
        InputStream is = null;
        try {
            is = new HttpHelper().sendRequest(wethearUrl, AppConstants.GET, null, null);
            if(is != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return new JSONObject(response.toString());
            }
        }catch (IOException e) {
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }catch (JSONException e) {
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            try {
                if(is != null)
                    is.close();
            }catch (Exception e){
                LogUtils.debug(tag,"Exception in closing the inputstream");
            }
            LogUtils.debug(tag, "Weather Services - Ended");
        }

        return null;
    }


    public JSONObject getJSON(String city, String wethearUrl){
        InputStream is = null;
        try {
            TreeMap<String,String> headers = new TreeMap<>();
            headers.put("x-api-key", AppConstants.open_weather_maps_app_id);
            is = new HttpHelper().sendRequest(String.format(wethearUrl,urlEncode(city)), AppConstants.GET, headers, null);
            if(is != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                JSONObject data = new JSONObject(response.toString());
                if (data.getInt("cod") != 200) {
                    return null;
                }
                return data;
            }
        }
        catch (IOException e) {
            /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        catch (JSONException e) {
           /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }finally {
            try {
                if(is != null)
                    is.close();
            }catch (Exception e){
                LogUtils.debug(tag,"Exception in closing the inputstream");
            }
            LogUtils.debug(tag,"Weather Services - Ended");
        }

        return null;
    }

    private int weatherIconId(int actualId) {
        int icon;
        if(actualId == 800){
            icon= R.drawable.sun;//for cleare sky
        } else {
            icon=R.drawable.cloud;//other than clear
        }
        return icon;
    }


    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }


    public String urlEncode(String value){
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
          /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
        }
        return encoded;
    }

}
