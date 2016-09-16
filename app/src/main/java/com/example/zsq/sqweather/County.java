package com.example.zsq.sqweather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zsq on 16-9-1.
 */
public class County implements Serializable {
    private int id;
    private String countyName;
    private String countyCode;
    private String cityName;
    private String provinceName;

    public County() {

    }

    public County(JSONObject json){
        try {
            this.countyName=json.getString("countyName");
            this.cityName = json.getString("cityName");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        if (cityName == null) {
           return countyName+"     ("+provinceName+")";
        }
        return countyName+"     ("+provinceName+","+cityName+")";
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("countyName", countyName);
            jsonObject.put("cityName", cityName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
