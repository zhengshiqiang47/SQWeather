package com.example.zsq.sqweather;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-2.
 */
public class CountyWeather {
    private String date;
    private String countyName;
    private String updateTime;
    private String moon;
    private String realtemperature;
    private String realhumidity;
    private String realinfo;
    private String wind;
    private String wuran;
    private String ziwaixian;
    private String yundong;
    private String chuanyi;

    public String getYundong() {
        return yundong;
    }

    public void setYundong(String yundong) {
        this.yundong = yundong;
    }

    public String getChuanyi() {
        return chuanyi;
    }

    public void setChuanyi(String chuanyi) {
        this.chuanyi = chuanyi;
    }

    public String getZiwaixian() {
        return ziwaixian;
    }

    public void setZiwaixian(String ziwaixian) {
        this.ziwaixian = ziwaixian;
    }

    private String week;
    private String PM25;

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    private ArrayList<Weather> weathers;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }

    public String getRealtemperature() {
        return realtemperature;
    }

    public void setRealtemperature(String realtemperature) {
        this.realtemperature = realtemperature;
    }

    public String getRealhumidity() {
        return realhumidity;
    }

    public void setRealhumidity(String realhumidity) {
        this.realhumidity = realhumidity;
    }

    public String getRealinfo() {
        return realinfo;
    }

    public void setRealinfo(String realinfo) {
        this.realinfo = realinfo;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWuran() {
        return wuran;
    }

    public void setWuran(String wuran) {
        this.wuran = wuran;
    }

    public ArrayList<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(ArrayList<Weather> weathers) {
        this.weathers = weathers;
    }
}
