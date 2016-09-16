package com.example.zsq.sqweather;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-2.
 */
public class CountyLB {

    public static CountyLB countyLB;
    private ArrayList<County> counties;
    private Context mAppContext;

    private CountyLB(Context context) {
        counties = new ArrayList<County>();
    }

    public static CountyLB get(Context context) {
        if (countyLB == null) {
            countyLB = new CountyLB((context));
        }
        return countyLB;
    }

    public ArrayList<County> getCounties(){
        return counties;
    }
}
