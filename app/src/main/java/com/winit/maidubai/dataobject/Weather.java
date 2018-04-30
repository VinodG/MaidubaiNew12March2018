package com.winit.maidubai.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WINIT on 25-Jul-16.
 */
public class Weather implements Serializable {

    public double temperature;
    public double tempmin;
    public double tempmax;
    public String description = "";
    public String date = "";
    public String Day = "";
    public Date day;
    public int iconId;
    public boolean isFetched;

}

