package com.example.zsq.sqweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by zsq on 16-9-3.
 */
public class WeatherFragment extends Fragment implements NewScrollView.ScrollViewListener{
    private static final String TAG = "WeathreFragment";
    private String countyName;
    private ImageView topImg;
    private ImageView bottomImg;
    private ImageView weatherIcon;
    private TextView temptureTv;
    private TextView weatherInfo;
    private TextView weatherInfoBig;
    private TextView maxTempTv;
    private TextView minTempTv;
    private TextView lifeInfoTv;
    private TextView lifeInfochuanyi;
    private TextView lifeInfoyundong;
    private TextView lifeInfoziwaixian;
    private TextView getHeightTv;
    private ListView predictWeatherListView;
    private NewScrollView scrollView;
    private TextView countyNameTv;
    private Context context;
    private Resources resources;
    private LinearLayout weatherLayout;
    private LinearLayout weatherInfoLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private County county;
    private CountyWeather countyWeather;
    private ArrayList<Weather> weatherslist;
    private ImageView menu_icon;
    private ImageView add_icon;
    private DrawerLayout drawerLayout;
    private int mAlpha;
    private Bitmap backbottom;
    private Bitmap backtop;
    private RelativeLayout backImgLayout;

    public static WeatherFragment newIntance(County county) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable("countyName",  county);
        weatherFragment.setArguments(args);
        return weatherFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity().getApplicationContext();
        county = (County)getArguments().getSerializable("countyName");
        countyName = county.getCountyName();
        Log.i(TAG, "干：" + countyName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new getWeather().execute();
            }
        }).start();
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getActivity().getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
            } else if (msg.what == 2) {
                Toast.makeText(getActivity().getApplicationContext(), "刷新失败，请检查网络", Toast.LENGTH_SHORT).show();
                if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        backImgLayout = (RelativeLayout) v.findViewById(R.id.backimg_layout);
        menu_icon = (ImageView) v.findViewById(R.id.weather_menu_icon);
        add_icon = (ImageView) v.findViewById(R.id.weather_addicon);
        topImg = (ImageView) v.findViewById(R.id.imageview_top);
        bottomImg = (ImageView) v.findViewById(R.id.imageview_bottom);
        weatherLayout = (LinearLayout) v.findViewById(R.id.weather_predicition_linearlayout);
        weatherInfoLayout = (LinearLayout) v.findViewById(R.id.weather_info_layout);
        weatherInfo = (TextView) v.findViewById(R.id.weather_info_tv);
        temptureTv = (TextView) v.findViewById(R.id.tempure_tv);
        maxTempTv=(TextView)v.findViewById(R.id.maxtemp_tv);
        minTempTv = (TextView) v.findViewById(R.id.mintemp_tv);
        predictWeatherListView = (ListView) v.findViewById(R.id.weather_predicition_listview);
        scrollView = (NewScrollView) v.findViewById(R.id.fragment_scrollview);
        scrollView.setScrollViewListener(this);
        countyNameTv = (TextView) v.findViewById(R.id.countyName_tv);
        lifeInfoTv = (TextView) v.findViewById(R.id.weather_lifewuran);
        lifeInfochuanyi = (TextView) v.findViewById(R.id.weather_lifechuanyi);
        lifeInfoyundong = (TextView) v.findViewById(R.id.weather_lifeyundong);
        lifeInfoziwaixian = (TextView) v.findViewById(R.id.weather_lifeziwaixian);
        weatherInfoBig = (TextView) v.findViewById(R.id.weather_info_tv_big);
        getHeightTv = (TextView) v.findViewById(R.id.getHeighttextView);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.weather_swiprefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new getWeather().execute();
                    }
                }).start();
            }
        });
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchCityActivity.class);
                getActivity().startActivityForResult(intent, 0);
            }
        });
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        Log.i(TAG, "Height"+height);
        getHeightTv.setHeight((int)(height*0.3));
        weatherIcon = (ImageView) v.findViewById(R.id.weather_imag);
        countyNameTv.setText(countyName);
        weatherLayout.getBackground().setAlpha(110);
        weatherInfoLayout.getBackground().setAlpha(110);
        return v;
    }

    private int mScrollY=0;
    @Override
    public void onScrollChanged(NewScrollView scrollView, int x, int y, int oldx, int oldy) {
        int dy = y - oldy;
        mScrollY += dy;
        if (mScrollY < 0) {
            mScrollY=0;
        }
        if (y> 1000) {
            mAlpha=100;
        }else {
            mAlpha = Math.abs(y)/ 10;
        }
        topImg.setTranslationY(-mAlpha*3);
        bottomImg.setTranslationY(-mAlpha*3);
        topImg.setAlpha((int) (mAlpha * 2.55));
    }

    WeatherAdapter adapter;
    //显示
    private void setContent() {
        weatherInfo.setText(countyWeather.getRealinfo());
        weatherIcon.setImageBitmap(getWeatherIcon(countyWeather.getRealinfo()));
        weatherInfoBig.setText(countyWeather.getRealinfo());
        temptureTv.setText(countyWeather.getRealtemperature());
        maxTempTv.setText(countyWeather.getWeathers().get(0).getHighTemp());
        minTempTv.setText(countyWeather.getWeathers().get(0).getLowTemp());
        lifeInfoTv.setText(countyWeather.getWuran());
        lifeInfochuanyi.setText(countyWeather.getChuanyi());
        lifeInfoziwaixian.setText(countyWeather.getZiwaixian());
        lifeInfoyundong.setText(countyWeather.getYundong());
        weatherslist = countyWeather.getWeathers();
        setImgHeight(bottomImg);
        setImgHeight(topImg);
        backbottom=getWeatherBac(countyWeather.getRealinfo(),context,resources,1);
        backtop = blurBitmap(backbottom, context);
        bottomImg.setImageBitmap(backbottom);
        topImg.setImageBitmap(backtop);
        bottomImg.setScaleType(ImageView.ScaleType.FIT_XY);
        topImg.setScaleType(ImageView.ScaleType.FIT_XY);
        Log.i(TAG, "listSize" + weatherslist.size());
        adapter = new WeatherAdapter(weatherslist);
        predictWeatherListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(predictWeatherListView);
        scrollView.smoothScrollTo(0,2);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (backbottom != null && backtop != null) {
            Log.i(TAG, "回收");
            topImg.setImageBitmap(null);
            bottomImg.setImageBitmap(null);
            backbottom.recycle();
            backtop.recycle();
            backbottom = null;
            backtop = null;
        }
        System.gc();
        super.onDestroy();

    }

    private class WeatherAdapter extends ArrayAdapter<Weather> {
        public WeatherAdapter(ArrayList<Weather> weathers) {
            super(getActivity(), 0, weathers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.weather_list_item, null);
            }
            Weather weather = getItem(position);
            TextView week = (TextView) convertView.findViewById(R.id.item_week);
            ImageView weathericon=(ImageView) convertView.findViewById(R.id.item_weather_icon);
            TextView maxTemp = (TextView) convertView.findViewById(R.id.item_maxtemp);
            TextView minTemp = (TextView) convertView.findViewById(R.id.item_mintemp);
            week.setText("星期"+weather.getWeek());
            maxTemp.setText(weather.getHighTemp());
            minTemp.setText(weather.getLowTemp());
            weathericon.setImageBitmap(getWeatherIcon(weather.getDayWeatherInfo()));
            return convertView;
        }
    }

    private class getWeather extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            countyWeather = JSONAnalyze.JSONAnalyze(getActivity(), county.getCountyName());
            if (countyWeather == null) {
                Log.i(TAG, "成" + county.getCityName());
                countyWeather=JSONAnalyze.JSONAnalyze(getActivity(),county.getCityName());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (countyWeather != null) {
                setContent();
                mHandler.sendEmptyMessage(1);
            } else mHandler.sendEmptyMessage(2);

        }
    }

    public Bitmap getWeatherBac(String weatherinfo, Context context, Resources resources,int size) {
        if (resources==null)
            resources=context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=size;
        if(weatherinfo.contains("雷")){
            return BitmapFactory.decodeResource(resources, R.drawable.lei,options);
        } else if (weatherinfo.contains("雨")) {
            return BitmapFactory.decodeResource(resources, R.drawable.yu_2_45,options);
        }else if(weatherinfo.contains("阴")){
            return  BitmapFactory.decodeResource(resources, R.drawable.wuyun2,options);
        }else if(weatherinfo.contains("云")){
            return BitmapFactory.decodeResource(resources, R.drawable.yun_60,options);
        } else if (weatherinfo.contains("晴")) {
            return BitmapFactory.decodeResource(resources, R.drawable.qingtian_60,options);
        } else if (weatherinfo.contains("雪")) {
            return BitmapFactory.decodeResource(resources, R.drawable.xuehua16_9,options);
        } else {
            return BitmapFactory.decodeResource(resources, R.drawable.yun_60,options);
        }
    }

    private Bitmap getWeatherIcon(String weatherinfo) {
        if (resources==null)
            resources=context.getResources();
        if (weatherinfo.contains("大雨")||(weatherinfo.contains("暴")&&weatherinfo.contains("雨"))) {
            return  BitmapFactory.decodeResource(resources, R.mipmap.weather_clouds_bolt_rain_46px);
        }else if(weatherinfo.contains("雨")){
            return BitmapFactory.decodeResource(resources, R.mipmap.weather_cloud_storm_41px);
        }else if(weatherinfo.contains("云")){
            return BitmapFactory.decodeResource(resources, R.mipmap.weather_cloud_sun);
        } else if (weatherinfo.contains("晴天")) {
            return BitmapFactory.decodeResource(resources, R.mipmap.ic_sunny_white_24dp);
        } else if (weatherinfo.contains("雪")) {
            return BitmapFactory.decodeResource(resources, R.mipmap.weather_clouds_snow_45px);
        } else {
            return BitmapFactory.decodeResource(resources, R.mipmap.weather_cloud_sun);
        }
    }





    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    //设置照片高度
    private void setImgHeight(ImageView imageView) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height + 550;
        Log.i(TAG, "point.y: " + point.y + " height:" +params.height);
        imageView.setLayoutParams(params);
        imageView.requestLayout();
    }

    private Bitmap blurBitmap(Bitmap bitmap, Context context) {
        Log.i(TAG,"bluer");
        Bitmap outBitmap=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        rs.destroy();
        return outBitmap;
    }

    public void refreshWeather() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new getWeather().execute();
                }
            }).start();
        }
    }

}
