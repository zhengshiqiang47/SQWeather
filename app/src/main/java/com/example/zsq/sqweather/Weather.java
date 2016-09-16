package com.example.zsq.sqweather;

/**
 * Created by zsq on 16-9-2.
 */
public class Weather {
    private String date;
    private String dayWeatherInfo;
    private String nightWeatherInfo;
    private String highTemp;
    private String lowTemp;
    private String dayWind;
    private String nightWind;
    private String dayWindPower;
    private String nightWindPower;
    private String week;
    private String moon;

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayWeatherInfo() {
        return dayWeatherInfo;
    }

    public void setDayWeatherInfo(String dayWeatherInfo) {
        this.dayWeatherInfo = dayWeatherInfo;
    }

    public String getNightWeatherInfo() {
        return nightWeatherInfo;
    }

    public void setNightWeatherInfo(String nightWeatherInfo) {
        this.nightWeatherInfo = nightWeatherInfo;
    }

    public String getNightWind() {
        return nightWind;
    }

    public void setNightWind(String nightWind) {
        this.nightWind = nightWind;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getDayWind() {
        return dayWind;
    }

    public void setDayWind(String dayWind) {
        this.dayWind = dayWind;
    }

    public String getDayWindPower() {
        return dayWindPower;
    }

    public void setDayWindPower(String dayWindPower) {
        this.dayWindPower = dayWindPower;
    }

    public String getNightWindPower() {
        return nightWindPower;
    }

    public void setNightWindPower(String nightWindPower) {
        this.nightWindPower = nightWindPower;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }

}
