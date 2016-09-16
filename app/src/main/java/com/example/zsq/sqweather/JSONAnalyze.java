package com.example.zsq.sqweather;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-2.
 */
public class JSONAnalyze {
    private static final String TAG = "JSONAnalyze";
    public static CountyWeather JSONAnalyze(Context context,String county){
        try {
            JSONObject resultJson = GetAPI.getRequest(county.substring(0, county.length() - 1));
            ArrayList<CountyWeather> counties = CountyWeatherLB.get(context).getCountiesWeather();
            try {
                JSONObject resultObj = resultJson.getJSONObject("result");
                JSONObject dataObj = resultObj.getJSONObject("data");
                JSONObject realtimeObj = dataObj.getJSONObject("realtime");
                CountyWeather countyWeather = new CountyWeather();
                String cityName = realtimeObj.getString("city_name");
                countyWeather.setCountyName(cityName);
                countyWeather.setDate(realtimeObj.getString("date"));
                countyWeather.setUpdateTime(realtimeObj.getString("time"));
                countyWeather.setWeek(realtimeObj.getString("week"));
                countyWeather.setMoon(realtimeObj.getString("moon"));
                JSONObject realtimeWeather = realtimeObj.getJSONObject("weather");
                countyWeather.setRealtemperature(realtimeWeather.getString("temperature"));
                countyWeather.setRealhumidity(realtimeWeather.getString("humidity"));
                countyWeather.setRealinfo(realtimeWeather.getString("info"));
                JSONObject realtimeWind = realtimeObj.getJSONObject("wind");
                countyWeather.setWind(realtimeWind.getString("direct") + realtimeWind.getString("power"));
                JSONObject lifeObj = dataObj.getJSONObject("life");
                JSONObject lifeInfoObj = lifeObj.getJSONObject("info");
                countyWeather.setWuran(lifeInfoObj.getJSONArray("wuran").getString(0) + "\n\n" + lifeInfoObj.getJSONArray("wuran").getString(1) + "\n");
                countyWeather.setChuanyi(lifeInfoObj.getJSONArray("chuanyi").getString(0) + "\n\n" + lifeInfoObj.getJSONArray("chuanyi").getString(1) + "\n");
                countyWeather.setYundong(lifeInfoObj.getJSONArray("yundong").getString(0) + "\n\n" + lifeInfoObj.getJSONArray("yundong").getString(1) + "\n");
                countyWeather.setZiwaixian(lifeInfoObj.getJSONArray("ziwaixian").getString(0) + "\n\n" + lifeInfoObj.getJSONArray("ziwaixian").getString(1) + "\n");
                JSONArray weatherArray = dataObj.getJSONArray("weather");
                ArrayList<Weather> weathers = new ArrayList<Weather>();
                for (int i = 0; i < weatherArray.length(); i++) {
                    Weather weather = new Weather();
                    JSONObject weatherObj = weatherArray.getJSONObject(i);
                    weather.setDate(weatherObj.getString("date"));
                    JSONObject weatherInfo = weatherObj.getJSONObject("info");
                    JSONArray dayArray = weatherInfo.getJSONArray("day");
                    weather.setDayWeatherInfo(dayArray.getString(1));
                    weather.setHighTemp(dayArray.getString(2));
                    weather.setDayWind(dayArray.getString(3));
                    weather.setDayWindPower(dayArray.getString(4));
                    JSONArray nightArray = weatherInfo.getJSONArray("night");
                    weather.setNightWeatherInfo(nightArray.getString(1));
                    weather.setLowTemp(nightArray.getString(2));
                    weather.setNightWind(nightArray.getString(3));
                    weather.setNightWindPower(nightArray.getString(4));
                    weather.setWeek(weatherObj.getString("week"));
                    weather.setMoon(weatherObj.getString("nongli"));
                    weathers.add(weather);
//                    Log.i(TAG, "第" + i + "天" + weather.getDayWeatherInfo());
                }
                countyWeather.setWeathers(weathers);
                return countyWeather;
            } catch (JSONException e) {
                Log.i(TAG, "献茶询失败，换成市");
            }
        } catch (Exception e) {
            Log.i(TAG, "无这个城市信息");
        }
        return null;
    }
}
