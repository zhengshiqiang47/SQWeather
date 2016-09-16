package com.example.zsq.sqweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-2.
 */
public class SearchCityActivity extends AppCompatActivity{
    private static final String TAG = "SearchCityActivity";

    private SearchView searchView;
    private ListView searchList;
    private ArrayList<County> counties;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcity);
        CityJSONAnalize.getCity(getApplicationContext());
        counties = CountyLB.get(getApplicationContext()).getCounties();
        searchList = (ListView) findViewById(R.id.search_city_list);
        searchList.setAdapter(new ArrayAdapter<County>(this ,android.R.layout.simple_list_item_1, counties));
        searchList.setTextFilterEnabled(true);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                County county = (County) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MainActivity.RESULT_COUNTYNAME, county);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.i(TAG, "close");
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {
                    searchList.setFilterText(s.toString());
                }else {
                    searchList.clearTextFilter();
                }
                return false;
            }
        });
    }
}
