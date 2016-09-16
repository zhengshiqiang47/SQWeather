package com.example.zsq.sqweather;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String RESULT_COUNTYNAME = "countyname";
    private static final String TAG = "MainActivity";
    private static final String mFilename = "selected.json";

    private String countyName="闽侯县";//闽侯
    private FragmentManager fm;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private ArrayList<WeatherFragment> fragmentlist;
    private MyFragmentPagerAdapter pagerAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<County> selectedCounties;
    private ListView selectedListView;
    private CitySelectedAdapter citySelectedAdapter;
    private int currentPosition;
    private int toPostion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        int writePermission= ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission!= PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }
        initview();
        fm=getSupportFragmentManager();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchCityActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "position:" + position + "  positionOffset" + positionOffset+"  pixels" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;
                WeatherFragment weatherFragment = fragmentlist.get(position);
                weatherFragment.refreshWeather();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "state" + state);
            }
        });
        fragmentlist = new ArrayList<WeatherFragment>();
        pagerAdapter=new MyFragmentPagerAdapter(fm, fragmentlist);
        selectedCounties = loadCountySelected(getApplicationContext());
        if (selectedCounties.size() != 0) {
            viewPager.setAdapter(pagerAdapter);
            for (County county : selectedCounties) {
                setFragment(county);
            }
        }else {
            setDefaultFragment();
        }
        citySelectedAdapter = new CitySelectedAdapter(selectedCounties);
        selectedListView.setAdapter(citySelectedAdapter);
    }

    private void initview() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        selectedListView = (ListView) findViewById(R.id.city_list);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(TAG, "调用了");
                if (toPostion != currentPosition)
                    viewPager.setCurrentItem(toPostion);
                currentPosition = toPostion;
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                County county =(County) data.getSerializableExtra(RESULT_COUNTYNAME);
                selectedCounties.add(county);
                countyName=county.getCountyName();
                setFragment(county);
            }
        }
    }

    private void setFragment(County county){
        WeatherFragment weatherFragment = WeatherFragment.newIntance(county);
        fragmentlist.add(weatherFragment);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(fragmentlist.size()-1);
        currentPosition=fragmentlist.size()-1;
    }

    private void setDefaultFragment() {
        County county=new County();
        county.setCountyName("闽侯县");
        county.setCityName("福州市");
        WeatherFragment weatherFragment = WeatherFragment.newIntance(county);
        fragmentlist.add(weatherFragment);
        selectedCounties.add(county);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(fragmentlist.size()-1);
        currentPosition=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCountySelected(selectedCounties, getApplicationContext());
    }

    public static ArrayList<County> loadCountySelected(Context context) {
        ArrayList<County> counties = new ArrayList<County>();
        BufferedReader reader = null;
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i=0;i<jsonArray.length();i++) {
                counties.add(new County(jsonArray.getJSONObject(i)));
            }
            Log.i(TAG, "加载成功");
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return counties;
    }

    public static void saveCountySelected(ArrayList<County> counties,Context context) {
        JSONArray array = new JSONArray();
        for (County county : counties) {
            array.put(county.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = null;
            out = context.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CitySelectedAdapter extends ArrayAdapter<County> {
        private ArrayList<County> counties;
        private TextView citySelectedName;
        private Context context;

        public CitySelectedAdapter(ArrayList<County> counties) {
            super(getApplicationContext(), 0, counties);
            this.counties = counties;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.selected_list_item, parent, false);
            }
            citySelectedName = (TextView) convertView.findViewById(R.id.selected_city_name);
            County county = getItem(position);
            citySelectedName.setText(county.getCountyName());
            citySelectedName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPostion = position;
                    Log.i(TAG, "topostion" + toPostion + " current position" + currentPosition);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
            return convertView;
        }
    }
}
