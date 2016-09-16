package com.example.zsq.sqweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsq on 16-9-1.
 */
public class DB {
    public static final String DB_NAME = "weather";
    public static final int VERSION = 1;
    private static DB weatherDB;
    private SQLiteDatabase db;

    private DB(Context context) {
        MyOpenHelper helper = new MyOpenHelper(context, DB_NAME, null, VERSION);
        db = helper.getWritableDatabase();
    }

    public synchronized static DB get(Context context) {
        if (weatherDB == null) {
            weatherDB = new DB(context);
        }
        return weatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("procince_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void saveCity(City province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", province.getCityName());
            values.put("city_code", province.getCityCode());
            values.put("province_id", province.getProvinceId());
            db.insert("City", null, values);
        }
    }

    public List<City> loadCities() {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, null, null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                City province = new City();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                province.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void saveCounty(County province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", province.getCountyName());
            values.put("county_code", province.getCountyCode());
            db.insert("County", null, values);
        }
    }

    public List<County> loadCounty() {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, null, null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                County province = new County();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                province.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}

