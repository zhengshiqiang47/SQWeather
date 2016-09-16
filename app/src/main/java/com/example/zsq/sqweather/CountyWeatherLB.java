package com.example.zsq.sqweather;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-2.
 */
public class CountyWeatherLB {
    public static CountyWeatherLB countyLB;
    private ArrayList<CountyWeather> counties;
    private Context mAppContext;

    private CountyWeatherLB(Context context) {
        counties = new ArrayList<CountyWeather>();
    }

    public static CountyWeatherLB get(Context context) {
        if (countyLB == null) {
            countyLB = new CountyWeatherLB((context));
        }
        return countyLB;
    }

    public ArrayList<CountyWeather> getCountiesWeather(){
        return counties;
    }
}
