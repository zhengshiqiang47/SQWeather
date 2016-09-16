package com.example.zsq.sqweather;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

/**
 * Created by zsq on 16-8-31.
 */
public class CityJSONAnalize {
    private static final String TAG = "CityJSONAnalize";

    public static Boolean getCity(Context context) {
        ArrayList<City> cites = new ArrayList<City>();
        AssetManager AM=context.getAssets();
        JSONObject json = new JSONObject();
        try {
            InputStream is = AM.open("city");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String JSONStr = new String(buffer, "utf-8");
            is.close();
            json = new JSONObject(JSONStr);
            Iterator iterator = json.keys();
            ArrayList<County> counties = CountyLB.get(context).getCounties();
            counties.clear();
            while (iterator.hasNext()) {
                Province province=new Province();
                String provinceNumberKey=iterator.next().toString();
                JSONArray provinceList=json.getJSONArray(provinceNumberKey);
                String provinceName = provinceList.get(0).toString();
                String provinceCode = provinceNumberKey;
                JSONObject provinceJSON = provinceList.getJSONObject(2);
                Iterator cityIterator=provinceJSON.keys();
                while (cityIterator.hasNext()) {
                    String cityKey = cityIterator.next().toString();
                    JSONArray cityList = provinceJSON.getJSONArray(cityKey);
                    String cityName = cityList.getString(0);
                    County city=new County();
                    city.setCountyName(cityName);
                    city.setProvinceName(provinceName);
                    counties.add(city);
                    String cityCode =cityKey;
                    try {
                        JSONObject cityJSON = cityList.getJSONObject(2);
                        Iterator countyIterator=cityJSON.keys();
                        while (countyIterator.hasNext()) {
                            County county = new County();
                            String countyCode=countyIterator.next().toString();
                            String countyName = cityJSON.getJSONArray(countyCode).getString(0);
                            county.setCountyName(countyName);
                            county.setCityName(cityName);
                            county.setProvinceName(provinceName);
                            counties.add(county);
//                            Log.i(TAG, countyName + "　" + cityName + " " + provinceName);
                        }
                    }catch (Exception e){

                    }
                }
//                Log.i(TAG, "省级名称" + provinceName);
//                Log.i(TAG,"市级代码"+provinceList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}
