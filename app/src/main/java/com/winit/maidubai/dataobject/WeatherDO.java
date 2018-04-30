package com.winit.maidubai.dataobject;

/**
 * Created by Jayasai on 7/1/2016.
 */
public class WeatherDO extends BaseDO{
    public String day;
    String tempture;
    String minTemp;
    String maxTenp;
    public int image;
    public void   setTempture(int min,int max){
       this.tempture=""+min+(char) 0x00B0+"/"+max+(char) 0x00B0;
    }
    public String getTempture(){
        return tempture;
    }


}
